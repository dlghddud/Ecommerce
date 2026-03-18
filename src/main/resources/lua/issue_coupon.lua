-- KEYS[1] = stock key
-- KEYS[2] = issued key

-- 중복 체크
if redis.call('exists', KEYS[2]) == 1 then
    return -1
end

-- 재고 확인
local stock = redis.call('get', KEYS[1])
if not stock or tonumber(stock) <= 0 then
    return -2
end

-- 발급 처리
redis.call('decr', KEYS[1])
redis.call('set', KEYS[2], 1)

return 1