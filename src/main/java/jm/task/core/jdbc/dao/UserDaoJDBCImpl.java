package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final Connection connection;
    private static final PreparedStatement checkTable;
    private static final PreparedStatement dropTable;
    private static final PreparedStatement saveUser;
    private static final PreparedStatement getUsers;
    private static final PreparedStatement removeUser;
    private static final PreparedStatement cleanTable;
    private static final PreparedStatement createTable;
    static {
        try {
            connection  = Util.getConnection();
            checkTable  = connection.prepareStatement("SHOW TABLES LIKE 'users'");
            dropTable   = connection.prepareStatement("DROP TABLE USERS");
            saveUser    = connection.prepareStatement("INSERT INTO USERS (NAME, LASTNAME, AGE) VALUES (?, ?, ?)");
            getUsers    = connection.prepareStatement("SELECT ID, NAME, LASTNAME, AGE FROM USERS");
            removeUser  = connection.prepareStatement("DELETE FROM USERS WHERE ID = ?");
            cleanTable  = connection.prepareStatement("DELETE FROM USERS");
            createTable = connection.prepareStatement("CREATE TABLE `users` (id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                                                       "name VARCHAR(255), lastName VARCHAR(255), age TINYINT)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDaoJDBCImpl() {}

    public void createUsersTable() {
        try {
            if (!checkTable.executeQuery().next()) {
                createTable.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try {
            if (checkTable.executeQuery().next()) {
                dropTable.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            if (!checkTable.executeQuery().next()) {
                createTable.execute();
            }
            saveUser.setString(1, name);
            saveUser.setString(2, lastName);
            saveUser.setByte(3, age);
            saveUser.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try {
            removeUser.setLong(1, id);
            removeUser.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        try {
            ResultSet result = getUsers.executeQuery();
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
        try {
            if (checkTable.executeQuery().next()) {
                cleanTable.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
