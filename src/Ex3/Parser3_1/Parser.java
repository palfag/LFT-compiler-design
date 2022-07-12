package Ex3.Parser3_1;

import Ex2.*;
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

/*
GUIDA <start> ::= <expr> EOF = {(, NUM}
*/
    public void start() {
        switch (look.tag){
            case '(':
            case Tag.NUM:
                expr();
                match(Tag.EOF);
                break;
            default:
                error ("Syntax error in start. {(, NUM} are expected");
        }
    }

/*
GUIDA <expr> ::= <term><exprp> = {(, NUM}
*/
    private void expr() {
        switch (look.tag){
            case '(':
            case Tag.NUM:
                term();
                exprp();
                break;
            default:
                error ("Syntax error in expr. {(, NUM} are expected");
        }
    }

/*
GUIDA:
<exprp> ::= + <term><exprp> = {+}
<exprp> ::= - <term><exprp> = {-}
<exprp> ::= eps = {), EOF}
 */
    private void exprp() {
            switch(look.tag){
                case '+':
                    match('+');
                    term();
                    exprp();
                    break;
                case'-':
                    match('-');
                    term();
                    exprp();
                    break;
                case')':
                case Tag.EOF:
                    break;
                default:
                    error("Syntax error in exprp. {+, -, ), EOF} are expected");
            }
    }

/*
GUIDA <term> ::= <fact><termp> = {(, NUM}
 */
    private void term() {
        switch (look.tag) {
            case '(':
            case Tag.NUM:
                fact();
                termp();
                break;
            default:
                error("Syntax error in term. {(, NUM} are expected");
        }
    }

/*
GUIDA
<termp> ::= * <fact><termp> = {*}
<termp> ::= * <fact><termp> = {/}
<termp> ::= eps = {), EOF, +, -}
 */
    private void termp() {
        switch (look.tag) {
            case '*':
                match('*');
                fact();
                termp();
                break;
            case '/':
                match('/');
                fact();
                termp();
                break;
            case ')':
            case Tag.EOF:
            case '+':
            case '-':
                break;
            default:
                error("Syntax error in termp. {*, /, ), EOF} are expected");
        }
    }

/*
GUIDA
<fact> ::= (<expr>) = {(}
<fact> ::= NUM = (NUM)
 */
    private void fact() {
        switch (look.tag){
            case '(':
                match('(');
                expr();
                match(')');
                break;
            case Tag.NUM:
                match(Tag.NUM);
                break;
            default:
                error("Syntax error in fact. {(, NUM} are expected");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "src/input"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}