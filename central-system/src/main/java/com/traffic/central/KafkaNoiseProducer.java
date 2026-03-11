package com.traffic.central;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaNoiseProducer {

    private KafkaProducer<String, String> producer;

    public KafkaNoiseProducer() {

        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(props);
    }

    public void sendNoiseData(String data) {

        ProducerRecord<String, String> record =
                new ProducerRecord<>("noise-topic", data);

        producer.send(record);

        System.out.println("Sent to Kafka: " + data);
    }
}