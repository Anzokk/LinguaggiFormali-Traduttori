// Esercizio 3.2

import java.io.*;

public class Parser2 {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser2(Lexer l, BufferedReader br) {
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

    public void prog() {
        //System.out.println("prog");
        statlist();
        match(Tag.EOF);
    }

    private void statlist() {
        //System.out.println("statlist");
        stat();
        statlistp();
    }    

    private void statlistp() {
        //System.out.println("statlistp");
        if (look.tag ==';'){
            move();
            stat();
            statlistp();
        }
    }
    
    private void stat() {
        //System.out.println("stat");
        switch (look.tag){
            case Tag.ASSIGN:
                assignlist();
            break;
            case Tag.PRINT:
                move();
                match('(');
                exprlist();
                match(')');
            break;
            case Tag.READ:
                move();
                match('(');
                idlist();
                match(')');
            break;
            case Tag.FOR:
                match(Tag.FOR);
                match('(');
                forID();
                bexpr();
                match(')');
                match (Tag.DO);
                stat();
            break;
            case Tag.IF:
                match(Tag.IF);
                match('(');
                bexpr();
                match(')');
                stat();
                ifElse();
                match(Tag.END);
            break;
            case '{':
                match('{');
                statlist();
                match('}');
            break;
        }
    }

    private void forID(){
        if (look.tag==Tag.ID){
            move();
            match (Tag.INIT);
            expr();
            match (';');
        }
    }

    private void ifElse(){
        if (look.tag == Tag.ELSE ){
            stat();
        }
    }


    private void assignlist() {
        //System.out.println("assignlist");
        move();
        match('[');
        expr();
        match (Tag.TO);
        idlist();
        match(']');
        assignlistp();
        
    }

    private void assignlistp() {
        //System.out.println("assignlistp");
        if(look.tag=='['){
            move();
            expr();
            match (Tag.TO);
            idlist();
            match(']');
            assignlistp();
        }
    }

    private void idlist() {
        //System.out.println("idlist");
        match(Tag.ID);
        idlistp();
    }

    private void idlistp() {
        //System.out.println("idlistp");
        if(look.tag==','){
            move();
            match(Tag.ID);
            idlistp();
        }
    }

    private void bexpr() {
        System.out.println("bexpr");
        match(Tag.RELOP);
        expr();
        expr();
    }

    private void expr() {
        System.out.println("expr");
        switch (look.tag){
            case '+':
                match('+');
                match('(');
                exprlist();
                match(')');
            break;
            case '-': 
                match('-');
                expr();
                expr();
            break;
            case '*':
                match('*');
                match('(');
                exprlist();
                match(')');
            break;
            case '/':
                match('/');
                expr();
                expr();
            break;
            case Tag.ID:
                move();
            break;
            case Tag.NUM:
                move();
            break;
        }
    }

    private void exprlist() {
        //System.out.println("exprlist");
        expr();
        exprlistp();
    }

    private void exprlistp() {
        //System.out.println("exprlistp");
        if (look.tag==','){
            move();
            expr();
            exprlistp();
        }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\edoar\\Desktop\\LFT\\LFT Lab\\Es 3\\Test2.txt"; 
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser2 parser = new Parser2(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}