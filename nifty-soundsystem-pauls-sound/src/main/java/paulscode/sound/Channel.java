package paulscode.sound;

import java.util.LinkedList;
import javax.sound.sampled.AudioFormat;

/**
 * The Channel class is the base class which can be extended for 
 * library-specific channels.  It is also used in the "no-sound" library.  
 * A channel is a reserved sound-card voice through which sources are played 
 * back.  Channels can be either streaming channels or normal (non-streaming) 
 * ones.  For consistant naming conventions, each sub-class should have the 
 * name prefix "Channel".
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
public class Channel
{
/**
 * The library class associated with this type of channel.
 */
    protected Class libraryType = Library.class;
    
/**
 * Global identifier for the type of channel (normal or streaming).  Possible 
 * values for this varriable can be found in the 
 * {@link paulscode.sound.SoundSystemConfig SoundSystemConfig} class.
 */
    public int channelType;

/**
 * Processes status messages, warnings, and error messages.
 */
    private SoundSystemLogger logger;

/**
 * Whatever source is attached to this channel.
 */
    public Source attachedSource = null;

/**
 * Cumulative counter of the buffers played then unqued.
 */
    public int buffersUnqueued = 0;
    
/**
 * Constructor:  Takes channelType identifier as a paramater.  Possible values 
 * for channel type can be found in the 
 * {@link paulscode.sound.SoundSystemConfig SoundSystemConfig} class.
 * @param type Type of channel (normal or streaming).
 */
    public Channel( int type )
    {
        // grab a handle to the message logger:
        logger = SoundSystemConfig.getLogger();
        
        channelType = type;
    }
    
/**
 * Shuts the channel down and removes references to all instantiated objects.
 */
    public void cleanup()
    {
        logger = null;
    }
    
/**
 * Queues up the initial byte[] buffers of data to be streamed.
 * @param bufferList List of the first buffers to be played for a streaming source.
 * @return False if an error occurred or if end of stream was reached.
 */
    public boolean preLoadBuffers( LinkedList<byte[]> bufferList )
    {
        return true;
    }
    
/**
 * Queues up a byte[] buffer of data to be streamed.
 * @param buffer The next buffer to be played for a streaming source.
 * @return False if an error occurred or if the channel is shutting down.
 */
    public boolean queueBuffer( byte[] buffer )
    {
        return false;
    }

/**
 * Feeds raw data to the stream.
 * @param buffer Buffer containing raw audio data to stream.
 * @return Number of prior buffers that have been processed.
 */
    public int feedRawAudioData( byte[] buffer )
    {
        return 1;
    }
    
/**
 * Returns the number of queued byte[] buffers that have finished playing.
 * @return Number of buffers processed.
 */
    public int buffersProcessed()
    {
        return 0;
    }
    
/**
 * Calculates the number of milliseconds since the channel began playing.
 * @return Milliseconds, or -1 if unable to calculate.
 */
    public float millisecondsPlayed()
    {
        return -1;
    }
/**
 * Plays the next queued byte[] buffer.  This method is run from the seperate 
 * {@link paulscode.sound.StreamThread StreamThread}.
 * @return False when no more buffers are left to process.
 */
    public boolean processBuffer()
    {
        return false;
    }

/**
 * Sets the channel up to receive the specified audio format.
 */
    public void setAudioFormat( AudioFormat audioFormat )
    {}
    
/**
 * Dequeues all previously queued data.
 */
    public void flush()
    {}
    
/**
 * Stops the channel, dequeues any queued data, and closes the channel.
 */
    public void close()
    {}
    
/**
 * Plays the currently attached normal source, opens this channel up for 
 * streaming, or resumes playback if this channel was paused.
 */
    public void play()
    {}
    
/**
 * Temporarily stops playback for this channel.
 */
    public void pause()
    {}
    
/**
 * Stops playback for this channel and rewinds the attached source to the 
 * beginning.
 */
    public void stop()
    {}
    
/**
 * Rewinds the attached source to the beginning.  Stops the source if it was 
 * paused.
 */
    public void rewind()
    {}
    
/**
 * Used to determine if a channel is actively playing a source.  This method 
 * will return false if the channel is paused or stopped and when no data is 
 * queued to be streamed.
 * @return True if this channel is playing a source.
 */
    public boolean playing()
    {
        return false;
    }
    
/**
 * Returns the name of the class.
 * @return "Channel" + library title.
 */
    public String getClassName()
    {
        String libTitle = SoundSystemConfig.getLibraryTitle( libraryType );

        if( libTitle.equals( "No Sound" ) )
            return "Channel";
        else
            return "Channel" + libTitle;
    }
    
/**
 * Prints a message.
 * @param message Message to print.
 */
    protected void message( String message )
    {
        logger.message( message, 0 );
    }
    
/**
 * Prints an important message.
 * @param message Message to print.
 */
    protected void importantMessage( String message )
    {
        logger.importantMessage( message, 0 );
    }
    
/**
 * Prints the specified message if error is true.
 * @param error True or False.
 * @param message Message to print if error is true.
 * @return True if error is true.
 */
    protected boolean errorCheck( boolean error, String message )
    {
        return logger.errorCheck( error, getClassName(), message, 0 );
    }
    
/**
 * Prints an error message.
 * @param message Message to print.
 */
    protected void errorMessage( String message )
    {
        logger.errorMessage( getClassName(), message, 0 );
    }
    
/**
 * Prints an exception's error message followed by the stack trace.
 * @param e Exception containing the information to print.
 */
    protected void printStackTrace( Exception e )
    {
        logger.printStackTrace( e, 1 );
    }
}
