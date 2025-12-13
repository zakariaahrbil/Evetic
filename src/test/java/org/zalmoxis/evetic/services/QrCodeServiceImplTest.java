package org.zalmoxis.evetic.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.zalmoxis.evetic.entities.QrCode;
import org.zalmoxis.evetic.entities.QrCodeStatusEnum;
import org.zalmoxis.evetic.entities.Ticket;
import org.zalmoxis.evetic.exceptions.QrCodeGenerationException;
import org.zalmoxis.evetic.exceptions.QrCodeNotFoundException;
import org.zalmoxis.evetic.repositories.QrCodeRepo;
import org.zalmoxis.evetic.services.implementations.QrCodeServiceImpl;

import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QrCodeServiceImplTest {

    @Mock
    private QRCodeWriter qrCodeWriter;

    @Mock
    private QrCodeRepo qrCodeRepo;

    @InjectMocks
    private QrCodeServiceImpl qrCodeService;

    private UUID ticketId;
    private UUID ownerId;
    private Ticket ticket;
    private QrCode qrCode;

    @BeforeEach
    void setUp() {
        ticketId = UUID.randomUUID();
        ownerId = UUID.randomUUID();

        ReflectionTestUtils.setField(qrCodeService, "qrCodeHeight", "200");
        ReflectionTestUtils.setField(qrCodeService, "qrCodeWidth", "200");

        ticket = Ticket.builder()
                .id(ticketId)
                .build();

        // Valid base64 encoded PNG data (a simple small image)
        String validBase64 = Base64.getEncoder().encodeToString(new byte[]{0, 1, 2, 3});

        qrCode = QrCode.builder()
                .id(UUID.randomUUID())
                .status(QrCodeStatusEnum.ACTIVE)
                .code(validBase64)
                .ticket(ticket)
                .build();
    }

    @Test
    void getQrCodeImageForOwner_ShouldReturnImageBytes_WhenExists() {
        when(qrCodeRepo.findByTicketIdAndTicketOwnerId(ticketId, ownerId))
                .thenReturn(Optional.of(qrCode));

        byte[] result = qrCodeService.getQrCodeImageForOwner(ticketId, ownerId);

        assertNotNull(result);
        verify(qrCodeRepo, times(1)).findByTicketIdAndTicketOwnerId(ticketId, ownerId);
    }

    @Test
    void getQrCodeImageForOwner_ShouldThrowException_WhenNotFound() {
        when(qrCodeRepo.findByTicketIdAndTicketOwnerId(ticketId, ownerId))
                .thenReturn(Optional.empty());

        assertThrows(QrCodeNotFoundException.class,
            () -> qrCodeService.getQrCodeImageForOwner(ticketId, ownerId));
    }

    @Test
    void getQrCodeImageForOwner_ShouldThrowException_WhenInvalidBase64() {
        qrCode.setCode("invalid-base64!!!");
        when(qrCodeRepo.findByTicketIdAndTicketOwnerId(ticketId, ownerId))
                .thenReturn(Optional.of(qrCode));

        assertThrows(QrCodeGenerationException.class,
            () -> qrCodeService.getQrCodeImageForOwner(ticketId, ownerId));
    }
}

