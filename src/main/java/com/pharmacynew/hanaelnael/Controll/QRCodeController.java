package com.pharmacynew.hanaelnael.Controll;

import com.pharmacynew.hanaelnael.Service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generateQR")
public class QRCodeController {

    private final QRCodeService qrCodeService;

    @Autowired
    public QRCodeController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping(value = "/{content}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(@PathVariable String content) {
        byte[] qrCodeImage = qrCodeService.generateQRCode(content);
        if (qrCodeImage.length > 0) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrCodeImage);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}