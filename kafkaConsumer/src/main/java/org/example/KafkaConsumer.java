package org.example;

import org.example.entity.MessageEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//@EnableTransactionManagement
@EnableRetry
public class KafkaConsumer {
    private final MessageService messageService;

    public KafkaConsumer(MessageService messageService) {
        this.messageService = messageService;
    }

//    private final KafkaTemplate<String, MessageEntity> kafkaTemplate;
//
//    public KafkaConsumer(KafkaTemplate<String, MessageEntity> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }

    @KafkaListener(topics = "my-topic", groupId = "my-consumer-group", containerFactory = "kafkaListenerContainerFactory")
    @Transactional("kafkaTransactionManager")
    public void listen(@Payload MessageEntity message, Acknowledgment ack) throws Exception {
        try {
            System.out.println("Received message: " + message);
            // 在这里可以处理消息，如保存到数据库等
            // 保证消息的幂等性（例如根据唯一键或ID进行去重处理）
            messageService.saveMessage(message);
            // 事务结束后，手动提交偏移量
            ack.acknowledge();
        } catch (Exception e) {
            // 如果出现异常，事务将回滚，消息不会被确认并且会重新消费
            System.err.println("Error processing message, transaction will be rolled back.");
            throw e;
        }
    }
}