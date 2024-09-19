package coupon.application;

import coupon.domain.Coupon;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

// 쿠폰과 관련된 동시성 테스트 환경을 설정한다.
interface CouponConcurrencyTaskSetting {

    void initializeTask(
            int threadCount,
            ExecutorService executorService,
            CountDownLatch finishLatch,
            CountDownLatch startLatch,
            Coupon coupon
    );
}
