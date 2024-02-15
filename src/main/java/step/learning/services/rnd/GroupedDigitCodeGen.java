package step.learning.services.rnd;

import com.google.inject.Inject;
import java.util.Random;

public class GroupedDigitCodeGen implements CodeGen {
    private final Random random;

    @Inject
    public GroupedDigitCodeGen(Random random) {
        this.random = random;
    }

    @Override
    public String newCode(int length) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
            if ((i + 1) % 4 == 0 && i != length - 1) {
                code.append("-");
            }
        }
        return code.toString();
    }
}