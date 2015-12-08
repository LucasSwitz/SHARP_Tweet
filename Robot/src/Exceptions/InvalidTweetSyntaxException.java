package Exceptions;

/**
 * Created by Switzer on 12/6/2015.
 */
public class InvalidTweetSyntaxException extends Exception {

    public InvalidTweetSyntaxException(char badCharacter)
    {
        super("Invalid Tweet Syntax at: "+badCharacter);
    }
    public InvalidTweetSyntaxException(String message)
    {
        super(message);
    }
}
