package coupon.application;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import coupon.domain.Benefit;
import coupon.domain.BenefitRepository;
import coupon.domain.MemberCoupon;
import coupon.domain.MemberCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberCouponService {

    private final MemberCouponIssuer memberCouponIssuer;
    private final MemberCouponIssueLock memberCouponIssueLock;
    private final MemberCouponRepository memberCouponRepository;
    private final BenefitRepository benefitRepository;
    private final HikariDataSource hikariDataSource;

    public Long issue(Long memberId, Long couponId) {
        HikariPoolMXBean hikariPoolMXBean = hikariDataSource.getHikariPoolMXBean();
        String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();

        log.info("active connection count = {} current transaction = {}", hikariPoolMXBean.getActiveConnections(), currentTransactionName);
        memberCouponIssueLock.lock(memberId, couponId);
        log.info("active connection count = {} current transaction = {}", hikariPoolMXBean.getActiveConnections(), currentTransactionName);

        try {
            return memberCouponIssuer.issue(memberId, couponId);
        } finally {
            memberCouponIssueLock.unlock(memberId, couponId);
        }
    }

    @Transactional
    public void exchange(Long memberCouponId) {
        log.info("쿠폰 금액 교환 memberCouponId = {}", memberCouponId);

        MemberCoupon memberCoupon = memberCouponRepository.findById(memberCouponId).orElseThrow();
        Benefit exchange = memberCoupon.exchange();
        benefitRepository.save(exchange);

        log.info("금액 교환 종료 memberCouponId = {}", memberCouponId);
    }
}
