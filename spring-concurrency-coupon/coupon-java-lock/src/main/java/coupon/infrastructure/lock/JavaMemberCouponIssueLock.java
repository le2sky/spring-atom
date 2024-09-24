package coupon.infrastructure.lock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import coupon.application.MemberCouponIssueLock;
import org.springframework.stereotype.Component;

@Component
public class JavaMemberCouponIssueLock implements MemberCouponIssueLock {

    private final Map<String, Lock> lockTable = new ConcurrentHashMap<>();

    @Override
    public void lock(Long memberId, Long couponId) {
        Lock lock = lockTable.computeIfAbsent(createKey(memberId, couponId), k -> new ReentrantLock());
        lock.lock();
    }

    @Override
    public void unlock(Long memberId, Long couponId) {
        String key = createKey(memberId, couponId);
        Lock lock = lockTable.get(key);
        if (lock != null) {
            lock.unlock();
        }
    }

    private String createKey(Long memberId, Long couponId) {
        return memberId + "-" + couponId;
    }
}
