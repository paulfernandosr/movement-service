package com.nttdata.movementservice.model;

import com.nttdata.movementservice.util.Constants;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@Document(collection = Constants.CREDIT_CARD_MOVEMENTS_COLLECTION)
public class CreditCardMovement {

    @Id
    private String id;
    private Double amount;
    private LocalDateTime timestamp;
    private String movementTypeId;
    private String personalCreditCardId;
    private String businessCreditCardId;

}
