package com.traffic.vehicle;

import org.apache.kafka.clients.producer.*;
import java.util.Properties;

public class VehiclePublisher {

    public static void main(String[] args) throws InterruptedException {

        VehicleSimulator simulator = new VehicleSimulator();

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        System.out.println("Vehicle Publisher started... Sending traffic data to Central System");

        while (true) {

            int routeId = simulator.generateRouteId();
            int volume = simulator.generateVolume();
            double speed = simulator.generateAverageSpeed();

            // ⚡ Message corrigé pour TrafficAnalyzer
            String message = "Route " + routeId + ": Traffic," + volume + "," + speed;

            ProducerRecord<String, String> record =
                    new ProducerRecord<>("vehicle-flow", message);

            producer.send(record);

            System.out.println("Sent: " + message);

            Thread.sleep(2000); // envoi toutes les 2 secondes
        }
    }
}