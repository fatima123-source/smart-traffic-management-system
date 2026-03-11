/*package com.traffic.central;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class NoiseTCPServer {

    public static void main(String[] args) {

        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Noise TCP Server started on port " + port);

            while (true) {

                Socket socket = serverSocket.accept();
                System.out.println("Sensor connected");

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String data;

                while ((data = reader.readLine()) != null) {

                    System.out.println("Received data: " + data);

                    // analyse des données
                    TrafficAnalyzer.analyze(data);
                }

                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/

package com.traffic.central;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class NoiseTcpServer {

    public static void main(String[] args) {

        try {

            ServerSocket serverSocket = new ServerSocket(5000);

            System.out.println("Noise TCP Server started on port 5000");

            KafkaNoiseProducer producer = new KafkaNoiseProducer();

            while (true) {

                Socket socket = serverSocket.accept();

                System.out.println("Sensor connected");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );

                String data;

                while ((data = reader.readLine()) != null) {

                    System.out.println("Received data: " + data);

                    producer.sendNoiseData(data);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}