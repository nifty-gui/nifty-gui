package paulscode.sound;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

/**
 * The MidiChannel class provides an interface for playing MIDI files, using 
 * the JavaSound API.  For more information about the JavaSound API, visit 
 * http://java.sun.com/products/java-media/sound/ 
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
public class MidiChannel implements MetaEventListener
{
/**
 * Processes status messages, warnings, and error messages.
 */
    private SoundSystemLogger logger;
    
/**
 * Filename/URL to the file:
 */
    private FilenameURL filenameURL;
    
/**
 * Unique source identifier for this MIDI source.
 */
    private String sourcename;
    
/**
 * Global identifier for the MIDI "change volume" event.
 */
    private static final int CHANGE_VOLUME = 7;
    
/**
 * Global identifier for the MIDI "end of track" event.
 */
    private static final int END_OF_TRACK = 47;
    
/**
 * Used to return a current value from one of the synchronized 
 * boolean-interface methods.
 */
    private static final boolean GET = false;
    
/**
 * Used to set the value in one of the synchronized boolean-interface methods.
 */
    private static final boolean SET = true;
    
/**
 * Used when a parameter for one of the synchronized boolean-interface methods 
 * is not aplicable.
 */
    private static final boolean XXX = false;
    
/**
 * Runs the assigned sequence, passing information on to the synthesizer for 
 * playback.
 */
    private Sequencer sequencer = null;
    
/**
 * Converts MIDI events into audio.
 */
    private Synthesizer synthesizer = null;

/**
 * Converts MIDI events into audio if there is no default Synthesizer.
 */
    private MidiDevice synthDevice = null;
    
/**
 * Sequence of MIDI events defining sound.
 */
    private Sequence sequence = null;
    
/**
 * Should playback loop or play only once.
 */
    private boolean toLoop = true;
    
/**
 * Playback volume, float value (0.0f - 1.0f).
 */
    private float gain = 1.0f;
    
/**
 * True while sequencer is busy being set up.
 */
    private boolean loading = true;
    
/**
 * The list of MIDI files to play when the current sequence finishes.
 */
    private LinkedList<FilenameURL> sequenceQueue = null;

/**
 * Ensures that only one thread accesses the sequenceQueue at a time.
 */
    private final Object sequenceQueueLock = new Object();
    
/**
 * Specifies the gain factor used for the fade-out effect, or -1 when
 * playback is not currently fading out.
 */
    protected float fadeOutGain = -1.0f;

/**
 * Specifies the gain factor used for the fade-in effect, or 1 when
 * playback is not currently fading in.
 */
    protected float fadeInGain = 1.0f;

/**
 * Specifies the number of miliseconds it should take to fade out.
 */
    protected long fadeOutMilis = 0;

/**
 * Specifies the number of miliseconds it should take to fade in.
 */
    protected long fadeInMilis = 0;

/**
 * System time in miliseconds when the last fade in/out volume check occurred.
 */
    protected long lastFadeCheck = 0;

/**
 * Used for fading in and out effects.
 */
    private FadeThread fadeThread = null;

/**
 * Constructor: Defines the basic source information.
 * @param toLoop Should playback loop or play only once?
 * @param sourcename Unique identifier for this source.
 * @param filename Name of the MIDI file to play.
 */
    public MidiChannel( boolean toLoop, String sourcename, String filename )
    {
        // let others know we are busy loading:
        loading( SET, true );

        // grab a handle to the message logger:
        logger = SoundSystemConfig.getLogger();
        
        // save information about the source:
        filenameURL( SET, new FilenameURL( filename ) );
        sourcename( SET, sourcename );
        setLooping( toLoop );

        // initialize the MIDI channel:
        init();

        // finished loading:
        loading( SET, false );
    }

/**
 * Constructor: Defines the basic source information.  The fourth parameter,
 * 'identifier' should look like a filename, and it must have the correct
 * extension (.mid or .midi).
 * @param toLoop Should playback loop or play only once?
 * @param sourcename Unique identifier for this source.
 * @param midiFile URL to the MIDI file to play.
 * @param identifier Filename/identifier for the MIDI file.
 */
    public MidiChannel( boolean toLoop, String sourcename, URL midiFile,
                        String identifier )
    {
        // let others know we are busy loading
        loading( SET, true );

        // grab a handle to the message logger:
        logger = SoundSystemConfig.getLogger();

        // save information about the source:
        filenameURL( SET, new FilenameURL( midiFile, identifier ) );
        sourcename( SET, sourcename );
        setLooping( toLoop );

        // initialize the MIDI channel:
        init();

        // finished loading:
        loading( SET, false );
    }

/**
 * Constructor: Defines the basic source information.
 * @param toLoop Should playback loop or play only once?
 * @param sourcename Unique identifier for this source.
 * @param midiFilenameURL Filename/URL to the MIDI file to play.
 */
    public MidiChannel( boolean toLoop, String sourcename,
                        FilenameURL midiFilenameURL )
    {
        // let others know we are busy loading
        loading( SET, true );

        // grab a handle to the message logger:
        logger = SoundSystemConfig.getLogger();

        // save information about the source:
        filenameURL( SET, midiFilenameURL );
        sourcename( SET, sourcename );
        setLooping( toLoop );

        // initialize the MIDI channel:
        init();

        // finished loading:
        loading( SET, false );
    }

/**
 * Initializes the sequencer, loads the sequence, and sets up the synthesizer.
 */
    private void init()
    {
        // Load a sequencer:
        getSequencer();
        
        // Load the sequence to play:
        setSequence( filenameURL( GET, null).getURL() );

        // Load a synthesizer to play the sequence on:
        getSynthesizer();

        // Ensure the initial volume is correct:
        // (TODO: doesn't always work??)
        resetGain();
    }

/**
 * Shuts the channel down and removes references to all instantiated objects.
 */
    public void cleanup()
    {
        loading( SET, true );
        setLooping( true );
        
        if( sequencer != null )
        {
            try
            {
                sequencer.stop();
                sequencer.close();
                sequencer.removeMetaEventListener( this );
            }
            catch( Exception e )
            {}
        }
        
        logger = null;
        sequencer = null;
        synthesizer = null;
        sequence = null;

        synchronized( sequenceQueueLock )
        {
            if( sequenceQueue != null )
                sequenceQueue.clear();
            sequenceQueue = null;
        }
        
        // End the fade effects thread if it exists:
        if( fadeThread != null )
        {
            boolean killException = false;
            try
            {
                fadeThread.kill();        // end the fade effects thread.
                fadeThread.interrupt();   // wake the thread up so it can end.
            }
            catch( Exception e )
            {
                killException = true;
            }

            if( !killException )
            {
                // wait up to 5 seconds for fade effects thread to end:
                for( int i = 0; i < 50; i++ )
                {
                    if( !fadeThread.alive() )
                        break;
                    try{Thread.sleep( 100 );}catch(InterruptedException e){}
                }
            }

            // Let user know if there was a problem ending the fade thread
            if( killException || fadeThread.alive() )
            {
                errorMessage( "MIDI fade effects thread did not die!" );
                message( "Ignoring errors... continuing clean-up." );
            }
        }

        fadeThread = null;
        
        loading( SET, false );
    }
    
/**
 * Queues up the next MIDI sequence to play when the previous sequence ends.
 * @param filenameURL MIDI sequence to play next.
 */
    public void queueSound( FilenameURL filenameURL )
    {
        if( filenameURL == null )
        {
            errorMessage( "Filename/URL not specified in method 'queueSound'" );
            return;
        }

        synchronized( sequenceQueueLock )
        {
            if( sequenceQueue == null )
                sequenceQueue = new LinkedList<FilenameURL>();
            sequenceQueue.add( filenameURL );
        }
    }

/**
 * Removes the first occurrence of the specified filename/identifier from the
 * list of MIDI sequences to play when the previous sequence ends.
 * @param filename Filename or identifier of a MIDI sequence to remove from the
 * queue.
 */
    public void dequeueSound( String filename )
    {
        if( filename == null || filename.equals( "" ) )
        {
            errorMessage( "Filename not specified in method 'dequeueSound'" );
            return;
        }

        synchronized( sequenceQueueLock )
        {
            if( sequenceQueue != null )
            {
                ListIterator<FilenameURL> i = sequenceQueue.listIterator();
                while( i.hasNext() )
                {
                    if( i.next().getFilename().equals( filename ) )
                    {
                        i.remove();
                        break;
                    }
                }
            }
        }
    }

/**
 * Fades out the volume of whatever sequence is currently playing, then
 * begins playing the specified MIDI file at the previously assigned
 * volume level.  If the filenameURL parameter is null or empty, playback will
 * simply fade out and stop.  The miliseconds parameter must be non-negative or
 * zero.  This method will remove anything that is currently in the list of
 * queued MIDI sequences that would have played next when current playback
 * finished.
 * @param filenameURL MIDI file to play next, or null for none.
 * @param milis Number of miliseconds the fadeout should take.
 */
    public void fadeOut( FilenameURL filenameURL, long milis )
    {
        if( milis < 0 )
        {
            errorMessage( "Miliseconds may not be negative in method " +
                          "'fadeOut'." );
            return;
        }

        fadeOutMilis = milis;
        fadeInMilis = 0;
        fadeOutGain = 1.0f;
        lastFadeCheck = System.currentTimeMillis();

        synchronized( sequenceQueueLock )
        {
            if( sequenceQueue != null )
                sequenceQueue.clear();

            if( filenameURL != null )
            {
                if( sequenceQueue == null )
                    sequenceQueue = new LinkedList<FilenameURL>();
                sequenceQueue.add( filenameURL );
            }
        }
        if( fadeThread == null )
        {
            fadeThread = new FadeThread();
            fadeThread.start();
        }
        fadeThread.interrupt();
    }

/**
 * Fades out the volume of whatever sequence is currently playing, then
 * fades the volume back in playing the specified MIDI file.  Final volume
 * after fade-in completes will be equal to the previously assigned volume
 * level.  The filenameURL parameter may not be null or empty.  The miliseconds
 * parameters must be non-negative or zero.  This method will remove anything
 * that is currently in the list of queued MIDI sequences that would have
 * played next when current playback finished.
 * @param filenameURL MIDI file to play next, or null for none.
 * @param milisOut Number of miliseconds the fadeout should take.
 * @param milisIn Number of miliseconds the fadein should take.
 */
    public void fadeOutIn( FilenameURL filenameURL, long milisOut,
                           long milisIn )
    {
        if( filenameURL == null )
        {
            errorMessage( "Filename/URL not specified in method 'fadeOutIn'." );
            return;
        }
        if( milisOut < 0 || milisIn < 0 )
        {
            errorMessage( "Miliseconds may not be negative in method " +
                          "'fadeOutIn'." );
            return;
        }

        fadeOutMilis = milisOut;
        fadeInMilis = milisIn;
        fadeOutGain = 1.0f;
        lastFadeCheck = System.currentTimeMillis();

        synchronized( sequenceQueueLock )
        {
            if( sequenceQueue == null )
                sequenceQueue = new LinkedList<FilenameURL>();
            sequenceQueue.clear();
            sequenceQueue.add( filenameURL );
        }
        if( fadeThread == null )
        {
            fadeThread = new FadeThread();
            fadeThread.start();
        }
        fadeThread.interrupt();
    }

/**
 * Resets this source's volume if it is fading out or in.  Returns true if this
 * source is currently in the process of fading out.  When fade-out completes,
 * this method transitions the source to the next sound in the sound sequence
 * queue if there is one.  This method has no effect on non-streaming sources.
 * @return True if this source is in the process of fading out.
 */
    private synchronized boolean checkFadeOut()
    {
        if( fadeOutGain == -1.0f && fadeInGain == 1.0f )
            return false;

        long currentTime = System.currentTimeMillis();
        long milisPast = currentTime - lastFadeCheck;
        lastFadeCheck = currentTime;

        if( fadeOutGain >= 0.0f )
        {
            if( fadeOutMilis == 0 )
            {
                fadeOutGain = 0.0f;
                fadeInGain = 0.0f;
                if( !incrementSequence() )
                    stop();
                rewind();
                resetGain();
                return false;
            }
            else
            {
                float fadeOutReduction = ((float)milisPast) / ((float)fadeOutMilis);

                fadeOutGain -= fadeOutReduction;
                if( fadeOutGain <= 0.0f )
                {
                    fadeOutGain = -1.0f;
                    fadeInGain = 0.0f;
                    if( !incrementSequence() )
                        stop();
                    rewind();
                    resetGain();
                    return false;
                }
            }
            resetGain();
            return true;
        }

        if( fadeInGain < 1.0f )
        {
            fadeOutGain = -1.0f;
            if( fadeInMilis == 0 )
            {
                fadeOutGain = -1.0f;
                fadeInGain = 1.0f;
            }
            else
            {
                float fadeInIncrease = ((float)milisPast) / ((float)fadeInMilis);
                fadeInGain += fadeInIncrease;
                if( fadeInGain >= 1.0f )
                {
                    fadeOutGain = -1.0f;
                    fadeInGain = 1.0f;
                }
            }
            resetGain();
        }

        return false;
    }

/**
 * Removes the next sequence from the queue and assigns it to the sequencer.
 * @return True if there was something in the queue.
 */
    private boolean incrementSequence()
    {
        synchronized( sequenceQueueLock )
        {
            // Is there a queue, and if so, is there anything in it:
            if( sequenceQueue != null && sequenceQueue.size() > 0 )
            {
                // grab the next filename/URL from the queue:
                filenameURL( SET, sequenceQueue.remove( 0 ) );

                // Let everyone know we are busy loading:
                loading( SET, true );

                // Check if we have a sequencer:
                if( sequencer == null )
                {
                    // nope, try and get one now:
                    getSequencer();
                }
                else
                {
                    // We have a sequencer.  Stop it now:
                    sequencer.stop();
                    // rewind to the beginning:
                    sequencer.setMicrosecondPosition( 0 );
                    // Stop listening for a moment:
                    sequencer.removeMetaEventListener( this );
                    // wait a bit for the sequencer to shut down and rewind:
                    try{ Thread.sleep( 100 ); }catch( InterruptedException e ){}
                }
                // We need to have a sequencer at this point:
                if( sequencer == null )
                {
                    errorMessage( "Unable to set the sequence in method " +
                                  "'incrementSequence', because there wasn't " +
                                  "a sequencer to use." );

                    // Finished loading:
                    loading( SET, false );
                    
                    // failure:
                    return false;
                }
                // set the new sequence to be played:
                setSequence( filenameURL( GET, null ).getURL() );
                // start playing again:
                sequencer.start();
                // make sure we play at the correct volume:
                // (TODO: This doesn't always work??)
                resetGain();
                // start listening for end of track event again:
                sequencer.addMetaEventListener( this );

                // Finished loading:
                loading( SET, false );

                // We successfully moved to the next sequence:
                return true;
            }
        }
        
        // Nothing left to load
        return false;
    }

/**
 * Plays the MIDI file from the beginning, or from where it left off if it was 
 * paused.
 */
    public void play()
    {
        if( !loading() )
        {
            // Make sure there is a sequencer:
            if( sequencer == null )
                return;

            try
            {
                // start playing:
                sequencer.start();
                // event will be sent when end of track is reached:
                sequencer.addMetaEventListener( this );
            }
            catch( Exception e )
            {
                errorMessage( "Exception in method 'play'" );
                printStackTrace( e );
                SoundSystemException sse = new SoundSystemException(
                                                               e.getMessage() );
                SoundSystem.setException( sse );
            }
        }
    }
    
/**
 * Stops playback and rewinds to the beginning.
 */
    public void stop()
    {
        if( !loading() )
        {
            // Make sure there is a sequencer:
            if( sequencer == null )
                return;

            try
            {
                // stop playback:
                sequencer.stop();
                // rewind to the beginning:
                sequencer.setMicrosecondPosition( 0 );
                // No need to listen any more:
                sequencer.removeMetaEventListener( this );
            }
            catch( Exception e )
            {
                errorMessage( "Exception in method 'stop'" );
                printStackTrace( e );
                SoundSystemException sse = new SoundSystemException(
                                                               e.getMessage() );
                SoundSystem.setException( sse );
            }
        }
    }
    
/**
 * Temporarily stops playback without rewinding.
 */
    public void pause()
    {
        if( !loading() )
        {
            // Make sure there is a sequencer:
            if( sequencer == null )
                return;

            try
            {
                //stop playback.  Will resume from this location next play.
                sequencer.stop();
            }
            catch( Exception e )
            {
                errorMessage( "Exception in method 'pause'" );
                printStackTrace( e );
                SoundSystemException sse = new SoundSystemException(
                                                               e.getMessage() );
                SoundSystem.setException( sse );
            }
        }
    }
    
/**
 * Returns playback to the beginning.
 */
    public void rewind()
    {
        if( !loading() )
        {
            // Make sure there is a sequencer:
            if( sequencer == null )
                return;

            try
            {
                // rewind to the beginning:
                sequencer.setMicrosecondPosition( 0 );
            }
            catch( Exception e )
            {
                errorMessage( "Exception in method 'rewind'" );
                printStackTrace( e );
                SoundSystemException sse = new SoundSystemException(
                                                               e.getMessage() );
                SoundSystem.setException( sse );
            }
        }
    }
    
/**
 * Changes the volume of MIDI playback.
 * @param value Float value (0.0f - 1.0f).
 */
    public void setVolume( float value )
    {
        gain = value;
        resetGain();
    }
    
/**
 * Returns the current volume for the MIDI source.
 * @return Float value (0.0f - 1.0f).
 */
    public float getVolume()
    {
        return gain;
    }
    
/**
 * Changes the basic information about the MIDI source.  This method removes
 * any queued filenames/URLs from the list of MIDI sequences that would have
 * played after the current sequence ended.
 * @param toLoop Should playback loop or play only once?
 * @param sourcename Unique identifier for this source.
 * @param filename Name of the MIDI file to play.
 */
    public void switchSource( boolean toLoop, String sourcename,
                              String filename )
    {
        // Let everyone know we are busy loading:
        loading( SET, true );

        // save information about the source:
        filenameURL( SET, new FilenameURL( filename ) );
        sourcename( SET, sourcename );
        setLooping( toLoop );

        reset();

        // Finished loading:
        loading( SET, false );
    }

/**
 * Changes the basic information about the MIDI source.  This method removes
 * any queued filenames/URLs from the list of MIDI sequences that would have
 * played after the current sequence ended.  The fourth parameter,
 * 'identifier' should look like a filename, and it must have the correct
 * extension (.mid or .midi).
 * @param toLoop Should playback loop or play only once?
 * @param sourcename Unique identifier for this source.
 * @param midiFile URL to the MIDI file to play.
 * @param identifier Filename/identifier for the MIDI file.
 */
    public void switchSource( boolean toLoop, String sourcename, URL midiFile,
                              String identifier )
    {
        // Let everyone know we are busy loading:
        loading( SET, true );

        // save information about the source:
        filenameURL( SET, new FilenameURL( midiFile, identifier ) );
        sourcename( SET, sourcename );
        setLooping( toLoop );

        reset();

        // Finished loading:
        loading( SET, false );
    }

/**
 * Changes the basic information about the MIDI source.  This method removes
 * any queued filenames/URLs from the list of MIDI sequences that would have
 * played after the current sequence ended.
 * @param toLoop Should playback loop or play only once?
 * @param sourcename Unique identifier for this source.
 * @param filenameURL Filename/URL of the MIDI file to play.
 */
    public void switchSource( boolean toLoop, String sourcename,
                              FilenameURL filenameURL )
    {
        // Let everyone know we are busy loading:
        loading( SET, true );

        // save information about the source:
        filenameURL( SET, filenameURL );
        sourcename( SET, sourcename );
        setLooping( toLoop );

        reset();

        // Finished loading:
        loading( SET, false );
    }

/**
 * Stops and rewinds the sequencer, and resets the sequence.
 */
    private void reset()
    {
        synchronized( sequenceQueueLock )
        {
            if( sequenceQueue != null )
                sequenceQueue.clear();
        }
        
        // Check if we have a sequencer:
        if( sequencer == null )
        {
            // nope, try and get one now:
            getSequencer();
        }
        else
        {
            // We have a sequencer.  Stop it now:
            sequencer.stop();
            // rewind to the beginning:
            sequencer.setMicrosecondPosition( 0 );
            // Stop listening for a moment:
            sequencer.removeMetaEventListener( this );
            // wait a bit for the sequencer to shut down and rewind:
            try{ Thread.sleep( 100 ); }catch( InterruptedException e ){}
        }
        // We need to have a sequencer at this point:
        if( sequencer == null )
        {
            errorMessage( "Unable to set the sequence in method " +
                          "'reset', because there wasn't " +
                          "a sequencer to use." );
            return;
        }

        // set the new sequence to be played:
        setSequence( filenameURL( GET, null ).getURL() );
        // start playing again:
        sequencer.start();
        // make sure we play at the correct volume:
        // (TODO: This doesn't always work??)
        resetGain();
        // start listening for end of track event again:
        sequencer.addMetaEventListener( this );
    }

/**
 * Sets the value of boolean 'toLoop'.
 * @param value True or False.
 */
    public void setLooping( boolean value )
    {
        toLoop( SET, value );
    }
    
/**
 * Returns the value of boolean 'toLoop'.
 * @return True while looping.
 */
    public boolean getLooping()
    {
        return toLoop( GET, XXX );
    }
    
/**
 * Sets or returns the value of boolean 'toLoop'.
 * @param action GET or SET.
 * @param value New value if action == SET, or XXX if action == GET.
 * @return True while looping.
 */
    private synchronized boolean toLoop( boolean action, boolean value )
    {
        if( action == SET )
            toLoop = value;
        return toLoop;
    }
    
/**
 * Check if a MIDI file is in the process of loading.
 */
    public boolean loading()
    {
        return( loading( GET, XXX ) );
    }
    
/**
 * Sets or returns the value of boolean 'loading'.
 * @param action GET or SET.
 * @param value New value if action == SET, or XXX if action == GET.
 * @return True while a MIDI file is in the process of loading.
 */
    private synchronized boolean loading( boolean action, boolean value )
    {
        if( action == SET )
            loading = value;
        return loading;
    }
    
/**
 * Defines the unique identifier for this source
 * @param value New source name.
 */
    public void setSourcename( String value )
    {
        sourcename( SET, value );
    }
    
/**
 * Returns the unique identifier for this source.
 * @return The source's name.
 */
    public String getSourcename()
    {
        return sourcename( GET, null );
    }
    
/**
 * Sets or returns the value of String 'sourcename'.
 * @param action GET or SET.
 * @param value New value if action == SET, or null if action == GET.
 * @return The source's name.
 */
    private synchronized String sourcename( boolean action, String value )
    {
        if( action == SET )
            sourcename = value;
        return sourcename;
    }
    
/**
 * Defines which MIDI file to play.
 * @param value Path to the MIDI file.
 */
    public void setFilenameURL( FilenameURL value )
    {
        filenameURL( SET, value );
    }
    
/**
 * Returns the filename/identifier of the MIDI file being played.
 * @return Filename of identifier of the MIDI file.
 */
    public String getFilename()
    {
        return filenameURL( GET, null ).getFilename();
    }

/**
 * Returns the MIDI file being played.
 * @return Filename/URL of the MIDI file.
 */
    public FilenameURL getFilenameURL()
    {
        return filenameURL( GET, null );
    }
    
/**
 * Sets or returns the value of filenameURL.
 * @param action GET or SET.
 * @param value New value if action == SET, or null if action == GET.
 * @return Path to the MIDI file.
 */
    private synchronized FilenameURL filenameURL( boolean action,
                                                  FilenameURL value )
    {
        if( action == SET )
            filenameURL = value;
        return filenameURL;
    }
    
/**
 * Called when MIDI events occur.
 * @param message Meta mssage describing the MIDI event.
 */
    public void meta( MetaMessage message )
    {
        if( message.getType() == END_OF_TRACK )
        {
            // Generate an EOS event:
            SoundSystemConfig.notifyEOS( sourcename, sequenceQueue.size() );

            // check if we should loop or not:
            if( toLoop )
            {
                // looping
                // Check if playback is in the process of fading out.
                if( !checkFadeOut() )
                {
                    // Not fading out, progress to the next MIDI sequence if
                    // any are queued.
                    if( !incrementSequence() )
                    {
                        try
                        {
                            // Rewind to the beginning.
                            sequencer.setMicrosecondPosition( 0 );
                            sequencer.start();
                            // Make sure playback volume is correct.
                            resetGain();
                        }
                        catch( Exception e ){}
                    }
                }
                else if( sequencer != null )
                {
                    try
                    {
                        // Rewind to the beginning.
                        sequencer.setMicrosecondPosition( 0 );
                        sequencer.start();
                        // Make sure playback volume is correct.
                        resetGain();
                    }
                    catch( Exception e ){}
                }
            }
            else
            {
                //non-looping
                if( !checkFadeOut() )
                {
                    if( !incrementSequence() )
                    {
                        try
                        {
                            // stop playback:
                            sequencer.stop();
                            // rewind to the beginning:
                            sequencer.setMicrosecondPosition( 0 );
                            // stop looping:
                            sequencer.removeMetaEventListener( this );
                        }
                        catch( Exception e ){}
                    }
                }
                else
                {
                    try
                    {
                        // stop playback:
                        sequencer.stop();
                        // rewind to the beginning:
                        sequencer.setMicrosecondPosition( 0 );
                        // stop looping:
                        sequencer.removeMetaEventListener( this );
                    }
                    catch( Exception e ){}
                }
            }
        }
    }
    
/**
 * Resets playback volume to the correct level.
 */
    public void resetGain()
    {
        // make sure the value for gain is valid (between 0 and 1)
        if( gain < 0.0f )
            gain = 0.0f;
        if( gain > 1.0f )
            gain = 1.0f;
        
        int midiVolume = (int) ( gain * SoundSystemConfig.getMasterGain()
                                 * (float) Math.abs( fadeOutGain ) * fadeInGain
                                 * 127.0f );
        if( synthesizer != null )
        {
            javax.sound.midi.MidiChannel[] channels = synthesizer.getChannels();
            for( int c = 0; channels != null && c < channels.length; c++ )
            {
                channels[c].controlChange( CHANGE_VOLUME, midiVolume );
            }
        }
        else if( synthDevice != null )
        {
            try
            {
                ShortMessage volumeMessage = new ShortMessage();
                for( int i = 0; i < 16; i++ )
                {
                    volumeMessage.setMessage( ShortMessage.CONTROL_CHANGE, i,
                                              CHANGE_VOLUME, midiVolume );
                    synthDevice.getReceiver().send( volumeMessage, -1 );
                }
            }
            catch( Exception e )
            {
                errorMessage( "Error resetting gain on MIDI device" );
                printStackTrace( e );
            }
        }
        else if( sequencer != null && sequencer instanceof Synthesizer )
        {
            synthesizer = (Synthesizer) sequencer;
            javax.sound.midi.MidiChannel[] channels = synthesizer.getChannels();
            for( int c = 0; channels != null && c < channels.length; c++ )
            {
                channels[c].controlChange( CHANGE_VOLUME, midiVolume );
            }
        }
        else
        {
            try
            {
                Receiver receiver = MidiSystem.getReceiver();
                ShortMessage volumeMessage= new ShortMessage();
                for( int c = 0; c < 16; c++ )
                {
                    volumeMessage.setMessage( ShortMessage.CONTROL_CHANGE, c,
                                              CHANGE_VOLUME, midiVolume );
                    receiver.send( volumeMessage, -1 );
                }
            }
            catch( Exception e )
            {
                errorMessage( "Error resetting gain on default receiver" );
                printStackTrace( e );
            }
        }
    }

/**
 * Attempts to load the default sequencer.  If it fails, then other common
 * sequencers are tried.  If none can be loaded, then variable 'sequencer'
 * remains null.
 */
    private void getSequencer()
    {
        try
        {
            sequencer = MidiSystem.getSequencer();
            if( sequencer != null )
            {
                try
                {
                    sequencer.getTransmitter();
                }
                catch( MidiUnavailableException mue )
                {
                    message( "Unable to get a transmitter from the " +
                             "default MIDI sequencer" );
                }
                sequencer.open();
            }
        }
        catch( MidiUnavailableException mue )
        {
            message( "Unable to open the default MIDI sequencer" );
            sequencer = null;
        }
        catch( Exception e )
        {
            if( e instanceof InterruptedException )
            {
                message( "Caught InterruptedException while attempting to " +
                        "open the default MIDI sequencer.  Trying again." );
                sequencer = null;
            }
            try
            {
                sequencer = MidiSystem.getSequencer();
                if( sequencer != null )
                {
                    try
                    {
                        sequencer.getTransmitter();
                    }
                    catch( MidiUnavailableException mue )
                    {
                        message( "Unable to get a transmitter from the " +
                                 "default MIDI sequencer" );
                    }
                    sequencer.open();
                }
            }
            catch( MidiUnavailableException mue )
            {
                message( "Unable to open the default MIDI sequencer" );
                sequencer = null;
            }
            catch( Exception e2 )
            {
                message( "Unknown error opening the default MIDI sequencer" );
                sequencer = null;
            }
        }

        if( sequencer == null )
            sequencer = openSequencer( "Real Time Sequencer" );
        if( sequencer == null )
            sequencer = openSequencer( "Java Sound Sequencer");
        if( sequencer == null )
        {
            errorMessage( "Failed to find an available MIDI sequencer" );
            return;
        }
    }

/**
 * Loads the MIDI sequence form the specified URL, and sets the sequence.  If
 * variable 'sequencer' is null or an error occurs, then variable 'sequence'
 * remains null.
 * @param midiSource URL to a MIDI file.
 */
    private void setSequence( URL midiSource )
    {
        if( sequencer == null )
        {
            errorMessage( "Unable to update the sequence in method " +
                          "'setSequence', because variable 'sequencer' " +
                          "is null" );
            return;
        }

        if( midiSource == null )
        {
            errorMessage( "Unable to load Midi file in method 'setSequence'." );
            return;
        }

        try
        {
            sequence = MidiSystem.getSequence( midiSource );
        }
        catch( IOException ioe )
        {
            errorMessage( "Input failed while reading from MIDI file in " +
                          "method 'setSequence'." );
            printStackTrace( ioe );
            return;
        }
        catch( InvalidMidiDataException imde )
        {
            errorMessage( "Invalid MIDI data encountered, or not a MIDI " +
                          "file in method 'setSequence' (1)." );
            printStackTrace( imde );
            return;
        }
        if( sequence == null )
        {
            errorMessage( "MidiSystem 'getSequence' method returned null " +
                          "in method 'setSequence'." );
        }
        else
        {
            try
            {
                sequencer.setSequence( sequence );
            }
            catch( InvalidMidiDataException imde )
            {
                errorMessage( "Invalid MIDI data encountered, or not a MIDI " +
                              "file in method 'setSequence' (2)." );
                printStackTrace( imde );
                return;
            }
            catch( Exception e )
            {
                errorMessage( "Problem setting sequence from MIDI file in " +
                              "method 'setSequence'." );
                printStackTrace( e );
                return;
            }
        }
    }

/**
 * First attempts to load the specified "override MIDI synthesizer" if one was
 * defined.  If none was defined or unable to use it, then attempts to load the
 * default synthesizer.  If that fails, then other common synthesizers are
 * attempted.  If none can be loaded, then MIDI is not possible on this system.
 */
    private void getSynthesizer()
    {
        if( sequencer == null )
        {
            errorMessage( "Unable to load a Synthesizer in method " +
                          "'getSynthesizer', because variable 'sequencer' " +
                          "is null" );
            return;
        }

        // Check if an alternate MIDI synthesizer was specified to use
        String overrideMIDISynthesizer =
                                     SoundSystemConfig.getOverrideMIDISynthesizer();
        if( overrideMIDISynthesizer != null
            && !overrideMIDISynthesizer.equals( "" ) )
        {
            // Try and open the specified device:
            synthDevice = openMidiDevice( overrideMIDISynthesizer );
            // See if we got it:
            if( synthDevice != null )
            {
                // Got it, try and link it to the sequencer:
                try
                {
                    sequencer.getTransmitter().setReceiver(
                                                    synthDevice.getReceiver() );
                    // Success!
                    return;
                }
                catch( MidiUnavailableException mue )
                {
                    // Problem linking the two, let the user know
                    errorMessage( "Unable to link sequencer transmitter " +
                                  "with receiver for MIDI device '" +
                                  overrideMIDISynthesizer + "'" );
                }
            }
        }

        // No alternate MIDI synthesizer was specified, or unable to use it.

        // If the squencer were also a synthesizer, that would make things easy:
        if( sequencer instanceof Synthesizer )
        {
            synthesizer = (Synthesizer) sequencer;
        }
        else
        {
            // Try getting the default synthesizer first:
            try
            {
                synthesizer = MidiSystem.getSynthesizer();
                synthesizer.open();
            }
            catch( MidiUnavailableException mue )
            {
                message( "Unable to open the default synthesizer" );
                synthesizer = null;
            }

            // See if we were sucessful:
            if( synthesizer == null )
            {
                // Try for the common MIDI synthesizers:
                synthDevice = openMidiDevice( "Java Sound Synthesizer" );
                if( synthDevice == null )
                    synthDevice = openMidiDevice( "Microsoft GS Wavetable" );
                if( synthDevice == null )
                    synthDevice = openMidiDevice( "Gervill" );
                if( synthDevice == null )
                {
                    // Still nothing, MIDI is not going to work
                    errorMessage( "Failed to find an available MIDI " +
                                  "synthesizer" );
                    return;
                }
            }

            // Are we using the default synthesizer or something else?
            if( synthesizer == null )
            {
                // Link the sequencer and synthesizer:
                try
                {
                    sequencer.getTransmitter().setReceiver(
                                                    synthDevice.getReceiver() );
                }
                catch( MidiUnavailableException mue )
                {
                    errorMessage( "Unable to link sequencer transmitter " +
                                  "with MIDI device receiver" );
                }
            }
            else
            {
                // Bug-fix for multiple-receivers playing simultaneously
                if( synthesizer.getDefaultSoundbank() == null )
                {
                    // Link the sequencer to the default receiver:
                    try
                    {
                        sequencer.getTransmitter().setReceiver(
                                                     MidiSystem.getReceiver() );
                    }
                    catch( MidiUnavailableException mue )
                    {
                        errorMessage( "Unable to link sequencer transmitter " +
                                      "with default receiver" );
                    }
                }
                else
                {
                    // Link the sequencer to the default synthesizer:
                    try
                    {
                        sequencer.getTransmitter().setReceiver(
                                                    synthesizer.getReceiver() );
                    }
                    catch( MidiUnavailableException mue )
                    {
                        errorMessage( "Unable to link sequencer transmitter " +
                                      "with synthesizer receiver" );
                    }
                }
                // End bug-fix
            }
       }
    }

/**
 * Attempts to open the Sequencer with a name containing the specified string.
 * @param containsString Part or all of a Sequencer's name.
 * @return Handle to the Sequencer, or null if not found or error.
 */
    private Sequencer openSequencer( String containsString )
    {
        Sequencer s = null;
        s = (Sequencer) openMidiDevice( containsString );
        if( s == null )
            return null;
        try
        {
            s.getTransmitter();
        }
        catch( MidiUnavailableException mue )
        {
            message( "    Unable to get a transmitter from this sequencer" );
            s = null;
            return null;
        }

        return s;
    }

/**
 * Attempts to open the MIDI device with a name containing the specified
 * string.
 * @param containsString Part or all of a MIDI device's name.
 * @return Handle to the MIDI device, or null if not found or error.
 */
    private MidiDevice openMidiDevice( String containsString )
    {
        message( "Searching for MIDI device with name containing '" +
                 containsString + "'" );
        MidiDevice device = null;
        MidiDevice.Info[] midiDevices = MidiSystem.getMidiDeviceInfo();
        for( int i = 0; i < midiDevices.length; i++ )
        {
            device = null;
            try
            {
                device = MidiSystem.getMidiDevice( midiDevices[i] );
            }
            catch( MidiUnavailableException e )
            {
                message( "    Problem in method 'getMidiDevice':  " +
                         "MIDIUnavailableException was thrown" );
                device = null;
            }
            if( device != null && midiDevices[i].getName().contains(
                                                              containsString ) )
            {
                message( "    Found MIDI device named '" +
                         midiDevices[i].getName() + "'" );
                if( device instanceof Synthesizer )
                    message( "        *this is a Synthesizer instance" );
                if( device instanceof Sequencer )
                    message( "        *this is a Sequencer instance" );
                try
                {
                    device.open();
                }
                catch( MidiUnavailableException mue )
                {
                    message( "    Unable to open this MIDI device" );
                    device = null;
                }
                return device;
            }
        }
        message( "    MIDI device not found" );
        return null;
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
        return logger.errorCheck( error, "MidiChannel", message, 0 );
    }
    
/**
 * Prints an error message.
 * @param message Message to print.
 */
    protected void errorMessage( String message )
    {
        logger.errorMessage( "MidiChannel", message, 0 );
    }
    
/**
 * Prints an exception's error message followed by the stack trace.
 * @param e Exception containing the information to print.
 */
    protected void printStackTrace( Exception e )
    {
        logger.printStackTrace( e, 1 );
    }

/**
 * The FadeThread class handles sequence changing, timing, and volume change
 * messages in the background.
 */
    private class FadeThread extends SimpleThread
    {
        @Override
/**
 * Runs in the background, timing fade in and fade out, changing the sequence, 
 * and issuing the appropriate volume change messages.
 */
        public void run()
        {
            while( !dying() )
            {
                // if not currently fading in or out, put the thread to sleep
                if( fadeOutGain == -1.0f && fadeInGain == 1.0f )
                    snooze( 3600000 );
                checkFadeOut();
                // only update every 50 miliseconds (no need to peg the cpu)
                snooze( 50 );
            }
            // Important!
            cleanup();
        }
    }
    
}

