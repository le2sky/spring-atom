package coupon.infra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.springframework.stereotype.Component;

@Component
public class MySqlDistributedLock {

    public void tryLock(Connection connection, String key, int timeout) {
        String sql = "select get_lock(?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, key);
            preparedStatement.setInt(2, timeout);
            preparedStatement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void releaseLock(Connection connection, String key) {
        String sql = "select release_lock(?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, key);
            preparedStatement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
