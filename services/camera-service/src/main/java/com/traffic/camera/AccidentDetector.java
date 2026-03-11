package com.traffic.camera;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class AccidentDetector {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            CameraRemote camera = (CameraRemote) registry.lookup("CameraService");

            int[] routes = {1, 2, 3, 4, 5};
            Random random = new Random();

            while (true) {
                Thread.sleep(120000); // simulate accident after 2 minutes
                int routeId = routes[random.nextInt(routes.length)];
                String description = "Accident simule par camera";

                camera.detectAccident(routeId, description);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}