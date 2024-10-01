package coupon.infrastructure.lock;

import java.time.Duration;
import coupon.application.MemberCouponIssueLock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RettuceMemberCouponIssueLock implements MemberCouponIssueLock {

    private final StringRedisTemplate redisTemplate;

    @Override
    public boolean lock(Long memberId, Long couponId) {
        Boolean result = redisTemplate
                .opsForValue() // opsForX는 커맨드를 호출할 수 있는 기능을 모은 인터페이스를 반환
                .setIfAbsent(generateKey(memberId, couponId), "empty", Duration.ofSeconds(3));

        return Boolean.TRUE.equals(result);
    }

    @Override
    public void unlock(Long memberId, Long couponId) {
        redisTemplate.delete(generateKey(memberId, couponId));
    }

    private String generateKey(Long memberId, Long couponId) {
        return "memberCoupon-" + memberId.toString() + couponId.toString();
    }
}
