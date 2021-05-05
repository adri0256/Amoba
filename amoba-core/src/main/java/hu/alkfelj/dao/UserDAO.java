package hu.alkfelj.dao;

import hu.alkfelj.model.User;

import java.util.List;

public interface UserDAO {
    List<User> findAll();
    User findUser(String name);
    User findUser(int userId);
    int find(String userName);
    void insert(User user);
    void update(User user);
    void delete(User user);
}
