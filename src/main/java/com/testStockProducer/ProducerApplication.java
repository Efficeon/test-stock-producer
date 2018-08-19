package com.testStockProducer;

import org.apache.kafka.clients.producer.*;
import java.util.*;

public class ProducerApplication {

    private final static String TOPIC_MASTER = "stock-topic-master";

    public static void main(String[] args) {
        ProducerService.createTopic(TOPIC_MASTER);
        startProducer(TOPIC_MASTER);
    }

    private static void startProducer(String topicName) {
        List<String> tickerList = Arrays.asList("GOOG", "LINK", "AMZN", "TSHB", "FACE", "JBRA");
        Double leftLimit = 1.5;
        Double rightLimit = 3.5;

        Producer<String, Double> producer = new KafkaProducer<>(ProducerService.initConfig());

        for (String ticker : tickerList) {
            for (int i = 0; i < 20; i++) {
                Double random = leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
                final ProducerRecord<String, Double> producerRecord = new ProducerRecord<>(topicName, ticker, random);
                producer.send(producerRecord);
            }
        }
        producer.flush();
        producer.close();
    }
}
