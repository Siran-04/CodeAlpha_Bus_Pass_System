import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class buspass {
    public static void main(String[] args) throws Exception {

        String url = "jdbc:mysql://localhost:3306/bus_pass_db";
        String user = "root";
        String password = "naresh1234";

        Connection con = DriverManager.getConnection(url, user, password);

        String sql = "INSERT INTO bus_passes " +
            "(student_name, college_name, source, destination, pass_type, valid_until) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, "Siran");
        ps.setString(2, "Adhi College of Engineering");
        ps.setString(3, "Kilpennathur");
        ps.setString(4, "Tiruvannamalai");
        ps.setString(5, "Monthly");
        ps.setDate(6, java.sql.Date.valueOf("2026-12-31"));

        ps.executeUpdate();

        System.out.println("Bus Pass Saved Successfully!");

        ps.close();
        con.close();
    }
}