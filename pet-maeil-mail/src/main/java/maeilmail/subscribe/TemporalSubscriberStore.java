package maeilmail.subscribe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class TemporalSubscriberStore {

    private static final String KEY_FORMAT = "%s:%d";
    private static final String INVALID_EMAIL_MESSAGE = "인증되지 않은 이메일입니다.";
    private final static Map<String, Boolean> store = new ConcurrentHashMap<>();

    public static void add(String email, Long code) {
        store.put(createKey(email, code), false);
    }

    public static void verify(String email, Long code) {
        Boolean isVerified = store.get(createKey(email, code));
        if (isVerified == null) {
            throw new IllegalStateException(INVALID_EMAIL_MESSAGE);
        }

        store.put(createKey(email, code), true);
    }

    public static void requireVerified(String email) {
        String key = store.keySet().stream()
                .filter(it -> it.contains(email))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(INVALID_EMAIL_MESSAGE));

        if (!store.get(key)) {
            throw new IllegalStateException(INVALID_EMAIL_MESSAGE);
        }
    }

    private static String createKey(String email, Long code) {
        return String.format(KEY_FORMAT, email, code);
    }
}
