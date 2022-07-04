package com.nttdata.movementservice.repo;

import com.nttdata.movementservice.model.MovementType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IMovementTypeRepo extends ReactiveMongoRepository<MovementType, String> {
}
