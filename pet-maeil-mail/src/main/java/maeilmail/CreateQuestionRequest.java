package maeilmail;

record CreateQuestionRequest(String title, String content, String category) {

    public Question toDomainEntity() {
        return new Question(title, content, QuestionCategory.from(category));
    }
}
