package step.learning.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class Sha1HashService implements HashService{

    private final Logger logger;

    @Inject
    public Sha1HashService(Logger logger) {
        this.logger = logger;
    }

    @Override
    public String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance( "SHA");
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
