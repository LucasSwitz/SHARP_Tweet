import Exceptions.InvalidTweetSyntaxException;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Switzer on 12/6/2015.
 */
public abstract class SHARPTweetCommandGroup extends CommandGroup {

    protected HashMap<Character, ArrayList<Integer>> incomingCommands;
    protected HashMap<Character, CommandClassArg> switchDefenitions;
    protected TweetSerialListener listener;
    CommandGroup commands;

    public SHARPTweetCommandGroup() {

        listener = new TweetSerialListener();
        incomingCommands = new HashMap<Character, ArrayList<Integer>>();
        switchDefenitions = new HashMap<Character, CommandClassArg>();
        commands = new CommandGroup();
        setSwitchDefenitions();
    }

    public void addCommand(Command command)
    {
        addSequential(command);
    }

    public void loadInstructions() throws NullPointerException
    {
        if(instructionsAvailable()) {
            List commandKeys = new ArrayList(incomingCommands.keySet());

            for (int i = 0; i < commandKeys.size(); i++) {
                Character currentChar = (Character)commandKeys.get(i);
                try {
                    processCommand(switchDefenitions.get(currentChar));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processCommand(CommandClassArg c) throws InvalidTweetSyntaxException {
        Constructor[] constructors = c.classy.getDeclaredConstructors();
        boolean foundMatch = false;

        for(int i =0; i < constructors.length;i++)
        {
            if(constructors[i].getParameterCount() == c.arguments.size())
            {
                Class[] pType  = constructors[i].getParameterTypes();
                int matched = 0;

                for(int k =0; k < constructors[i].getParameterCount(); k++)
                {
                    System.out.println(c.arguments.get(k).getClass());
                    if(pType[k] == c.arguments.get(k).TYPE)
                    {
                       matched++;
                    }
                    System.out.println(matched);
                }
                if(matched == constructors[i].getParameterCount() && matched == c.arguments.size())
                {
                    foundMatch = true;
                    try {
                        processCommand(constructors[i], c);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(!foundMatch)
        {
            throw new InvalidTweetSyntaxException("No mathcing constructor found for class : "+ c.classy.toString()+
                    " with " + c.arguments.size()+ " arguments");
        }
    }

    private void processCommand(Constructor<?> constructor, CommandClassArg c) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        int numberOfArgs = constructor.getParameterCount();
        Command command = null;

        switch(numberOfArgs)
        {
            case 0 :
                command = (Command)constructor.newInstance();
                break;
            case 1:
                command = (Command)constructor.newInstance(c.arguments.get(0));
                break;
            case 2:
                command = (Command)constructor.newInstance(c.arguments.get(0),c.arguments.get(1));
                break;
            case 3:
                command = (Command)constructor.newInstance(c.arguments.get(0),c.arguments.get(1),c.arguments.get(2));
                break;
            case 4:
                command = (Command)constructor.newInstance(c.arguments.get(0),c.arguments.get(1),c.arguments.get(2),c.arguments.get(3));
                break;
            default:
        }
        addCommand(command);
    }

    protected boolean instructionsAvailable()
    {
        return incomingCommands != null;
    }

    public TweetSerialListener getListener()
    {
        return listener;
    }

    public void readAndUpdate()
    {
        //listener reads from serial and then update the currentCommandSet
        listener.read();

        if(listener.commandSetAvailable()) {
            incomingCommands = listener.getCommandSet();
            loadInstructions();
        }
    }

    public void update()
    {
        //only currentCommandSet gets upated  NOTICE: May have to read from serial prior to this step. Try readAndUpdate().
        if(listener.commandSetAvailable())
            incomingCommands = listener.getCommandSet();
    }

    protected void addSwitchDefenition(char c, Class classy, int arguments)
    {
        switchDefenitions.put(c, new CommandClassArg(classy,arguments));
    }

    protected abstract void setSwitchDefenitions();
}

class CommandClassArg
{
    public Class<?> classy;
    public ArrayList<Integer> arguments;
    CommandClassArg(Class classy, int argument)
    {
        this.classy = classy;
        this.arguments = new ArrayList<Integer>();
        arguments.add(argument);
    }

}



