package step.learning.services;

import com.google.inject.Inject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Md5HashService implements HashService {
    private final Logger logger;

    @Inject
    public Md5HashService(Logger logger) {
        this.logger = logger;
    }

    @Override
    public String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance( "MD5");
            StringBuilder stringBuilder = new StringBuilder();
            for( byte b:
                    digest.digest(input.getBytes(StandardCharsets.UTF_8) ) ) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        }
        catch
        (NoSuchAlgorithmException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            return null;
        }
    }
}
