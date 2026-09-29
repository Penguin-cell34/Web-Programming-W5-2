<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <title>Thank You</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/main.css">

</head>

<body>

<div class="container">

    <h1>Thank you!</h1>

    <p>
        Your information has been saved.
    </p>

    <p>
        You can now use the SQL Gateway.
    </p>

    <p>
        <a href="${pageContext.request.contextPath}/sqlGateway">
            Continue to SQL Gateway
        </a>
    </p>

</div>

</body>

</html>