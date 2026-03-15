package com.traffic.vehicle;

import java.util.Random;

public class VehicleSimulator {

    private Random random = new Random();
    private int[] routes = {1, 2, 3, 4, 5};

    // Générer volume véhicules (1 à 400)
    public int generateVolume() {
        return random.nextInt(400) + 1;
    }

    // Générer id de la route
    public int generateRouteId() {
        return routes[random.nextInt(routes.length)];
    }

    // Générer vitesse moyenne (20 à 100 km/h)
    public double generateAverageSpeed() {
        return 20 + (random.nextDouble() * 80);
    }

    // Générer message format TrafficAnalyzer
    public String generateMessage() {
        int routeId = generateRouteId();
        int volume = generateVolume();
        double speed = generateAverageSpeed();
        return "Route:" + routeId + ",Traffic," + volume + "," + speed;
    }
}