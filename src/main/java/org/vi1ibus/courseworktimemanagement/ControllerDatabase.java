package org.vi1ibus.courseworktimemanagement;

import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;

public class ControllerDatabase {
    private static String url="jdbc:mysql://localhost:3306/app?useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String username="root";
    private static final String password="root";
    private static Connection connection;
    private static PreparedStatement statement;

    public static void connectDB() throws ClassNotFoundException, SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    public static User authenticationUser(String login, String password) {
        try {
            connectDB();
            String query = "SELECT * from users WHERE login=? and password=?;";
            statement = connection.prepareStatement(query);
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(4));
        } catch (SQLException | ClassNotFoundException e){
            return null;
        }
    }

    public static int registrationUser(String login, String password, String email){
        try {
            connectDB();
            String query = "INSERT INTO users(login, password, email) VALUES (?, ?, ?);";
            statement = connection.prepareStatement(query);
            statement.setString(1, login);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.executeUpdate();
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

    public static void createTaskList(TaskList taskList){
        try {
            connectDB();
            String query = "INSERT INTO tasks_list(name, everyone, owner) VALUES (?, ?, ?);";
            statement = connection.prepareStatement(query);
            statement.setString(1, taskList.getName());
            statement.setInt(2, taskList.getEveryone());
            statement.setInt(3, taskList.getOwner());
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e){
            return;
        }
    }

    public static ArrayList<TaskList> getOwnerTasksLists(int userId){
        try {
            connectDB();
            ArrayList<TaskList> tasksLists = new ArrayList<>();
            String query = "SELECT * FROM tasks_list WHERE owner=?;";
            statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                tasksLists.add(new TaskList(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4)
                ));
            }
            return tasksLists;
        } catch (SQLException | ClassNotFoundException e){
            return null;
        }
    }

    public static boolean checkDuplicateNameTaskList(int userId, String name){
        try {
            connectDB();
            String query = "SELECT * FROM tasks_list WHERE name=? and owner=?;";
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setInt(2, userId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException | ClassNotFoundException e){
            return false;
        }
    }

    public static TaskList getTaskList(int taskListId){
        try {
            connectDB();
            String query = "SELECT * FROM tasks_list WHERE id=?;";
            statement = connection.prepareStatement(query);
            statement.setInt(1, taskListId);
            ResultSet resultSet = statement.executeQuery();
            return new TaskList(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getInt(4)
            );

        } catch (SQLException | ClassNotFoundException e){
            return null;
        }
    }

    public static ArrayList<Pair<String, Integer>> getTaskListStatuses(int taskListId) {
        try {
            connectDB();
            String query = "SELECT id, name FROM statuses WHERE tasks_list_id=?;";
            statement = connection.prepareStatement(query);
            statement.setInt(1, taskListId);
            ResultSet resultStatusNames = statement.executeQuery();
            ArrayList<Pair<String, Integer>> arrayList = new ArrayList<>();
            while (resultStatusNames.next()){
                query = "SELECT COUNT(*) FROM task WHERE tasks_list_id=? AND status=?;";
                statement = connection.prepareStatement(query);
                statement.setInt(1, taskListId);
                statement.setInt(2, resultStatusNames.getInt(1));
                ResultSet resultStatusCount = statement.executeQuery();
                resultStatusCount.next();
                arrayList.add(new Pair<>(resultStatusNames.getString(2), resultStatusCount.getInt(1)));
            }
            return arrayList;
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }



}
