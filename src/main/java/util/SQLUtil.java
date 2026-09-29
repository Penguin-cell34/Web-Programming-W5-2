package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLUtil {

    private static final String URL =
            "jdbc:postgresql://ep-ancient-violet-b5k483r0-pooler.c-7.us-east-2.aws.neon.tech/"
            + "neondb?sslmode=require&channel_binding=require";

    private static final String USER =
            "neondb_owner";

    private static final String PASSWORD =
            "YOUR_NEW_NEON_PASSWORD";

    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {

            throw new SQLException(
                    "PostgreSQL JDBC Driver not found.",
                    e
            );
        }

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}