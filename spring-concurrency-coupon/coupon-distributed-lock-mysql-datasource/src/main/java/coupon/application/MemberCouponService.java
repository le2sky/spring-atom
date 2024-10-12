package coupon.application;

import javax.sql.DataSource;
import java.sql.Connection;
import coupon.DataSourceConfig;
import coupon.domain.Benefit;
import coupon.domain.BenefitRepository;
import coupon.domain.MemberCoupon;
import coupon.domain.MemberCouponRepository;
import coupon.infra.MySqlDistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberCouponService {

    private final MemberCouponRepository memberCouponRepository;
    private final BenefitRepository benefitRepository;
    private final MemberCouponIssuer memberCouponIssuer;
    private final MySqlDistributedLock distributedLock;
    private final ApplicationContext applicationContext;

    public Long issue(Long memberId, Long couponId) {
        String key = memberId + "-" + couponId;

        DataSource lockDataSource = getLockDataSource();
        try (Connection connection = lockDataSource.getConnection()) {
            distributedLock.tryLock(connection, key, 3);
            try {
                return memberCouponIssuer.issue(memberId, couponId);
            } finally {
                distributedLock.releaseLock(connection, key);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private DataSource getLockDataSource() {
        return applicationContext.getBean(DataSourceConfig.LOCK_DATA_SOURCE, DataSource.class);
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
