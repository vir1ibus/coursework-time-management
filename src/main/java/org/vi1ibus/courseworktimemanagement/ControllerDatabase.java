package org.vi1ibus.courseworktimemanagement;

import java.io.IOException;
import java.sql.*;

public class ControllerDatabase {
    private static final String url="jdbc:mysql://localhost:3306/app?useSSL=true&serverTimezone=UTC";
    private static final String username="root";
    private static final String password="root";
    private static Connection connection;
    private static Statement statement;

    public static void connectDB() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection= DriverManager.getConnection(url, username, password);
        statement = connection.createStatement();
    }

    public static void disconnectDB() throws SQLException {
        statement.close();
        connection.close();
    }

    public static int authenticationUser(String login, String password) {
        try {
            connectDB();
            String query = "SELECT * from users WHERE login='" + login + "' and password='" + password + "';";
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            int id = Integer.parseInt(resultSet.getString(1));
            return id;
        } catch (SQLException | ClassNotFoundException e){
            return -1;
        }
    }

    public static int registrationUser(String login, String password, String email){
        try {
            connectDB();
            String query = "INSERT INTO users(login, password, email) VALUES ('" + login + "', '" + password + "', '" + email +"');";
            statement.executeUpdate(query);
            return 0;
        } catch (SQLIntegrityConstraintViolationException e){
            if(e.toString().contains("login")){
                return 1;
            }
            if(e.toString().contains("email")){
                return 2;
            }
            return -1;
        } catch (SQLException | ClassNotFoundException e){
            return -1;
        }
    }

    public static boolean checkExistenceUserLogin(String login){
        try {
            connectDB();
            String query = "SELECT * from users WHERE login='" + login + "';";
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            resultSet.getString(1);
            return true;
        } catch (SQLException | ClassNotFoundException e){
            return false;
        }
    }

    public static boolean checkExistenceUserEmail(String email){
        try {
            connectDB();
            String query = "SELECT * from users WHERE email='" + email + "';";
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            resultSet.getString(1);
            return true;
        } catch (SQLException | ClassNotFoundException e){
            return false;
        }
    }
}
