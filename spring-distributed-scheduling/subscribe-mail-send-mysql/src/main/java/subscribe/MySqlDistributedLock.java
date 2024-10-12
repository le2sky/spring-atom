package subscribe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Component;

@Component
public class MySqlDistributedLock {

    public boolean tryLock(Connection connection, String key, int timeout) {
        String sql = "select get_lock(?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, key);
            preparedStatement.setInt(2, timeout);
            return getLockResult(preparedStatement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean getLockResult(PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                int result = resultSet.getInt(1);
                return result == 1;
            }

            return false;
        }
    }

    public void releaseAllLock(Connection connection) {
        String sql = "select release_all_lock()";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
