package maeilmail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class AdminQuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public void createQuestion(Question question) {
        questionRepository.save(question);
    }
}
