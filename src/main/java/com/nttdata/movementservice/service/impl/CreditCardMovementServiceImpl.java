package com.nttdata.movementservice.service.impl;

import com.nttdata.movementservice.dto.CreditCardConsumptionDto;
import com.nttdata.movementservice.dto.CreditCardMovementDto;
import com.nttdata.movementservice.dto.CreditCardPaymentDto;
import com.nttdata.movementservice.exception.BadRequestException;
import com.nttdata.movementservice.exception.MovementNotFoundException;
import com.nttdata.movementservice.repo.ICreditCardMovementRepo;
import com.nttdata.movementservice.service.ICreditCardMovementService;
import com.nttdata.movementservice.service.ICreditService;
import com.nttdata.movementservice.util.Constants;
import com.nttdata.movementservice.util.MovementMapper;
import lombok.RequiredArgsConstructor;
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
                .flatMap(creditCardDto -> creditCardPaymentDto.getAmount() <= creditCardDto.getConsumed()
                        ? creditService.updateCreditCardById(creditCardDto.getId(),
                        creditCardDto.toBuilder()
                                .consumed(creditCardDto.getConsumed() - creditCardPaymentDto.getAmount())
                                .build())
                        : Mono.error(new BadRequestException(Constants.PAYMENT_AMOUNT_IS_GREATER_THAN_CONSUMED)))
                .flatMap(updatedCreditCardDto -> repo.save(MovementMapper.toModel(creditCardPaymentDto).toBuilder()
                        .id(null)
                        .timestamp(LocalDateTime.now())
                        .build()))
                .map(MovementMapper::toPaymentDto);
    }

    @Override
    public Mono<CreditCardConsumptionDto> registerConsumption(CreditCardConsumptionDto creditCardConsumptionDto) {
        return creditService.getCreditCardById(creditCardConsumptionDto.getCreditCardId())
                .flatMap(creditCardDto -> creditCardDto.getConsumed() - creditCardConsumptionDto.getAmount() <= creditCardDto.getCreditLine()
                        ? creditService.updateCreditCardById(creditCardDto.getId(),
                        creditCardDto.toBuilder()
                                .consumed(creditCardDto.getConsumed() - creditCardConsumptionDto.getAmount())
                                .build())
                        : Mono.error(new BadRequestException(Constants.TOTAL_CONSUMPTION_AMOUNT_IS_GREATER_THAN_CREDIT_LINE)))
                .flatMap(updatedCreditCardDto -> repo.save(MovementMapper.toModel(creditCardConsumptionDto).toBuilder()
                        .id(null)
                        .timestamp(LocalDateTime.now())
                        .build()))
                .map(MovementMapper::toConsumptionDto);
    }

    @Override
    public Mono<CreditCardMovementDto> updateById(String id, CreditCardMovementDto creditCardMovementDto) {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return null;
    }

}
