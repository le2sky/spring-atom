package maeilmail.subscribe;

import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maeilmail.question.Question;
import maeilmail.question.QuestionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
class SendQuestionScheduler {

    private final EmailSender mailSender;
    private final SubscribeRepository subscribeRepository;
    private final QuestionRepository questionRepository;

    @Scheduled(cron = "0 0 7 1/1 * ?", zone = "Asia/Seoul")
    @Transactional
    public void sendMail() {
        log.info("메일 전송을 시작합니다.");
        List<Subscribe> subscribes = subscribeRepository.findAll();
        log.info("{}명의 사용자에게 메일을 전송합니다.", subscribes.size());

        subscribes.stream()
                .map(this::selectRandomQuestionAndMapToMail)
                .forEach(mailSender::sendMail);

        log.info("메일 전송을 마칩니다.");
    }

    private MailMessage selectRandomQuestionAndMapToMail(Subscribe subscribe) {
        Random rand = new Random();
        List<Question> questions = questionRepository.findAllByCategory(subscribe.getCategory());
        int index = rand.nextInt(questions.size());
        Question question = questions.get(index);

        return new MailMessage(subscribe.getEmail(), question.getTitle());
    }
}
