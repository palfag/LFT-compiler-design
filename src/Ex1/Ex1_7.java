package Ex1;

public class Ex1_7
{
    public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state !=-1 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
		if (ch == 'L')
		    state = 1;
		else
		    state = 5;
		break;

	    case 1:
		if (ch == 'a')
		    state = 2;
		else
		    state = 6;
		break;

	    case 2:
		if (ch == 'v')
		    state = 3;
		else
		    state = 7;
		break;

        case 3:
		if (ch == 'i')
		    state = 4;
		else
		    state = 8;
		break;

        case 4:
		if ((int)ch!=0)
		    state = -1;
		break;

        case 5:
		if (ch == 'a')
		    state = 6;
		else
		    state = -1;
		break;

        case 6:
		if (ch == 'v')
		    state = 7;
		else
		    state = -1;
		break;

        case 7:
		if (ch == 'i')
		    state = 8;
		else
		    state = -1;
		break;

        case 8:
		if ((int)ch!=0)
		    state = -1;
		break;
	}
    }
	return state == 4 || state == 8;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}