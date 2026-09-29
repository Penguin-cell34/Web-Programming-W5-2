package util;

import java.util.Properties;

import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailUtilGmail {

    public static void sendMail(
            String to,
            String from,
            String username,
            String password,
            String subject,
            String body,
            boolean bodyIsHTML)
            throws MessagingException {

        Properties props = new Properties();

        // Gmail SMTP
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", "smtp.gmail.com");
        props.put("mail.smtps.port", "465");
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtps.quitwait", "false");

        Session session = Session.getDefaultInstance(props);

        // Hiện thông tin SMTP trong console để debug
        session.setDebug(true);

        // Tạo email
        Message message = new MimeMessage(session);

        message.setSubject(subject);

        if (bodyIsHTML) {
            message.setContent(body, "text/html; charset=UTF-8");
        } else {
            // Jakarta Mail trong project của bạn đang yêu cầu setText(String)
            message.setText(body);
        }

        // From
        Address fromAddress = new InternetAddress(from);

        // To
        Address toAddress = new InternetAddress(to);

        message.setFrom(fromAddress);
        message.setRecipient(
                Message.RecipientType.TO,
                toAddress
        );

        // Kết nối Gmail SMTP
        Transport transport = session.getTransport();

        try {
            transport.connect(username, password);

            transport.sendMessage(
                    message,
                    message.getAllRecipients()
            );

        } finally {
            transport.close();
        }
    }
}