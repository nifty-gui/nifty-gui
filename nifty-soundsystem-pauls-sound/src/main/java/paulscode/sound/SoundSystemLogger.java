package paulscode.sound;

/**
 * The SoundSystemLogger class handles all status messages, warnings, and error 
 * messages for the SoundSystem library.  This class can be extended and 
 * methods overriden to change how messages are handled.  To do this, the 
 * overridden class should be instantiated, and a call should be made to method 
 * SoundSystemConfig.setLogger() BEFORE creating the SoundSystem object.  If 
 * the setLogger() method is called after the SoundSystem has been created, 
 * there will be handles floating around to two different message loggers, and 
 * the results will be undesirable.  
 * See {@link paulscode.sound.SoundSystemConfig SoundSystemConfig} for more 
 * information about changing default settings.  If an alternate logger is not 
 * set by the user, then an instance of this base class will be automatically
 * created by default when the SoundSystem class is instantiated.
 *<br><br>
 *<b><i>    SoundSystem License:</b></i><br><b><br>
 *    You are free to use this library for any purpose, commercial or otherwise.
 *    You may modify this library or source code, and distribute it any way you
 *    like, provided the following conditions are met:
 *<br>
 *    1) You may not falsely claim to be the author of this library or any
 *    unmodified portion of it.
 *<br>
 *    2) You may not copyright this library or a modified version of it and then
 *    sue me for copyright infringement.
 *<br>
 *    3) If you modify the source code, you must clearly document the changes
 *    made before redistributing the modified source code, so other users know
 *    it is not the original code.
 *<br>
 *    4) You are not required to give me credit for this library in any derived
 *    work, but if you do, you must also mention my website:
 *    http://www.paulscode.com
 *<br>
 *    5) I the author will not be responsible for any damages (physical,
 *    financial, or otherwise) caused by the use if this library or any part
 *    of it.
 *<br>
 *    6) I the author do not guarantee, warrant, or make any representations,
 *    either expressed or implied, regarding the use of this library or any
 *    part of it.
 * <br><br>
 *    Author: Paul Lamb
 * <br>
 *    http://www.paulscode.com
 * </b>
 */
public class SoundSystemLogger
{
/**
 * Prints a message.
 * @param message Message to print.
 * @param indent Number of tabs to indent the message.
 */
    public void message( String message, int indent )
    {
        String messageText;
        // Determine how many spaces to indent:
        String spacer = "";
        for( int x = 0; x < indent; x++ )
        {
            spacer += "    ";
        }
        // indent the message:
        messageText = spacer + message;
        
        // Print the message:
        System.out.println( messageText );
    }
    
/**
 * Prints an important message.
 * @param message Message to print.
 * @param indent Number of tabs to indent the message.
 */
    public void importantMessage( String message, int indent )
    {
        String messageText;
        // Determine how many spaces to indent:
        String spacer = "";
        for( int x = 0; x < indent; x++ )
        {
            spacer += "    ";
        }
        // indent the message:
        messageText = spacer + message;
        
        // Print the message:
        System.out.println( messageText );
    }
    
/**
 * Prints the specified message if error is true.
 * @param error True or False.
 * @param classname Name of the class checking for an error.
 * @param message Message to print if error is true.
 * @param indent Number of tabs to indent the message.
 * @return True if error is true.
 */
    public boolean errorCheck( boolean error, String classname, String message, 
                             int indent )
    {
        if( error )
            errorMessage( classname, message, indent );
        return error;
    }
    
/**
 * Prints the classname which generated the error, followed by the error 
 * message.
 * @param classname Name of the class which generated the error.
 * @param message The actual error message.
 * @param indent Number of tabs to indent the message.
*/
    public void errorMessage( String classname, String message, int indent )
    {
        String headerLine, messageText;
        // Determine how many spaces to indent:
        String spacer = "";
        for( int x = 0; x < indent; x++ )
        {
            spacer += "    ";
        }
        // indent the header:
        headerLine = spacer + "Error in class '" + classname + "'";
        // indent the message one more than the header:
        messageText = "    " + spacer + message;
        
        // Print the error message:
        System.out.println( headerLine );
        System.out.println( messageText );
    }
    
/**
 * Prints an exception's error message followed by the stack trace.
 * @param e Exception containing the information to print.
 * @param indent Number of tabs to indent the message and stack trace.
 */
    public void printStackTrace( Exception e, int indent )
    {
        printExceptionMessage( e, indent );
        importantMessage( "STACK TRACE:", indent );
        if( e == null )
            return;
        
        StackTraceElement[] stack = e.getStackTrace();
        if( stack == null )
            return;
        
        StackTraceElement line;
        for( int x = 0; x < stack.length; x++ )
        {
            line = stack[x];
            if( line != null )
                message( line.toString(), indent + 1 );
        }
    }
    
/**
 * Prints an exception's error message.
 * @param e Exception containing the message to print.
 * @param indent Number of tabs to indent the message.
 */
    public void printExceptionMessage( Exception e, int indent )
    {
        importantMessage( "ERROR MESSAGE:", indent );
        if( e.getMessage() == null )
            message( "(none)", indent + 1 );
        else
            message( e.getMessage(), indent + 1 );
    }
}
