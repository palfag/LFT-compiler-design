package Lexer;

import java.io.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }

        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;

            // ... gestire i casi di ( ) { } + - * / ; , ... //

            case '(':
                peek = ' ';
                return Token.lpt;


            case ')':
                peek = ' ';
                return Token.rpt;


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
                readch(br);

                if (peek == '/') {
                    // ignora fino a quando non trova un "\n" o EOF
                    while(peek != '\n' && peek != (char) Tag.EOF){
                        readch(br);
                    }
                    return lexical_scan(br);
                }

                else if(peek == '*'){
                    readch(br);

                    boolean isAsteriskSecondLast = false;

                    // il penultimo è * e l'ultimo è /
                    while(!(peek == '/' && isAsteriskSecondLast)){

                        if(peek == '*')
                            isAsteriskSecondLast = true;

                        else if (peek == (char) Tag.EOF){
                            System.err.println("Error : Comment has not been closed");
                            return null;
                        }

                        else isAsteriskSecondLast = false;

                        readch(br);
                    }

                    peek = ' ';
                    return lexical_scan(br);
                }
                else
                    return Token.div;


            case ';':
                peek = ' ';
                return Token.semicolon;


            case ',':
                peek = ' ';
                return Token.comma;


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

                // ... gestire i casi di || < > <= >= == <> ... //


            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : "  + peek );
                    return null;
                }



            case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                }
                else if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                }
                else return Word.lt;


            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else return Word.gt;

            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    System.err.println("Erroneous character"
                            + " after = : "  + peek );
                    return null;
                }


            case (char)-1:
                return new Token(Tag.EOF);

            default:
                if (Character.isLetter(peek) || peek == '_') {

                    // ... gestire il caso degli identificatori e delle parole chiave //
                    String s = "";
                    boolean check = false;

                    while(Character.isLetterOrDigit(peek) || peek == '_'){

                        if(Character.isLetterOrDigit(peek))
                            check = true;

                        s +=  peek;
                        readch(br);
                    }

                    if(check){
                        if(s.equals("assign")) return Word.assign;
                        else if(s.equals("to")) return Word.to;
                        else if(s.equals("if")) return Word.iftok;
                        else if(s.equals("else")) return Word.elsetok;
                        else if(s.equals("while")) return Word.whiletok;
                        else if(s.equals("begin")) return Word.begin;
                        else if(s.equals("end")) return Word.end;
                        else if(s.equals("print")) return Word.print;
                        else if(s.equals("read")) return Word.read;
                        else return new Word(Tag.ID,s);
                    }
                    else {
                        System.err.println("Error: You cannot create an ID with only underscores.");
                        return null;
                    }
            

                } else if (Character.isDigit(peek)) {

                    // ... gestire il caso dei numeri ... //
                    String s = "";

                    while(Character.isDigit(peek)){
                        s += peek;
                        readch(br);
                    }

                    if(s.length() > 1 && s.charAt(0) == '0'){
                        System.err.println("The following number does not follow the pattern: " + s);
                        return null;
                    }

                    int number = Integer.parseInt(s);
                    return new NumberTok(Tag.NUM, number);

                } else {
                    System.err.println("Erroneous character: "
                            + peek );
                    return null;
                }
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "src/input"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }

}