package maeilmail.subscribe.policy;

import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import maeilmail.question.Question;
import maeilmail.question.QuestionRepository;
import maeilmail.subscribe.ChoiceQuestionPolicy;
import maeilmail.subscribe.Subscribe;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RandomChoicePolicy implements ChoiceQuestionPolicy {

    private final QuestionRepository questionRepository;

    @Override
    public Question choice(Subscribe subscribe) {
        Random rand = new Random();
        List<Question> questions = questionRepository.findAllByCategory(subscribe.getCategory());
        int index = rand.nextInt(questions.size());

        return questions.get(index);
    }
}
