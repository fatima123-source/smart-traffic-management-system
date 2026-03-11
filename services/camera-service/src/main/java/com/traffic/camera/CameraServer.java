package com.traffic.camera;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import com.traffic.camera.KafkaCameraProducer;
public class CameraServer implements CameraRemote {

    private KafkaCameraProducer producer;

    public CameraServer() {
        producer = new KafkaCameraProducer();
    }

    @Override
    public void detectAccident(int routeId, String description) {
        String message = "Route " + routeId + ": ACCIDENT - " + description;
        System.out.println("Accident detected: " + message);
        producer.sendAccidentData(message);
    }

    public static void main(String[] args) {
        try {
            CameraServer server = new CameraServer();
            CameraRemote stub = (CameraRemote) UnicastRemoteObject.exportObject(server, 0);

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("CameraService", stub);

            System.out.println("Camera RMI Server is running on port 1099...");

            // Bloquer le thread principal
            Thread.currentThread().join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}