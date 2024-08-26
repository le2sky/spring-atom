package maeilmail.subscribe;

import lombok.RequiredArgsConstructor;
import maeilmail.question.QuestionCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class SubscribeApi {

    private final SubscribeQuestionService subscribeQuestionService;

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribe(@RequestBody SubscribeQuestionRequest request) {
        subscribeQuestionService.subscribe(request.email(), QuestionCategory.from(request.category()));

        return ResponseEntity.noContent().build();
    }
}
