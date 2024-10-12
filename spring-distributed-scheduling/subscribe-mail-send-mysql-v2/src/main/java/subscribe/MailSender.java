package subscribe;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class MailSender {

    private final SubscribeRepository subscribeRepository;
    private final ApplicationContext applicationContext;
    private final MySqlDistributedLock distributedLock;

    @Scheduled(cron = "0 * * * * *")
    public void sendMail() {
        List<Subscribe> subscribes = subscribeRepository.findAll();
        log.info("[start] {}명의 구독자에게 메일을 전송을 시도합니다.", subscribes.size());

        int totalCount = 0;
        DataSource lockDataSource = getLockDataSource();
        try (Connection connection = lockDataSource.getConnection()) {
            totalCount = sendAll(connection, subscribes, totalCount);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        log.info("[end] {}명의 구독자에게 메일을 성공적으로 보냈습니다.", totalCount);
    }

    private int sendAll(Connection connection, List<Subscribe> subscribes, int totalCount) {
        for (Subscribe subscribe : subscribes) {
            boolean canSend = distributedLock.tryLock(connection, "schedule" + subscribe.getId(), 0);
            if (canSend) {
                totalCount = send(totalCount, subscribe);
            }
        }

        return totalCount;
    }

    private int send(int totalCount, Subscribe subscribe) {
        try {
            Thread.sleep(100);
        } catch (Exception ignored) {
        }
        log.info("{}번 구독자에게 메일을 전송했습니다.", subscribe.getId());
        return totalCount + 1;
    }

    private DataSource getLockDataSource() {
        return applicationContext.getBean(DataSourceConfig.LOCK_DATA_SOURCE, DataSource.class);
    }
}
