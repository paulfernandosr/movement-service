package com.nttdata.movementservice.controller;

import com.nttdata.movementservice.dto.MovementDto;
import com.nttdata.movementservice.dto.request.*;
import com.nttdata.movementservice.service.IMovementService;
import com.nttdata.movementservice.util.Constants;
import com.nttdata.movementservice.util.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.MOVEMENT_CONTROLLER)
public class MovementController {

    private final IMovementService service;
    private final RequestValidator validator;

    @GetMapping(Constants.GET_ALL_METHOD)
    public Mono<ResponseEntity<Flux<MovementDto>>> getAll() {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getAll()));
    }

    @GetMapping(Constants.GET_BY_ID_METHOD)
    public Mono<ResponseEntity<MovementDto>> getById(@PathVariable(Constants.ID) String id) {
        return service.getById(id).map(account -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(account));
    }

    @GetMapping(Constants.GET_BY_CREDIT_ID_METHOD)
    public Mono<ResponseEntity<Flux<MovementDto>>> getByCreditId(@PathVariable(Constants.CREDIT_ID) String creditId) {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getByCreditId(creditId)));
    }

    @GetMapping(Constants.GET_BY_BANK_ACCOUNT_ID_METHOD)
    public Mono<ResponseEntity<Flux<MovementDto>>> getByBankAccountId(@PathVariable(Constants.BANK_ACCOUNT_ID) String bankAccountId) {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getByBankAccountId(bankAccountId)));
    }

    @GetMapping(Constants.GET_BANK_ACCOUNT_FEES_METHOD)
    public Mono<ResponseEntity<Flux<MovementDto>>> getBankAccountFees(@RequestBody FeesBankAccountDto feesBankAccount) {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getBankAccountFees(feesBankAccount)));
    }

    @PostMapping(Constants.REGISTER_DEPOSIT_METHOD)
    public Mono<ResponseEntity<MovementDto>> registerDeposit(@RequestBody TransactionBankAccountDto movement, final ServerHttpRequest request) {
        return validator.validate(movement)
                .flatMap(validatedMovement -> service.registerDeposit(validatedMovement)
                        .map(registeredMovement -> ResponseEntity.created(URI.create(request.getURI() + Constants.SLASH + registeredMovement.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(registeredMovement)));
    }

    @PostMapping(Constants.REGISTER_WITHDRAWAL_METHOD)
    public Mono<ResponseEntity<MovementDto>> registerWithdrawal(@RequestBody TransactionBankAccountDto movement, final ServerHttpRequest request) {
        return validator.validate(movement)
                .flatMap(validatedMovement -> service.registerWithdrawal(validatedMovement)
                        .map(registeredMovement -> ResponseEntity.created(URI.create(request.getURI() + Constants.SLASH + registeredMovement.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(registeredMovement)));
    }

    @PostMapping(Constants.REGISTER_DEBIT_CARD_PAYMENT_METHOD)
    public Mono<ResponseEntity<MovementDto>> registerDebitCardPayment(@RequestBody MovementDebitCardDto movement, final ServerHttpRequest request) {
        return validator.validate(movement)
                .flatMap(validatedMovement -> service.registerDebitCardPayment(validatedMovement)
                        .map(registeredMovement -> ResponseEntity.created(URI.create(request.getURI() + Constants.SLASH + registeredMovement.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(registeredMovement)));
    }

    @PostMapping(Constants.REGISTER_DEBIT_CARD_WITHDRAWAL_METHOD)
    public Mono<ResponseEntity<MovementDto>> registerDebitCardWithdrawal(@RequestBody MovementDebitCardDto movement, final ServerHttpRequest request) {
        return validator.validate(movement)
                .flatMap(validatedMovement -> service.registerDebitCardWithdrawal(validatedMovement)
                        .map(registeredMovement -> ResponseEntity.created(URI.create(request.getURI() + Constants.SLASH + registeredMovement.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(registeredMovement)));
    }

    @PostMapping(Constants.REGISTER_TRANSFER_TO_OWN_BANK_ACCOUNT_METHOD)
    public Mono<ResponseEntity<MovementDto>> registerTransferToOwnBankAccount(@RequestBody TransferBankAccountDto movement, final ServerHttpRequest request) {
        return validator.validate(movement)
                .flatMap(validatedMovement -> service.registerTransferToOwnBankAccount(validatedMovement)
                        .map(registeredMovement -> ResponseEntity.created(URI.create(request.getURI() + Constants.SLASH + registeredMovement.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(registeredMovement)));
    }

    @PostMapping(Constants.REGISTER_TRANSFER_TO_THIRD_PARTIES_BANK_ACCOUNT_METHOD)
    public Mono<ResponseEntity<MovementDto>> registerTransferToThirdPartiesBankAccount(@RequestBody TransferBankAccountDto movement, final ServerHttpRequest request) {
        return validator.validate(movement)
                .flatMap(validatedMovement -> service.registerTransferToThirdPartiesBankAccount(validatedMovement)
                        .map(registeredMovement -> ResponseEntity.created(URI.create(request.getURI() + Constants.SLASH + registeredMovement.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(registeredMovement)));
    }

    @PostMapping(Constants.REGISTER_CREDIT_PAYMENT)
    public Mono<ResponseEntity<MovementDto>> registerCreditPayment(@RequestBody MovementCreditDto movement, final ServerHttpRequest request) {
        return validator.validate(movement)
                .flatMap(validatedMovement -> service.registerCreditPayment(validatedMovement)
                        .map(registeredMovement -> ResponseEntity.created(URI.create(request.getURI() + Constants.SLASH + registeredMovement.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(registeredMovement)));
    }

    @PostMapping(Constants.REGISTER_CREDIT_CARD_PAYMENT)
    public Mono<ResponseEntity<MovementDto>> registerCreditCardPayment(@RequestBody MovementCreditDto movement, final ServerHttpRequest request) {
        return validator.validate(movement)
                .flatMap(validatedMovement -> service.registerCreditCardPayment(validatedMovement)
                        .map(registeredMovement -> ResponseEntity.created(URI.create(request.getURI() + Constants.SLASH + registeredMovement.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(registeredMovement)));
    }

    @PostMapping(Constants.REGISTER_CREDIT_CARD_CONSUMPTION)
    public Mono<ResponseEntity<MovementDto>> registerCreditCardConsumption(@RequestBody MovementCreditDto movement, final ServerHttpRequest request) {
        return validator.validate(movement)
                .flatMap(validatedMovement -> service.registerCreditCardConsumption(validatedMovement)
                        .map(registeredMovement -> ResponseEntity.created(URI.create(request.getURI() + Constants.SLASH + registeredMovement.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(registeredMovement)));
    }

    @PutMapping(Constants.UPDATE_BY_ID_METHOD)
    public Mono<ResponseEntity<MovementDto>> updateById(@PathVariable(Constants.ID) String id, @RequestBody MovementDto movement) {
        return validator.validate(movement)
                .flatMap(validatedMovement -> service.updateById(id, validatedMovement)
                        .map(updatedMovement -> ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(updatedMovement)));
    }

    @DeleteMapping(Constants.DELETE_BY_ID_METHOD)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable(Constants.ID) String id) {
        return service.deleteById(id).thenReturn(new ResponseEntity<>(HttpStatus.OK));
    }

}
