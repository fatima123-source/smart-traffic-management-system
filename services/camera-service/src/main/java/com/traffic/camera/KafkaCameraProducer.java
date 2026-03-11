package com.traffic.camera;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

public class KafkaCameraProducer {

    private KafkaProducer<String, String> producer;

    public KafkaCameraProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(props);
    }

    public void sendAccidentData(String data) {
        ProducerRecord<String, String> record =
                new ProducerRecord<>("camera-topic", data);
        producer.send(record);
        System.out.println("Sent to Kafka (camera-topic): " + data);
    }
}