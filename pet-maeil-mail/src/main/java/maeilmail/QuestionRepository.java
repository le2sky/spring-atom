package maeilmail;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface QuestionRepository extends JpaRepository<Question, Long> {

    default QuestionSummary queryById(Long id) {
        return findOneById(id).orElseThrow(NoSuchElementException::new);
    }

    @Query("""
            select new maeilmail.QuestionSummary(q)
            from Question q
            where q.id = :id
             """)
    Optional<QuestionSummary> findOneById(Long id);
}
