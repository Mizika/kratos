package com.kratos.pluginDB;

import com.intellij.openapi.actionSystem.AnActionEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class SQLiteAction {

    AnActionEvent event;
    public SQLiteAction(AnActionEvent event) {
        this.event = event;
    }

    public void createDBWithTableUsers(){
        Connection connection = createConnection(event.getProject().getBasePath());
        createTableInDb(connection);
        closeConnection(connection);
    }

    public String selectDataFromTableUsers(){
        Connection connection = createConnection(event.getProject().getBasePath());
        String token = selectFirstToken(connection);
        closeConnection(connection);
        return token;
    }

    public void deleteDataFromTableUsers(){
        Connection connection = createConnection(event.getProject().getBasePath());
        deleteAllFromTable(connection);
        closeConnection(connection);
    }

    public void saveTokenToTableUsers(String token){
        Connection connection = createConnection(event.getProject().getBasePath());
        addTokenInTable(connection, token);
        closeConnection(connection);
    }

    /**
     * Создание коннекшена
     */
    private Connection createConnection(String projectDir) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" +createBuildDir(projectDir)+ File.separator +"kratos.db");
            log.info("SQLite database created successfully. Connection established.");
        } catch (SQLException e) {
            log.error("SQLite database can not create:" + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    /**
     * Убить коннекшен
     */
    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                log.info("SQLite database connection stop successfully.");
            } catch (SQLException e) {
                log.error("Connection cannot be stopped: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Создание таблицы users в БД
     */
    private void createTableInDb(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "token TEXT NOT NULL)";
            statement.executeUpdate(createTableSQL);
            log.info("Table created successfully.");
        } catch (SQLException e) {
            log.error("Table can not created: " + e.getMessage());
            e.printStackTrace();

        }
    }

    /**
     * Добавление токена в таблицу users
     */
    private void addTokenInTable(Connection connection, String token){
        String insertSQL = "INSERT INTO users(token) VALUES(?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, token);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            log.info("Token add successfully.");
        } catch (SQLException e) {
            log.error("Error while adding token: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * Очистить все данные в таблице users
     */
    private void deleteAllFromTable(Connection connection) {
        String deleteSQL = "DELETE FROM users";
        try (Statement statement = connection.createStatement()) {
            int rowCount = statement.executeUpdate(deleteSQL);
            log.info("Deleted " + rowCount + " rows from table");
        } catch (SQLException e) {
            log.error("Error while deleting from table: " + e.getMessage());
            throw new RuntimeException("Failed to delete from table.", e);
        }
    }

    /**
     * Вывбрать первый токен
     */
    private String selectFirstToken(Connection connection) {
        String selectSQL = "SELECT token FROM users LIMIT 1";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectSQL);
            if (resultSet.next()) {
                return resultSet.getString("token");
            } else {
                log.info("No tokens found.");
                return null;
            }
        } catch (SQLException e) {
            log.error("Error while selecting first token: " + e.getMessage());
            throw new RuntimeException("Failed to select first token.", e);
        }
    }

    public String createBuildDir(String projectDir) {
        Path projectPath = Paths.get(projectDir);
        Path buildDirPath = projectPath.resolve("build");
        try {
            if (!Files.exists(buildDirPath)) {
                Files.createDirectories(buildDirPath);
                log.info("Build directory created: " + buildDirPath);
            } else {
                log.info("Build directory already exists: " + buildDirPath);
            }
        } catch (IOException e) {
            log.error("Failed to create the build directory: " + e.getMessage());
        }
        return buildDirPath.toString();
    }

}
