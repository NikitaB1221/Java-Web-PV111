package step.learning.services.rnd;
import com.google.inject.Inject;
import java.util.Random;
public class DigitCodeGen implements CodeGen{
    private final Random random;
    @Inject
    public DigitCodeGen(Random random) {
        this.random = random;
    }

    @Override
    public String newCode(int length) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}