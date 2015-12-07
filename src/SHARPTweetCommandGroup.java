import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Switzer on 12/6/2015.
 */
public abstract class SHARPTweetCommandGroup extends CommandGroup {

    protected HashMap<Character, ArrayList<Integer>> commands;
    protected HashMap<Character, CommandClassArg> switchDefenitions;
    protected TweetSerialListener listener;

    public SHARPTweetCommandGroup() {

        listener = new TweetSerialListener();
        switchDefenitions = new HashMap<>();
        commands = new HashMap<>();
        setSwitchDefenitions();
    }

    public void loadInstructions() throws NullPointerException
    {
        if(instructionsAvailable()) {
            List commandKeys = new ArrayList(commands.keySet());

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

    private void processCommand(CommandClassArg c) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ArrayList<Integer> arguments = commands.get(c);
        Constructor<?> cons = null;
        Command command = null;
        switch(c.arguments)
        {
            //TODO: Add more argument lengths or find a way to make this implicit
            case 0:
                cons = c.classy.getConstructor();
                command = (Command)cons.newInstance();
                break;

            case 1:
                cons = c.classy.getConstructor();
                command = (Command)cons.newInstance(arguments.get(0));
                break;

            case 2:
                cons = c.classy.getConstructor();
                command = (Command)cons.newInstance(arguments.get(0),arguments.get(1));
                break;

            default:

        }

        if(command != null)
        addSequential(command);
        else
        {

        }
    }

    protected boolean instructionsAvailable()
    {
        return commands != null;
    }

    public TweetSerialListener getListener()
    {
        return listener;
    }

    public void readAndUpdate()
    {
        //listener reads from serial and then update the currentCommandSet
        listener.read();

        if(listener.commandSetAvailable())
            commands = listener.getCommandSet();
    }

    public void update()
    {
        //only currentCommandSet gets upated  NOTICE: May have to read from serial prior to this step. Try readAndUpdate().
        if(listener.commandSetAvailable())
            commands = listener.getCommandSet();
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
    public int arguments;
    CommandClassArg(Class classy, int arguments)
    {
        this.classy = classy;
        this.arguments = arguments;
    }

}




