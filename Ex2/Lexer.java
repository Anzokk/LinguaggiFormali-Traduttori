import java.io.*; 

public class Lexer {

    public static int line = 1;
    private char peek = ' ';
    private String identificatore = "";

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan (BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') {
                line++;
            }
            readch(br);
        }
        
        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;
            case '(':
                peek = ' ';
                return Token.lpt;
            case ')':
                peek = ' ';
                return Token.rpt;
            case '[':
                peek = ' ';
                return Token.lpq;
            case ']':
                peek = ' ';
                return Token.rpq;
            case '{':
                peek = ' ';
                return Token.lpg;
            case '}':
                peek = ' ';
                return Token.rpg;
            case '+':
                peek = ' ';
                return Token.plus;
            case '-':
                peek = ' ';
                return Token.minus;
            case '*':
                peek = ' ';
                return Token.mult;
            case '/':
                peek = ' ';
                return Token.div;
            case ';':
                peek = ' ';
                return Token.semicolon;
            case ',':
                peek = ' ';
                return Token.comma;

	// ... gestire i casi di ( ) [ ] { } + - * / ; , ... //
	
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }
            case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                }else if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                }else {
                    return Word.lt;
                }
            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                }else {
                    return Word.gt;
                }
            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                }else {
                    return Word.init;
                }
            case '_':
                identificatore = identificatore + peek;
                peek = ' ';
                return new Token('_');
	// ... gestire i casi di || < > <= >= == <> ... //
          
            case (char)-1:
                return new Token(Tag.EOF);

            default:
                if (Character.isLetter(peek)) {
                    String s="";
                    while (Character.isLetter(peek) || Character.isDigit(peek)){
                        s = s + peek;
                        readch(br);
                    }
                    identificatore = identificatore + s;
                    switch (s){
                        case "assign":
                            return Word.assign;
                        case "to":
                            return Word.to;
                        case "if":
                            return Word.iftok;
                        case "else":
                            return Word.elsetok;
                        case "do":
                            return Word.dotok;
                        case "for":
                            return Word.fortok;
                        case "begin":
                            return Word.begin;
                        case "end":
                            return Word.end;
                        case "print":
                            return Word.print;
                        case "read":
                            return Word.read;
                        default: 
                            return new Word (Tag.ID, s);
                    }
	// ... gestire il caso degli identificatori e delle parole chiave //

                } else if (Character.isDigit(peek) && identificatore != "") {
                    String s="";
                    while (Character.isDigit(peek)){
                        s = s + peek;
                        readch(br);
                    }
                    identificatore = identificatore + s;
                    return new NumberTok (s);
	// ... gestire il caso dei numeri ... //
                } else if (identificatore == ""){
                    System.err.println("Erroneous number in string start: " + peek );
                    return null;
                } else {
                    System.err.println("Erroneous character: " + peek );
                    return null;
                }
         }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/Users/anzo/Documents/Uni/LFT Final/Es 2/Prova.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            if (lex.identificatore.equals("_")) {
                System.out.println("Cannot end with only 1 '_' ");
            }
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }

}
