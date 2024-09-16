package jakarta.http;

import java.io.IOException;
import jakarta.servlet.http.HttpSession;

public interface HttpSessionWrapper {

    HttpSession getSession(boolean sessionCreateIfAbsent, String sessionId) throws IOException;
}
