package subscribe;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class MailSender {

    private final SubscribeRepository subscribeRepository;

    @Scheduled(cron = "0 * * * * *")
    public void sendMail() {
        List<Subscribe> subscribes = subscribeRepository.findAll();
        log.info("[start] {}명의 구독자에게 메일을 전송을 시도합니다.", subscribes.size());

        int totalCount = 0;
        for (Subscribe subscribe : subscribes) {
            totalCount = send(totalCount, subscribe);
        }

        log.info("[end] {}명의 구독자에게 메일을 성공적으로 보냈습니다.", totalCount);
    }

    private int send(int totalCount, Subscribe subscribe) {
        return totalCount + 1;
    }
}
