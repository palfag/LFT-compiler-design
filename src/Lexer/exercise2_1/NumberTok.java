package Lexer.exercise2_1;

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
		Lexer.exercise2_1.NumberTok n = new Lexer.exercise2_1.NumberTok(Lexer.exercise2_1.Tag.NUM, 32);
		System.out.println(n);
	}
*/
}
