package com.testStockProducer;

import kafka.utils.ZKStringSerializer$;
import org.I0Itec.zkclient.ZkClient;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

public class ProducerService {

    private final static String BOOTSTRAP_SERVER = "localhost:9092";
    private final static String ZK_SERVER = "localhost:2181";

    static void createTopic(String topicName) {
        AdminClient admin = AdminClient.create(initConfig());

        ZkClient zkClient = new ZkClient(ZK_SERVER, 10000, 10000, ZKStringSerializer$.MODULE$);
        admin.createTopics(Collections.singletonList(new NewTopic(topicName, 1, (short) 1).configs(new HashMap<>())));
        zkClient.close();
    }

    static Properties initConfig() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducer");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG , StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG , DoubleSerializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG , "earliest");

        return properties;
    }
}
