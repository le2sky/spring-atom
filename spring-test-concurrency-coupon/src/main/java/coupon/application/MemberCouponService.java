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
class MemberCouponService {

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final BenefitRepository benefitRepository;

    @Transactional
    public void issue(Long memberId, Long couponId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Coupon coupon = couponRepository.findById(couponId).orElseThrow();
        MemberCoupon memberCoupon = MemberCoupon.issue(member, coupon);

        memberCouponRepository.save(memberCoupon);
    }

    @Transactional
    public void exchange(Long memberCouponId) {
        MemberCoupon memberCoupon = memberCouponRepository.findById(memberCouponId).orElseThrow();
        Benefit exchange = memberCoupon.exchange();

        benefitRepository.save(exchange);
    }
}
