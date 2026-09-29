package murach;

import java.util.List;

public class SQLResult {

    private boolean queryResult;

    private int rowsAffected;

    private List<String> columns;

    private List<List<String>> rows;

    private SQLResult() {
    }

    public static SQLResult selectResult(
            List<String> columns,
            List<List<String>> rows) {

        SQLResult result =
                new SQLResult();

        result.queryResult = true;

        result.columns = columns;

        result.rows = rows;

        return result;
    }

    public static SQLResult updateResult(
            int rowsAffected) {

        SQLResult result =
                new SQLResult();

        result.queryResult = false;

        result.rowsAffected =
                rowsAffected;

        return result;
    }

    public boolean isQueryResult() {
        return queryResult;
    }

    public int getRowsAffected() {
        return rowsAffected;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<List<String>> getRows() {
        return rows;
    }
}