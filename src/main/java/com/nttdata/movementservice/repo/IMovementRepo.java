package com.nttdata.movementservice.repo;

import com.nttdata.movementservice.model.Movement;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface IMovementRepo extends ReactiveMongoRepository<Movement, String> {

    Flux<Movement> findByTargetBankAccountId(String bankAccountId);

    Flux<Movement> findByCreditId(String bankAccountId);

    Flux<Movement> findByTypeStartsWithAndTargetBankAccountId(String type, String bankAccountId);

    Flux<Movement> findByTypeAndTargetBankAccountIdAndTimestampBetween(String type, String bankAccountId, LocalDateTime start, LocalDateTime end);



}
