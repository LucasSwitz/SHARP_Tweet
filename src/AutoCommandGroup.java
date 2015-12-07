import edu.wpi.first.wpilibj.command.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Switzer on 12/6/2015.
 */
public class AutoCommandGroup extends SHARPTweetCommandGroup {

    @Override
    protected void setSwitchDefenitions() {
        addSwitchDefenition('F', Drive.class, 0);
    }
}

class Drive extends Command
{

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {

    }

    @Override
    protected void interrupted() {

    }
}
