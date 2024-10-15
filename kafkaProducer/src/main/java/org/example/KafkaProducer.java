package org.example;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.example.entity.MessageEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducer {

    private static final String TOPIC = "my-topic";

    private final KafkaTemplate<String, MessageEntity> kafkaTemplate;

    public KafkaProducer(@Qualifier("kafkaTemplate") KafkaTemplate<String, MessageEntity> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public void sendMessage(MessageEntity messageEntity) {
        System.out.println("Sending message: " + messageEntity);
        kafkaTemplate.send(TOPIC, messageEntity).thenAccept(result -> System.out.println("Sent message:" + result.getProducerRecord()));

        messageEntity.setTimestamp(1);
        System.out.println("Sending message: " + messageEntity);
        kafkaTemplate.send(TOPIC, messageEntity).thenAccept(result -> System.out.println("Sent message:" + result.getProducerRecord()));;
        if("senderError".equals(messageEntity.getMessage())) {
            throw new RuntimeException("Throwing Exception, producer should rollback 2 messages due to the transactional sending.");
        }
    }

    public static void main(String[] args) {

        // 配置Kafka Producer
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all"); // 等待所有副本确认

        org.apache.kafka.clients.producer.KafkaProducer<String, String> kafkaProducer = new org.apache.kafka.clients.producer.KafkaProducer(props);
        try {
            // 发送消息
            for (int i = 0; i < 10; i++) {
                String message = "message" + i;
                ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, new MessageEntity(message, 1234321).toString());
                kafkaProducer.send(record, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if (exception != null) {
                            exception.printStackTrace();
                        } else {
                            System.out.println("Sent message: " + message + " to partition: " + metadata.partition());
                        }
                    }
                });
            }
        } finally {
            kafkaProducer.close();
        }
    }
}