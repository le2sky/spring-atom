package coupon.infrastructure.lock;

import java.util.concurrent.TimeUnit;
import coupon.application.MemberCouponIssueLock;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RedissonMemberCouponIssueLock implements MemberCouponIssueLock {

    private final RedissonClient redissonClient;

    @Override
    public void lock(Long memberId, Long couponId) {
        RLock lock = redissonClient.getLock(generateKey(memberId, couponId));
        try {
            boolean acquired = lock.tryLock(5, TimeUnit.SECONDS);
            if (!acquired) {
                // 락 획득 실패
                throw new RuntimeException();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unlock(Long memberId, Long couponId) {
        RLock lock = redissonClient.getLock(generateKey(memberId, couponId));
        lock.unlock();
    }

    private String generateKey(Long memberId, Long couponId) {
        return "memberCoupon-" + memberId.toString() + couponId.toString();
    }
}
