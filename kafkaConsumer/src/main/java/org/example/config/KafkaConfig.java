package org.example.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.entity.MessageEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
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
//        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // 幂等生产
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transactional-id");
//        DefaultKafkaProducerFactory<String, MessageEntity> stringStringDefaultKafkaProducerFactory = ;
//        stringStringDefaultKafkaProducerFactory.setValueSerializer(new JsonSerializer<>());
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean("kafkaTemplate")
    public KafkaTemplate<String, MessageEntity> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaTransactionManager<String, MessageEntity> kafkaTransactionManager() {
        return new KafkaTransactionManager<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, MessageEntity> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // 禁用自动提交偏移量
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed"); // 只读取已提交的消息
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new ErrorHandlingDeserializer<>(new JsonDeserializer<>(MessageEntity.class)));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MessageEntity> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MessageEntity> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3); // 并发处理
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL); // 每条消息确认
//        factory.setRecordMessageConverter(new JsonMessageConverter());
        return factory;
    }
}