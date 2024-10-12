package subscribe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
class SubscribeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubscribeApplication.class, args);
    }
}
