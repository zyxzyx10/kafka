package org.example;


import org.example.entity.MessageEntity;
import org.example.exception.RetryableException;
import org.example.repo.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import java.security.*;

import static net.i2p.crypto.eddsa.Utils.bytesToHex;


@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Transactional(value="dbTransactionManager", rollbackFor = Exception.class)
    public MessageEntity saveMessage(MessageEntity messageEntity) throws Exception, RetryableException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048); // 密钥长度
        KeyPair keyPair = keyGen.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // 加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedMessage = cipher.doFinal(messageEntity.getMessage().getBytes());
        System.out.println("Encrypted message: " + bytesToHex(encryptedMessage));

        // 解密
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessage = cipher.doFinal(encryptedMessage);
        System.out.println("Decrypted message: " + new String(decryptedMessage));

        MessageEntity result = messageRepository.save(messageEntity);
        if ("error".equals(messageEntity.getMessage()) && messageEntity.getTimestamp() == 1) {
            throw new RetryableException("error............");
        }
        return result;
    }
}
