package step.learning.services.kdf;

/**
 * Key Derivation Function
 * by <a href="https://datatracker.ietf.org/doc/html/rfc2898">RFC 2898</a>
 */
public interface KdfService {
    String dk(String password, String salt);
}
