package com.traffic.central;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class SimpleHttpServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // =========================
        // API EVENTS
        // =========================
        server.createContext("/api/events", (HttpExchange exchange) -> {

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            JSONArray array = new JSONArray();

            try {
                DatabaseService db = new DatabaseService();

                String sql = "SELECT * FROM evenements";
                Statement st = db.getConnection().createStatement();
                ResultSet rs = st.executeQuery(sql);

                while (rs.next()) {
                    JSONObject obj = new JSONObject();

                    obj.put("type_evenement", rs.getString("type_evenement"));
                    obj.put("route_id", rs.getInt("route_id"));
                    obj.put("description", rs.getString("description"));
                    obj.put("timestamp", rs.getString("timestamp"));

                    array.put(obj);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            String response = array.toString();
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        // =========================
        // API NOISE
        // =========================
        server.createContext("/api/noise", (HttpExchange exchange) -> {

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            JSONArray array = new JSONArray();

            try {
                DatabaseService db = new DatabaseService();

                String sql = "SELECT * FROM bruit ORDER BY timestamp ASC";
                Statement st = db.getConnection().createStatement();
                ResultSet rs = st.executeQuery(sql);

                while (rs.next()) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", rs.getInt("id"));
                    obj.put("sensor_id", rs.getInt("sensor_id"));
                    obj.put("route_id", rs.getInt("route_id"));
                    obj.put("niveau_db", rs.getInt("niveau_db"));
                    obj.put("timestamp", rs.getString("timestamp"));
                    array.put(obj);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            String response = array.toString();
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        // =========================
        // API ALERTES ✅ (NOUVEAU)
        // =========================
        server.createContext("/api/alertes", (HttpExchange exchange) -> {

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    exchange.getResponseHeaders().add("Content-Type", "application/json");

    JSONArray array = new JSONArray();

    try {
        DatabaseService db = new DatabaseService();

                String sql = "SELECT a.type_alerte, a.route_id, a.timestamp, r.action_recommandee " +
             "FROM alertes a " +
             "JOIN recommandations r ON a.recommandation_id = r.id " +
             "ORDER BY a.timestamp DESC";
        Statement st = db.getConnection().createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            JSONObject obj = new JSONObject();

            obj.put("type_alerte", rs.getString("type_alerte"));
            obj.put("route_id", rs.getInt("route_id"));
            obj.put("timestamp", rs.getString("timestamp"));
            obj.put("action_recommandee", rs.getString("action_recommandee"));

            array.put(obj);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    String response = array.toString();
    exchange.sendResponseHeaders(200, response.length());
    OutputStream os = exchange.getResponseBody();
    os.write(response.getBytes());
    os.close();
});

        // =========================
        // START SERVER
        // =========================
        server.start();
        System.out.println("✅ Server running on:");
        System.out.println("http://localhost:8080/api/events");
        System.out.println("http://localhost:8080/api/noise");
        System.out.println("http://localhost:8080/api/alertes");
    }
}