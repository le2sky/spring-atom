package coupon.core;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 1. 발행 요청이 들어오면 쿠폰의 잔여 발행 제한을 확인한다.
 * 2. 발행 수를 증가시킨 이후에는 발행 로그를 출력한다.
 * 3. 전체 쿠폰을 조회할 수 있다.
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
class CouponService {

    private final CouponRepository couponRepository;

    @Transactional
    public void issue(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new NoSuchElementException("coupon id " + couponId + " not found"));

        coupon.issue();

        log.info("{} 쿠폰이 발행되었습니다.", coupon.getId());
    }

    public List<CouponSummary> getAllCoupons() {
        return couponRepository.queryCouponSummaries();
    }
}
