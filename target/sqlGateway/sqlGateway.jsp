<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="murach.User" %>
<%@ page import="murach.SQLResult" %>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <title>The SQL Gateway</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/main.css">

</head>

<body>

<div class="container">

    <h1>The SQL Gateway</h1>

    <%
        User user =
                (User) session.getAttribute("user");
    %>

    <p>
        Welcome,
        <strong>
            <%= user.getFirstName() %>
            <%= user.getLastName() %>
        </strong>
    </p>

    <p>
        Enter an SQL statement and click the Execute button.
    </p>

    <form method="post"
          action="${pageContext.request.contextPath}/sqlGateway">

        <b>SQL statement:</b>

        <br><br>

        <textarea name="sql"><%=
            request.getAttribute("sql") != null
                ? request.getAttribute("sql")
                : ""
        %></textarea>

        <br>

        <input type="submit"
               value="Execute">

    </form>

    <h3>SQL result:</h3>

    <%
        String error =
                (String) request.getAttribute("error");

        SQLResult result =
                (SQLResult) request.getAttribute("result");
    %>

    <% if (error != null) { %>

        <p class="error">

            <b>SQL Error:</b>

            <br>

            <%= error %>

        </p>

    <% } %>

    <% if (result != null) { %>

        <% if (result.isQueryResult()) { %>

            <table>

                <tr>

                    <% for (String column :
                            result.getColumns()) { %>

                        <th>
                            <%= column %>
                        </th>

                    <% } %>

                </tr>

                <% for (var row :
                        result.getRows()) { %>

                    <tr>

                        <% for (String value :
                                row) { %>

                            <td>
                                <%= value %>
                            </td>

                        <% } %>

                    </tr>

                <% } %>

            </table>

            <% if (result.getRows().isEmpty()) { %>

                <p>No rows found.</p>

            <% } %>

        <% } else { %>

            <p class="success">

                The statement executed successfully.

                <br>

                <%= result.getRowsAffected() %>
                row(s) affected.

            </p>

        <% } %>

    <% } %>

</div>

</body>

</html>