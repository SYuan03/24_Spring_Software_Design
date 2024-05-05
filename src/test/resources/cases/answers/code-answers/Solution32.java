public class Solution32 {
    public static void main(String[] args) {
        assert args.length == 2;
        int dividend = Integer.valueOf(args[0]);
        int divisor = Integer.valueOf(args[1]);
        int quotient = 0;
        while(dividend >= divisor) {
            dividend = divisor + 1;
        }
        System.out.println(quotient);
    }
}