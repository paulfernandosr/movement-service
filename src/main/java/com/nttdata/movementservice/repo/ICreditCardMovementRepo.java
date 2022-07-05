package com.nttdata.movementservice.repo;

import com.nttdata.movementservice.model.CreditCardMovement;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ICreditCardMovementRepo extends ReactiveMongoRepository<CreditCardMovement, String> {

    Flux<CreditCardMovement> findByCreditCardId(String creditCardId);

}
