package coupon.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TestServiceTest {

    @Test
    @DisplayName("동시화 테스트")
    void synchronizedTest() {
        for (int i = 0; i < 1000; i++) {

        }
        extracted();

    }

    private void extracted() {
        TestService testService = new TestService();
        Thread thread1 = new Thread(() -> testService.한명씩_드루와ㅋㅋ(1L));
        Thread thread2 = new Thread(() -> testService.한명씩_드루와ㅋㅋ(2L));

        thread1.start();
        thread2.start();
    }
}
