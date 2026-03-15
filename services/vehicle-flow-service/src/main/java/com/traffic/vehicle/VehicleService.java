package com.traffic.vehicle;

import com.traffic.central.DatabaseService;

public class VehicleService {

    private DatabaseService db;

    public VehicleService() throws Exception {
        db = new DatabaseService();
    }

    // Sauvegarde du trafic dans la DB via DatabaseService
    public void saveTrafficData(int routeId, int volume, double vitesseMoyenne) {
        try {
            db.insertTrafficFull(routeId, volume, vitesseMoyenne);
            System.out.println("Traffic data saved successfully via DatabaseService");
        } catch (Exception e) {
            System.err.println("Error saving traffic data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}