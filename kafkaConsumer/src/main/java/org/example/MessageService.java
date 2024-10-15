package org.example;


import org.example.entity.MessageEntity;
import org.example.repo.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Transactional(value="dbTransactionManager", rollbackFor = Exception.class)
    public MessageEntity saveMessage(MessageEntity messageEntity) {
        MessageEntity result = messageRepository.save(messageEntity);
        if ("error".equals(messageEntity.getMessage())) {
            throw new RuntimeException("error............");
        }
        return result;
    }
}
