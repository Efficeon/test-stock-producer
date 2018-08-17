package com.testStockProducer;

import kafka.admin.AdminUtils;
import kafka.common.TopicExistsException;
import kafka.utils.ZKStringSerializer$;
import org.I0Itec.zkclient.ZkClient;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;

public class ProducerApplication {

    private final static String TOPIC_MASTER = "stock-topic-master";
    private final static String BOOTSTRAP_SERVER = "localhost:9092";
    private final static String ZK_SERVER = "localhost:2181";

    public static void main(String[] args) {
        createTopic();
        startProducer();
    }

    private static void createTopic() {
        ZkClient zkClient = new ZkClient(ZK_SERVER, 10000, 10000, ZKStringSerializer$.MODULE$);

        try {
            AdminUtils.createTopic(zkClient, TOPIC_MASTER, 10, 1, new Properties());
        } catch (TopicExistsException e) {
            System.out.println("*** Topic '" + TOPIC_MASTER + "' already exist.");
        } finally {
            zkClient.close();
        }
    }

    private static void startProducer() {
        List<String> tickerList = Arrays.asList("GOOG", "LINK", "AMZN", "TSHB", "FACE", "JBRA");
        Double leftLimit = 1.5;
        Double rightLimit = 3.5;

        Producer<String, Double> producer = createProducer();

        for (String ticker : tickerList) {
            for (int i = 0; i < 20; i++) {
                Double random = leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
                final ProducerRecord<String, Double> producerRecord = new ProducerRecord<>(TOPIC_MASTER, ticker, random);
                producer.send(producerRecord);
            }
        }

        producer.flush();
        producer.close();
    }

    private static Producer<String, Double> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DoubleSerializer.class.getName());

        return new KafkaProducer<>(props);
    }
}
