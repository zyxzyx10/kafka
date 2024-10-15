package org.example.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.entity.MessageEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, MessageEntity> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // 幂等生产
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transactional-id");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, "1000");// to avoid error
        //org.apache.kafka.common.errors.ProducerFencedException: There is a newer producer with the same transactionalId which fences the current one.
        DefaultKafkaProducerFactory<String, MessageEntity> stringStringDefaultKafkaProducerFactory = new DefaultKafkaProducerFactory<>(props);
        stringStringDefaultKafkaProducerFactory.setValueSerializer(new JsonSerializer<>());
        return stringStringDefaultKafkaProducerFactory;
    }

    @Bean("kafkaTemplate")
    public KafkaTemplate<String, MessageEntity> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaTransactionManager<String, MessageEntity> kafkaTransactionManager() {
        return new KafkaTransactionManager<>(producerFactory());
    }
}