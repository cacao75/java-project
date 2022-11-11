import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class FibTest {

    @ParameterizedTest
    @ValueSource(ints = {10, 20, 30, 40})
    void fib2Test(int number) {
        System.out.println(Fib2.fib2(number));
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 20, 30, 40})
    void fib3Test(int number) {
        System.out.println(Fib3.fib3(number));
    }
}
