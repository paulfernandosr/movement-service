package com.nttdata.movementservice.controller;

import com.nttdata.movementservice.dto.BankAccountDepositDto;
import com.nttdata.movementservice.dto.BankAccountMovementDto;
import com.nttdata.movementservice.dto.BankAccountWithdrawalDto;
import com.nttdata.movementservice.service.IBankAccountMovementService;
import com.nttdata.movementservice.util.Constants;
import com.nttdata.movementservice.util.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.BANK_ACCOUNT_MOVEMENT_CONTROLLER)
public class BankAccountMovementController {

    private final IBankAccountMovementService service;
    private final RequestValidator validator;

    @GetMapping(Constants.GET_ALL_METHOD)
    public Mono<ResponseEntity<Flux<BankAccountMovementDto>>> getAll() {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getAll()));
    }

    @GetMapping(Constants.GET_BY_ID_METHOD)
    public Mono<ResponseEntity<BankAccountMovementDto>> getById(@PathVariable(Constants.PATH_ID_VARIABLE) String id) {
        return service.getById(id).map(account -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(account));
    }

    @GetMapping(Constants.GET_BY_SAVING_ACCOUNT_ID_METHOD)
    public Mono<ResponseEntity<Flux<BankAccountMovementDto>>> getBySavingAccountId(@PathVariable(Constants.PATH_ID_VARIABLE) String id) {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getBySavingAccountId(id)));
    }

    @GetMapping(Constants.GET_BY_FIXED_TERM_ACCOUNT_ID_METHOD)
    public Mono<ResponseEntity<Flux<BankAccountMovementDto>>> getByFixedTermAccountId(@PathVariable(Constants.PATH_ID_VARIABLE) String id) {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getByFixedTermAccountId(id)));
    }

    @GetMapping(Constants.GET_BY_PERSONAL_CHECKING_ACCOUNT_ID_METHOD)
    public Mono<ResponseEntity<Flux<BankAccountMovementDto>>> getByPersonalCheckingAccountId(@PathVariable(Constants.PATH_ID_VARIABLE) String id) {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getByPersonalCheckingAccountId(id)));
    }

    @GetMapping(Constants.GET_BY_BUSINESS_CHECKING_ACCOUNT_ID_METHOD)
    public Mono<ResponseEntity<Flux<BankAccountMovementDto>>> getByBusinessCheckingAccountId(@PathVariable(Constants.PATH_ID_VARIABLE) String id) {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getByBusinessCheckingAccountId(id)));
    }

    @PostMapping(Constants.REGISTER_DEPOSIT_METHOD)
    public Mono<ResponseEntity<BankAccountDepositDto>> registerDeposit(@RequestBody BankAccountDepositDto account, final ServerHttpRequest request) {
        return validator.validate(account)
                .flatMap(validatedAccount -> service.registerDeposit(account)
                        .map(registeredAccount -> ResponseEntity.created(URI.create(request.getURI() + Constants.SLASH + registeredAccount.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(registeredAccount)));
    }

    @PostMapping(Constants.REGISTER_WITHDRAWAL_METHOD)
    public Mono<ResponseEntity<BankAccountWithdrawalDto>> registerWithdrawal(@RequestBody BankAccountWithdrawalDto account, final ServerHttpRequest request) {
        return validator.validate(account)
                .flatMap(validatedAccount -> service.registerWithdrawal(account)
                        .map(registeredAccount -> ResponseEntity.created(URI.create(request.getURI() + Constants.SLASH + registeredAccount.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(registeredAccount)));
    }

}
