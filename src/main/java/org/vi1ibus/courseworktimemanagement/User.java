package org.vi1ibus.courseworktimemanagement;

public class User {
    int userID;
    String login;
    String email;

    public User(int userID, String login, String email){
        this.userID = userID;
        this.login = login;
        this.email = email;
    }

    public int getUserID() {
        return userID;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }
}
