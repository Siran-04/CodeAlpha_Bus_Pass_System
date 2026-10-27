import io.javalin.Javalin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BusPassApp {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/bus_pass_db";
    private static final String DB_USER = "root";

    // Replace with your own MySQL password
    private static final String DB_PASSWORD = "naresh1234";

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
        });

        app.get("/", ctx -> {
            ctx.redirect("/index.html");
        });

        app.get("/test-db", ctx -> {
            try (Connection con = DriverManager.getConnection(
                    DB_URL, DB_USER, DB_PASSWORD)) {
                ctx.result("MySQL connected successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
                ctx.status(500).result(
                        "Database connection failed. Check MySQL settings.");
            }
        });

        app.post("/apply", ctx -> {
            String name = ctx.formParam("student_name");
            String college = ctx.formParam("college_name");
            String source = ctx.formParam("source");
            String destination = ctx.formParam("destination");
            String passType = ctx.formParam("pass_type");
            String expiryDate = ctx.formParam("expiry_date");

            if (name == null || name.isBlank()
                    || college == null || college.isBlank()
                    || source == null || source.isBlank()
                    || destination == null || destination.isBlank()
                    || passType == null || passType.isBlank()
                    || expiryDate == null || expiryDate.isBlank()) {
                ctx.status(400).result("Please fill in all fields.");
                return;
            }

            String sql = "INSERT INTO bus_passes "
                    + "(student_name, college_name, source, destination, "
                    + "pass_type, expiry_date) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection con = DriverManager.getConnection(
                        DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, name);
                ps.setString(2, college);
                ps.setString(3, source);
                ps.setString(4, destination);
                ps.setString(5, passType);
                ps.setDate(6, java.sql.Date.valueOf(expiryDate));

                ps.executeUpdate();

                ctx.html("<h2>Bus pass application submitted!</h2>"
                        + "<p>Student: "
                        + escapeHtml(name)
                        + "</p><a href='/index.html'>Go back</a>");

            } catch (SQLException | IllegalArgumentException e) {
                e.printStackTrace();
                ctx.status(500).result(
                        "Could not save application. Check database columns and date.");
            }
        });

        app.start(7070);

        System.out.println("Bus Pass System is running!");
        System.out.println("Open http://localhost:7070");
    }

    private static String escapeHtml(String value) {
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}