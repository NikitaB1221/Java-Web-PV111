package step.learning.services.rnd;

import com.google.inject.Inject;
import java.util.Random;

public class AlphaNumericCodeGen implements CodeGen {
    private final Random random;

    @Inject
    public AlphaNumericCodeGen(Random random) {
        this.random = random;
    }

    @Override
    public String newCode(int length) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int type = random.nextInt(2);
            if (type == 0) {
                // Generate a random digit
                code.append(random.nextInt(10));
            } else {
                // Generate a random uppercase letter
                code.append((char) (random.nextInt(26) + 'A'));
            }
        }
        return code.toString();
    }
}
