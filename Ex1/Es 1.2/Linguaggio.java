public class Linguaggio {
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0:
                if (Character.isLetter(ch))
                    state = 1;
                else if (Character.isDigit(ch))
                    state = 2;
                else if (ch=='_')
                    state = 3;
                else
                    state = -1;
                break;
            case 1:
                if (Character.isLetter(ch) || Character.isDigit(ch) || ch=='_')
                    state = 1;
                else
                    state = -1;
                break;
            case 2:
                if (Character.isLetter(ch) || Character.isDigit(ch) || ch=='_')
                    state =2;
                else
                    state = -1;
                break;
            case 3:
                if (ch == '_')
                    state = 3;
                else if (Character.isLetter(ch) || Character.isDigit(ch))
                    state = 1;
                else
                    state = -1;
                break;
            }
        }
        return state == 1;
    }
    public static void main(String[] args){

        System.out.println("rotture con space e stringa vuota");

        System.out.println(scan(""));
        System.out.println(scan("x x"));

        System.out.println("\nrisultato true");

        System.out.println(scan("x"));
        System.out.println(scan("flag1"));
        System.out.println(scan("x2y2"));
        System.out.println(scan("x_1"));
        System.out.println(scan("lft_lab"));
        System.out.println(scan("_temp"));
        System.out.println(scan("x_1_y_2"));
        System.out.println(scan("x___"));
        System.out.println(scan("__5"));

        System.out.println("\nrisultato false");

        System.out.println(scan("5"));
        System.out.println(scan("221B"));
        System.out.println(scan("123"));
        System.out.println(scan("9_to_5"));
        System.out.println(scan("___"));

    }
}

// “x”, “flag1”, “x2y2”, “x 1”, “lft lab”, “ temp”, “x 1 y 2”, “x ”, “ 5”