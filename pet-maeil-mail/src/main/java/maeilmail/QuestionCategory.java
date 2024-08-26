package maeilmail;

enum QuestionCategory {

    FRONTEND, BACKEND;

    public String toLowerCase() {
        return this.name().toLowerCase();
    }
}
