package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        LinkedList<User> userList = new LinkedList<>();
        userList.add(new User("Ivan", "Nikolaev", (byte) 36));
        userList.add(new User("John", "Doe", (byte) 77));
        userList.add(new User("Oleg", "Thcethcevitsyn", (byte) 28));
        userList.add(new User("Fyodor", "Sumkin", (byte) 33));

        userService.createUsersTable();

        for (User user : userList) {
            userService.saveUser(user.getName(), user.getLastName(), user.getAge());
            System.out.println("User с именем — " + user.getName() + " добавлен в базу данных");
        }

        userList.clear();

        userList = (LinkedList<User>) userService.getAllUsers();
        for (User user : userList) {
            System.out.println(user.toString());
        }

        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
