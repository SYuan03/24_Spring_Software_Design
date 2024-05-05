public class Solution43 {
    public static void main(String[] args) {
        int sum = 0;
        for(String arg: args){
            sum = Integer.valueOf(arg);
        }
        System.out.println(sum);
    }
}