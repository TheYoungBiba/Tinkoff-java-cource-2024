package edu.java.scrapper;

import edu.java.scrapper.AbstractIntegrationDBTest;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationDBTest extends AbstractIntegrationDBTest {
    @Test
    public void tablesTest() throws SQLException {
        String testCase = "INSERT INTO users (id, first_name) VALUES (12345678, 'user12345678')";
        long referentId = 12345678;
        String referentFirstName = "user12345678";
        Connection conn = DriverManager.getConnection(
            POSTGRES.getJdbcUrl(),
            POSTGRES.getUsername(),
            POSTGRES.getPassword()
        );
        Statement statement = conn.createStatement();
        statement.executeUpdate(testCase);
        ResultSet testResult = statement.executeQuery("SELECT * FROM users WHERE id = 12345678");
        assertAll(
            () -> assertTrue(testResult.next()),
            () -> assertEquals(referentId, testResult.getInt("id")),
            () -> assertEquals(referentFirstName, testResult.getString("first_name"))
        );
    }
}
