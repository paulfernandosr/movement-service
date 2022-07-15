package com.nttdata.movementservice.model;

import com.nttdata.movementservice.util.Constants;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@Document(collection = Constants.MOVEMENTS_COLLECTION)
public class Movement {

    @Id
    private String id;
    private Double amount;
    private LocalDateTime timestamp;
    private String type;
    private String movementId;
    private String sourceBankAccountId;
    private String targetBankAccountId;
    private String creditId;

}
