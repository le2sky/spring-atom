package maeilmail.subscribe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class TemporalSubscriberStore {

    private static final int EMAIL_POSITION = 0;
    private static final String KEY_DELIMITER = ":";
    private static final String KEY_FORMAT = "%s" + KEY_DELIMITER + "%d";
    private static final String INVALID_EMAIL_MESSAGE = "인증되지 않은 이메일입니다.";
    private static final Map<String, Boolean> store = new ConcurrentHashMap<>();

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
        boolean isVerified = store.keySet().stream()
                .filter(it -> isSameEmail(it, email))
                .anyMatch(store::get);

        if (!isVerified) {
            throw new IllegalStateException(INVALID_EMAIL_MESSAGE);
        }
    }

    private static boolean isSameEmail(String key, String email) {
        String[] keyToken = key.split(KEY_DELIMITER);

        return keyToken[EMAIL_POSITION].equals(email);
    }

    private static String createKey(String email, Long code) {
        return String.format(KEY_FORMAT, email, code);
    }
}
