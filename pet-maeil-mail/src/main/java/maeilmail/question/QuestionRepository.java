package maeilmail.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    default QuestionSummary queryById(Long id) {
        return findOneById(id).orElseThrow(NoSuchElementException::new);
    }

    @Query("""
            select new maeilmail.question.QuestionSummary(q)
            from Question q
            where q.id = :id
             """)
    Optional<QuestionSummary> findOneById(Long id);

    @Query(value = """
            select new maeilmail.question.QuestionSummary(q)
            from Question q
            where
                upper(:categoryName) = 'ALL' OR
                upper(:categoryName) = q.category
            """)
    List<QuestionSummary> queryAll(String categoryName);

    List<Question> findAllByCategory(QuestionCategory category);
}
