package com.nttdata.movementservice.repo;

import com.nttdata.movementservice.model.BankAccountMovement;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface IBankAccountMovementRepo extends ReactiveMongoRepository<BankAccountMovement, String> {

    Flux<BankAccountMovement> findBySavingAccountId(String savingAccountId);

    Flux<BankAccountMovement> findByFixedTermAccountId(String fixedTermAccountId);

    Flux<BankAccountMovement> findByCheckingAccountId(String checkingAccountId);

}
