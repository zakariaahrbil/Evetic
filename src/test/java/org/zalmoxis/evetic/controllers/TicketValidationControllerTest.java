package org.zalmoxis.evetic.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.zalmoxis.evetic.dtos.ticketvalidation.request.TicketValidationReqDto;
import org.zalmoxis.evetic.dtos.ticketvalidation.response.TicketValidationResDto;
import org.zalmoxis.evetic.entities.TicketValidation;
import org.zalmoxis.evetic.entities.TicketValidationMethodEnum;
import org.zalmoxis.evetic.entities.TicketValidationStatusEnum;
import org.zalmoxis.evetic.mappers.TicketValidationMapper;
import org.zalmoxis.evetic.services.TicketValidationService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketValidationControllerTest {

    @Mock
    private TicketValidationService ticketValidationService;

    @Mock
    private TicketValidationMapper ticketValidationMapper;

    @InjectMocks
    private TicketValidationController ticketValidationController;

    private UUID eventId;
    private UUID ticketId;
    private UUID qrCodeId;
    private TicketValidation ticketValidation;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        ticketId = UUID.randomUUID();
        qrCodeId = UUID.randomUUID();

        ticketValidation = TicketValidation.builder()
                .status(TicketValidationStatusEnum.VALID)
                .build();
    }

    @Test
    void validateTicket_ShouldValidateManually_WhenMethodIsManualEntry() {
        TicketValidationReqDto reqDto = new TicketValidationReqDto(ticketId, TicketValidationMethodEnum.MANUAL_ENTRY);
        TicketValidationResDto resDto = new TicketValidationResDto();
        resDto.setTicketId(ticketId);
        resDto.setValidationStatus(TicketValidationStatusEnum.VALID);

        when(ticketValidationService.validateTicketManually(ticketId, eventId)).thenReturn(ticketValidation);
        when(ticketValidationMapper.toTicketValidationResDto(ticketValidation)).thenReturn(resDto);

        ResponseEntity<TicketValidationResDto> response = ticketValidationController.validateTicket(reqDto, eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ticketId, response.getBody().getTicketId());
        assertEquals(TicketValidationStatusEnum.VALID, response.getBody().getValidationStatus());
        verify(ticketValidationService, times(1)).validateTicketManually(ticketId, eventId);
        verify(ticketValidationService, never()).validateTicketByQrCode(any(), any());
    }

    @Test
    void validateTicket_ShouldValidateByQrCode_WhenMethodIsQrCode() {
        TicketValidationReqDto reqDto = new TicketValidationReqDto(qrCodeId, TicketValidationMethodEnum.QR_CODE);
        TicketValidationResDto resDto = new TicketValidationResDto();
        resDto.setTicketId(ticketId);
        resDto.setValidationStatus(TicketValidationStatusEnum.VALID);

        when(ticketValidationService.validateTicketByQrCode(qrCodeId, eventId)).thenReturn(ticketValidation);
        when(ticketValidationMapper.toTicketValidationResDto(ticketValidation)).thenReturn(resDto);

        ResponseEntity<TicketValidationResDto> response = ticketValidationController.validateTicket(reqDto, eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TicketValidationStatusEnum.VALID, response.getBody().getValidationStatus());
        verify(ticketValidationService, times(1)).validateTicketByQrCode(qrCodeId, eventId);
        verify(ticketValidationService, never()).validateTicketManually(any(), any());
    }

    @Test
    void validateTicket_ShouldReturnInvalidStatus_WhenTicketAlreadyValidated() {
        TicketValidationReqDto reqDto = new TicketValidationReqDto(ticketId, TicketValidationMethodEnum.MANUAL_ENTRY);
        ticketValidation.setStatus(TicketValidationStatusEnum.INVALID);
        TicketValidationResDto resDto = new TicketValidationResDto();
        resDto.setTicketId(ticketId);
        resDto.setValidationStatus(TicketValidationStatusEnum.INVALID);

        when(ticketValidationService.validateTicketManually(ticketId, eventId)).thenReturn(ticketValidation);
        when(ticketValidationMapper.toTicketValidationResDto(ticketValidation)).thenReturn(resDto);

        ResponseEntity<TicketValidationResDto> response = ticketValidationController.validateTicket(reqDto, eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TicketValidationStatusEnum.INVALID, response.getBody().getValidationStatus());
    }
}

