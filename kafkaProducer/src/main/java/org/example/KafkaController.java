package org.example;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class KafkaController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Operation(summary = "根据用户ID获取用户信息", description = "通过用户ID获取用户的详细信息")
    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@Parameter(name = "Kafka消息内容", required = true) @RequestBody @NotNull MessageEntity messageEntity) {
        kafkaProducer.sendMessage(messageEntity);
        return ResponseEntity.ok("Message sent to Kafka topic");
    }
}