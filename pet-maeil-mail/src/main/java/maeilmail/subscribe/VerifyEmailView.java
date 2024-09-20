package maeilmail.subscribe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Component
@RequiredArgsConstructor
class VerifyEmailView implements EmailView {

    private final SpringTemplateEngine templateEngine;

    @Override
    public String render(Map<Object, Object> attribute) {
        Context context = new Context();
        context.setVariable("code", attribute.get("code"));

        return templateEngine.process("verify-email", context);
    }
}
