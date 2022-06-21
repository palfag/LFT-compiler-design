package Lexer;

public class NumberTok extends Token {
    public int lexeme;

    public NumberTok(int tag, int num){
        super(tag);
        lexeme = num;
    }


    public String toString() {
        return "<" + tag + ", " + lexeme + ">";
    }

/*
	public static void main(String[] args) {
		Lexer.NumberTok n = new Lexer.NumberTok(Lexer.Tag.NUM, 32);
		System.out.println(n);
	}
*/
}
