/*package com.traffic.camera;

import java.rmi.Naming;

public class CameraClient {
    public static void main(String[] args) {
        try {
CameraRemote camera = (CameraRemote) Naming.lookup("rmi://127.0.0.1:1099/CameraService");
            for (int i = 1; i <= 5; i++) {
                String result = camera.detectAccident(i);
                System.out.println("Client received: " + result);
                Thread.sleep(10000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/