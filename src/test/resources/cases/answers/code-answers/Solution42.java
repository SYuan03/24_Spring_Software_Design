public class Solution42 {
    public static void main(String[] args) {
        assert args.length == 2;
        int dividend = Integer.valueOf(args[0]);
        int divisor = Integer.valueOf(args[1]);
        if (dividend == 0 || divisor == 1) {
            System.out.println(dividend);
        } else {
            System.out.println(divide(dividend, divisor));
        }
    }

    private static int divide(int dividend, int divisor) {
        int quotient = 0;
        do {
            dividend -= divisor;
            quotient++;
        } while (dividend >= divisor)
        return quotient;
    }
}