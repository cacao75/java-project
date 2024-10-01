public class Fib4 {

    // Remember, any problem that can be solved recursively can also be solved iteratively!!
    private static int fib4(int n) {
        int last=0;  // fib(0)
        int next=1;  // fib(1)

        for (int i=0; i < n; i++) {
            int oldLast = last;
            last = next;
            next = oldLast + next;
        }
        return last;
    }

    public static void main(String[] args) {
        System.out.println(fib4(5));
        System.out.println(fib4(40));
    }
}
