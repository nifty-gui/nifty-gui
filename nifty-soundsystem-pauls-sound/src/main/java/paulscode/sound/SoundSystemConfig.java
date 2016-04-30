package paulscode.sound;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ListIterator;
import java.util.LinkedList;

/**
 * The SoundSystemConfig class is used to access global sound system settings,
 * and to link with external pluggins. All members of this class are static.
 * SoundSystemConfig is sort of a "catch all" configuration class, so if you
 * are not sure where to find something in the SoundSystem library, this is
 * probably a good place to start.
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
public class SoundSystemConfig
{
//  GLOBAL THREAD SYNCHRONIZATION
/**
 * Lock object used to synchronize the three threads used by SoundSystem.
 * Synchronize on this anytime you manually manipulate a Source's properties.
 */
    public static final Object THREAD_SYNC = new Object();
//  END GLOBAL THREAD SYNCHRONIZATION

//  GLOBAL IDENTIFIERS
    
/**
 * A normal (non-streaming) source.  Also used to define a Channel type as 
 * normal.
 */
    public static final int TYPE_NORMAL          = 0;
/**
 * A streaming source.  Also used to define a Channel type as streaming.
 */
    public static final int TYPE_STREAMING       = 1;
    
/**
 * Global identifier for no attenuation.  Attenuation is how a source's volume 
 * fades with distance.  When there is no attenuation, a source's volume 
 * remains constaint regardles of distance.
 */
    public static final int ATTENUATION_NONE     = 0;  // no attenuation
/**
 * Global identifier for rolloff attenuation.  Rolloff attenuation is a 
 * realistic attenuation model, which uses a rolloff factor to determine how 
 * quickly a source fades with distance.  A smaller rolloff factor will fade at 
 * a further distance, and a rolloff factor of 0 will never fade.  NOTE: In 
 * OpenAL, rolloff attenuation only works for monotone sounds.
 */
    public static final int ATTENUATION_ROLLOFF  = 1;  // logrithmic attenuation
/**
 * Global identifier for linear attenuation.  Linear attenuation is less
 * realistic than rolloff attenuation, but it allows the user to specify a 
 * maximum "fade distance" where a source's volume becomes zero.
 */
    public static final int ATTENUATION_LINEAR   = 2;  // linear attenuation
    
/**
 * A Regular expression for determining if a file's extension is MIDI.
 */
    public static String EXTENSION_MIDI = ".*[mM][iI][dD][iI]?$";
    
/**
 * A Regular expression for determining if a path is an online URL.
 */
    public static String PREFIX_URL = "^[hH][tT][tT][pP]://.*";
    
//  END GLOBAL IDENTIFIERS
 
    
//  PRIVATE STATIC VARIABLES

/**
 * Handle to the message logger.  The default logger can be changed by 
 * overridding the {@link paulscode.sound.SoundSystemLogger SoundSystemLogger} 
 * class and calling the setLogger() method (must be done BEFORE instantiating 
 * the SoundSystem class!)
 */
    private static SoundSystemLogger logger = null;

/**
 * List of library types in their order of priority.
 */
    private static LinkedList<Class> libraries;

/**
 * List of codecs and the file formats they are associated with.
 */
    private static LinkedList<Codec> codecs = null;

/**
 * List of stream listeners.
 */
    private static LinkedList<IStreamListener> streamListeners = null;
/**
 * For synchronizing access to the streamListeners list.
 */
    private static final Object streamListenersLock = new Object();
    
/**
 * Maximum number of normal (non-streaming) channels that can be created.
 * NOTE: JavaSound may require the total number of channels (non-streaming + 
 * streaming) to be 32.
 */
    private static int numberNormalChannels = 28;
/**
 * Maximum number of streaming channels that can be created.
 * NOTE: JavaSound may require the total number of channels (non-streaming + 
 * streaming) to be 32.
 */
    private static int numberStreamingChannels = 4;
/**
 * Overall volume, affecting all sources.  Float value (0.0f - 1.0f).
 */
    private static float masterGain = 1.0f;
/**
 * Attenuation model to use if not specified.  Attenuation is how a source's  
 * volume fades with distance.
 */
    private static int defaultAttenuationModel = ATTENUATION_ROLLOFF;
/**
 * Default value to use for the rolloff factor if not specified.
 */
    private static float defaultRolloffFactor = 0.03f;
/**
 * Value to use for the doppler factor, for determining Doppler scale.
 */
    private static float dopplerFactor = 0.0f;
/**
 * Value to use for the doppler velocity.
 */
    private static float dopplerVelocity = 1.0f;
/**
 * Default value to use for fade distance if not specified.
 */
    private static float defaultFadeDistance = 1000.0f;
/**
 * Package where the sound files are located (must be followed by '/').
 */
    private static String soundFilesPackage = "Sounds/";
    
/**
 * Number of bytes to load at a time when streaming.
 */
    private static int streamingBufferSize = 131072;
/**
 * Number of buffers used for each streaming sorce.  Slow codecs may require
 * this number to be greater than 2 to prevent audio skipping during playback.
 */
    private static int numberStreamingBuffers = 3;
/**
 * Enables a transition-speed optimization by assuming all sounds in each
 * streaming source's queue will have exactly the same format once decoded
 * (including channels, sample rate, and sample size).  This is an advanced
 * setting which should only be changed by experienced developers.
 */
    private static boolean streamQueueFormatsMatch = false;
/**
 * The maximum number of bytes to read in for (non-streaming) files.  
 * Increase this value if non-streaming sounds are getting cut off.  
 * Decrease this value if large sound files are causing lag during load time.  
 */
    private static int maxFileSize = 268435456;
/**
 * Size of each chunk to read at a time for loading (non-streaming) files.
 * Increase if loading sound files is causing significant lag.
 */
    private static int fileChunkSize = 1048576;

/**
 * Indicates whether or not there is a codec for reading from MIDI files.  If
 * there is no codec for MIDI, then SoundSystem uses javax.sound.midi.
 */
    private static boolean midiCodec = false;

/**
 * MIDI device to try using as the Synthesizer.  May be the full name or part
 * of the name.  If this String is empty, the default Synthesizer will be used,
 * or one of the common alternate synthesizers if the default Synthesizer is
 * unavailable.
 */
    private static String overrideMIDISynthesizer = "";

//  END PRIVATE STATIC VARIABLES
    
// THESE TWO METHODS PROVIDE INFORMATION ABOUT THE INDIVIDUAL SOUND LIBRARIES
    
/**
 * Adds an entry to the list of library types.  This method has no effect if
 * the specified library type is already in the list of libraries.
 * NOTE: The parameterless constructor of the SoundSystem class will try to
 * load libraries in the order that they were entered into the list.
 * @param libraryClass Derivitive of class 'Library'.
*/
    public static void addLibrary( Class libraryClass )
                                                     throws SoundSystemException
    {
        if( libraryClass == null )
            throw new SoundSystemException(
                                        "Parameter null in method 'addLibrary'",
                                        SoundSystemException.NULL_PARAMETER );
        if( !Library.class.isAssignableFrom( libraryClass ) )
            throw new SoundSystemException( "The specified class does not " +
                              "extend class 'Library' in method 'addLibrary'" );

        if( libraries == null )
            libraries = new LinkedList<Class>();

        if( !libraries.contains( libraryClass ) )
            libraries.add( libraryClass );
    }
    
/**
 * Removes the specified library from the list of library types.
 * @param libraryClass Derivitive of class 'Library'.
*/
    public static void removeLibrary( Class libraryClass )
                                                     throws SoundSystemException
    {
        if( libraries == null || libraryClass == null )
            return;

        libraries.remove( libraryClass );
    }

/**
 * Returns the list of library types.
 * @return LinkedList of classes derived from 'Library', or null if none were specified.
*/
    public static LinkedList<Class> getLibraries()
    {
        return libraries;
    }
    
/**
 * Checks if the specified library class is compatible on the user's machine.
 * @param libraryClass Library type to check.
 * @return True or false.
*/
    public static boolean libraryCompatible( Class libraryClass )
    {
        if( libraryClass == null )
        {
            errorMessage( "Parameter 'libraryClass' null in method" +
                          "'librayCompatible'" );
            return false;
        }
        if( !Library.class.isAssignableFrom( libraryClass ) )
        {
            errorMessage( "The specified class does not extend class " +
                          "'Library' in method 'libraryCompatible'" );
            return false;
        }

        Object o = runMethod( libraryClass, "libraryCompatible",
                                     new Class[0], new Object[0] );

        if( o == null )
        {
            errorMessage( "Method 'Library.libraryCompatible' returned " +
                          "'null' in method 'libraryCompatible'" );
            return false;
        }

        return( ( (Boolean) o ).booleanValue() );
    }

/**
 * Return the short title of the specified library, or null if error.
 * @param libraryClass Derivitive of class 'Library'.
 * @return String containing the library title.
*/
    public static String getLibraryTitle( Class libraryClass )
    {
        if( libraryClass == null )
        {
            errorMessage( "Parameter 'libraryClass' null in method" +
                          "'getLibrayTitle'" );
            return null;
        }
        if( !Library.class.isAssignableFrom( libraryClass ) )
        {
            errorMessage( "The specified class does not extend class " +
                          "'Library' in method 'getLibraryTitle'" );
            return null;
        }

        Object o = runMethod( libraryClass, "getTitle", new Class[0],
                                     new Object[0] );
        if( o == null )
        {
            errorMessage( "Method 'Library.getTitle' returned " +
                          "'null' in method 'getLibraryTitle'" );
            return null;
        }

        return( (String) o );
    }

/**
 * Return the longer description of the specified library, or null if error.
 * @param libraryClass Derivitive of class 'Library'.
 * @return String containing the library title.
*/
    public static String getLibraryDescription( Class libraryClass )
    {
        if( libraryClass == null )
        {
            errorMessage( "Parameter 'libraryClass' null in method" +
                          "'getLibrayDescription'" );
            return null;
        }
        if( !Library.class.isAssignableFrom( libraryClass ) )
        {
            errorMessage( "The specified class does not extend class " +
                          "'Library' in method 'getLibraryDescription'" );
            return null;
        }
        
        Object o = runMethod( libraryClass, "getDescription",
                                     new Class[0], new Object[0] );
        if( o == null )
        {
            errorMessage( "Method 'Library.getDescription' returned " +
                          "'null' in method 'getLibraryDescription'" );
            return null;
        }

        return( (String) o );
    }

/**
 * Return whether or not requires reversal of audio data byte-order.
 * @param libraryClass Derivitive of class 'Library'.
 * @return True if byte-order reversal is required.
*/
    public static boolean reverseByteOrder( Class libraryClass )
    {
        if( libraryClass == null )
        {
            errorMessage( "Parameter 'libraryClass' null in method" +
                          "'reverseByteOrder'" );
            return false;
        }
        if( !Library.class.isAssignableFrom( libraryClass ) )
        {
            errorMessage( "The specified class does not extend class " +
                          "'Library' in method 'reverseByteOrder'" );
            return false;
        }

        Object o = runMethod( libraryClass, "reversByteOrder",
                                     new Class[0], new Object[0] );
        if( o == null )
        {
            errorMessage( "Method 'Library.reverseByteOrder' returned " +
                          "'null' in method 'getLibraryDescription'" );
            return false;
        }

        return( ((Boolean) o).booleanValue() );
    }

// END LIBRARY INFORMATION

// Use the following methods to interface the private variables above:
    
// STATIC NONSYNCHRONIZED INTERFACE METHODS
/**
 * Changes the message logger to use for handling status messages, warnings, 
 * and error messages.  This method should only be called BEFORE instantiating 
 * the SoundSystem class!  If this method is called after the SoundSystem has 
 * been created, there will be handles floating around to two different 
 * loggers, and the results will be undesirable.  This method can be used to 
 * change how messages are handled.  First, the 
 * {@link paulscode.sound.SoundSystemLogger SoundSystemLogger} class should be 
 * extended and methods overriden to change how messages are handled.  Then, 
 * the overridden class should be instantiated, and a call made to 
 * SoundSystemConfig.setLogger() before creating the SoundSystem object.  
 * If an alternate logger is not set by the user before the SoundSystem is 
 * instantiated, then an instance of the base SoundSystemLogger class will be 
 * used by default.
 * @param l Handle to a message logger.
 */
    public static void setLogger( SoundSystemLogger l )
    {
        logger = l;
    }
/**
 * Returns a handle to the message logger.
 * @return The current message logger.
 */
    public static SoundSystemLogger getLogger()
    {
        return logger;
    }
    
//  STATIC SYNCHRONIZED INTERFACE METHODS
    
/**
 * Sets the maximum number of normal (non-streaming) channels that can be 
 * created.  Streaming channels are created first, so the higher the maximum 
 * number of streaming channels is set, the fewer non-streaming channels will 
 * be available.  If unable to create the number of channels specified, 
 * SoundSystem will create as many as possible.
 * NOTE: Some sound library pluggins may require the total number of channels
 * (non-streaming + streaming) to be 32.
 * @param number How many normal audio channels.
 */
    public static synchronized void setNumberNormalChannels( int number )
    {
        numberNormalChannels = number;
    }
    
/**
 * Returns the maximum number of normal (non-streaming) channels that can be 
 * created.
 * @return Maximum non-streaming channels.
 */
    public static synchronized int getNumberNormalChannels()
    {
        return numberNormalChannels;
    }
    
/**
 * Sets the maximum number of streaming channels that can be created.  
 * Streaming channels are created first, so the higher the maximum number of 
 * streaming channels is set, the fewer non-streaming channels will 
 * be available.  If unable to create the number of channels specified, 
 * SoundSystem will create as many as possible.
 * NOTE: Some sound library pluggins may require the total number of channels
 * (non-streaming + streaming) to be 32.
 * @param number How many streaming audio channels.
 */
    public static synchronized void setNumberStreamingChannels( int number )
    {
        numberStreamingChannels = number;
    }
    
/**
 * Returns the maximum number of streaming channels that can be created.
 * @return Maximum streaming channels.
 */
    public static synchronized int getNumberStreamingChannels()
    {
        return numberStreamingChannels;
    }
    
/**
 * Sets the varriable used for overall volume, affecting all sources.
 * @param value Float value (0.0f - 1.0f).
 */
    public static synchronized void setMasterGain( float value )
    {
        masterGain = value;
    }
    
/**
 * Returns the value for the overall volume.
 * @return A float value (0.0f - 1.0f).
 */
    public static synchronized float getMasterGain()
    {
        return masterGain;
    }

/**
 * Sets the default attenuation model to use when one is not specified. 
 * Attenuation is how a source's volume fades with distance.
 * @param model A global attenuation model identifier.
 */
    public static synchronized void setDefaultAttenuation( int model )
    {
        defaultAttenuationModel = model;
    }
/**
 * Returns the default attenuation model used when one is not specified.
 * @return A global attenuation model identifier
 */
    public static synchronized int getDefaultAttenuation()
    {
        return defaultAttenuationModel;
    }
/**
 * Sets the default rolloff factor to use when one is not specified.
 * @param rolloff Rolloff factor.
 */
    public static synchronized void setDefaultRolloff( float rolloff )
    {
        defaultRolloffFactor = rolloff;
    }
/**
 * Returns the doppler factor, for determining Doppler Effect scale.
 * @return Doppler factor
 */
    public static synchronized float getDopplerFactor()
    {
        return dopplerFactor;
    }
/**
 * Sets the doppler factor, for determining Doppler Effect scale.  Use this
 * method BEFORE instantiating the SoundSystem.  To change the Doppler factor
 * after the SoundSystem is instantiated, use the
 * SoundSystem.changeDopplerFactor method instead.
 * @param factor Doppler factor.
 */
    public static synchronized void setDopplerFactor( float factor )
    {
        dopplerFactor = factor;
    }
/**
 * Returns the Doppler Velocity, for use in Doppler Effect.
 * @return Doppler velocity.
 */
    public static synchronized float getDopplerVelocity()
    {
        return dopplerVelocity;
    }
/**
 * Sets the Doppler velocity, for use in Doppler Effect.  Use this method
 * BEFORE instantiating the SoundSystem.  To change the Doppler velocity after
 * the SoundSystem is instantiated, use the SoundSystem.changeDopplerVelocity
 * method instead.
 * @param velocity Doppler velocity.
 */
    public static synchronized void setDopplerVelocity( float velocity )
    {
        dopplerVelocity = velocity;
    }
/**
 * Returns the default rolloff factor used when one is not specified.
 * @return Default rolloff factor
 */
    public static synchronized float getDefaultRolloff()
    {
        return defaultRolloffFactor;
    }
/**
 * Sets the default fade distance to use when one is not specified.
 * @param distance Fade Distance.
 */
    public static synchronized void setDefaultFadeDistance( float distance )
    {
        defaultFadeDistance = distance;
    }
/**
 * Returns the default fade distance used when one is not specified.
 * @return Default fade distance
 */
    public static synchronized float getDefaultFadeDistance()
    {
        return defaultFadeDistance;
    }
/**
 * Sets the package where sound files are located.
 * @param location Path to the sound files location (must be followed by '/').
 */
    public static synchronized void setSoundFilesPackage( String location )
    {
        soundFilesPackage = location;
    }
/**
 * Returns the package where sound files are located.
 * @return Path to the sound files location
 */
    public static synchronized String getSoundFilesPackage()
    {
        return soundFilesPackage;
    }
/**
 * Sets the number of bytes to load at a time when streaming.
 * @param size Size in bytes.
 */
    public static synchronized void setStreamingBufferSize( int size )
    {
        streamingBufferSize = size;
    }
/**
 * Returns the number of bytes to load at a time when streaming.
 * @return Size in bytes.
 */
    public static synchronized int getStreamingBufferSize()
    {
        return streamingBufferSize;
    }
/**
 * Sets the number of buffers used for each streaming sorce.
 * Slow codecs may require this number to be greater than 2 to prevent audio
 * skipping during playback.
 * @param num How many buffers.
 */
    public static synchronized void setNumberStreamingBuffers( int num )
    {
        numberStreamingBuffers = num;
    }
/**
 * Returns the number of buffers used for each streaming sorce.
 * @return How many buffers.
 */
    public static synchronized int getNumberStreamingBuffers()
    {
        return numberStreamingBuffers;
    }

/**
 * Enables a transition-speed optimization by assuming all sounds in each
 * streaming source's queue will have exactly the same format once decoded
 * (including channels, sample rate, and sample size).  This is an advanced
 * setting which should only be changed by experienced developers.
 * @param val False by default.
 */
    public static synchronized void setStreamQueueFormatsMatch( boolean val )
    {
        streamQueueFormatsMatch = val;
    }

/**
 * Returns whether or not all sounds in each streaming source's queue will be
 * handled as if they have exactly the same format once decoded (including
 * channels, sample rate, and sample size).  This is an advanced setting which
 * should only be changed by experienced developers.
 * @return Normally false.
 */
    public static synchronized boolean getStreamQueueFormatsMatch()
    {
        return streamQueueFormatsMatch;
    }

/**
 * Sets the maximum number of bytes to read in for (non-streaming) files.  
 * Increase this value if non-streaming sounds are getting cut off.  
 * Decrease this value if large sound files are causing lag during load time.  
 * @param size Size in bytes.
 */
    public static synchronized void setMaxFileSize( int size )
    {
        maxFileSize = size;
    }
/**
 * Returns the maximum number of bytes to read in for (non-streaming) files.  
 * @return Size in bytes.
 */
    public static synchronized int getMaxFileSize()
    {
        return maxFileSize;
    }
/**
 * Sets the size of each chunk to read at a time for loading (non-streaming)
 * files.  Increase if loading sound files is causing significant lag.
 * @param size Size in bytes.
 */
    public static synchronized void setFileChunkSize( int size )
    {
        fileChunkSize = size;
    }
/**
 * Returns the size of each chunk to read at a time for loading (non-streaming)
 * files.
 * @return Size in bytes.
 */
    public static synchronized int getFileChunkSize()
    {
        return fileChunkSize;
    }
/**
 * Returns the name of the MIDI synthesizer to use instead of the default, or
 * empty string if none was specified.
 * @return All or part of a MIDI device name, or empty string for not specified.
 */
    public static synchronized String getOverrideMIDISynthesizer()
    {
        return overrideMIDISynthesizer;
    }
/**
 * Sets the name of the MIDI synthesizer to use instead of the default.  If
 * 'name' is an empty string, the default Synthesizer will be used, or one of
 * the common alternate synthesizers if the default Synthesizer is unavailable.
 * @param name All or part of the MIDI device name.
 */
    public static synchronized void setOverrideMIDISynthesizer( String name )
    {
        overrideMIDISynthesizer = name;
    }
/**
 * Uses the specified file extension to associate a particular file format
 * with the codec used to read audio data from it.
 * @param extension File extension to be associated with the specified codec.
 * @param iCodecClass Codec type to use for files with the specified extension.
 */
    public static synchronized void setCodec( String extension,
                                              Class iCodecClass )
                                                     throws SoundSystemException
    {
        if( extension == null )
            throw new SoundSystemException( "Parameter 'extension' null in " +
                                          "method 'setCodec'.",
                                          SoundSystemException.NULL_PARAMETER );
        if( iCodecClass == null )
            throw new SoundSystemException( "Parameter 'iCodecClass' null in " +
                                          "method 'setCodec'.",
                                          SoundSystemException.NULL_PARAMETER );
        if( !ICodec.class.isAssignableFrom( iCodecClass ) )
            throw new SoundSystemException( "The specified class does " +
                        "not implement interface 'ICodec' in method 'setCodec'",
                        SoundSystemException.CLASS_TYPE_MISMATCH );

        if( codecs == null )
            codecs = new LinkedList<Codec>();
        
        ListIterator<Codec> i = codecs.listIterator();
        Codec codec;
        
        while( i.hasNext() )
        {
            codec = i.next();
            if( extension.matches( codec.extensionRegX ) )
                i.remove();
        }
        codecs.add( new Codec( extension, iCodecClass ) );

        // Let SoundSystem know if this is a MIDI codec, so it won't use
        // javax.sound.midi anymore:
        if( extension.matches( EXTENSION_MIDI ) )
            midiCodec = true;
    }
/**
 * Returns the codec that can be used to read audio data from the specified
 * file.
 * @param filename File to get a codec for.
 * @return Codec to use for reading audio data.
 */
    public static synchronized ICodec getCodec( String filename )
    {
        if( codecs == null )
            return null;

        ListIterator<Codec> i = codecs.listIterator();
        Codec codec;

        while( i.hasNext() )
        {
            codec = i.next();
            if( filename.matches( codec.extensionRegX ) )
                return codec.getInstance();
        }

        return null;
    }

/**
 * Indicates whether or not there is a codec for reading from MIDI files.  If
 * there is no codec for MIDI, then SoundSystem uses javax.sound.midi.
 * @return True if there the user defined a MIDI codec.
 */
    public static boolean midiCodec()
    {
        return midiCodec;
    }

/**
 * Adds an entry to the list of stream listeners.  If the instance is already
 * in the list, the command is ignored.
 * @param streamListener Implementation of interface 'IStreamListener'.
*/
    public static void addStreamListener( IStreamListener streamListener )
    {
        synchronized( streamListenersLock )
        {
            if( streamListeners == null )
                streamListeners = new LinkedList<IStreamListener>();

            if( !streamListeners.contains( streamListener ) )
                streamListeners.add( streamListener );
        }
    }

/**
 * Removes an entry from the list of stream listeners.
 * @param streamListener Implementation of interface 'IStreamListener'.
*/
    public static void removeStreamListener( IStreamListener streamListener )
    {

        synchronized( streamListenersLock )
        {
            if( streamListeners == null )
                streamListeners = new LinkedList<IStreamListener>();

            if( streamListeners.contains( streamListener ) )
                streamListeners.remove( streamListener );
        }
    }

/**
 * Notifies all stream listeners that an End Of Stream was reached.  If there
 * are no listeners, the command is ignored.
 * @param sourcename String identifier of the source which reached the EOS.
 * @param queueSize Number of items left the the stream's play queue, or zero if none.
*/
    public static void notifyEOS( String sourcename, int queueSize )
    {
        synchronized( streamListenersLock )
        {
            if( streamListeners == null )
                return;
        }
        final String srcName = sourcename;
        final int qSize = queueSize;

        new Thread()
        {
            @Override
            public void run()
            {
                synchronized( streamListenersLock )
                {
                    if( streamListeners == null )
                        return;
                    ListIterator<IStreamListener> i = streamListeners.listIterator();
                    IStreamListener streamListener;
                    while( i.hasNext() )
                    {
                        streamListener = i.next();
                        if( streamListener == null )
                            i.remove();
                        else
                            streamListener.endOfStream( srcName, qSize );
                    }
                }
            }
        }.start();
    }

//  END STATIC SYNCHRONIZED INTERFACE METHODS


//  PRIVATE INTERNAL METHODS

/**
 * Display the specified error message using the current logger.
 * @param message Error message to display.
*/
    private static void errorMessage( String message )
    {
        if( logger != null )
            logger.errorMessage( "SoundSystemConfig", message, 0 );
    }

    // We don't know what Class parameter 'c' is, so we will ignore the 
    // warning message "unchecked call to getMethod".
    @SuppressWarnings("unchecked")
/**
 * Returns the results of calling the specified method from the specified 
 * class using the specified parameters.
 * @param c Class to call the method on.
 * @param method Name of the method.
 * @param paramTypes Data types of the parameters being passed to the method.
 * @param params Actual parameters to pass to the method.
 * @return Specified method's return value, or null if error or void.
*/
    private static Object runMethod( Class c, String method, Class[] paramTypes,
                                     Object[] params )
    {
        Method m = null;
        try
        {
            m = c.getMethod( method, paramTypes );  // <--- generates a warning
        }
        catch( NoSuchMethodException nsme )
        {
            errorMessage( "NoSuchMethodException thrown when attempting " +
                          "to call method '" + method + "' in " +
                          "method 'runMethod'" );
            return null;
        }
        catch( SecurityException se )
        {
            errorMessage( "Access denied when attempting to call method '" +
                          method + "' in method 'runMethod'" );
            return null;
        }
        catch( NullPointerException npe )
        {
            errorMessage( "NullPointerException thrown when attempting " +
                          "to call method '" + method + "' in " +
                          "method 'runMethod'" );
            return null;
        }
        if( m == null )
        {
            errorMessage( "Method '" + method + "' not found for the class " +
                          "specified in method 'runMethod'" );
            return null;
        }

        Object o = null;
        try
        {
            o = m.invoke( null, params );
        }
        catch( IllegalAccessException iae )
        {
            errorMessage( "IllegalAccessException thrown when attempting " +
                          "to invoke method '" + method + "' in " +
                          "method 'runMethod'" );
            return null;
        }
        catch( IllegalArgumentException iae )
        {
            errorMessage( "IllegalArgumentException thrown when attempting " +
                          "to invoke method '" + method + "' in " +
                          "method 'runMethod'" );
            return null;
        }
        catch( InvocationTargetException ite )
        {
            errorMessage( "InvocationTargetException thrown while attempting " +
                          "to invoke method 'Library.getTitle' in " +
                          "method 'getLibraryTitle'" );
            return null;
        }
        catch( NullPointerException npe )
        {
            errorMessage( "NullPointerException thrown when attempting " +
                          "to invoke method '" + method + "' in " +
                          "method 'runMethod'" );
            return null;
        }
        catch( ExceptionInInitializerError eiie )
        {
            errorMessage( "ExceptionInInitializerError thrown when " +
                          "attempting to invoke method '" + method + "' in " +
                          "method 'runMethod'" );
            return null;
        }

        return( o );
    }

//  END PRIVATE INTERNAL METHODS


//  PRIVATE INTERNAL CLASSES

/**
 * The Codec class is used to associate individual file formats with the
 * codecs used to load audio data from them.
 *
 * Author: Paul Lamb
 */
    private static class Codec
    {
/**
 * A regular expression used to match a file's extension.  This is used to
 * determine the file format.
 */
        public String extensionRegX;
/**
 * Codec used to load audio data from this file format.
 */
        public Class iCodecClass;
/**
 * Constructor:  Converts the specified extension string into a regular
 * expression, and associates that with the specified codec.
 * @param extension File extension to be associated with the specified codec.
 * @param iCodec Codec to use for files with the specified extension.
 */
        public Codec( String extension, Class iCodecClass )
        {
            extensionRegX = "";
            // Make sure an extension was specified:
            if( extension != null && extension.length() > 0 )
            {
                // We are only interested in the file extension.  The filename
                // can begin with whatever:
                extensionRegX = ".*";
                String c;
                for( int x = 0; x < extension.length(); x++ )
                {
                    // Each character could be either upper or lower case:
                    c = extension.substring( x, x + 1 );
                    extensionRegX += "[" + c.toLowerCase( Locale.ENGLISH )
                                     + c.toUpperCase( Locale.ENGLISH ) + "]";
                }
                // The extension will be at the end of the filename:
                extensionRegX += "$";
            }
            // remember the codec to use for this format:
            this.iCodecClass = iCodecClass;
        }

        public ICodec getInstance()
        {
            if( iCodecClass == null )
                return null;

            Object o = null;
            try
            {
                o = iCodecClass.newInstance();
            }
            catch( InstantiationException ie )
            {
                instantiationErrorMessage();
                return null;
            }
            catch( IllegalAccessException iae )
            {
                instantiationErrorMessage();
                return null;
            }
            catch( ExceptionInInitializerError eiie )
            {
                instantiationErrorMessage();
                return null;
            }
            catch( SecurityException se )
            {
                instantiationErrorMessage();
                return null;
            }


            if( o == null )
            {
                instantiationErrorMessage();
                return null;
            }

            return (ICodec) o;
        }

        private void instantiationErrorMessage()
        {
            errorMessage( "Unrecognized ICodec implementation in method " +
                          "'getInstance'.  Ensure that the implementing " +
                          "class has one public, parameterless constructor." );
        }
    }
//  END PRIVATE INTERNAL CLASSES
}
