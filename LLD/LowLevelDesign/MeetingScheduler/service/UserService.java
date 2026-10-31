package service;

import model.User;

import java.util.Map;

public class UserService {
    private Map<String, User> users;

    public UserService(Map<String, User> users) {
        this.users = users;
    }

    public User getUser(String userId) {
        return users.get(userId);
    }
}
