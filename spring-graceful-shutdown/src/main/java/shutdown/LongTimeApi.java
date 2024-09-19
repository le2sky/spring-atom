package shutdown;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class LongTimeApi {

    @GetMapping("/long")
    public String longTimeJob() throws InterruptedException {
        Thread.sleep(10_000);

        return "Hello, World";
    }
}
