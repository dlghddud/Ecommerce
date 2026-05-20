import http from 'k6/http';
import { check, sleep } from 'k6';

// --- 테스트 전제 조건 ---
// 1. DB에 ID=1 인 Coupon 데이터가 존재해야 합니다. (discountAmount가 설정된)
// 2. DB에 ID=1 인 Seat 데이터가 존재해야 합니다. (price가 설정된)
// 3. 테스트 시작 전, Seat의 상태는 AVAILABLE 이어야 합니다.

// --- 테스트 옵션 ---
export const options = {
    scenarios: {
        reservation_with_coupon: {
            executor: 'constant-vus',
            vus: 100,       // 100명의 동시 사용자
            duration: '10s', // 10초 동안 테스트
        },
    },
    // 실패한 요청(4xx, 5xx)이 있어도 테스트를 중단하지 않음
    thresholds: {
        'http_req_failed': ['rate<0.1'], // 실패율이 10% 미만이어야 함
    },
};

// --- 테스트 시작 전 1회 실행되는 부분 ---
export function setup() {
    // 테스트에 사용할 쿠폰 ID와 재고 설정
    const couponId = 1;
    const couponQuantity = 500;
    const initUrl = `http://localhost:8080/admin/coupon/init?couponId=${couponId}&quantity=${couponQuantity}`;

    const res = http.post(initUrl);

    check(res, {
        'Coupon stock initialized': (r) => r.status === 200,
    });

    // 테스트 데이터 반환
    return { couponId: couponId, seatId: 1 };
}

// --- 가상 사용자(Virtual User)가 실행하는 메인 테스트 로직 ---
export default function (data) {
    // 각 가상 사용자(VU)는 고유한 ID를 가짐
    const userId = __VU;

    // --- 1. 쿠폰 발급받기 ---
    const issueUrl = `http://localhost:8080/coupons/${data.couponId}/issue?userId=${userId}`;
    const issueRes = http.post(issueUrl);

    // 쿠폰 발급 성공 여부 확인 (200 OK)
    const isCouponIssued = check(issueRes, {
        '[Step 1] Coupon Issued Successfully': (r) => r.status === 200,
    });

    // 쿠폰 발급에 실패하면 더 이상 진행하지 않음
    if (!isCouponIssued) {
        sleep(1);
        return;
    }

    // 발급받은 쿠폰의 고유 ID(couponIssuedId) 추출
    const couponIssuedId = issueRes.json('id');
    if (!couponIssuedId) {
        console.error(`VU ${userId}: Failed to get couponIssuedId from response: ${issueRes.body}`);
        return;
    }

    // --- 2. 쿠폰을 사용하여 좌석 예약하기 ---
    const reserveUrl = `http://localhost:8080/reservations?seatId=${data.seatId}&userId=${userId}&couponIssuedId=${couponIssuedId}`;
    const reserveRes = http.post(reserveUrl);

    // 예약 성공 여부 확인
    check(reserveRes, {
        '[Step 2] Reservation with Coupon Successful': (r) => r.status === 200,
        '[Step 2] Response contains "쿠폰 적용됨"': (r) => r.body.includes('쿠폰 적용됨'),
    });

    sleep(0.5); // 다음 요청 전 잠시 대기
}