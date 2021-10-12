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

    /* public static boolean checkExistsDB(){
        try {
            connectDB();
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS app;");
            url = "jdbc:mysql://localhost:3306/app?useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `statuses`(" +
                    "  `id` int NOT NULL," +
                    "  `name` varchar(45) NOT NULL," +
                    "  `tasks_list_id` int NOT NULL);");
            System.out.println(1);
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `task` (" +
                    "  `id` int NOT NULL," +
                    "  `tasks_list_id` int NOT NULL," +
                    "  `name` varchar(90) NOT NULL," +
                    "  `description` varchar(500) DEFAULT NULL," +
                    "  `status` int NOT NULL);");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `tasks_list` (" +
                    "  `id` int NOT NULL," +
                    "  `name` varchar(90) NOT NULL," +
                    "  `everyone` tinyint(1) NOT NULL DEFAULT '0'," +
                    "  `owner` int NOT NULL);");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `tasks_list_has_users` (" +
                    "  `tasks_list_id` int NOT NULL," +
                    "  `users_id` int NOT NULL," +
                    "  `privileges` varchar(10) NOT NULL DEFAULT 'viewer');");
            statement.executeUpdate("ALTER TABLE `statuses`" +
                    "  ADD PRIMARY KEY (`id`)," +
                    "  ADD KEY `fk_statuses_tasks_list1_idx` (`tasks_list_id`); " +
                    "ALTER TABLE `task`" +
                    "  ADD PRIMARY KEY (`id`)," +
                    "  ADD KEY `fk_task_statuses1_idx` (`status`)," +
                    "  ADD KEY `fk_task_tasks_list1_idx` (`tasks_list_id`); " +
                    "ALTER TABLE `tasks_list`" +
                    "  ADD PRIMARY KEY (`id`)," +
                    "  ADD KEY `owner` (`owner`); " +
                    "ALTER TABLE `tasks_list_has_users`" +
                    "  ADD PRIMARY KEY (`tasks_list_id`,`users_id`)," +
                    "  ADD KEY `fk_tasks_list_has_users_users1_idx` (`users_id`)," +
                    "  ADD KEY `fk_tasks_list_has_users_tasks_list_idx` (`tasks_list_id`); " +
                    "ALTER TABLE `users`" +
                    "  ADD PRIMARY KEY (`id`)," +
                    "  ADD UNIQUE KEY `login_UNIQUE` (`login`)," +
                    "  ADD UNIQUE KEY `email_UNIQUE` (`email`);");
            statement.executeUpdate("ALTER TABLE `statuses`" +
                    "  MODIFY `id` int NOT NULL AUTO_INCREMENT; " +
                    "ALTER TABLE `task`" +
                    "  MODIFY `id` int NOT NULL AUTO_INCREMENT; " +
                    "ALTER TABLE `tasks_list`\n" +
                    "  MODIFY `id` int NOT NULL AUTO_INCREMENT; " +
                    "ALTER TABLE `users`\n" +
                    "  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;");
            statement.executeUpdate(
                    "ALTER TABLE `statuses`" +
                    " ADD CONSTRAINT `fk_statuses_tasks_list1` FOREIGN KEY (`tasks_list_id`) REFERENCES `tasks_list` (`id`); " +
                    "ALTER TABLE `task`" +
                    " ADD CONSTRAINT `fk_task_statuses1` FOREIGN KEY (`status`) REFERENCES `statuses` (`id`),\n" +
                    " ADD CONSTRAINT `fk_task_tasks_list1` FOREIGN KEY (`tasks_list_id`) REFERENCES `tasks_list` (`id`); " +
                    "ALTER TABLE `tasks_list`" +
                    " ADD CONSTRAINT `tasks_list_ibfk_1` FOREIGN KEY (`owner`) REFERENCES `users` (`id`); " +
                    "ALTER TABLE `tasks_list_has_users`" +
                    " ADD CONSTRAINT `fk_tasks_list_has_users_tasks_list` FOREIGN KEY (`tasks_list_id`) REFERENCES `tasks_list` (`id`),\n" +
                    " ADD CONSTRAINT `fk_tasks_list_has_users_users1` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`);"
            );
            return true;
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
    } */

    public static void connectDB() throws ClassNotFoundException, SQLException {
        connection= DriverManager.getConnection(url, username, password);
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

    public static ArrayList<TaskList> getTasksLists(int userId){
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
