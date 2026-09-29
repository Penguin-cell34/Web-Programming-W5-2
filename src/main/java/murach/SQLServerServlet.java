package murach;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/sqlGateway")
public class SQLServerServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null
                || session.getAttribute("user") == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/index.jsp"
            );

            return;
        }

        request.getRequestDispatcher(
                "/sqlGateway.jsp"
        ).forward(
                request,
                response
        );
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session =
                request.getSession(false);

        if (session == null
                || session.getAttribute("user") == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/index.jsp"
            );

            return;
        }

        String sql =
                request.getParameter("sql");

        request.setAttribute(
                "sql",
                sql
        );

        if (sql == null
                || sql.trim().isEmpty()) {

            request.setAttribute(
                    "error",
                    "Please enter an SQL statement."
            );

            request.getRequestDispatcher(
                    "/sqlGateway.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        try {

            SQLResult result =
                    SQLGatewayDB.execute(sql);

            request.setAttribute(
                    "result",
                    result
            );

        } catch (SQLException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );
        }

        request.getRequestDispatcher(
                "/sqlGateway.jsp"
        ).forward(
                request,
                response
        );
    }
}