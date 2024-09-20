package maeilmail.subscribe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TemporalSubscriberStoreTest {

    @Test
    @DisplayName("이메일이 인증되지 않으면 검증에 실패한다.")
    void cantVerify() {
        assertThatThrownBy(() -> TemporalSubscriberStore.verify("test1@naver.com", 3211L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("인증되지 않은 이메일입니다.");
    }

    @Test
    @DisplayName("이메일 인증 코드가 다르면 검증에 실패한다.")
    void cantVerify2() {
        TemporalSubscriberStore.add("test2@naver.com", 3212L);

        assertThatThrownBy(() -> TemporalSubscriberStore.verify("test2@naver.com", 3211L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("인증되지 않은 이메일입니다.");
    }

    @Test
    @DisplayName("이메일 인증 코드가 같으면 검증에 성공한다.")
    void cantVerify3() {
        TemporalSubscriberStore.add("test3@naver.com", 3212L);

        assertThatCode(() -> TemporalSubscriberStore.verify("test3@naver.com", 3212L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("주어진 이메일이 검증되지 않았는지 확인한다.")
    void requireVerified() {
        assertThatThrownBy(() -> TemporalSubscriberStore.requireVerified("test4@naver.com"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("인증되지 않은 이메일입니다.");
    }

    @Test
    @DisplayName("주어진 이메일이 검증되었는지 확인한다.")
    void requireVerified2() {
        TemporalSubscriberStore.add("test5@naver.com", 3212L);
        TemporalSubscriberStore.verify("test5@naver.com", 3212L);

        assertThatCode(() -> TemporalSubscriberStore.requireVerified("test5@naver.com"))
                .doesNotThrowAnyException();
    }
    
    @Test
    @DisplayName("이메일에 대한 여러 인증 기록이 존재하는 경우, 하나라도 검증되면 허용한다.")
    void requireVerified3() {
        TemporalSubscriberStore.add("test6@naver.com", 3212L);
        TemporalSubscriberStore.add("test6@naver.com", 1234L);
        TemporalSubscriberStore.verify("test6@naver.com", 3212L);

        assertThatCode(() -> TemporalSubscriberStore.requireVerified("test6@naver.com"))
                .doesNotThrowAnyException();
    }
}
