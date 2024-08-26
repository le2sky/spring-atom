package maeilmail.subscribe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import maeilmail.question.QuestionCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SubscribeQuestionServiceTest {

    @Autowired
    private SubscribeQuestionService subscribeQuestionService;

    @Autowired
    private SubscribeRepository subscribeRepository;

    @Test
    @DisplayName("면접 질문을 받도록 구독한다.")
    void subscribe() {
        subscribeQuestionService.subscribe("test@gmail.com", QuestionCategory.BACKEND);

        List<Subscribe> result = subscribeRepository.findAll();

        assertThat(result).hasSize(1);
    }
}
