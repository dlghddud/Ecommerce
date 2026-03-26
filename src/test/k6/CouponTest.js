import http from 'k6/http';
import { check, sleep } from 'k6';

// 🔥 테스트 옵션
export const options = {
    scenarios: {
        coupon_test: {
            executor: 'constant-vus',
            vus: 500,        // 500명 동시 사용자
            duration: '10s', // 10초 동안 유지
        },
    },
};

// 🔥 테스트 시작 전에 1번 실행됨
export function setup() {
    const initUrl = 'http://localhost:8080/admin/coupon/init?couponId=1&quantity=100';

    const res = http.post(initUrl);

    check(res, {
        'init success': (r) => r.status === 200,
    });

    return { couponId: 1 };
}

// 🔥 실제 부하 테스트
export default function (data) {
    const userId = __VU; // 각 VU마다 고유 userId

    const url = `http://localhost:8080/coupons/${data.couponId}/issue?userId=${userId}`;

    const res = http.post(url);

    check(res, {
        'status is 200': (r) => r.status === 200,
    });
}