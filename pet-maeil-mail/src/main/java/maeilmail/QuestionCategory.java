package maeilmail;

import java.util.Arrays;
import java.util.NoSuchElementException;

enum QuestionCategory {

    FRONTEND, BACKEND;

    public static QuestionCategory from(String category) {
        return Arrays.stream(QuestionCategory.values())
                .filter((it) -> it.name().equalsIgnoreCase(category))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public String toLowerCase() {
        return this.name().toLowerCase();
    }
}
