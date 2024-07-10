package com.example.bty.Utils;

import com.example.bty.Entities.User;

public class Session {
    private static Session instance;
    private User loggedInUser;

    private Session() {

    }

    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }


    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User user) {
        loggedInUser = user;
    }
    public void logout() {
        loggedInUser = null;
    }
}
