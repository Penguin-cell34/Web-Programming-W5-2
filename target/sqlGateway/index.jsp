<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <title>Join our email list</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/main.css">

</head>

<body>

<div class="container">

    <h1>Join our email list</h1>

    <p>
        To access the SQL Gateway, please enter your
        information below.
    </p>

    <% if (request.getAttribute("error") != null) { %>

        <div class="error-message">

            <%= request.getAttribute("error") %>

        </div>

    <% } %>

    <form method="post"
          action="${pageContext.request.contextPath}/emailList">

        <div class="form-row">

            <label for="email">
                Email:
            </label>

            <input type="text"
                   id="email"
                   name="email"
                   value="<%= request.getAttribute("email") != null
                           ? request.getAttribute("email")
                           : "" %>">

        </div>

        <div class="form-row">

            <label for="firstName">
                First Name:
            </label>

            <input type="text"
                   id="firstName"
                   name="firstName"
                   value="<%= request.getAttribute("firstName") != null
                           ? request.getAttribute("firstName")
                           : "" %>">

        </div>

        <div class="form-row">

            <label for="lastName">
                Last Name:
            </label>

            <input type="text"
                   id="lastName"
                   name="lastName"
                   value="<%= request.getAttribute("lastName") != null
                           ? request.getAttribute("lastName")
                           : "" %>">

        </div>

        <div class="button-row">

            <input type="submit"
                   value="Continue">

        </div>

    </form>

</div>

</body>

</html>