package paulscode.sound;

import java.net.URL;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.sound.sampled.AudioFormat;

/**
 * The Source class is used to store information about a source.  
 * Source objects are stored in a map in the Library class.  The 
 * information they contain is used to create library-specific sources.
 * This is the template class which is extended for each specific library.  
 * This class is also used by the "No Sound" library to represent a mute 
 * source.  
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
public class Source
{
/**
 * The library class associated with this type of channel.
 */
    protected Class libraryType = Library.class;

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
 * Processes status messages, warnings, and error messages.
 */
    private SoundSystemLogger logger;
    
/**
 * True if this source is being directly fed with raw audio data.
 */
    public boolean rawDataStream = false;

/**
 * Format the raw data will be in if this is a Raw Data Stream source.
 */
    public AudioFormat rawDataFormat = null;

/**
 * Determines whether a source should be removed after it finishes playing.
 */
    public boolean temporary = false;
    
/**
 * Determines whether or not this is a priority source.  Priority sources will 
 * not be overwritten by other sources when there are no available channels.
 */
    public boolean priority = false;
    
/**
 * Whether or not this source should be streamed.
 */
    public boolean toStream = false;
    
/**
 * Whether this source should loop or only play once.
 */
    public boolean toLoop = false;
    
/**
 * Whether this source needs to be played (for example if it was playing and 
 * looping when it got culled).
 */
    public boolean toPlay = false;
    
/**
 * Unique name for this source.  More than one source can not have the same 
 * sourcename.
 */
    public String sourcename = "";
    
/**
 * The audio file which this source should play.
 */
    public FilenameURL filenameURL = null;
    
/**
 * This source's position in 3D space.
 */
    public Vector3D position;
    
/**
 * Attenuation model to use for this source.
 */
    public int attModel = 0;
    
/**
 * Either fade distance or rolloff factor, depending on the value of attModel.
 */
    public float distOrRoll = 0.0f;

/**
 * Source's velocity in world-space, for use in Doppler effect.
 */
    public Vector3D velocity;
    
/**
 * This source's volume (a float between 0.0 - 1.0).  This value is used 
 * internally for attenuation, and should not be used to manually change a 
 * source's volume.
 */
    public float gain = 1.0f;
    
/**
 * This value should be used to manually increase or decrease source volume.
 */
    public float sourceVolume = 1.0f;

/**
 * This value represents the source's pitch (float value between 0.5f - 2.0f).
 */
    protected float pitch = 1.0f;
    
/**
 * This source's distance from the listener.
 */
    public float distanceFromListener = 0.0f;
    
/**
 * Channel to play this source on.
 */
    public Channel channel = null;
    
/**
 * Holds the data used by normal sources.
 */
    public SoundBuffer soundBuffer = null;

/**
 * False when this source gets culled.
 */
    private boolean active = true;
    
/**
 * Whether or not this source has been stopped.
 */
    private boolean stopped = true;
    
/**
 * Whether or not this source has been paused.
 */
    private boolean paused = false;
    
/**
 * Codec used to read data for streaming sources.
 */
    protected ICodec codec = null;

/**
 * Codec used to read in some initial data from the next sound in the queue.
 */
    protected ICodec nextCodec = null;

/**
 * List of buffers to hold some initial data from the next sound in the queue.
 */
    protected LinkedList<SoundBuffer> nextBuffers = null;


/**
 * The list of files to stream when the current stream finishes.
 */
    protected LinkedList<FilenameURL> soundSequenceQueue = null;

/**
 * Ensures that only one thread accesses the soundSequenceQueue at a time.
 */
    protected final Object soundSequenceLock = new Object();
    
/**
 * Used by streaming sources to indicate whether or not the initial 
 * stream-buffers still need to be queued.
 */
    public boolean preLoad = false;

/**
 * Specifies the gain factor used for the fade-out effect, or -1 when
 * source is not currently fading out.
 */
    protected float fadeOutGain = -1.0f;

/**
 * Specifies the gain factor used for the fade-in effect, or 1 when
 * source is not currently fading in.
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
 * Constructor:  Creates a new source using the specified parameters.
 * @param priority Setting this to true will prevent other sounds from overriding this one.
 * @param toStream Setting this to true will create a streaming source.
 * @param toLoop Should this source loop, or play only once.
 * @param sourcename A unique identifier for this source.  Two sources may not use the same sourcename.
 * @param filenameURL The filename/URL of the sound file to play at this source.
 * @param soundBuffer Buffer containing audio data, or null if not loaded yet.
 * @param x X position for this source.
 * @param y Y position for this source.
 * @param z Z position for this source.
 * @param attModel Attenuation model to use.
 * @param distOrRoll Either the fading distance or rolloff factor, depending on the value of 'att'.
 * @param temporary Whether or not to remove this source after it finishes playing.
 */
    public Source( boolean priority, boolean toStream, boolean toLoop,
                   String sourcename, FilenameURL filenameURL,
                   SoundBuffer soundBuffer, float x, float y, float z,
                   int attModel, float distOrRoll, boolean temporary )
    {
        // grab a handle to the message logger:
        logger = SoundSystemConfig.getLogger();
        
        this.priority = priority;
        this.toStream = toStream;
        this.toLoop = toLoop;
        this.sourcename = sourcename;
        this.filenameURL = filenameURL;
        this.soundBuffer = soundBuffer;
        position = new Vector3D( x, y, z );
        this.attModel = attModel;
        this.distOrRoll = distOrRoll;
        this.velocity = new Vector3D( 0, 0, 0 );
        this.temporary = temporary;

        if( toStream && filenameURL != null )
            codec = SoundSystemConfig.getCodec( filenameURL.getFilename() );
    }
    
/**
 * Constructor:  Creates a new source matching the specified one.
 * @param old Source to copy information from.
 * @param soundBuffer Buffer containing audio data, or null if not loaded yet.
 */
    public Source( Source old, SoundBuffer soundBuffer )
    {
        // grab a handle to the message logger:
        logger = SoundSystemConfig.getLogger();
        
        priority = old.priority;
        toStream = old.toStream;
        toLoop = old.toLoop;
        sourcename = old.sourcename;
        filenameURL = old.filenameURL;
        position = old.position.clone();
        attModel = old.attModel;
        distOrRoll = old.distOrRoll;
        velocity = old.velocity.clone();
        temporary = old.temporary;
        
        sourceVolume = old.sourceVolume;

        rawDataStream = old.rawDataStream;
        rawDataFormat = old.rawDataFormat;
        
        this.soundBuffer = soundBuffer;

        if( toStream && filenameURL != null )
            codec = SoundSystemConfig.getCodec( filenameURL.getFilename() );
    }
    
/**
 * Constructor:  Creates a new streaming source that will be directly fed with
 * raw audio data.
 * @param audioFormat Format that the data will be in.
 * @param priority Setting this to true will prevent other sounds from overriding this one.
 * @param sourcename A unique identifier for this source.  Two sources may not use the same sourcename.
 * @param x X position for this source.
 * @param y Y position for this source.
 * @param z Z position for this source.
 * @param attModel Attenuation model to use.
 * @param distOrRoll Either the fading distance or rolloff factor, depending on the value of 'att'.
 */
    public Source( AudioFormat audioFormat, boolean priority, String sourcename,
                   float x, float y, float z, int attModel, float distOrRoll )
    {
        // grab a handle to the message logger:
        logger = SoundSystemConfig.getLogger();

        this.priority = priority;
        this.toStream = true;
        this.toLoop = false;
        this.sourcename = sourcename;
        this.filenameURL = null;
        this.soundBuffer = null;
        position = new Vector3D( x, y, z );
        this.attModel = attModel;
        this.distOrRoll = distOrRoll;
        this.velocity = new Vector3D( 0, 0, 0 );
        this.temporary = false;

        rawDataStream = true;
        rawDataFormat = audioFormat;
    }
/*  Override methods  */
    
/**
 * Shuts the source down and removes references to all instantiated objects.
 */
    public void cleanup()
    {
        if( codec != null )
            codec.cleanup();

        synchronized( soundSequenceLock )
        {
            if( soundSequenceQueue != null )
                soundSequenceQueue.clear();
            soundSequenceQueue = null;
        }
        
        sourcename = null;
        filenameURL = null;
        position = null;
        soundBuffer = null;
        codec = null;
    }

/**
 * If this is a streaming source, queues up the next sound to play when
 * the previous stream ends.  This method has no effect on non-streaming
 * sources.
 * @param filenameURL The filename/URL of the sound file to stream next.
 */
    public void queueSound( FilenameURL filenameURL )
    {
        if( !toStream )
        {
            errorMessage( "Method 'queueSound' may only be used for " +
                          "streaming and MIDI sources." );
            return;
        }
        if( filenameURL == null )
        {
            errorMessage( "File not specified in method 'queueSound'" );
            return;
        }

        synchronized( soundSequenceLock )
        {
            if( soundSequenceQueue == null )
                soundSequenceQueue = new LinkedList<FilenameURL>();
            soundSequenceQueue.add( filenameURL );
        }
    }

/**
 * Removes the first occurrence of the specified filename from the list of
 * sounds to play when the previous stream ends.  This method has no effect
 * on non-streaming sources.
 * @param filename Filename/identifier of a sound file to remove from the queue.
 */
    public void dequeueSound( String filename )
    {
        if( !toStream )
        {
            errorMessage( "Method 'dequeueSound' may only be used for " +
                          "streaming and MIDI sources." );
            return;
        }
        if( filename == null || filename.equals( "" ) )
        {
            errorMessage( "Filename not specified in method 'dequeueSound'" );
            return;
        }

        synchronized( soundSequenceLock )
        {
            if( soundSequenceQueue != null )
            {
                ListIterator<FilenameURL> i = soundSequenceQueue.listIterator();
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
 * Fades out the volume of whatever this source is currently playing, then
 * begins playing the specified filename at the source's previously assigned
 * volume level.  If the filename parameter is null or empty, the source will
 * simply fade out and stop.  The miliseconds parameter must be non-negative or
 * zero.  This method will remove anything that is currently in the list of
 * queued sounds that would have played next when the current sound finished
 * playing.  This method has no effect on non-streaming sources.
 * @param filenameURL Filename/URL of the sound file to play next, or null for none.
 * @param milis Number of miliseconds the fadeout should take.
 */
    public void fadeOut( FilenameURL filenameURL, long milis )
    {
        if( !toStream )
        {
            errorMessage( "Method 'fadeOut' may only be used for " +
                          "streaming and MIDI sources." );
            return;
        }
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

        synchronized( soundSequenceLock )
        {
            if( soundSequenceQueue != null )
                soundSequenceQueue.clear();

            if( filenameURL != null )
            {
                if( soundSequenceQueue == null )
                    soundSequenceQueue = new LinkedList<FilenameURL>();
                soundSequenceQueue.add( filenameURL );
            }
        }
    }
    
/**
 * Fades out the volume of whatever this source is currently playing, then
 * fades the volume back in playing the specified file.  Final volume after
 * fade-in completes will be equal to the source's previously assigned volume
 * level.  The filenameURL parameter may not be null or empty.  The miliseconds
 * parameters must be non-negative or zero.  This method will remove anything
 * that is currently in the list of queued sounds that would have played next
 * when the current sound finished playing.  This method has no effect on
 * non-streaming sources.
 * @param filenameURL Filename/URL of the sound file to play next, or null for none.
 * @param milisOut Number of miliseconds the fadeout should take.
 * @param milisIn Number of miliseconds the fadein should take.
 */
    public void fadeOutIn( FilenameURL filenameURL, long milisOut, long milisIn )
    {
        if( !toStream )
        {
            errorMessage( "Method 'fadeOutIn' may only be used for " +
                          "streaming and MIDI sources." );
            return;
        }
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

        synchronized( soundSequenceLock )
        {
            if( soundSequenceQueue == null )
                soundSequenceQueue = new LinkedList<FilenameURL>();
            soundSequenceQueue.clear();
            soundSequenceQueue.add( filenameURL );
        }
    }

/**
 * Resets this source's volume if it is fading out or in.  Returns true if this
 * source is currently in the process of fading out.  When fade-out completes,
 * this method transitions the source to the next sound in the sound sequence
 * queue if there is one.  This method has no effect on non-streaming sources.
 * @return True if this source is in the process of fading out.
 */
    public boolean checkFadeOut()
    {
        if( !toStream )
            return false;
        
        if( fadeOutGain == -1.0f && fadeInGain == 1.0f )
            return false;

        long currentTime = System.currentTimeMillis();
        long milisPast = currentTime - lastFadeCheck;
        lastFadeCheck = currentTime;

        if( fadeOutGain >= 0.0f )
        {
            if( fadeOutMilis == 0 )
            {
                fadeOutGain = -1.0f;
                fadeInGain = 0.0f;
                if( !incrementSoundSequence() )
                {
                    stop();
                }
                positionChanged();
                preLoad = true;
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
                    if( !incrementSoundSequence() )
                        stop();
                    positionChanged();
                    preLoad = true;
                    return false;
                }
            }
            positionChanged();
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
            positionChanged();
            return true;
        }
        return false;
    }

/**
 * Removes the next filename/URL from the sound sequence queue and assigns it to
 * this source.  This method has no effect on non-streaming sources.  This
 * method is used internally by SoundSystem, and it is unlikely that the user
 * will ever need to use it.
 * @return True if there was something in the queue.
 */
    public boolean incrementSoundSequence()
    {
        if( !toStream )
        {
            errorMessage( "Method 'incrementSoundSequence' may only be used " +
                          "for streaming and MIDI sources." );
            return false;
        }

        synchronized( soundSequenceLock )
        {
            if( soundSequenceQueue != null && soundSequenceQueue.size() > 0 )
            {
                filenameURL = soundSequenceQueue.remove( 0 );
                if( codec != null )
                    codec.cleanup();
                codec = SoundSystemConfig.getCodec( filenameURL.getFilename() );
                return true;
            }
        }
        return false;
    }

/**
 * Reads in initial buffers of data from the next sound in the sound sequence
 * queue, to reduce lag when the transition occurrs.  This method has no effect
 * on non-streaming sources.  This method is used internally by SoundSystem, and
 * it is unlikely that the user will ever need to use it.
 * @return False if there is nothing in the queue to read from.
 */
    public boolean readBuffersFromNextSoundInSequence()
    {
        if( !toStream )
        {
            errorMessage( "Method 'readBuffersFromNextSoundInSequence' may " +
                          "only be used for streaming sources." );
            return false;
        }

        synchronized( soundSequenceLock )
        {
            if( soundSequenceQueue != null && soundSequenceQueue.size() > 0 )
            {
                if( nextCodec != null )
                    nextCodec.cleanup();
                nextCodec = SoundSystemConfig.getCodec(
                                    soundSequenceQueue.get( 0 ).getFilename() );
                nextCodec.initialize( soundSequenceQueue.get( 0 ).getURL() );
                
                SoundBuffer buffer = null;
                for( int i = 0;
                     i < SoundSystemConfig.getNumberStreamingBuffers()
                         && !nextCodec.endOfStream();
                     i++ )
                {
                    buffer = nextCodec.read();
                    if( buffer != null )
                    {
                        if( nextBuffers == null )
                            nextBuffers = new LinkedList<SoundBuffer>();
                        nextBuffers.add( buffer );
                    }
                }
                return true;
            }
        }
        return false;
    }


/**
 * Returns the size of the sound sequence queue (if this is a streaming source).
 * @return Number of sounds left in the queue, or zero if none.
 */
    public int getSoundSequenceQueueSize()
    {
        if( soundSequenceQueue == null )
            return 0;
        return soundSequenceQueue.size();
    }

/**
 * Sets whether or not this source should be removed when it finishes playing.
 * @param tmp True or false.
 */
    public void setTemporary( boolean tmp )
    {
        temporary = tmp;
    }
    
/**
 * Called every time the listener's position or orientation changes.
 */
    public void listenerMoved()
    {}
    
/**
 * Moves the source to the specified position.
 * @param x X coordinate to move to.
 * @param y Y coordinate to move to.
 * @param z Z coordinate to move to.
 */
    public void setPosition( float x, float y, float z )
    {
        position.x = x;
        position.y = y;
        position.z = z;
    }
    
/**
 * Called every time the source changes position.
 */
    public void positionChanged()
    {}
    
/**
 * Sets whether or not this source is a priority source.  A priority source 
 * will not be overritten by another source if there are no channels available 
 * to play on.
 * @param pri True or false.
 */
    public void setPriority( boolean pri )
    {
        priority = pri;
    }
    
/**
 * Sets whether this source should loop or only play once.
 * @param lp True or false.
 */
    public void setLooping( boolean lp )
    {
        toLoop = lp;
    }
    
/**
 * Sets this source's attenuation model.
 * @param model Attenuation model to use.
 */
    public void setAttenuation( int model )
    {
        attModel = model;
    }

/**
 * Sets this source's fade distance or rolloff factor, depending on the 
 * attenuation model.
 * @param dr New value for fade distance or rolloff factor.
 */
    public void setDistOrRoll( float dr)
    {
        distOrRoll = dr;
    }

/**
 * Sets this source's velocity, for use in Doppler effect.
 * @param x Velocity along world x-axis.
 * @param y Velocity along world y-axis.
 * @param z Velocity along world z-axis.
 */
    public void setVelocity( float x, float y, float z )
    {
        this.velocity.x = x;
        this.velocity.y = y;
        this.velocity.z = z;
    }

/**
 * Returns the source's distance from the listener.
 * @return How far away the source is.
 */
    public float getDistanceFromListener()
    {
        return distanceFromListener;
    }
    
/**
 * Manually sets the specified source's pitch.
 * @param value A float value ( 0.5f - 2.0f ).
 */
    public void setPitch( float value )
    {
        float newPitch = value;
        if( newPitch < 0.5f )
            newPitch = 0.5f;
        else if( newPitch > 2.0f )
            newPitch = 2.0f;
        pitch = newPitch;
    }

/**
 * Returns the pitch of the specified source.
 * @return Float value representing the source pitch (0.5f - 2.0f).
 */
    public float getPitch()
    {
        return pitch;
    }

/**
 * Indicates whether or not this source's associated library requires some
 * codecs to reverse-order the audio data they generate.
 * @return True if audio data should be reverse-ordered.
 */
    public boolean reverseByteOrder()
    {
        return SoundSystemConfig.reverseByteOrder( libraryType );
    }

/**
 * Changes the sources peripheral information to match the supplied parameters. 
 * @param priority Setting this to true will prevent other sounds from overriding this one.
 * @param toStream Setting this to true will create a streaming source.
 * @param toLoop Should this source loop, or play only once.
 * @param sourcename A unique identifier for this source.  Two sources may not use the same sourcename.
 * @param filenameURL Filename/URL of the sound file to play at this source.
 * @param x X position for this source.
 * @param y Y position for this source.
 * @param z Z position for this source.
 * @param attModel Attenuation model to use.
 * @param distOrRoll Either the fading distance or rolloff factor, depending on the value of 'att'.
 * @param temporary Whether or not to remove this source after it finishes playing.
 */
    public void changeSource( boolean priority, boolean toStream,
                              boolean toLoop, String sourcename,
                              FilenameURL filenameURL, SoundBuffer soundBuffer,
                              float x, float y, float z, int attModel,
                              float distOrRoll, boolean temporary )
    {
        this.priority = priority;
        this.toStream = toStream;
        this.toLoop = toLoop;
        this.sourcename = sourcename;
        this.filenameURL = filenameURL;
        this.soundBuffer = soundBuffer;
        position.x = x;
        position.y = y;
        position.z = z;
        this.attModel = attModel;
        this.distOrRoll = distOrRoll;
        this.temporary = temporary;
    }

/**
 * Feeds raw data to the specified channel.
 * @param buffer Byte buffer containing raw audio data to stream.
 * @param c Channel to stream on.
 * @return Number of prior buffers that have been processed, or -1 if unable to queue the buffer (if the source was culled, for example).
 */
    public int feedRawAudioData( Channel c, byte[] buffer )
    {
        if( !active( GET, XXX) )
        {
            toPlay = true;
            return -1;
        }
        if( channel != c )
        {
            channel = c;
            channel.close();
            channel.setAudioFormat( rawDataFormat );
            positionChanged();
        }

        // change the state of this source to not stopped and not paused:
        stopped( SET, false );
        paused( SET, false );

        return channel.feedRawAudioData( buffer );
    }

/**
 * Plays the source on the specified channel.
 * @param c Channel to play on.
 */
    public void play( Channel c )
    {
        if( !active( GET, XXX) )
        {
            if( toLoop )
                toPlay = true;
            return;
        }
        if( channel != c )
        {
            channel = c;
            channel.close();
        }
        // change the state of this source to not stopped and not paused:
        stopped( SET, false );
        paused( SET, false );
    }    
/*  END Override methods  */
    
/**
 * Streams the source on its current channel
 * @return False when stream has finished playing.
 */
    public boolean stream()
    {
        if( channel == null )
            return false;

        if( preLoad )
        {
            if( rawDataStream )
                preLoad = false;
            else
                return preLoad();
        }

        if( rawDataStream )
        {
            if( stopped() || paused() )
                return true;
            if( channel.buffersProcessed() > 0 )
                channel.processBuffer();
            return true;
        }
        else
        {
            if( codec == null )
                return false;
            if( stopped() )
                return false;
            if( paused() )
                return true;

            int processed = channel.buffersProcessed();

            SoundBuffer buffer = null;
            for( int i = 0; i < processed; i++ )
            {
                buffer = codec.read();
                if( buffer != null )
                {
                    if( buffer.audioData != null )
                        channel.queueBuffer( buffer.audioData );
                    buffer.cleanup();
                    buffer = null;
                    return true;
                }
                else if( codec.endOfStream() )
                {
                    synchronized( soundSequenceLock )
                    {
                        if( SoundSystemConfig.getStreamQueueFormatsMatch() )
                        {
                            if( soundSequenceQueue != null &&
                                soundSequenceQueue.size() > 0 )
                            {
                                if( codec != null )
                                    codec.cleanup();
                                filenameURL = soundSequenceQueue.remove( 0 );
                                codec = SoundSystemConfig.getCodec(
                                                        filenameURL.getFilename() );
                                codec.initialize( filenameURL.getURL() );
                                buffer = codec.read();
                                if( buffer != null )
                                {
                                    if( buffer.audioData != null )
                                        channel.queueBuffer( buffer.audioData );
                                    buffer.cleanup();
                                    buffer = null;
                                    return true;
                                }
                            }
                            else if( toLoop )
                            {
                                codec.initialize( filenameURL.getURL() );
                                buffer = codec.read();
                                if( buffer != null )
                                {
                                    if( buffer.audioData != null )
                                        channel.queueBuffer( buffer.audioData );
                                    buffer.cleanup();
                                    buffer = null;
                                    return true;
                                }
                            }
                        }
                    }
                }
/*
                if( codec.endOfStream() )
                {
                    synchronized( soundSequenceLock )
                    {
                        if( SoundSystemConfig.getStreamQueueFormatsMatch() )
                        {
                            if( soundSequenceQueue != null &&
                                soundSequenceQueue.size() > 0 )
                            {
                                if( codec != null )
                                    codec.cleanup();
                                filenameURL = soundSequenceQueue.remove( 0 );
                                codec = SoundSystemConfig.getCodec(
                                                        filenameURL.getFilename() );
                                codec.initialize( filenameURL.getURL() );
                                return true;
                            }
                            else if( toLoop )
                            {
                                codec.initialize( filenameURL.getURL() );
                                buffer = codec.read();
                                if( buffer != null )
                                {
                                    if( buffer.audioData != null )
                                        channel.queueBuffer( buffer.audioData );
                                    buffer.cleanup();
                                    buffer = null;
                                }
                            }
                        }
                    }
                    return false;
                }
*/
            }
        }
        return false;
    }
    
/**
 * Queues up the initial stream-buffers for the stream.
 * @return False if the end of the stream was reached.
 */
    public boolean preLoad()
    {
        if( channel == null )
            return false;

        if( codec == null )
            return false;

        SoundBuffer buffer = null;

        boolean noNextBuffers = false;
        synchronized( soundSequenceLock )
        {
            if( nextBuffers == null || nextBuffers.isEmpty() )
                noNextBuffers = true;
        }

        if( nextCodec != null && !noNextBuffers )
        {
            codec = nextCodec;
            nextCodec = null;
            synchronized( soundSequenceLock )
            {
                while( !nextBuffers.isEmpty() )
                {
                    buffer = nextBuffers.remove( 0 );
                    if( buffer != null )
                    {
                        if( buffer.audioData != null )
                            channel.queueBuffer( buffer.audioData );
                        buffer.cleanup();
                        buffer = null;
                    }
                }
            }
        }
        else
        {
            nextCodec = null;
            URL url = filenameURL.getURL();

            codec.initialize( url );
            for( int i = 0; i < SoundSystemConfig.getNumberStreamingBuffers();
                 i++ )
            {
                buffer = codec.read();
                if( buffer != null )
                {
                    if( buffer.audioData != null )
                        channel.queueBuffer( buffer.audioData );
                    buffer.cleanup();
                    buffer = null;
                }
            }
        }

        return true;
    }
    
/**
 * Pauses the source.
 */
    public void pause()
    {
        toPlay = false;
        paused( SET, true );
        if( channel != null )
            channel.pause();
        else
            errorMessage( "Channel null in method 'pause'" );
    }
    
/**
 * Stops the source.
 */
    public void stop()
    {
        toPlay = false;
        stopped( SET, true );
        paused( SET, false );
        if( channel != null )
            channel.stop();
        else
            errorMessage( "Channel null in method 'stop'" );
    }
    
/**
 * Rewinds the source.  If the source was paused, then it is stopped.
 */
    public void rewind()
    {
        if( paused( GET, XXX ) )
        {
            stop();
        }
        if( channel != null )
        {
            boolean rePlay = playing();
            channel.rewind();
            if( toStream && rePlay )
            {
                stop();
                play( channel );
            }
        }
        else
            errorMessage( "Channel null in method 'rewind'" );
    }    
    
/**
 * Dequeues any previously queued data.
 */
    public void flush()
    {
        if( channel != null )
            channel.flush();
        else
            errorMessage( "Channel null in method 'flush'" );
    }
    
/**
 * Stops and flushes the source, and prevents it from being played again until 
 * the activate() is called.  
 */
    public void cull()
    {
        if( !active( GET, XXX ) )
            return;
        if( playing() && toLoop )
            toPlay = true;
        if( rawDataStream )
            toPlay = true;
        active( SET, false );
        if( channel != null )
            channel.close();
        channel = null;
    }
    
/**
 * Allows a previously culled source to be played again.
 */
    public void activate()
    {
        active( SET, true );
    }
    
/**
 * Returns false if the source has been culled.  
 * @return True or False
 */
    public boolean active()
    {
        return active( GET, XXX );
    }
    
/**
 * Returns true if the source is playing.  
 * @return True or False
 */
    public boolean playing()
    {
        if( channel == null || channel.attachedSource != this )
            return false;
        else if( paused() || stopped() )
            return false;
        else
            return channel.playing();
    }
    
/**
 * Returns true if the source has been stopped.  
 * @return True or False
 */
    public boolean stopped()
    {
        return stopped( GET, XXX );
    }
    
/**
 * Returns true if the source has been paused.  
 * @return True or False
 */
    public boolean paused()
    {
        return paused( GET, XXX );
    }
    
/**
 * Returns the number of miliseconds since the source began playing.
 * @return miliseconds, or -1 if not playing or unable to calculate
 */
    public float millisecondsPlayed()
    {
        if( channel == null )
            return( -1 );
        else
            return channel.millisecondsPlayed();
    }

/**
 * Sets or returns whether or not the source has been culled.  
 * @return True or False
 */
    private synchronized boolean active( boolean action, boolean value )
    {
        if( action == SET )
            active = value;
        return active;
    }
    
/**
 * Sets or returns whether or not the source has been stopped.  
 * @return True or False
 */
    private synchronized boolean stopped( boolean action, boolean value )
    {
        if( action == SET )
            stopped = value;
        return stopped;
    }
    
/**
 * Sets or returns whether or not the source has been paused.  
 * @return True or False
 */
    private synchronized boolean paused( boolean action, boolean value )
    {
        if( action == SET )
            paused = value;
        return paused;
    }
    
/**
 * Returns the name of the class.
 * @return SoundLibraryXXXX.
 */
    public String getClassName()
    {
        String libTitle = SoundSystemConfig.getLibraryTitle( libraryType );

        if( libTitle.equals( "No Sound" ) )
            return "Source";
        else
            return "Source" + libTitle;
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
