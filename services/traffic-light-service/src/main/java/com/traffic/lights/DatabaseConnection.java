package trafficlight;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection getConnection() {

        try {
            String url = "jdbc:mysql://localhost:3309/smart_traffic_1";
            String user = "root";
            String password = "";
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
System.out.println("Connexion réussie !");
            return conn;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}