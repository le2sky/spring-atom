package coupon.application;

import coupon.domain.Benefit;
import coupon.domain.BenefitRepository;
import coupon.domain.Coupon;
import coupon.domain.CouponRepository;
import coupon.domain.Member;
import coupon.domain.MemberCoupon;
import coupon.domain.MemberCouponRepository;
import coupon.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberCouponService {

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final BenefitRepository benefitRepository;

    @Transactional
    public Long issue(Long memberId, Long couponId) {
        log.info("신규 쿠폰 발급 coupon = {}, member = {}", couponId, memberId);

        validateAlreadyIssued(memberId, couponId);
        Member member = memberRepository.findById(memberId).orElseThrow();
        Coupon coupon = couponRepository.findById(couponId).orElseThrow();
        MemberCoupon memberCoupon = MemberCoupon.issue(member, coupon);
        memberCouponRepository.save(memberCoupon);

        log.info("쿠폰 발급 종료 member = {}", memberId);
        return memberCoupon.getId();
    }

    private void validateAlreadyIssued(Long memberId, Long couponId) {
        if (memberCouponRepository.existsMemberCouponByMemberIdAndCouponId(memberId, couponId)) {
            throw new IllegalStateException("해당 사용자는 이미 쿠폰을 발급했습니다.");
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
