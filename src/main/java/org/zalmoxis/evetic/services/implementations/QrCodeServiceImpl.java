package org.zalmoxis.evetic.services.implementations;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zalmoxis.evetic.entities.QrCode;
import org.zalmoxis.evetic.entities.QrCodeStatusEnum;
import org.zalmoxis.evetic.entities.Ticket;
import org.zalmoxis.evetic.exceptions.QrCodeGenerationException;
import org.zalmoxis.evetic.repositories.QrCodeRepo;
import org.zalmoxis.evetic.services.QrCodeService;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl
        implements QrCodeService
{
    @Value("${qrcode.height}")
    private String qrCodeHeight;
    @Value("${qrcode.width}")
    private String qrCodeWidth;

    private final QRCodeWriter qrCodeWriter;
    private final QrCodeRepo qrCodeRepo;

    @Override
    public QrCode generateQrCode(Ticket ticket)
    {
        UUID uuid = UUID.randomUUID();
        try{
            String qrCodeValue = generateQrCodeImage(uuid);

            QrCode qrCode = QrCode.builder()
                    .id(uuid)
                    .code(qrCodeValue)
                    .status(QrCodeStatusEnum.ACTIVE)
                    .ticket(ticket)
                    .build();

            return qrCodeRepo.saveAndFlush(qrCode);

        }catch (WriterException | IOException e ){
            throw new QrCodeGenerationException("Failed to generate QR code", e);
        }
    }

    private String generateQrCodeImage(UUID uuid)
            throws WriterException, IOException
    {
        BitMatrix bitMatrix = qrCodeWriter.encode(
                uuid.toString(),
                BarcodeFormat.QR_CODE,
                Integer.parseInt(qrCodeWidth),
                Integer.parseInt(qrCodeHeight)
        );

        BufferedImage qrCodeImageMatrix = MatrixToImageWriter.toBufferedImage(bitMatrix);

        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
            ImageIO.write(qrCodeImageMatrix, "PNG", byteArrayOutputStream);
            byte[] qrCodeImageBytes = byteArrayOutputStream.toByteArray();

            return Base64.getEncoder().encodeToString(qrCodeImageBytes);
        }
    }
}
