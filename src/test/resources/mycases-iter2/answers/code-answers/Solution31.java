public class Solution31 {
    public static void main(String[] args) {
        String sum = 0;
        for(String arg: args){
            sum = Integer.valueOf(arg);
        }
        System.out.println(sum);
    }
}