package org.zalmoxis.evetic.services;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
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
import org.zalmoxis.evetic.entities.TicketStatusEnum;
import org.zalmoxis.evetic.exceptions.QrCodeGenerationException;
import org.zalmoxis.evetic.exceptions.QrCodeNotFoundException;
import org.zalmoxis.evetic.repositories.QrCodeRepo;
import org.zalmoxis.evetic.services.implementations.QrCodeServiceImpl;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
                .status(TicketStatusEnum.PURCHASED)
                .build();

        String validBase64 = Base64.getEncoder().encodeToString(new byte[]{0, 1, 2, 3});

        qrCode = QrCode.builder()
                .id(UUID.randomUUID())
                .status(QrCodeStatusEnum.ACTIVE)
                .code(validBase64)
                .ticket(ticket)
                .build();
    }

    @Test
    void generateQrCode_ShouldCreateAndSaveQrCode() throws WriterException {
        BitMatrix bitMatrix = new BitMatrix(200, 200);
        when(qrCodeWriter.encode(anyString(), any(), eq(200), eq(200))).thenReturn(bitMatrix);
        when(qrCodeRepo.saveAndFlush(any(QrCode.class))).thenAnswer(invocation -> invocation.getArgument(0));

        QrCode result = qrCodeService.generateQrCode(ticket);

        assertNotNull(result);
        assertEquals(QrCodeStatusEnum.ACTIVE, result.getStatus());
        assertEquals(ticket, result.getTicket());
        assertNotNull(result.getCode());
        verify(qrCodeRepo, times(1)).saveAndFlush(any(QrCode.class));
    }

    @Test
    void generateQrCode_ShouldThrowException_WhenWriterExceptionOccurs() throws WriterException {
        when(qrCodeWriter.encode(anyString(), any(), eq(200), eq(200)))
                .thenThrow(new WriterException("Writer error"));

        assertThrows(QrCodeGenerationException.class, () -> qrCodeService.generateQrCode(ticket));
        verify(qrCodeRepo, never()).saveAndFlush(any(QrCode.class));
    }

    @Test
    void getQrCodeImageForOwner_ShouldReturnImageBytes_WhenExists() {
        when(qrCodeRepo.findByTicketIdAndTicketOwnerId(ticketId, ownerId))
                .thenReturn(Optional.of(qrCode));

        byte[] result = qrCodeService.getQrCodeImageForOwner(ticketId, ownerId);

        assertNotNull(result);
        assertArrayEquals(new byte[]{0, 1, 2, 3}, result);
        verify(qrCodeRepo, times(1)).findByTicketIdAndTicketOwnerId(ticketId, ownerId);
    }

    @Test
    void getQrCodeImageForOwner_ShouldThrowException_WhenNotFound() {
        when(qrCodeRepo.findByTicketIdAndTicketOwnerId(ticketId, ownerId))
                .thenReturn(Optional.empty());

        QrCodeNotFoundException exception = assertThrows(QrCodeNotFoundException.class,
            () -> qrCodeService.getQrCodeImageForOwner(ticketId, ownerId));

        assertTrue(exception.getMessage().contains(ticketId.toString()));
    }

    @Test
    void getQrCodeImageForOwner_ShouldThrowException_WhenInvalidBase64() {
        qrCode.setCode("invalid-base64!!!");
        when(qrCodeRepo.findByTicketIdAndTicketOwnerId(ticketId, ownerId))
                .thenReturn(Optional.of(qrCode));

        QrCodeGenerationException exception = assertThrows(QrCodeGenerationException.class,
            () -> qrCodeService.getQrCodeImageForOwner(ticketId, ownerId));

        assertTrue(exception.getMessage().contains(ticketId.toString()));
    }

    @Test
    void generateQrCode_ShouldSetCorrectQrCodeProperties() throws WriterException {
        BitMatrix bitMatrix = new BitMatrix(200, 200);
        when(qrCodeWriter.encode(anyString(), any(), eq(200), eq(200))).thenReturn(bitMatrix);
        when(qrCodeRepo.saveAndFlush(any(QrCode.class))).thenAnswer(invocation -> invocation.getArgument(0));

        QrCode result = qrCodeService.generateQrCode(ticket);

        assertNotNull(result.getId());
        assertNotNull(result.getCode());
        assertEquals(QrCodeStatusEnum.ACTIVE, result.getStatus());
        assertEquals(ticket, result.getTicket());
    }

    @Test
    void getQrCodeImageForOwner_ShouldDecodeBase64Correctly() {
        byte[] originalBytes = "test image data".getBytes();
        String base64Encoded = Base64.getEncoder().encodeToString(originalBytes);
        qrCode.setCode(base64Encoded);

        when(qrCodeRepo.findByTicketIdAndTicketOwnerId(ticketId, ownerId))
                .thenReturn(Optional.of(qrCode));

        byte[] result = qrCodeService.getQrCodeImageForOwner(ticketId, ownerId);

        assertArrayEquals(originalBytes, result);
    }
}
