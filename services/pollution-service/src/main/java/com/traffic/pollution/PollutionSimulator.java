package com.traffic.pollution;

import java.util.Properties;
import java.util.Random;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class PollutionSimulator {

    public static void main(String[] args) {

        Properties props = new Properties();

        props.put("bootstrap.servers","localhost:9092");
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String,String> producer =
                new KafkaProducer<>(props);

        Random random = new Random();

        // routes disponibles
        int[] routes = {1,2,3,4,5};

        // types de pollution
        String[] types = {"CO2","NO2","PM2.5","PM10"};

        while(true){

            try{

                int routeId = routes[random.nextInt(routes.length)];

                String type = types[random.nextInt(types.length)];

                // valeur pollution (0 → 200)
                double level = 50 + random.nextInt(160);

                String message =
                        "Route " + routeId + ": Pollution, " + type + ", " + level;

                ProducerRecord<String,String> record =
                        new ProducerRecord<>("pollution", message);

                producer.send(record);

                System.out.println("Sent: " + message);

                Thread.sleep(10000); // toutes les 10 secondes

            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}