package maeilmail.subscribe;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class SendQuestionScheduler {

    @Scheduled(cron = "0 0 7 1/1 * ?", zone = "Asia/Seoul")
    public void sendMail() {
    }
}
