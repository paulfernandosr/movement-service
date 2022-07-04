package com.nttdata.movementservice.repo;

import com.nttdata.movementservice.model.BankAccountMovement;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface IBankAccountMovementRepo extends ReactiveMongoRepository<BankAccountMovement, String> {

    Flux<BankAccountMovement> findBySavingAccountId(String personalSavingAccountId);

    Flux<BankAccountMovement> findByFixedTermAccountId(String personalFixedTermAccountId);

    Flux<BankAccountMovement> findByPersonalCheckingAccountId(String personalCheckingAccountId);

    Flux<BankAccountMovement> findByBusinessCheckingAccountId(String businessCheckingAccountId);

}
