package org.zalmoxis.evetic.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalmoxis.evetic.dtos.ticketValidation.request.TicketValidationReqDto;
import org.zalmoxis.evetic.dtos.ticketValidation.response.TicketValidationResDto;
import org.zalmoxis.evetic.entities.TicketValidation;
import org.zalmoxis.evetic.entities.TicketValidationMethodEnum;
import org.zalmoxis.evetic.mappers.TicketValidationMapper;
import org.zalmoxis.evetic.services.TicketValidationService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ticket-validations/{eventId}")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class TicketValidationController
{

    private final TicketValidationService ticketValidationService;
    private final TicketValidationMapper ticketValidationMapper;

    @PostMapping("/validate")
    public ResponseEntity<TicketValidationResDto> validateTicket(
            @Valid @RequestBody TicketValidationReqDto ticketValidationReqDto,
            @PathVariable("eventId") UUID eventId
    )
    {
        TicketValidationMethodEnum ticketValidationMethodEnum = ticketValidationReqDto.getTicketValidationMethod();

        TicketValidation ticketValidation;
        if (ticketValidationMethodEnum.equals(TicketValidationMethodEnum.MANUAL_ENTRY)) {
            ticketValidation = ticketValidationService.validateTicketManually(ticketValidationReqDto.getId(), eventId);
        }else{
            ticketValidation = ticketValidationService.validateTicketByQrCode(ticketValidationReqDto.getId(), eventId);
        }

        return ResponseEntity.ok(ticketValidationMapper.toTicketValidationResDto(ticketValidation));
    }
}
