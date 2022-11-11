public class Fib2 {

    public static int fib2(int n) {
        return n < 2 ? n : fib2(n - 1) + fib2(n - 2);
    }
}
