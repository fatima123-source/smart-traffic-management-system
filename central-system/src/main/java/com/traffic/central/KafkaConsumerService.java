package com.traffic.central;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerService {

    public static void main(String[] args) {

        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "traffic-group");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // S'abonner à plusieurs topics Kafka
        consumer.subscribe(Arrays.asList(
                "noise-topic",
                "camera-topic",
                "vehicle-flow",
                "pollution"
        ));

        System.out.println("Kafka Consumer started... Listening on all Smart Traffic topics");

        while (true) {

            ConsumerRecords<String, String> records =
                    consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, String> record : records) {

                String data = record.value();

                System.out.println("Kafka received from topic [" +
                        record.topic() + "]: " + data);

                // Analyse les données (traffic, pollution, bruit, accident)
                TrafficAnalyzer.analyze(data);
            }
        }
    }
}