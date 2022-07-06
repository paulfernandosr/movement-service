package com.nttdata.movementservice.service.impl;

import com.nttdata.movementservice.dto.CreditCardConsumptionDto;
import com.nttdata.movementservice.dto.CreditCardMovementDto;
import com.nttdata.movementservice.dto.CreditCardPaymentDto;
import com.nttdata.movementservice.exception.DomainException;
import com.nttdata.movementservice.exception.MovementNotFoundException;
import com.nttdata.movementservice.repo.ICreditCardMovementRepo;
import com.nttdata.movementservice.service.ICreditCardMovementService;
import com.nttdata.movementservice.service.ICreditService;
import com.nttdata.movementservice.util.Constants;
import com.nttdata.movementservice.util.MovementMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreditCardMovementServiceImpl implements ICreditCardMovementService {

    private final ICreditCardMovementRepo repo;
    private final ICreditService creditService;

    @Override
    public Flux<CreditCardMovementDto> getAll() {
        return repo.findAll().map(MovementMapper::toDto);
    }

    @Override
    public Mono<CreditCardMovementDto> getById(String id) {
        return repo.findById(id)
                .map(MovementMapper::toDto)
                .switchIfEmpty(Mono.error(new MovementNotFoundException(String.format(Constants.MOVEMENT_NOT_FOUND, Constants.ID, id))));
    }

    @Override
    public Flux<CreditCardMovementDto> getByCreditCardId(String creditCardId) {
        return repo.findByCreditCardId(creditCardId).map(MovementMapper::toDto);
    }

    @Override
    public Mono<CreditCardPaymentDto> registerPayment(CreditCardPaymentDto creditCardPaymentDto) {
        return creditService.getCreditCardById(creditCardPaymentDto.getCreditCardId())
                .flatMap(creditCardDto -> creditCardDto.getBalance() + creditCardPaymentDto.getAmount() <= creditCardDto.getCreditLine()
                        ? creditService.updateCreditCardById(creditCardDto.getId(),
                        creditCardDto.toBuilder()
                                .balance(creditCardDto.getBalance() + creditCardPaymentDto.getAmount())
                                .build())
                        : Mono.error(new DomainException(HttpStatus.BAD_REQUEST, Constants.BALANCE_IS_GREATER_THAN_CREDIT_LINE)))
                .flatMap(updatedCreditCardDto -> repo.save(MovementMapper.toModel(creditCardPaymentDto).toBuilder()
                        .id(null)
                        .timestamp(LocalDateTime.now())
                        .build()))
                .map(MovementMapper::toPaymentDto);
    }

    @Override
    public Mono<CreditCardConsumptionDto> registerConsumption(CreditCardConsumptionDto creditCardConsumptionDto) {
        return creditService.getCreditCardById(creditCardConsumptionDto.getCreditCardId())
                .flatMap(creditCardDto -> creditCardDto.getBalance() + creditCardConsumptionDto.getAmount() >= 0
                        ? creditService.updateCreditCardById(creditCardDto.getId(),
                        creditCardDto.toBuilder()
                                .balance(creditCardDto.getBalance() + creditCardConsumptionDto.getAmount())
                                .build())
                        : Mono.error(new DomainException(HttpStatus.BAD_REQUEST, Constants.CONSUMPTION_AMOUNT_IS_GREATER_THAN_BALANCE)))
                .flatMap(updatedCreditCardDto -> repo.save(MovementMapper.toModel(creditCardConsumptionDto).toBuilder()
                        .id(null)
                        .timestamp(LocalDateTime.now())
                        .build()))
                .map(MovementMapper::toConsumptionDto);
    }

    @Override
    public Mono<CreditCardMovementDto> updateById(String id, CreditCardMovementDto creditCardMovementDto) {
        Mono<CreditCardMovementDto> movementDtoReqMono = Mono.just(creditCardMovementDto);
        Mono<CreditCardMovementDto> movementDtoDbMono = getById(id);
        return movementDtoReqMono.zipWith(movementDtoDbMono, (movementDtoReq, movementDtoDb) ->
                        MovementMapper.toModel(movementDtoDb.toBuilder()
                                .amount(movementDtoReq.getAmount())
                                .timestamp(movementDtoReq.getTimestamp())
                                .creditCardId(movementDtoReq.getCreditCardId())
                                .build()))
                .flatMap(movement -> repo.save(movement).map(MovementMapper::toDto));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return getById(id).flatMap(movementDto -> repo.deleteById(id));
    }

}
