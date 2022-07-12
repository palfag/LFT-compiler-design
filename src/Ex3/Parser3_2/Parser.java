package Ex3.Parser3_2;

import java.io.*;
import Ex2.*;

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
GUIDA <prog> ::= <statlist> EOF = {assign, print, read, while, if, { }
 */
    private void prog() {
        switch(look.tag) {
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.IF:
            case '{':
                statlist();
                match(Tag.EOF);
                break;
            default:
                error("Syntax error in prog.");
        }
    }
/*
GUIDA <statlist> ::= <stat><statlistp> = {assign, print, read, while, if, { }
*/
    private void statlist() {
        switch(look.tag) {
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.IF:
            case '{':
                stat();
                statlistp();
                break;
            default:
                error("Syntax error in statlist.");
        }
    }
/*
GUIDA <statlistp> ::= ; <stat><statlistp> = {;}
 */
    private void statlistp() {
        switch (look.tag) {
            case ';':
                match(';');
                stat();
                statlistp();
                break;
            case Tag.EOF:
            case '}':
                break;
            default:
                error("Syntax error in statlistp.");
        }
    }

/*
GUIDA
<stat> ::= assign <expr> to <idlist> = {assign}
<stat> ::= print ( <exprlist> ) = {print}
<stat> ::= read ( <idlist> ) = {read}
<stat> ::= while ( <bexpr> ) <stat> = {while}
<stat> ::= if ( <bexpr> ) <stat> <statp> = {if}
<stat> ::= { <statlist> } { { }
 */
    private void stat() {
        switch (look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist();
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                exprlist();
                match(')');
                break;
            case Tag.READ:
                match(Tag.READ);
                match('(');
                idlist();
                match(')');
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                match('(');
                bexpr();
                match(')');
                stat();
                break;
            case Tag.IF:
                match(Tag.IF);
                match('(');
                bexpr();
                match(')');
                stat();
                statp();
                break;
            case '{':
                match('{');
                statlist();
                match('}');
                break;
            default:
                error("Syntax error in stat.");
        }
    }

/*
GUIDA
<statp> ::= end = {end}
<statp> ::= else <stat> end = {else}
 */
    private void statp() {
        switch (look.tag){
            case Tag.END:
                match(Tag.END);
                break;
            case Tag.ELSE:
                match(Tag.ELSE);
                stat();
                match(Tag.END);
                break;
            default:
                error("Syntax error in statp.");
        }
    }

/*
GUIDA
<idlist> ::= ID <idlistp> = {ID}
 */
    private void idlist() {
        if (look.tag == Tag.ID) {
            match(Tag.ID);
            idlistp();
        } else {
            error("Syntax error in idlist.");
        }
    }

/*
GUIDA
<idlistp>::= , ID <idlistp> = {,}
<idlistp> ::= eps = {), ;, end, else, EOF, } }
 */
    private void idlistp() {
        switch (look.tag) {
            case ',':
                match(',');
                match(Tag.ID);
                idlistp();
                break;
            case ')':
            case ';':
            case '}':
            case Tag.END:
            case Tag.ELSE:
            case Tag.EOF:
                break;
            default:
                error("Syntax error in idlistp.");
        }
    }

/*
GUIDA
<bexpr> ::= RELOP <expr><expr> = {RELOP}
 */
    private void bexpr() {
        if (look.tag == Tag.RELOP) {
            match(Tag.RELOP);
            expr();
            expr();
        } else {
            error("Syntax error in bexpr.");
        }
    }

/*
GUIDA
<expr> ::= + ( <exprlist> ) = {+}
<expr> ::= -  <expr><expr> = {-}
<expr> ::= * ( <exprlist> ) = {*}
<expr> ::= / <expr><expr> = {/}
<expr> ::= NUM = {NUM}
<expr> ::= ID = {ID}
 */
    private void expr() {
        switch (look.tag) {
            case '+':
                match('+');
                match('(');
                exprlist();
                match(')');
                break;
            case '*':
                match('*');
                match('(');
                exprlist();
                match(')');
                break;
            case '-':
                match('-');
                expr();
                expr();
                break;
            case '/':
                match('/');
                expr();
                expr();
                break;
            case Tag.NUM:
                match(Tag.NUM);
                break;
            case Tag.ID:
                match(Tag.ID);
                break;
            default:
                error("Syntax error in expr.");
        }
    }

/*
GUIDA
<exprlist> ::= <expr><exprlistp> = {+, -, *, /, NUM, ID}
 */
    private void exprlist() {
        switch (look.tag) {
            case '+':
            case '-':
            case '*':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                expr();
                exprlistp();
                break;
            default:
                error("Syntax error in exprlist.");
        }
    }

/*
GUIDA
<exprlistp> ::= , <expr><exprlistp> = {,}
<exprlistp> ::= eps = {)}
 */
    private void exprlistp() {
        switch (look.tag) {
            case ',':
                match(',');
                expr();
                exprlistp();
                break;
            case ')':
                break;
            default:
                error("Syntax error in exprlistp.");
        }
    }
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "src/input"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
