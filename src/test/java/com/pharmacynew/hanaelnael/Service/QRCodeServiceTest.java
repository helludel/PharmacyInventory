package com.pharmacynew.hanaelnael.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class QRCodeServiceTest {
    @InjectMocks
    private QRCodeService qrCodeService;

    @Mock
    private QRCodeWriter qrCodeWriterMock;
    @Test
    void testGenerateQRCode() throws WriterException, IOException {
        // Arrange
        QRCodeService qrCodeService = new QRCodeService();
        String content = "test content";
        BitMatrix bitMatrix = new BitMatrix(300, 300);

        try (MockedStatic<QRCodeWriter> qrCodeWriterMockedStatic = Mockito.mockStatic(QRCodeWriter.class);
             MockedStatic<MatrixToImageWriter> matrixToImageWriterMockedStatic = Mockito.mockStatic(MatrixToImageWriter.class)) {

            // Mocking QRCodeWriter static method and instance method
            qrCodeWriterMockedStatic.when(QRCodeWriter::new).thenReturn(qrCodeWriterMock);
            Mockito.when(qrCodeWriterMock.encode(anyString(), any(BarcodeFormat.class), anyInt(), anyInt()))
                    .thenReturn(bitMatrix);

            // Mocking MatrixToImageWriter static method
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            matrixToImageWriterMockedStatic.when(() -> MatrixToImageWriter.writeToStream(any(BitMatrix.class), anyString(), any(ByteArrayOutputStream.class)))
                    .thenAnswer(invocation -> {
                        outputStream.write("fake image".getBytes());
                        return null;
                    });

            // Act
            byte[] qrCode = qrCodeService.generateQRCode(content);

            // Assert
            assertNotNull(qrCode);
            assertNotEquals(0, qrCode.length);
        }
    }
}
