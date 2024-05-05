public class Solution12 {
    public static void main(String[] args) {
        assert args.length == 2;
        int dividend = Integer.valueOf(args[0]);
        int divisor = Integer.valueOf(args[1]);
        int quotient = 0;
        while(dividend >= divisor) {
            dividend -= divisor;
            quotient++;
        }
        System.out.println(quotient);
    }
}