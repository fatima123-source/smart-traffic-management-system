package trafficlight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TrafficDataDAO {

    // ==========================
    // 🚗 VEHICLES
    // ==========================
    public static int getVehicleCount(int routeId) {

        int vehicles = 0;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();

            if (conn == null) {
                System.err.println("Erreur : connexion à la base de données non initialisée !");
                return 0;
            }

            String sql = "SELECT volume FROM traffic WHERE route_id = ? ORDER BY timestamp DESC LIMIT 1";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, routeId);

            rs = ps.executeQuery();

            if (rs.next()) {
                vehicles = rs.getInt("volume");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return vehicles;
    }


    // ==========================
    // 🔊 NOISE
    // ==========================
    public static int getNoiseLevel(int routeId) {

        int noise = 0;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();

            if (conn == null) {
                System.err.println("Erreur : connexion à la base de données non initialisée !");
                return 0;
            }

            String sql = "SELECT niveau_db FROM bruit WHERE route_id = ? ORDER BY timestamp DESC LIMIT 1";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, routeId);

            rs = ps.executeQuery();

            if (rs.next()) {
                noise = rs.getInt("niveau_db");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return noise;
    }


    // ==========================
    // 🌫 POLLUTION
    // ==========================
    public static int getPollutionLevel(int routeId) {

        int pollution = 0;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();

            if (conn == null) {
                System.err.println("Erreur : connexion à la base de données non initialisée !");
                return 0;
            }

            String sql = "SELECT niveau FROM pollution WHERE route_id = ? ORDER BY timestamp DESC LIMIT 1";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, routeId);

            rs = ps.executeQuery();

            if (rs.next()) {
                pollution = rs.getInt("niveau");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return pollution;
    }
}