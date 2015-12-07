/**
 * Created by Administrator on 12/7/2015.
 */
public class main {

    public static void main(String args[])
    {
        AutoCommandGroup test = new AutoCommandGroup();

        test.readAndUpdate();
        test.loadInstructions();
        System.out.println(test.getTestCommands().get(0).getArguments().get(0));
    }
}
