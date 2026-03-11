package com.traffic.central;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import java.sql.SQLException;

public class TrafficAnalyzer {

    public static void analyze(String data) {

        System.out.println("Analyzing data: " + data);

        try {

            DatabaseService db = new DatabaseService();

            // ======================
            // Extraire l'ID de la route
            // ======================
            int routeId = 1; // valeur par défaut
            if (data.contains("Route")) {
                String routePart = data.split(":")[0];
                routeId = Integer.parseInt(routePart.replaceAll("[^0-9]", ""));
            }

            int sensorId = db.getSensorIdByRoute(routeId);

            // ======================
            // CAS BRUIT
            // ======================
            if (data.contains("Noise level")) {

                int noise = 0;
                int indexNoise = data.indexOf("Noise level ");
                if (indexNoise != -1) {
                    int start = indexNoise + "Noise level ".length();
                    int end = data.indexOf(" dB", start);
                    if (end != -1) {
                        String noiseStr = data.substring(start, end).trim();
                        noise = Integer.parseInt(noiseStr);
                    }
                }

                db.insertNoise(routeId, sensorId, noise);

                if (noise > 85) {
                    System.out.println("ALERT: High Noise Level detected!");

                    int eventId = db.insertEvent(
                            "Bruit eleve",
                            routeId,
                            "Niveau de bruit " + noise + " dB detecte"
                    );

                    int recId = db.insertRecommendation(
                            eventId,
                            "Reduire trafic ou avertir conducteurs"
                    );

                    db.insertAlert("Bruit eleve", routeId, recId);
                }
            }

            // ======================
            // CAS ACCIDENT
            // ======================
            if (data.toLowerCase().contains("accident")) {

                System.out.println("ALERT: Accident detected!");

                // Extraire la description après "RouteX:"
                String[] parts = data.split(":");
                String description = parts.length > 1 ? parts[1].trim() : "Accident non precise";

                // Insérer dans la DB (accident -> evenement -> recommandation -> alerte)
                db.insertAccidentFull(routeId, sensorId, description);
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error analyzing data: " + e.getMessage());
        }
    }
}