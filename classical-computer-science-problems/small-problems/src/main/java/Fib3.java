import java.util.HashMap;
import java.util.Map;

// Memoization
public class Fib3 {

    // https://www.baeldung.com/java-initialize-hashmap#the-java-9-way
    static Map<Integer, Integer> memo = new HashMap<>(Map.of(0,0,1,1));

    public static int fib3(int n) {
        if (!memo.containsKey(n))
            memo.put(n, fib3(n - 1) + fib3(n - 2));
        return memo.get(n);
    }
}
