package paulscode.sound;

/**
 * The CommandThread class is designed to move all command processing into a
 * single thread to be run in the background and avoid conflicts between
 * threads.  Commands are processed in the order that they were queued.  The
 * arguements for each command are stored in a
 * {@link paulscode.sound.CommandObject CommandObject}.  The Command Queue is
 * located in the {@link paulscode.sound.SoundSystem SoundSystem} class.
 * Calling kill() stops the thread, and this should be immediatly followed
 * by a call to interrupt() to wake up the thread so it may end.  This class
 * also checks for temporary sources that are finished playing, and removes
 * them.
 *
 * NOTE: The command thread is created automatically by the sound system, so it
 * is unlikely that the user would ever need to use this class.
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
public class CommandThread extends SimpleThread
{
/**
 * Processes status messages, warnings, and error messages.
 */
    protected SoundSystemLogger logger;
    
/**
 * Handle to the Sound System.  This is where the Command Queue is located.
 */
    private SoundSystem soundSystem;
    
/**
 * Name of this class.
 */
    protected String className = "CommandThread";
    
/**
 * Constructor:  Takes a handle to the SoundSystem object as a parameter.
 * @param s Handle to the SoundSystem.
*/
    public CommandThread( SoundSystem s )
    {
        // grab a handle to the message logger:
        logger = SoundSystemConfig.getLogger();
        
        soundSystem = s;
    }
    
/**
 * Shuts the thread down and removes references to all instantiated objects.  
 * NOTE: Method alive() will return false when cleanup() has finished.
 */
    @Override
    protected void cleanup()
    {
        kill();
        
        logger = null;
        soundSystem = null;
        
        super.cleanup();  // Important!
    }
    
/**
 * The main loop for processing commands.  The Command Thread starts out 
 * asleep, and it sleeps again after it finishes processing commands, so it 
 * must be interrupted when commands are queued for processing.
 */
    @Override
    public void run()
    {
        long previousTime = System.currentTimeMillis();
        long currentTime = previousTime;
        
        if( soundSystem == null )
        {
            errorMessage( "SoundSystem was null in method run().", 0 );
            cleanup();
            return;
        }
        
        // Start out asleep:
        snooze( 3600000 );
        
        while( !dying() )
        {
            // Perform user-specific source management:
            soundSystem.ManageSources();

            // Process all queued commands:
            soundSystem.CommandQueue( null );
            
            // Remove temporary sources every ten seconds:
            currentTime = System.currentTimeMillis();
            if( (!dying()) && ((currentTime - previousTime) > 10000) )
            {
                previousTime = currentTime;
                soundSystem.removeTemporarySources();
            }
            
            // Wait for more commands:
            if( !dying() )
                snooze( 3600000 );
        }
        
        cleanup();   // Important!
    }
    
/**
 * Prints a message.
 * @param message Message to print.
 */
    protected void message( String message, int indent )
    {
        logger.message( message, indent );
    }
    
/**
 * Prints an important message.
 * @param message Message to print.
 */
    protected void importantMessage( String message, int indent )
    {
        logger.importantMessage( message, indent );
    }
    
/**
 * Prints the specified message if error is true.
 * @param error True or False.
 * @param message Message to print if error is true.
 * @return True if error is true.
 */
    protected boolean errorCheck( boolean error, String message )
    {
        return logger.errorCheck( error, className, message, 0 );
    }
    
/**
 * Prints an error message.
 * @param message Message to print.
 */
    protected void errorMessage( String message, int indent )
    {
        logger.errorMessage( className, message, indent );
    }
}
