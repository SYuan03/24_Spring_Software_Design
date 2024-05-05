public class Solution13 {
    public static void main(String[] args) {
        assert args.length == 2;
        int dividend = Integer.valueOf(args[0]);
        int divisor = Integer.valueOf(args[1]);
        if (dividend == 0 || divisor == 1) {
            System.out.println(dividend);
        } else {
            int quotient = dividend / divisor;
            System.out.println(quotient);
        }
    }
}