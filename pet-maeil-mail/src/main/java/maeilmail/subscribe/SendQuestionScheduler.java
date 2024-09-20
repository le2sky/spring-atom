package maeilmail.subscribe;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maeilmail.question.Question;
import maeilmail.question.QuestionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
class SendQuestionScheduler {

    private final EmailSender mailSender;
    private final SubscribeQuestionView subscribeQuestionView;
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
        log.info("{}님에게 메일을 전송합니다.", subscribe.getEmail());
        String subject = "오늘의 면접 질문을 보내드려요.";
        Question question = choiceQuestion(subscribe);
        String text = createText(question);

        return new MailMessage(subscribe.getEmail(), subject, text);
    }

    private Question choiceQuestion(Subscribe subscribe) {
        Random rand = new Random();
        List<Question> questions = questionRepository.findAllByCategory(subscribe.getCategory());
        int index = rand.nextInt(questions.size());

        return questions.get(index);
    }

    private String createText(Question question) {
        HashMap<Object, Object> attribute = new HashMap<>();
        attribute.put("questionId", question.getId());
        attribute.put("question", question.getTitle());

        return subscribeQuestionView.render(attribute);
    }
}
