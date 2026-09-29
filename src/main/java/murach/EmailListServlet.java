package murach;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.MailUtilBrevo;

@WebServlet("/emailList")
public class EmailListServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect(
                request.getContextPath() + "/"
        );
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // =====================================================
        // 1. Get information from form
        // =====================================================

        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        // Remove unnecessary spaces
        if (email != null) {
            email = email.trim();
        }

        if (firstName != null) {
            firstName = firstName.trim();
        }

        if (lastName != null) {
            lastName = lastName.trim();
        }

        // =====================================================
        // 2. Validate input
        // =====================================================

        if (email == null || email.isEmpty()
                || firstName == null || firstName.isEmpty()
                || lastName == null || lastName.isEmpty()) {

            request.setAttribute(
                    "error",
                    "Please enter your email, first name, and last name."
            );

            request.getRequestDispatcher(
                    "/index.jsp"
            ).forward(request, response);

            return;
        }

        // =====================================================
        // 3. Check if email already exists in database
        // =====================================================

        try {

            if (UserDB.emailExists(email)) {

                request.setAttribute(
                        "error",
                        "This email address already exists. "
                        + "Please enter another email address."
                );

                request.setAttribute("email", email);
                request.setAttribute("firstName", firstName);
                request.setAttribute("lastName", lastName);

                request.getRequestDispatcher(
                        "/index.jsp"
                ).forward(request, response);

                return;
            }

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Unable to check the email address in the database."
            );

            request.getRequestDispatcher(
                    "/index.jsp"
            ).forward(request, response);

            return;
        }

        // =====================================================
        // 4. Create User object
        // =====================================================

        User user = new User(
                firstName,
                lastName,
                email
        );

        // =====================================================
        // 5. Get Brevo configuration
        // =====================================================

        String brevoApiKey =
                System.getenv("BREVO_API_KEY");

        String senderEmail =
                System.getenv("BREVO_SENDER_EMAIL");

        String senderName =
                System.getenv("BREVO_SENDER_NAME");

        if (brevoApiKey == null || brevoApiKey.isBlank()
                || senderEmail == null || senderEmail.isBlank()) {

            request.setAttribute(
                    "error",
                    "Email configuration is missing. "
                    + "Please configure BREVO_API_KEY "
                    + "and BREVO_SENDER_EMAIL."
            );

            request.getRequestDispatcher(
                    "/index.jsp"
            ).forward(request, response);

            return;
        }

        // =====================================================
        // 6. Prepare email
        // =====================================================

        String subject =
                "Welcome to our SQL Gateway";

        String body =
                "Dear " + firstName + ",\n\n"
                + "Thank you for joining our email list.\n\n"
                + "Your information has been received successfully.\n"
                + "You can now continue to use the SQL Gateway.\n\n"
                + "Have a great day!\n\n"
                + "SQL Gateway Team";

        // =====================================================
        // 7. Send email using Brevo
        // =====================================================

        try {

            MailUtilBrevo.sendMail(
                    email,
                    firstName + " " + lastName,
                    subject,
                    body
            );

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Unable to send the thank-you email. "
                    + "Please check your Brevo configuration."
            );

            request.getRequestDispatcher(
                    "/index.jsp"
            ).forward(request, response);

            return;
        }

        // =====================================================
        // 8. Save User to Neon PostgreSQL
        // =====================================================

        try {

            UserDB.insert(user);

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "The email was sent successfully, "
                    + "but the user could not be saved to the database."
            );

            request.getRequestDispatcher(
                    "/index.jsp"
            ).forward(request, response);

            return;
        }

        // =====================================================
        // 9. Store User in session
        // =====================================================

        HttpSession session =
                request.getSession();

        session.setAttribute(
                "user",
                user
        );

        // =====================================================
        // 10. Continue to SQL Gateway
        // =====================================================

        response.sendRedirect(
                request.getContextPath()
                + "/sqlGateway"
        );
    }
}