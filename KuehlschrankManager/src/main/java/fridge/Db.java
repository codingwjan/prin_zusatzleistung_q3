package fridge;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Db {
    // MySQL Verbindung
    private static final String URL = "jdbc:mysql://localhost:3306/fridge?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false";

    // Anmeldedaten
    private static String USER = "root";
    private static String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void ensureDatabase() {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS items (" +
                    "name VARCHAR(255) PRIMARY KEY, " +
                    "quantity INTEGER NOT NULL CHECK(quantity >= 0)" +
                    ");";
            stmt.execute(sql);

        } catch (SQLException e) {
            System.err.println("Datenbank-Fehler (MySQL): " + e.getMessage());
            System.exit(1);
        }
    }
}
