package paulscode.sound;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * The StreamThread class is used to process all streaming sources.  This 
 * thread starts out asleep, and it sleeps when all streaming sources are 
 * finished playing, so it is necessary to call interrupt() after adding new 
 * streaming sources to the list.
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
 *
 */
public class StreamThread extends SimpleThread
{
/**
 * Processes status messages, warnings, and error messages.
 */
    private SoundSystemLogger logger;
    
/**
 * List of sources that are currently streaming.
 */
    private List<Source> streamingSources;
    
/**
 * Used to synchronize access to the streaming sources list.
 */
    private final Object listLock = new Object();
    
/**
 * Constructor:  Grabs a handle to the message logger and instantiates the 
 * streaming sources list.
 */
    public StreamThread()
    {
        // grab a handle to the message logger:
        logger = SoundSystemConfig.getLogger();
        
        streamingSources = new LinkedList<Source>();
    }
    
/**
 * Removes all references to instantiated objects, and changes the thread's 
 * state to "not alive".  Method alive() returns false when the cleanup() 
 * method has completed.
 */
    @Override
    protected void cleanup()
    {
        kill();
        super.cleanup();  // Important!!
    }
    
/**
 * The main loop for processing commands.  The thread sleeps when it finishes 
 * processing commands, and it must be interrupted to process more.
 */
    @Override
    public void run()
    {
        ListIterator<Source> iter;
        Source src;
        
        // Start out asleep:
        snooze( 3600000 );
        
        while( !dying() )
        {
            while( !dying() && !streamingSources.isEmpty() )
            {
                // Make sure noone else is accessing the list of sources:
                synchronized( listLock )
                {
                    iter = streamingSources.listIterator();
                    while( !dying() && iter.hasNext() )
                    {
                        src = iter.next();
                        if( src == null )
                        {
                            iter.remove();
                        }
                        else if( src.stopped() )
                        {
                            if( !src.rawDataStream )
                                iter.remove();
                        }
                        else if( !src.active() )
                        {
                            if( src.toLoop || src.rawDataStream )
                                src.toPlay = true;
                            iter.remove();
                        }
                        else if( !src.paused() )
                        {
                            src.checkFadeOut();
                            if( (!src.stream()) && (!src.rawDataStream) )
                            {
                                if( src.channel == null
                                               || !src.channel.processBuffer() )
                                {
                                    if( src.nextCodec == null )
                                    {
                                        src.readBuffersFromNextSoundInSequence();
                                    }
/*
                                    if( src.getSoundSequenceQueueSize() > 0 )
                                    {
                                        src.incrementSoundSequence();
                                    }

                                    // check if this is a looping source
                                    else*/ if( src.toLoop )
                                    {
                                        // wait for stream to finish playing
                                        if( !src.playing() )
                                        {
                                            // Generate an EOS event:
                                            SoundSystemConfig.notifyEOS(
                                                 src.sourcename,
                                                 src.getSoundSequenceQueueSize()
                                               );
                                            // Check if the source is currently
                                            // in the process of fading out.
                                            if( src.checkFadeOut() )
                                            {
                                                // Source is fading out.
                                                // Keep looping until it
                                                // finishes.
                                                src.preLoad = true;
                                            }
                                            else
                                            {
                                                // Source is not fading out.
                                                // If there is another sound in
                                                // the sequence, switch to it
                                                // before replaying.
                                                src.incrementSoundSequence();
                                                src.preLoad = true; // replay
                                            }
                                        }
                                    }
                                    else
                                    {
                                        // wait for stream to finish playing
                                        if( !src.playing() )
                                        {
                                            // Generate an EOS event:
                                            SoundSystemConfig.notifyEOS(
                                                src.sourcename,
                                                src.getSoundSequenceQueueSize()
                                              );
                                            // Check if the source is currently
                                            // in the process of fading out
                                            if( !src.checkFadeOut() )
                                            {
                                                // Source is not fading out.
                                                // Play anything else that is
                                                // in the sound sequence queue.
                                                if(
                                                  src.incrementSoundSequence() )
                                                    src.preLoad = true;
                                                else
                                                    iter.remove();  // finished
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if( !dying() && !streamingSources.isEmpty() )
                    snooze( 20 );  // sleep a bit so we don't peg the cpu
            }
            if( !dying() && streamingSources.isEmpty() )
                snooze( 3600000 );  // sleep until there is more to do.
        }
        
        cleanup();  // Important!!
    }
    
/**
 * Adds a new streaming source to the list.  If another source in the list is 
 * already playing on the same channel, it is stopped and removed from the 
 * list.  
 * @param source New source to stream.
 */
    public void watch( Source source )
    {
        // make sure the source exists:
        if( source == null )
            return;
        
        // make sure we aren't already watching this source:
        if( streamingSources.contains( source ) )
            return;
        
        ListIterator<Source> iter;
        Source src;
        
        // Make sure noone else is accessing the list of sources:
        synchronized( listLock )
        {
            // Any currently watched source which is null or playing on the 
            // same channel as the new source should be stopped and removed 
            // from the list.
            iter = streamingSources.listIterator();
            while( iter.hasNext() )
            {
                src = iter.next();
                if( src == null )
                {
                    iter.remove();
                }
                else if( source.channel == src.channel )
                {
                    src.stop();
                    iter.remove();
                }
            }
            
            // Add the new source to the list:
            streamingSources.add( source );
        }
    }
    
/**
 * Prints a message.
 * @param message Message to print.
 */
    private void message( String message )
    {
        logger.message( message, 0 );
    }
    
/**
 * Prints an important message.
 * @param message Message to print.
 */
    private void importantMessage( String message )
    {
        logger.importantMessage( message, 0 );
    }
    
/**
 * Prints the specified message if error is true.
 * @param error True or False.
 * @param message Message to print if error is true.
 * @return True if error is true.
 */
    private boolean errorCheck( boolean error, String message )
    {
        return logger.errorCheck( error, "StreamThread", message, 0 );
    }
    
/**
 * Prints an error message.
 * @param message Message to print.
 */
    private void errorMessage( String message )
    {
        logger.errorMessage( "StreamThread", message, 0 );
    }
}
