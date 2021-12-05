package org.vi1ibus.courseworktimemanagement;

import javafx.util.Pair;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ControllerDatabase {
    private static String url="jdbc:mysql://localhost:3306/app?useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String username="root";
    private static final String password="root";
    private static Connection connection;
    private static PreparedStatement statement;

    public static void connectDB() throws SQLException { connection = DriverManager.getConnection(url, username, password); }

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
        } catch (SQLException e){
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
        } catch (SQLException e){
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
        } catch (SQLException e){
            return;
        }
    }

    public static void deleteOwnerTasksLists(int taskListId){
        try {
            connectDB();
            String query = "SELECT * FROM tasks_list WHERE id=? AND owner=?;";
            statement = connection.prepareStatement(query);
            statement.setInt(1, taskListId);
            statement.setInt(2, MainApplication.getUser().getUserID());
            if(statement.executeQuery().next()) {
                query = "SELECT id FROM statuses WHERE tasks_list_id=?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, taskListId);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    query = "DELETE FROM task WHERE tasks_list_id=? AND status_id=?;";
                    statement = connection.prepareStatement(query);
                    statement.setInt(1, taskListId);
                    statement.setInt(2, resultSet.getInt(1));
                    statement.executeUpdate();
                }
                query = "DELETE FROM statuses WHERE tasks_list_id=?;";
                statement = connection.prepareStatement(query);
                statement.setInt(1, taskListId);
                statement.executeUpdate();
                query = "DELETE FROM tasks_list WHERE id=?;";
                statement = connection.prepareStatement(query);
                statement.setInt(1, taskListId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        } catch (SQLException e){
            return null;
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

        } catch (SQLException e){
            return null;
        }
    }

    public static HashMap<String, String> getStatuses(int taskListId){
        try {
            connectDB();
            HashMap<String, String> hashMap = new HashMap<>();
            String query = "SELECT * FROM statuses WHERE tasks_list_id=?;";
            statement = connection.prepareStatement(query);
            statement.setInt(1, taskListId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                hashMap.put(String.valueOf(resultSet.getInt(1)), resultSet.getString(2));
            }
            return hashMap;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void createStatus(String name){
        try {
            connectDB();
            String query = "INSERT INTO statuses(name, tasks_list_id) VALUES (?, ?);";
            statement = connection.prepareStatement(query);
            statement.setString(1, name.trim());
            statement.setInt(2, MainApplication.getCurrentTaskList().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Task> getTasksByStatus(String status){
        try {
            connectDB();
            HashMap<String, Task> hashMap = new HashMap<>();
            String query = "SELECT * FROM task WHERE status_id=? AND tasks_list_id=?;";
            statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(status));
            statement.setInt(2, MainApplication.getCurrentTaskList().getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                hashMap.put(String.valueOf(resultSet.getInt(1)),
                            new Task(resultSet.getInt(1),
                                     resultSet.getInt(2),
                                     resultSet.getString(3),
                                     resultSet.getString(4),
                                     resultSet.getInt(5)
                            ));
            }
            return hashMap;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Pair<String, Integer>> getCountTaskListStatuses(int taskListId) {
        try {
            connectDB();
            String query = "SELECT id, name FROM statuses WHERE tasks_list_id=?;";
            statement = connection.prepareStatement(query);
            statement.setInt(1, taskListId);
            ResultSet resultStatusNames = statement.executeQuery();
            ArrayList<Pair<String, Integer>> arrayList = new ArrayList<>();
            while (resultStatusNames.next()){
                query = "SELECT COUNT(*) FROM task WHERE tasks_list_id=? AND status_id=?;";
                statement = connection.prepareStatement(query);
                statement.setInt(1, taskListId);
                statement.setInt(2, resultStatusNames.getInt(1));
                ResultSet resultStatusCount = statement.executeQuery();
                resultStatusCount.next();
                arrayList.add(new Pair<>(resultStatusNames.getString(2), resultStatusCount.getInt(1)));
            }
            return arrayList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static void createTask(int taskListId, String name, String description, int statusId){
        try {
            connectDB();
            String query = "INSERT INTO task(tasks_list_id, name, description, status_id) VALUES (?, ?, ?, ?);";
            statement = connection.prepareStatement(query);
            statement.setInt(1, taskListId);
            statement.setString(2, name);
            statement.setString(3, description);
            statement.setInt(4, statusId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTask(int taskId) {
        try{
            connectDB();
            String query = "DELETE FROM task WHERE id=?;";
            statement = connection.prepareStatement(query);
            statement.setInt(1, taskId);
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteStatus(int statusId) {
        try {
            connectDB();
            String query = "DELETE FROM task WHERE status_id=?;";
            statement = connection.prepareStatement(query);
            statement.setInt(1, statusId);
            statement.executeUpdate();
            query = "DELETE FROM statuses WHERE id=?;";
            statement = connection.prepareStatement(query);
            statement.setInt(1, statusId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
