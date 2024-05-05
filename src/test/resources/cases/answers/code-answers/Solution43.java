public class Solution43 {
    public static void main(String[] args) {
        assert args.length == 2;
        int dividend = Integer.valueOf(args[0]);
        int divisor = Integer.valueOf(args[1]);
        int quotient = (divisor == 1) ? dividend : (dividend % divisor);
        System.out.println(quotient);
    }
}