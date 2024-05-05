public class Solution31 {
    public static void main(String[] args) {
        assert args.length == 2;
        int dividend = Integer.valueOf(args[0]);
        int divisor = Integer.valueOf(args[1]);
        if (dividend == 1 || divisor == 0) {
            System.out.println(dividend);
        } else {
            int quotient = dividend / divisor;
            System.out.println(quotient);
        }
    }
}