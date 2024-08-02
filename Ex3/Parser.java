//Esercizio 3.1

import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
	    throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }

    public void start() {
        //System.out.println("start");
        expr();
        match(Tag.EOF);
    }

    private void expr() {
        //System.out.println("expr");
        term();
        exprp();
    }

    private void exprp() {
        //System.out.println("exprp");
        if (look.tag == '+' || look.tag == '-'){
            move();
            term();
            exprp();
        }
    }

    private void term() {
        //System.out.println("term");
        fact();
        termp();
    }

    private void termp() {
        //System.out.println("termp");
        if (look.tag == '*' || look.tag == '/'){
            move();
            fact();
            termp();
        }
    }

    private void fact() {
        //System.out.println("fact");
        switch (look.tag){
            case '(':
                match ('(');
                expr();
                match (')');
            break;
            case Tag.NUM:
                NumberTok x=(NumberTok) look;
                System.out.println(x.lexeme);
                move();
            break;
        }
    }
		
    /*
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\edoar\\Desktop\\LFT Lab\\Es 3\\Test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }*/
}