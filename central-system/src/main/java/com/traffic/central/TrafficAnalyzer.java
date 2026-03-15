package com.traffic.central;

import java.sql.SQLException;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class TrafficAnalyzer {

    public static void analyze(String data) {

        System.out.println("Analyzing data: " + data);

        try {

            DatabaseService db = new DatabaseService();

            // ======================
            // Extraire l'ID de la route
            // ======================
            int routeId = 1;

            if (data.contains("Route")) {
                String routePart = data.split(":")[0];
                routeId = Integer.parseInt(routePart.replaceAll("[^0-9]", ""));
            }

            int sensorId = db.getSensorIdByRoute(routeId);

            // ======================
            // TRAFFIC ANALYSIS
            // ======================
            if (data.contains("Traffic")) {

                int volume = 0;
                double speed = 0;

                try {
                    String[] parts = data.split(",");

                    volume = Integer.parseInt(parts[1].trim());
                    speed = Double.parseDouble(parts[2].trim());

                } catch (Exception e) {
                    System.out.println("Invalid traffic data format");
                }

                db.insertTrafficFull(routeId, volume, speed);

                if (volume > 300) {
                    System.out.println("ALERT: High traffic detected!");
                }
            }

            // ======================
            // POLLUTION ANALYSIS
            // ======================
            if (data.contains("Pollution")) {

                String typePollution = "Air";
                double niveau = 0;

                try {

                    String[] parts = data.split(",");

                    typePollution = parts[1].trim();
                    niveau = Double.parseDouble(parts[2].trim());

                } catch (Exception e) {
                    System.out.println("Invalid pollution data format");
                }

                db.insertPollutionFull(routeId, typePollution, niveau);

                if (niveau > 150) {
                    System.out.println("ALERT: High pollution level detected!");
                }
            }

            // ======================
            // NOISE ANALYSIS
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
            // ACCIDENT ANALYSIS
            // ======================
            if (data.toLowerCase().contains("accident")) {

                System.out.println("ALERT: Accident detected!");

                String[] parts = data.split(":");
                String description = parts.length > 1 ? parts[1].trim() : "Accident non precise";

                db.insertAccidentFull(routeId, sensorId, description);
            }

        } catch (SQLException e) {

            System.out.println("Database error: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("Error analyzing data: " + e.getMessage());
        }
    }
}