package com.nttdata.movementservice.controller;

import com.nttdata.movementservice.dto.*;
import com.nttdata.movementservice.service.ICreditCardMovementService;
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
@RequestMapping(Constants.CREDIT_MOVEMENT_CONTROLLER)
public class CreditMovementController {

    private final ICreditCardMovementService service;
    private final RequestValidator validator;

    @GetMapping(Constants.GET_ALL_METHOD)
    public Mono<ResponseEntity<Flux<CreditCardMovementDto>>> getAll() {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getAll()));
    }

    @GetMapping(Constants.GET_BY_ID_METHOD)
    public Mono<ResponseEntity<CreditCardMovementDto>> getById(@PathVariable(Constants.ID_PATH_VARIABLE) String id) {
        return service.getById(id).map(movement -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(movement));
    }

    @GetMapping(Constants.GET_BY_CREDIT_CARD_ID_METHOD)
    public Mono<ResponseEntity<Flux<CreditCardMovementDto>>> getByCreditCardId(@PathVariable(Constants.ID_PATH_VARIABLE) String id) {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getByCreditCardId(id)));
    }

    @PostMapping(Constants.REGISTER_DEPOSIT_METHOD)
    public Mono<ResponseEntity<CreditCardPaymentDto>> registerPayment(@RequestBody CreditCardPaymentDto movement, final ServerHttpRequest request) {
        return validator.validate(movement)
                .flatMap(validatedMovement -> service.registerPayment(movement)
                        .map(registeredMovement -> ResponseEntity.created(URI.create(request.getURI() + Constants.SLASH + registeredMovement.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(registeredMovement)));
    }

    @PostMapping(Constants.REGISTER_WITHDRAWAL_METHOD)
    public Mono<ResponseEntity<CreditCardConsumptionDto>> registerConsumption(@RequestBody CreditCardConsumptionDto movement, final ServerHttpRequest request) {
        return validator.validate(movement)
                .flatMap(validatedMovement -> service.registerConsumption(movement)
                        .map(registeredMovement -> ResponseEntity.created(URI.create(request.getURI() + Constants.SLASH + registeredMovement.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(registeredMovement)));
    }

    @PutMapping(Constants.UPDATE_BY_ID_METHOD)
    public Mono<ResponseEntity<CreditCardMovementDto>> updateById(@PathVariable(Constants.ID_PATH_VARIABLE) String id, @RequestBody CreditCardMovementDto movement) {
        return validator.validate(movement)
                .flatMap(validatedMovement -> service.updateById(id, validatedMovement)
                        .map(updatedMovement -> ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(updatedMovement)));
    }

    @DeleteMapping(Constants.DELETE_BY_ID_METHOD)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable(Constants.ID_PATH_VARIABLE) String id) {
        return service.deleteById(id).thenReturn(new ResponseEntity<>(HttpStatus.OK));
    }

}
