package com.traffic.central;

import java.sql.*;

public class DatabaseService {
    private static final String URL = "jdbc:mysql://localhost:3307/smart_traffic";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private Connection connection;

    public DatabaseService() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public Connection getConnection() {
        return connection;
    }

   public int getSensorIdByRoute(int routeId) throws SQLException {

    String sql = "SELECT id FROM sensors WHERE route_id = ? LIMIT 1";

    PreparedStatement ps = connection.prepareStatement(sql);
    ps.setInt(1, routeId);

    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
        return rs.getInt("id");
    }

    throw new SQLException("No sensor found for route " + routeId);
}

    // Méthode pour insérer le bruit
    public int insertNoise(int routeId, int sensorId, int niveauDb) throws SQLException {
        String sql = "INSERT INTO bruit (route_id, sensor_id, niveau_db, timestamp) VALUES (?, ?, ?, NOW())";
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, routeId);
        ps.setInt(2, sensorId);
        ps.setInt(3, niveauDb);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) return rs.getInt(1);
        return 0;
    }

    // Méthode pour créer un événement
    public int insertEvent(String typeEvenement, int routeId, String description) throws SQLException {
        String sql = "INSERT INTO evenements (type_evenement, route_id, timestamp, description) VALUES (?, ?, NOW(), ?)";
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, typeEvenement);
        ps.setInt(2, routeId);
        ps.setString(3, description);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) return rs.getInt(1);
        return 0;
    }

    // Méthode pour créer une recommandation
    public int insertRecommendation(int evenementId, String action) throws SQLException {
        String sql = "INSERT INTO recommandations (evenement_id, action_recommandee) VALUES (?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, evenementId);
        ps.setString(2, action);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) return rs.getInt(1);
        return 0;
    }

    // Méthode pour créer une alerte
    public void insertAlert(String typeAlerte, int routeId, int recommendationId) throws SQLException {
        String sql = "INSERT INTO alertes (type_alerte, route_id, timestamp, recommandation_id) VALUES (?, ?, NOW(), ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, typeAlerte);
        ps.setInt(2, routeId);
        ps.setInt(3, recommendationId);
        ps.executeUpdate();
    }
    // Méthode pour insérer un accident
   // Méthode pour insérer un accident et récupérer l'id
public int insertAccident(int routeId, int sensorId, String description) throws SQLException {
    String sql = "INSERT INTO accidents (route_id, sensor_id, description, timestamp) VALUES (?, ?, ?, NOW())";
    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    ps.setInt(1, routeId);
    ps.setInt(2, sensorId);
    ps.setString(3, description);
    ps.executeUpdate();
    ResultSet rs = ps.getGeneratedKeys();
    if (rs.next()) return rs.getInt(1);
    return 0;
}

    // Méthode complète pour accident -> evenement -> recommandation -> alerte
        public void insertAccidentFull(int routeId, int sensorId, String description) throws SQLException {
            int accidentId = insertAccident(routeId, sensorId, description);

            int eventId = insertEvent(
                    "Accident",
                    routeId,
                    "Accident detecte par camera: " + description
            );

            int recId = insertRecommendation(
                    eventId,
                    "Devier le trafic vers une autre route"
            );

            insertAlert("Accident", routeId, recId);
    }

    // Ajout dans table pollution
    // Méthode pour insérer la pollution
    public int insertPollution(int routeId, String typePollution, double niveau) throws SQLException {

    String sql = "INSERT INTO pollution (route_id, type_pollution, niveau, timestamp) VALUES (?, ?, ?, NOW())";

    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

    ps.setInt(1, routeId);
    ps.setString(2, typePollution);
    ps.setDouble(3, niveau);

    ps.executeUpdate();

    ResultSet rs = ps.getGeneratedKeys();

    if (rs.next()) return rs.getInt(1);

    return 0;
}

public void insertPollutionFull(int routeId, String typePollution, double niveau) throws SQLException {

    int pollutionId = insertPollution(routeId, typePollution, niveau);

    int eventId = insertEvent(
            "Pollution",
            routeId,
            "Niveau de pollution detecte: " + typePollution + " = " + niveau
    );

    int recId = insertRecommendation(
            eventId,
            "Limiter la circulation sur cette route"
    );

    insertAlert("Pollution", routeId, recId);
}

// Méthode pour insérer les données de trafic
public int insertTraffic(int routeId, int volume, double vitesseMoyenne) throws SQLException {

    String sql = "INSERT INTO traffic (route_id, volume, vitesse_moyenne, timestamp) VALUES (?, ?, ?, NOW())";

    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

    ps.setInt(1, routeId);
    ps.setInt(2, volume);
    ps.setDouble(3, vitesseMoyenne);

    ps.executeUpdate();

    ResultSet rs = ps.getGeneratedKeys();

    if (rs.next()) return rs.getInt(1);

    return 0;
}

 public void insertTrafficFull(int routeId, int volume, double vitesseMoyenne) throws SQLException {

    int trafficId = insertTraffic(routeId, volume, vitesseMoyenne);

    int eventId = insertEvent(
            "Traffic",
            routeId,
            "Volume: " + volume + " vehicules, vitesse moyenne: " + vitesseMoyenne
    );

    int recId = insertRecommendation(
            eventId,
            "Optimiser les feux de circulation"
    );

    insertAlert("Traffic", routeId, recId);
}


}