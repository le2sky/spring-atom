package coupon.support;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.utility.DockerImageName;

public class RedisSetup implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
        RedisContainer redis = new RedisContainer(DockerImageName.parse("redis:alpine")).withExposedPorts(6379);
        redis.start();

        System.setProperty("spring.data.redis.host", redis.getHost());
        System.setProperty("spring.data.redis.port", String.valueOf(redis.getMappedPort(6379)));
    }
}
