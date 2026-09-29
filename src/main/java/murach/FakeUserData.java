package murach;

import java.util.ArrayList;
import java.util.List;

public class FakeUserData {

    private static final List<User> USERS = new ArrayList<>();

    static {
        USERS.add(new User(
                "John",
                "Smith",
                "john.smith@example.com"
        ));

        USERS.add(new User(
                "Alice",
                "Johnson",
                "alice.johnson@example.com"
        ));

        USERS.add(new User(
                "Michael",
                "Brown",
                "michael.brown@example.com"
        ));

        USERS.add(new User(
                "Emily",
                "Davis",
                "emily.davis@example.com"
        ));

        USERS.add(new User(
                "David",
                "Wilson",
                "david.wilson@example.com"
        ));
    }

    public static List<User> getUsers() {
        return USERS;
    }
}