import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.SerialPort;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Switzer on 12/6/2015.
 */
public class TweetSerialListener{

    private HashMap<Character, ArrayList<Integer>> commands;
    private SerialPort serial;

    public TweetSerialListener()
    {
        serial = new SerialPort(9600, SerialPort.Port.kUSB);
    }


    public void read() {
        String readData = serial.readString();

        if(readData != null && !readData.equals(""))
        {
            readData.replaceAll("\\s", "");

            try {

                stringToCommandSet(readData);
            }
            catch (InvalidTweetSyntaxException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void stringToCommandSet(String readData) throws InvalidTweetSyntaxException
    {
        char currentFlag = '/';
        String currentValue = "";
        ArrayList<Integer> currentValues = new ArrayList<Integer>();
        for(int i = 0; i < readData.length(); i++)
        {
           Character currentChar =  readData.charAt(i);
            switch (currentChar)
            {
                case '(':
                    currentFlag = '/';
                    currentValue = "";
                    currentValues.clear();
                    break;
                case ')':
                    addValueToCommandSet(currentFlag,currentValues);
                    break;
                case ',':
                    currentValues.add(Integer.parseInt(currentValue));
                    break;
                default:
                    if(Character.isAlphabetic(currentChar) && Character.isUpperCase(currentChar))
                    {
                        currentFlag = currentChar;
                    }
                    else if (Character.isDigit(currentChar))
                    {
                        currentValue+=currentChar;
                    }
                    else
                    {
                        throw new InvalidTweetSyntaxException(currentChar);
                    }
            }

        }
    }

    private void addValueToCommandSet(char key, ArrayList<Integer> values)
    {
        commands.put(key,values);
    }
    boolean commandSetAvailable()
    {

        return commands != null;
    }

    HashMap<Character,ArrayList<Integer>> getCommandSet() throws NullPointerException
    {
        return commands;
    }
}
