package coupon.api;

import coupon.application.MemberCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class MemberCouponApi {

    private final MemberCouponService memberCouponService;

    @PostMapping("/member-coupon")
    public ResponseEntity<Void> issueCoupon(@RequestBody IssueCouponRequest request) {
        memberCouponService.issue(request.memberId(), request.couponId());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/member-coupon/{memberCouponId}/exchange")
    public ResponseEntity<Void> issueCoupon(@PathVariable Long memberCouponId) {
        memberCouponService.exchange(memberCouponId);

        return ResponseEntity.noContent().build();
    }
}
