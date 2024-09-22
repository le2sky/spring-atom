package maeilmail.subscribe;

import maeilmail.question.Question;

public interface ChoiceQuestionPolicy {

    Question choice(Subscribe subscribe);
}
