package Ex1;

public class Ex1_4
{
    public static boolean scan(String s)
    {
        int state = 0;
        int i = 0;

        while(state!=-1 && i<s.length())
        {
            final char ch = s.charAt(i++);
            switch(state)
            {
                case 0:
                    if(ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
                        state = 1;
                    else if(ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
                        state = 2;
                    else if(ch==' ')
                        state = 0;
                    else
                        state = -1;
                break;

                case 1:
                    if(ch >= 'A' && ch <= 'K')
                        state = 3;
                    else if(ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
                        state = 1;
                    else if(ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
                        state = 2;
                    else if(ch==' ')
                        state = 5;
                    else
                        state = -1;
                break;

                case 2:
                    if(ch >= 'L' && ch <= 'Z')
                        state = 3;
                    else if(ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
                        state = 1;
                    else if(ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
                        state = 2;
                    else if(ch==' ')
                        state = 6;
                    else
                        state = -1;
                break;

                case 3:
                    if((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))
                        state = 3;
                    else if(ch==' ')
                        state = 4;
                    else
                        state = -1;
                break;

                case 4:
                    if(ch >= 'A' && ch <= 'Z')
                        state = 3;
                    else if(ch==' ')
                        state = 4;
                    else
                        state = -1; 
                break;

                case 5:
                    if(ch >= 'A' && ch <= 'K')
                        state = 3;
                    else if(ch==' ')
                        state = 5;
                    else
                        state = -1;
                break;

                case 6:
                    if(ch >= 'L' && ch <= 'Z')
                        state = 3;
                    else if(ch==' ')
                        state = 6;
                    else
                        state = -1;
                break;

            }
        }
        return state == 3 || state == 4;
    }

    public static void main(String[] args)
    {
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}