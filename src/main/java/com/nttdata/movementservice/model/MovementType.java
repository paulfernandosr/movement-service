package com.nttdata.movementservice.model;

import com.nttdata.movementservice.util.Constants;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder(toBuilder = true)
@Document(collection = Constants.MOVEMENT_TYPES_COLLECTION)
public class MovementType {

    private String id;
    private String name;
    private String description;

}
