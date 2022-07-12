package Ex1;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;

public class Ex1_2{
	public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
		if (ch == '_')
		    state = 2;
		else if (isLetter(ch))
		    state = 1;
		else
		    state = -1;
		break;

	    case 1:
		if (ch == '_' || isLetter(ch) || isDigit(ch))
		    state = 1;
		else
		    state = -1;
        break;
        
        case 2:
        if(ch == '_')
            state = 2;
        else if(isLetter(ch) || isDigit(ch))
            state = 1;
        else 
            state = -1;
        break;
        }   
	}
	return state == 1;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}