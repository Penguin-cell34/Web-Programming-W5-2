package murach;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.SQLUtil;

public class SQLGatewayDB {

    public static SQLResult execute(
            String sql)
            throws SQLException {

        try (
            Connection connection =
                    SQLUtil.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ) {

            String trimmedSQL =
                    sql.trim().toLowerCase();

            if (trimmedSQL.startsWith("select")) {

                try (
                    ResultSet resultSet =
                            statement.executeQuery()
                ) {

                    return createResult(
                            resultSet
                    );
                }

            } else {

                int rowsAffected =
                        statement.executeUpdate();

                return SQLResult.updateResult(
                        rowsAffected
                );
            }
        }
    }

    private static SQLResult createResult(
            ResultSet resultSet)
            throws SQLException {

        ResultSetMetaData metaData =
                resultSet.getMetaData();

        int columnCount =
                metaData.getColumnCount();

        List<String> columns =
                new ArrayList<>();

        for (int i = 1;
             i <= columnCount;
             i++) {

            columns.add(
                    metaData.getColumnName(i)
            );
        }

        List<List<String>> rows =
                new ArrayList<>();

        while (resultSet.next()) {

            List<String> row =
                    new ArrayList<>();

            for (int i = 1;
                 i <= columnCount;
                 i++) {

                Object value =
                        resultSet.getObject(i);

                row.add(
                        value == null
                                ? ""
                                : value.toString()
                );
            }

            rows.add(row);
        }

        return SQLResult.selectResult(
                columns,
                rows
        );
    }
}