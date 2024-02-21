package step.learning.services.kdf;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.HashService;

/**
 * Hash-based KDF
 */
@Singleton
public class HashKdfService implements KdfService {
    private final HashService hashService;

    @Inject
    public HashKdfService(HashService hashService) {
        this.hashService = hashService;
    }

    @Override
    public String dk(String password, String salt) {
        String t1 = hashService.hash(password + salt);
        return hashService.hash(t1);
    }
}
