package hu.alkfelj.dao;

import hu.alkfelj.config.Configuration;
import hu.alkfelj.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

public class UserDAOImpl implements UserDAO{
    private static final String FIND_USER = "SELECT * FROM USER WHERE name = ?";
    private static final String FIND_USER_BY_ID = "SELECT * FROM USER WHERE id = ?";
    private static final String FIND_ALL_USERS = "SELECT * FROM User";
    private static final String INSERT_USER = "INSERT INTO USER(name, creationDate) VALUES(?, ?)";
    private static final String UPDATE_USER = "UPDATE USER SET name=?, creationDate=? WHERE id=?";
    private static final String DELETE_USER = "DELETE FROM USER WHERE id=?";

    private String connectionUrl;

    public UserDAOImpl() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connectionUrl = Configuration.getValue("db.url");
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (Connection c = DriverManager.getConnection(connectionUrl);
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(FIND_ALL_USERS);
        ) {
            while (rs.next()) {
                User user = new User();

                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                Date date = Date.valueOf(rs.getString("creationDate"));
                user.setCreationDate(date == null ? LocalDate.now() : date.toLocalDate());

                users.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return users;
    }

    @Override
    public User findUser(String userName) {
        if(userName == null || userName.equals(""))
            return null;

        User user = new User();

        try (Connection c = DriverManager.getConnection(connectionUrl);
             PreparedStatement stmt = c.prepareStatement(FIND_USER)
        ) {
            stmt.setString(1, userName);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                Date date = Date.valueOf(rs.getString("creationDate"));
                user.setCreationDate(date == null ? LocalDate.now() : date.toLocalDate());
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return user;
    }

    @Override
    public User findUser(int userId) {
        User user = new User();

        try (Connection c = DriverManager.getConnection(connectionUrl);
             PreparedStatement stmt = c.prepareStatement(FIND_USER_BY_ID)
        ) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                Date date = Date.valueOf(rs.getString("creationDate"));
                user.setCreationDate(date == null ? LocalDate.now() : date.toLocalDate());
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return user;
    }

    @Override
    public int find(String userName) {
        if( userName == null || userName.equals(""))
            return -1;

        int affectedRows = 0;

        try (Connection c = DriverManager.getConnection(connectionUrl);
             PreparedStatement stmt = c.prepareStatement(FIND_USER)
        ) {
            stmt.setString(1, userName);

            affectedRows = stmt.executeUpdate();
        }
        catch (SQLException throwables) {
            affectedRows = 0;
        }

        return affectedRows;
    }

    @Override
    public void insert(User user) {
        if (user == null)
            return;

        try (Connection c = DriverManager.getConnection(connectionUrl);
             PreparedStatement stmt = c.prepareStatement(INSERT_USER)
        ) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getCreationDate().toString());

            stmt.executeUpdate();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(User user) {
        if (user == null)
            return;

        try (Connection c = DriverManager.getConnection(connectionUrl);
             PreparedStatement stmt = c.prepareStatement(UPDATE_USER)
        ) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getCreationDate().toString());
            stmt.setInt(3, user.getId());

            stmt.executeUpdate();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void delete(User user) {
        try (Connection c = DriverManager.getConnection(connectionUrl);
             PreparedStatement stmt = c.prepareStatement(DELETE_USER)
        ) {
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
