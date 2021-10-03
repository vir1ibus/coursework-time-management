package org.vi1ibus.courseworktimemanagement;

import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;

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

    public static User authenticationUser(String login, String password) {
        try {
            connectDB();
            String query = "SELECT * from users WHERE login='" + login + "' and password='" + password + "';";
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            int id = Integer.parseInt(resultSet.getString(1));
            return new User(Integer.parseInt(resultSet.getString(1)), resultSet.getString(2), resultSet.getString(4));
        } catch (SQLException | ClassNotFoundException e){
            return null;
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

    public static void createTaskList(TaskList taskList){
        try {
            connectDB();
            String query = "INSERT INTO tasks_list(name, everyone, owner) VALUES ('"
                    + taskList.getName() + "', "
                    + taskList.getEveryone() + ", "
                    + taskList.getOwner() + ");";
            statement.executeUpdate(query);
        } catch (SQLException | ClassNotFoundException e){
            return;
        }
    }

    public static TaskList getTaskList(int taskListId){
        try {
            connectDB();
            String query = "SELECT * FROM tasks_list WHERE id=" + taskListId + ";";
            ResultSet resultSet = statement.executeQuery(query);

            return new TaskList(
                    Integer.parseInt(resultSet.getString(1)),
                    resultSet.getString(2),
                    Integer.parseInt(resultSet.getString(3)),
                    Integer.parseInt(resultSet.getString(4))
            );

        } catch (SQLException | ClassNotFoundException e){
            return null;
        }
    }

    public static ArrayList<Pair<String, Integer>> getTaskListStatuses(int taskListId) {
        try {
            connectDB();
            String query = "SELECT id, name FROM statuses WHERE tasks_list_id=" + taskListId + ";";
            ResultSet resultSetNames = statement.executeQuery(query);

            ArrayList<Pair<String, Integer>> arrayList = new ArrayList<>();
            while (resultSetNames.next()){
                query = "SELECT count(*) FROM task WHERE tasks_list_id=" + taskListId + " and status="+ resultSetNames.getString(1) + ";";
                ResultSet resultSetCount = statement.executeQuery(query);
                arrayList.add(new Pair<>(resultSetNames.getString(2), Integer.parseInt(resultSetCount.getString(1))));
            }
            return arrayList;
        } catch (SQLException | ClassNotFoundException e){
            return null;
        }
    }



}
