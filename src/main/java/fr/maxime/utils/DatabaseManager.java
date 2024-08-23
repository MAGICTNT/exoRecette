package fr.maxime.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URI = "jdbc:postgresql://localhost:5432/exoRecette";
    private static final String USER = "postgres";
    private static final String PASSWORD = "2203";

    public static Connection getConnection() throws SQLException{
        Connection connection = DriverManager.getConnection(URI,USER,PASSWORD);
        connection.setAutoCommit(false);
        return connection;
    }
}