package jdbc;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class CustomConnector {
    private final Properties properties = new Properties();

    public CustomConnector() throws IOException {
        properties.load(new FileReader("src/main/resources/app.properties"));
    }

    public Connection getConnection(String url) throws SQLException {
        return getConnection(url, properties.getProperty("postgres.user"),
                properties.getProperty("postgres.password"));
    }

    public Connection getConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection() {
        try {
            return this.getConnection(properties.getProperty("postgres.url"), properties.getProperty("postgres.user"),
                    properties.getProperty("postgres.password"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
