package RTP_Project.example.Client;

import java.util.ArrayList;
import java.util.List;

public class UserDatabase {
    private static List<User> users = new ArrayList<>();

    static {
        users.add(new User("aini", "aini1"));
        users.add(new User("awanis", "awanis2"));
        users.add(new User("alani", "alani3"));
    }

    public static boolean authenticate(String username, String password) {
        return users.stream().anyMatch(user ->
                user.getUsername().equals(username) &&
                        user.getPassword().equals(password)
        );
    }
}
