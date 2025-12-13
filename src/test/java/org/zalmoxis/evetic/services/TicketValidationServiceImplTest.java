package org.zalmoxis.evetic.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalmoxis.evetic.entities.QrCode;
import org.zalmoxis.evetic.entities.QrCodeStatusEnum;
import org.zalmoxis.evetic.entities.Ticket;
import org.zalmoxis.evetic.entities.TicketValidation;
import org.zalmoxis.evetic.entities.TicketValidationMethodEnum;
import org.zalmoxis.evetic.entities.TicketValidationStatusEnum;
import org.zalmoxis.evetic.exceptions.QrCodeInvalidException;
import org.zalmoxis.evetic.exceptions.TicketNotFoundException;
import org.zalmoxis.evetic.repositories.QrCodeRepo;
import org.zalmoxis.evetic.repositories.TicketRepo;
import org.zalmoxis.evetic.repositories.TicketValidationRepo;
import org.zalmoxis.evetic.services.implementations.TicketValidationServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketValidationServiceImplTest {

    @Mock
    private QrCodeRepo qrCodeRepo;

    @Mock
    private TicketValidationRepo ticketValidationRepo;

    @Mock
    private TicketRepo ticketRepo;

    @InjectMocks
    private TicketValidationServiceImpl ticketValidationService;

    private UUID qrCodeId;
    private UUID eventId;
    private UUID ticketId;
    private Ticket ticket;
    private QrCode qrCode;

    @BeforeEach
    void setUp() {
        qrCodeId = UUID.randomUUID();
        eventId = UUID.randomUUID();
        ticketId = UUID.randomUUID();

        ticket = Ticket.builder()
                .id(ticketId)
                .ticketValidations(new ArrayList<>())
                .build();

        qrCode = QrCode.builder()
                .id(qrCodeId)
                .status(QrCodeStatusEnum.ACTIVE)
                .ticket(ticket)
                .build();
    }

    @Test
    void validateTicketByQrCode_ShouldReturnValidStatus_WhenFirstValidation() {
        when(qrCodeRepo.findByIdAndStatusAndEventId(qrCodeId, QrCodeStatusEnum.ACTIVE, eventId))
                .thenReturn(Optional.of(qrCode));
        when(ticketValidationRepo.save(any(TicketValidation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TicketValidation result = ticketValidationService.validateTicketByQrCode(qrCodeId, eventId);

        assertNotNull(result);
        assertEquals(TicketValidationStatusEnum.VALID, result.getStatus());
        assertEquals(TicketValidationMethodEnum.QR_CODE, result.getMethod());
        assertNotNull(result.getValidationDate());
        verify(ticketValidationRepo, times(1)).save(any(TicketValidation.class));
    }

    @Test
    void validateTicketByQrCode_ShouldReturnInvalidStatus_WhenAlreadyValidated() {
        TicketValidation existingValidation = TicketValidation.builder()
                .status(TicketValidationStatusEnum.VALID)
                .build();
        ticket.setTicketValidations(List.of(existingValidation));

        when(qrCodeRepo.findByIdAndStatusAndEventId(qrCodeId, QrCodeStatusEnum.ACTIVE, eventId))
                .thenReturn(Optional.of(qrCode));
        when(ticketValidationRepo.save(any(TicketValidation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TicketValidation result = ticketValidationService.validateTicketByQrCode(qrCodeId, eventId);

        assertNotNull(result);
        assertEquals(TicketValidationStatusEnum.INVALID, result.getStatus());
        assertEquals(TicketValidationMethodEnum.QR_CODE, result.getMethod());
    }

    @Test
    void validateTicketByQrCode_ShouldThrowException_WhenQrCodeInvalid() {
        when(qrCodeRepo.findByIdAndStatusAndEventId(qrCodeId, QrCodeStatusEnum.ACTIVE, eventId))
                .thenReturn(Optional.empty());

        assertThrows(QrCodeInvalidException.class,
            () -> ticketValidationService.validateTicketByQrCode(qrCodeId, eventId));
        verify(ticketValidationRepo, never()).save(any(TicketValidation.class));
    }

    @Test
    void validateTicketManually_ShouldReturnValidStatus_WhenFirstValidation() {
        when(ticketRepo.findByIdAndEventId(ticketId, eventId)).thenReturn(Optional.of(ticket));
        when(ticketValidationRepo.save(any(TicketValidation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TicketValidation result = ticketValidationService.validateTicketManually(ticketId, eventId);

        assertNotNull(result);
        assertEquals(TicketValidationStatusEnum.VALID, result.getStatus());
        assertEquals(TicketValidationMethodEnum.MANUAL_ENTRY, result.getMethod());
        assertNotNull(result.getValidationDate());
        verify(ticketValidationRepo, times(1)).save(any(TicketValidation.class));
    }

    @Test
    void validateTicketManually_ShouldReturnInvalidStatus_WhenAlreadyValidated() {
        TicketValidation existingValidation = TicketValidation.builder()
                .status(TicketValidationStatusEnum.VALID)
                .build();
        ticket.setTicketValidations(List.of(existingValidation));

        when(ticketRepo.findByIdAndEventId(ticketId, eventId)).thenReturn(Optional.of(ticket));
        when(ticketValidationRepo.save(any(TicketValidation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TicketValidation result = ticketValidationService.validateTicketManually(ticketId, eventId);

        assertNotNull(result);
        assertEquals(TicketValidationStatusEnum.INVALID, result.getStatus());
        assertEquals(TicketValidationMethodEnum.MANUAL_ENTRY, result.getMethod());
    }

    @Test
    void validateTicketManually_ShouldThrowException_WhenTicketNotFound() {
        when(ticketRepo.findByIdAndEventId(ticketId, eventId)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class,
            () -> ticketValidationService.validateTicketManually(ticketId, eventId));
        verify(ticketValidationRepo, never()).save(any(TicketValidation.class));
    }
}

