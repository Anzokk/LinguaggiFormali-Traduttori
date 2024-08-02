public class Commenti2 {
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0:
                    if (ch=='*' || ch=='a')
                        state = 1;
                    else if (ch=='/')
                        state = 2;
                    else
                        state = -1;
                    break;
                case 1:
                    if (ch=='*' || ch=='a')
                        state = 1;
                    else if (ch=='/')
                        state = 2;
                    else
                        state = -1;
                    break;
                case 2:
                    if (ch=='a')
                        state =1;
                    else if (ch=='/')
                        state=2;
                    else if (ch=='*')
                        state=3;
                    else
                        state = -1;
                    break;
                case 3:
                    if (ch=='*')
                        state = 4;
                    else if (ch=='/' || ch=='a')
                        state = 3;
                    else
                        state = -1;
                    break;
                case 4:
                    if (ch=='*')
                        state = 4;
                    else if (ch=='a')
                        state = 3;
                    else if (ch=='/')
                        state = 1;
                    else
                        state = -1;
                    break;
            }
        }
        return (state == 0  || state == 1 || state == 2);
    }
    public static void main(String[] args){
        System.out.println("Workanti");
        System.out.println(scan("aaa/****/aa"));
        System.out.println(scan("aa/*a*a*/"));
        System.out.println(scan("aaaa"));
        System.out.println(scan("/****/"));
        System.out.println(scan("/*aa*/"));
        System.out.println(scan("*/a"));
        System.out.println(scan("a/**/***a"));
        System.out.println(scan("a/**/***/a"));
        System.out.println(scan("a/**/aa/***/a"));
        System.out.println("NOOOO!");
        System.out.println(scan("aaa/*/aa"));
        System.out.println(scan("aa/*aa"));
        System.out.println(scan("a/**//***a"));
    }
}
