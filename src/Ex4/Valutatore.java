package Ex4;

import java.io.*;
import Ex2.*;

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) {
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
        int expr_val;
        switch (look.tag){
            case '(':
            case Tag.NUM:
                expr_val = expr();
                match(Tag.EOF);
                System.out.println(expr_val);
                break;
            default:
                error ("Syntax error in start. {(, NUM} are expected");
        }
    }

    private int expr() {
        int term_val, exprp_val = 0;
        switch (look.tag){
            case '(':
            case Tag.NUM:
                term_val = term();
                exprp_val = exprp(term_val);
                break;
            default:
                error ("Syntax error in expr. {(, NUM} are expected");
        }
        return exprp_val;
    }

    private int exprp(int exprp_i) {
        int term_val, exprp_val = 0;
        switch(look.tag){
            case '+':
                match('+');
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
                break;
            case'-':
                match('-');
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);
                break;
            case')':
            case Tag.EOF:
                exprp_val = exprp_i;
                break;
            default:
                error("Syntax error in exprp. {+, -, ), EOF} are expected");
        }
        return exprp_val;
    }

    private int term() {
        int fact_val, termp_val = 0;
        switch (look.tag) {
            case '(':
            case Tag.NUM:
                fact_val = fact();
                termp_val = termp(fact_val);
                break;
            default:
                error("Syntax error in term. {(, NUM} are expected");
        }
        return termp_val;
    }

    private int termp(int termp_i) {
        int fact_val, termp_val = 0;
        switch (look.tag) {
            case '*':
                match('*');
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
                break;
            case '/':
                match('/');
                fact_val = fact();
                termp_val = termp(termp_i / fact_val);
                break;
            case ')':
            case Tag.EOF:
            case '+':
            case '-':
                termp_val = termp_i;
                break;
            default:
                error("Syntax error in termp. {*, /, ), EOF} are expected");
        }
        return termp_val;
    }

    private int fact() {
        int fact_val = 0;
        switch (look.tag){
            case '(':
                match('(');
                fact_val = expr();
                match(')');
                break;
            case Tag.NUM:
                /*NumberTok num = (NumberTok) look;
                fact_val = num.lexeme;*/
                fact_val = ((NumberTok)look).lexeme;
                match(Tag.NUM);
                break;
            default:
                error("Syntax error in fact. {(, NUM} are expected");
        }
        return fact_val;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "src/input"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
