package maeilmail.subscribe;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class SubscribeApi {

    private final SubscribeQuestionService subscribeQuestionService;

    @PostMapping("/subscribe/verify")
    public ResponseEntity<Void> request(@RequestBody VerifyEmailRequest request) {
        subscribeQuestionService.verify(request);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/subscribe/check-verified")
    public ResponseEntity<Void> checkVerified(@RequestBody CheckVerifiedRequest request) {
        subscribeQuestionService.checkVerified(request);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribe(@RequestBody SubscribeQuestionRequest request) {
        subscribeQuestionService.subscribe(request);

        return ResponseEntity.noContent().build();
    }
}
