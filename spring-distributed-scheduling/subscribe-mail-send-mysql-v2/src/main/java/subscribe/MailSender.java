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
        int batchSize = 10;
        int nowSize = 1;
        int index = 0;
        int loopCount = (int) (Math.ceil(((double) subscribes.size() / batchSize)));

        while (index++ < loopCount) {
            boolean canSend = distributedLock.tryLock(connection, "schedule-" + nowSize, 0);
            if (canSend) {
                int from = nowSize - 1;
                int to = from + batchSize;
                totalCount = sendRangeWithLock(subscribes, from, to, totalCount);
            }

            nowSize += batchSize;
        }

        return totalCount;
    }

    private int sendRangeWithLock(List<Subscribe> subscribes, int from, int to, int totalCount) {
        log.info("{}부터 {}까지 잠금을 수행합니다.", from, to);
        for (int index = from; index < to; index++) {
            totalCount = trySend(subscribes, totalCount, index);
        }

        return totalCount;
    }

    private int trySend(List<Subscribe> subscribes, int totalCount, int index) {
        if (index < subscribes.size()) {
            totalCount = send(totalCount, subscribes.get(index));
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
