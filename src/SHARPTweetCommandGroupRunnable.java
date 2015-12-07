import edu.wpi.first.wpilibj.command.CommandGroup;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Switzer on 12/6/2015.
 */

public abstract class SHARPTweetCommandGroupRunnable extends SHARPTweetCommandGroup implements Runnable {

    SHARPTweetCommandGroupRunnable()
    {
        new Thread(this).start();
    }
    public void run() {
        listener.read();

        if (listener.commandSetAvailable()) {
            this.commands = listener.getCommandSet();
        }
    }
}





