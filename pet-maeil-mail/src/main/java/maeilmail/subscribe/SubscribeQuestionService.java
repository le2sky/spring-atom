package maeilmail.subscribe;

import lombok.RequiredArgsConstructor;
import maeilmail.question.QuestionCategory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class SubscribeQuestionService {

    private final SubscribeRepository subscribeRepository;

    @Transactional
    public void subscribe(String email, QuestionCategory questionCategory) {
        Subscribe subscribe = new Subscribe(email, questionCategory);

        subscribeRepository.save(subscribe);
    }
}
