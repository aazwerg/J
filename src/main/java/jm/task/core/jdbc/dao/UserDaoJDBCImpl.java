package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final Connection connection;

    static {
        try {
            connection = Util.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try (Statement s = connection.createStatement()) {
            s.execute("CREATE TABLE `users` (id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255), lastName VARCHAR(255), age TINYINT);");
        } catch (SQLException ignored) {
        }
    }

    public void dropUsersTable() {
        try (Statement s = connection.createStatement()) {
            s.execute("DROP TABLE users;");
        } catch (SQLException ignored) {
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement s = connection.prepareStatement("INSERT INTO users (NAME, LASTNAME, AGE) " +
                "VALUES (?, ?, ?)")) {
            s.setString(1, name);
            s.setString(2, lastName);
            s.setByte(3, age);
            s.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (Statement s = connection.createStatement()) {
            s.execute("DELETE FROM users WHERE ID = " + id + ";");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        try (Statement s = connection.createStatement()) {
            ResultSet result = s.executeQuery("SELECT ID, NAME, LASTNAME, AGE FROM users;");
            LinkedList<User> userList = new LinkedList<>();
            while (result.next()) {
                User user = new User(result.getString(2),
                        result.getString(3),
                        result.getByte(4));
                user.setId(result.getLong(1));
                userList.add(user);
            }
            return userList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanUsersTable() {
        try (Statement s = connection.createStatement()) {
            s.execute("DELETE FROM users;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
