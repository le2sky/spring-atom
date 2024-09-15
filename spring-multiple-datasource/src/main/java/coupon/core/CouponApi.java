package coupon.core;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class CouponApi {

    private final CouponService couponService;

    @PostMapping("/coupon/{id}/issue")
    public ResponseEntity<Void> issue(@PathVariable Long id) {
        couponService.issue(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/coupon")
    public ResponseEntity<List<CouponSummary>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }
}
