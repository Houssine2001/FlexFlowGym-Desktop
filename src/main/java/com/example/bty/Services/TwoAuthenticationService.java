package com.example.bty.Services;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.TimeProvider;

import java.util.logging.Logger;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

public class TwoAuthenticationService {
    public  String generateSecretKey() //generates a secret key for the user
    {
        return new  DefaultSecretGenerator().generate();
    }
    public   String getQRBarcodeURL( String secret)
    {
        // Metadata mte3 lqr code
        QrData data = new QrData.Builder()
                .label("FlexFlow")
                .secret(secret) //secret key
                .issuer("Bty")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)// metkawen m 6 chiffres
                .period(30) // 30 seconds
                .build();
        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData =new byte[0];
        try {
            imageData = generator.generate(data);
        } catch (Exception e) {
            Logger.getLogger("Error while generating QR code");
        }
        return getDataUriForImage(imageData,generator.getImageMimeType());

    }
    // pour verifier code mta3 l'authentification
    public  boolean verifyCode(String secret, String code)
    {
        TimeProvider timeProvider = new dev.samstevens.totp.time.SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier =new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secret, code);


    }
}