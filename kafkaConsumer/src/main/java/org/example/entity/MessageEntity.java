package org.example.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MessageEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 自增长主键
    private int id;
    private String message;
    private long timestamp;
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
