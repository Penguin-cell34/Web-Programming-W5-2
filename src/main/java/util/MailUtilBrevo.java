package util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class MailUtilBrevo {

    private static final String BREVO_API_URL =
            "https://api.brevo.com/v3/smtp/email";

    private static final HttpClient HTTP_CLIENT =
            HttpClient.newHttpClient();

    public static void sendMail(
            String to,
            String toName,
            String subject,
            String body)
            throws IOException, InterruptedException {

        String apiKey = System.getenv("BREVO_API_KEY");
        String senderEmail = System.getenv("BREVO_SENDER_EMAIL");
        String senderName = System.getenv("BREVO_SENDER_NAME");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "BREVO_API_KEY is not configured.");
        }

        if (senderEmail == null || senderEmail.isBlank()) {
            throw new IllegalStateException(
                    "BREVO_SENDER_EMAIL is not configured.");
        }

        if (senderName == null || senderName.isBlank()) {
            senderName = "SQL Gateway";
        }

        String jsonBody =
                "{"
                + "\"sender\":{"
                + "\"name\":\"" + escapeJson(senderName) + "\","
                + "\"email\":\"" + escapeJson(senderEmail) + "\""
                + "},"
                + "\"to\":[{"
                + "\"email\":\"" + escapeJson(to) + "\","
                + "\"name\":\"" + escapeJson(toName) + "\""
                + "}],"
                + "\"subject\":\"" + escapeJson(subject) + "\","
                + "\"textContent\":\"" + escapeJson(body) + "\""
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BREVO_API_URL))
                .header("accept", "application/json")
                .header("api-key", apiKey)
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        jsonBody,
                        StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response =
                HTTP_CLIENT.send(
                        request,
                        HttpResponse.BodyHandlers.ofString(
                                StandardCharsets.UTF_8));

        int statusCode = response.statusCode();

        if (statusCode < 200 || statusCode >= 300) {
            throw new IOException(
                    "Brevo API error. HTTP "
                    + statusCode
                    + ": "
                    + response.body());
        }

        System.out.println(
                "Email sent successfully through Brevo.");
    }

    private static String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }
}