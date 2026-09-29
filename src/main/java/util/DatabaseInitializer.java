package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String SERVER_URL =
            "jdbc:sqlserver://LAPTOP-20HP2MQ8:1433;"
            + "databaseName=master;"
            + "integratedSecurity=true;"
            + "encrypt=true;"
            + "trustServerCertificate=true;";

    private static final String DATABASE_NAME = "SQLGateway";

    private static final String DATABASE_URL =
            "jdbc:sqlserver://LAPTOP-20HP2MQ8:1433;"
            + "databaseName=SQLGateway;"
            + "integratedSecurity=true;"
            + "encrypt=true;"
            + "trustServerCertificate=true;";

    public static void initialize() {

        try {
            // Explicitly load Microsoft SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            createDatabase();
            createOrUpgradeUserTable();

        } catch (Exception ex) {
            throw new RuntimeException(
                    "Database initialization failed: " + ex.getMessage(),
                    ex
            );
        }
    }

    private static void createDatabase() throws SQLException {

        try (Connection conn = DriverManager.getConnection(SERVER_URL);
             Statement stmt = conn.createStatement()) {

            String sql =
                    "IF DB_ID('" + DATABASE_NAME + "') IS NULL "
                    + "CREATE DATABASE [" + DATABASE_NAME + "]";

            stmt.executeUpdate(sql);

            System.out.println("Database check completed: " + DATABASE_NAME);
        }
    }

    private static void createOrUpgradeUserTable() throws SQLException {

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {

            // Check whether [User] exists
            String checkTable =
                    "SELECT COUNT(*) "
                    + "FROM INFORMATION_SCHEMA.TABLES "
                    + "WHERE TABLE_NAME = 'User'";

            var rs = stmt.executeQuery(checkTable);

            boolean tableExists = false;

            if (rs.next()) {
                tableExists = rs.getInt(1) > 0;
            }

            rs.close();

            if (!tableExists) {

                createUserTable(stmt);

                System.out.println("[User] table created.");

            } else {

                // Check whether old table has userId column
                String checkUserId =
                        "SELECT COUNT(*) "
                        + "FROM INFORMATION_SCHEMA.COLUMNS "
                        + "WHERE TABLE_NAME = 'User' "
                        + "AND COLUMN_NAME = 'userId'";

                rs = stmt.executeQuery(checkUserId);

                boolean hasUserId = false;

                if (rs.next()) {
                    hasUserId = rs.getInt(1) > 0;
                }

                rs.close();

                if (!hasUserId) {

                    System.out.println(
                            "Old [User] table detected. Recreating table..."
                    );

                    stmt.executeUpdate("DROP TABLE [User]");

                    createUserTable(stmt);

                    System.out.println(
                            "[User] table recreated successfully."
                    );

                } else {

                    System.out.println(
                            "[User] table already exists."
                    );
                }
            }
        }
    }

    private static void createUserTable(Statement stmt)
            throws SQLException {

        String sql =
                "CREATE TABLE [User] ("
                + "userId BIGINT IDENTITY(1,1) NOT NULL, "
                + "firstName NVARCHAR(100) NOT NULL, "
                + "lastName NVARCHAR(100) NOT NULL, "
                + "email NVARCHAR(255) NOT NULL, "
                + "CONSTRAINT PK_User PRIMARY KEY (userId), "
                + "CONSTRAINT UQ_User_Email UNIQUE (email)"
                + ")";

        stmt.executeUpdate(sql);
    }
}