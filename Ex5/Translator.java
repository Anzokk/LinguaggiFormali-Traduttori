import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

    public Translator(Lexer l, BufferedReader br) {
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
        int lnext_prog = code.newLabel();
        statlist(lnext_prog);
        code.emit(OpCode.GOto, lnext_prog);
        code.emitLabel(lnext_prog);
        match(Tag.EOF);
        try {
        	code.toJasmin();
        }
        catch(java.io.IOException e) {
        	System.out.println("IO error\n");
        };
    }

    private void statlist(int L) {
        //System.out.println("statlist");
        stat(L);
        statlistp(L);
    }    

    private void statlistp(int L) {
        //System.out.println("statlistp");
        if (look.tag ==';'){
            move();
            stat(L);
            statlistp(L);
        }
    }

    private void stat(int L) {
        //System.out.println("stat");
        switch (look.tag){
            case Tag.ASSIGN:
                assignlist();
            break;
            case Tag.PRINT:
                move();
                match('(');
                exprlist('p');
                match(')');
            break;
            case Tag.READ:
                code.emit(OpCode.invokestatic, 0);
                move();
                match('(');
                idlist(-1);
                match(')');
            break;
            case Tag.FOR:
                match(Tag.FOR);
                int lback = code.newLabel();
                int lTrue = code.newLabel();
                match('(');
                forID(lback);
                bexpr(lTrue);
                L = code.newLabel();
                code.emit(OpCode.GOto, L);
                code.emitLabel(lTrue);
                match(')');
                match (Tag.DO);
                stat(L);
                code.emit(OpCode.GOto, lback);
                code.emitLabel(L);
            break;
            case Tag.IF:
                int ltrue = code.newLabel();
                L = code.newLabel();
                int Lprec= L;
                match (Tag.IF);
                match('(');
                bexpr(ltrue);
                code.emit(OpCode.GOto, L);
                match(')');
                code.emitLabel(ltrue);
                stat(L);
                L = code.newLabel();
                code.emit(OpCode.GOto, L);
                code.emitLabel(Lprec);
                ifElse(L);
                code.emit(OpCode.GOto, L);
                code.emitLabel(L);
                match(Tag.END);
            break;
            case '{':
                match('{');
                statlist(L);
                match('}');
            break;
        }
    }

    private void assignlist() {
        //System.out.println("assignlist");
        move();
        match('[');
        expr();
        match (Tag.TO);
        idlist(0);
        match(']');
        assignlistp();
        
    }

    private void ifElse(int Lab){
        if (look.tag==Tag.ELSE){
            match(Tag.ELSE);
            stat(Lab);
            System.out.println("09101");
        }
    }

    private void forID(int back){
        if (look.tag==Tag.ID){
            int id_addr = st.lookupAddress(((Word)look).lexeme);
            if (id_addr==-1) {
                id_addr = count;
                st.insert(((Word)look).lexeme,id_addr);
                count++;
            }
            move();
            match (Tag.INIT);
            expr();
            code.emit(OpCode.istore, id_addr);
            code.emitLabel(back);
            match (';');
        }
        else{
            code.emitLabel(back);
        }
    }

    private void assignlistp() {
        //System.out.println("assignlistp");
        if(look.tag=='['){
            move();
            expr();
            match (Tag.TO);
            idlist(0);
            match(']');
            assignlistp();
        }
    }

    private void idlist(int comm) {
        if(look.tag==Tag.ID){
        	int id_addr = st.lookupAddress(((Word)look).lexeme);
            if (id_addr==-1) {
                id_addr = count;
                st.insert(((Word)look).lexeme,id_addr);
                count++;
            }
            code.emit(OpCode.istore, id_addr);
            match(Tag.ID);
            if (comm==0){
                idlistp(id_addr);
            }else{
                idlistp(-1);
            }
            
    	}else{
            error("Missing variable");
        }
    }

    private void idlistp(int id_addr_e) { //FIXARE
        //System.out.println("idlistp");
        if(look.tag==','){
            if (id_addr_e>=0){
                code.emit(OpCode.iload, id_addr_e);
            }else{
                code.emit(OpCode.invokestatic, 0);
            }
            move();
            int id_addr = st.lookupAddress(((Word)look).lexeme);
            if (id_addr==-1) {
                id_addr = count;
                st.insert(((Word)look).lexeme,id_addr);
                count++;
            }
            code.emit(OpCode.istore, id_addr);
            match(Tag.ID);
            idlistp(id_addr_e);
        }
    }

    private void bexpr( int Lstat ) {
        //System.out.println("bexpr");
        if (look.tag==Tag.RELOP){
            String saveRelop= ((Word)look).lexeme;
            match (Tag.RELOP);
            expr();
            expr();
            switch (saveRelop) {
                case "<":
                    code.emit(OpCode.if_icmplt, Lstat);
                break;
                case "<=":
                    code.emit(OpCode.if_icmple, Lstat);
                break;
                case ">":
                    code.emit(OpCode.if_icmpgt, Lstat);
                break;
                case ">=":
                    code.emit(OpCode.if_icmpge, Lstat);
                break;
                case "==":
                    code.emit(OpCode.if_icmpeq, Lstat);
                break;
                case "<>":
                    code.emit(OpCode.if_icmpne, Lstat);
                break;
            }
        }else{
            error("Need a RELOP");
        }
    }

    private void expr() {
        //System.out.println("expr");
        int n;
        switch(look.tag) {
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            case '+':
                match('+');
                match('(');
                n = exprlist('o');
                if (n==0){
                    error ("Not enough elements in sum");
                }
                for (int c=0; c<n; c++){
                    code.emit(OpCode.iadd);
                }
                match(')');
            break;
            case '*':
                match('*');
                match('(');
                n = exprlist('o');
                if (n==0){
                    error ("Not enough elements in mul");
                }
                for (int c=0; c<n; c++){
                    code.emit(OpCode.imul);
                }
                match(')');
            break;
            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
            break;
            case Tag.ID:
                int id_word = st.lookupAddress(((Word)look).lexeme);
                if (id_word==-1) {
                    error("Missing variable");
                }
                code.emit(OpCode.iload, id_word);
                move();
            break;

            case Tag.NUM:
                code.emit(OpCode.ldc, Integer.parseInt(((NumberTok)look).lexeme));
                move();
            break;
        }
    }

    private int exprlist(char op) {
        //System.out.println("exprlist");
        expr();
        if (op=='p'){
            code.emit(OpCode.invokestatic, 1);
        }
        return exprlistp(0, op);
    }

    private int exprlistp(int cont_op, char op) {
        //System.out.println("exprlistp");
        if (look.tag==','){
            cont_op++;
            match (',');
            expr();
            if (op == 'p'){
                code.emit(OpCode.invokestatic, 1);
            }
            cont_op=exprlistp(cont_op, op);
        }
        return cont_op;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\edoar\\Desktop\\LFT\\LFT Lab\\Es 5\\Test.lft"; 
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator trans = new Translator(lex, br);
            trans.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}

 // RUN: java -jar jasmin.jar Output.j
 // Java Output
