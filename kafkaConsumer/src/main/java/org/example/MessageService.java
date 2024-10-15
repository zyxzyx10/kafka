package org.example;


import org.example.entity.MessageEntity;
import org.example.repo.MessageRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Transactional("dbTransactionManager")
    public MessageEntity saveMessage(MessageEntity messageEntity) throws Exception {
        MessageEntity result = messageRepository.save(messageEntity);
        if ("error".equals(messageEntity.getMessage())) {
            throw new Exception("error............");
        }
        return result;
    }
}
