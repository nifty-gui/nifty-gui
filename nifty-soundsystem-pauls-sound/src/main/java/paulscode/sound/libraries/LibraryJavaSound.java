package paulscode.sound.libraries;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;

import paulscode.sound.Channel;
import paulscode.sound.FilenameURL;
import paulscode.sound.ICodec;
import paulscode.sound.Library;
import paulscode.sound.Source;
import paulscode.sound.SoundBuffer;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;

/**
 * The LibraryJavaSound class interfaces the JavaSound library.  
 * For more information about the JavaSound API, visit 
 * http://java.sun.com/products/java-media/sound/ 
 *<br><br>
 *<b><i>    SoundSystem LibraryJavaSound License:</b></i><br><b><br>
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
public class LibraryJavaSound extends Library
{
/**
 * Used to return a current value from one of the synchronized interface
 * methods.
 */
    private static final boolean GET = false;

/**
 * Used to set the value in one of the synchronized interface methods.
 */
    private static final boolean SET = true;

/**
 * Null parameter for one the synchronized interface methods.
 */
    private static final int XXX = 0;

/**
 * The maximum safe size for a JavaSound clip.
 */
    private final int maxClipSize = 1048576;
    
/**
 * Mixes all the playing sources.
 */
    private static Mixer myMixer = null;

/**
 * Contains information on capabilities supported by the mixer.
 */
    private static MixerRanking myMixerRanking = null;

/**
 * Handle to the LibraryJavaSound instance.
 */
    private static LibraryJavaSound instance = null;

/**
 * Preferred minimum sample rate.
 */
    private static int minSampleRate = 4000;

/**
 * Preferred maximum sample rate.
 */
    private static int maxSampleRate = 48000;

/**
 * Preferred maximum number of output lines.
 */
    private static int lineCount = 32;

/**
 * Whether or not to use the gain control.
 */
    private static boolean useGainControl = true;

/**
 * Whether or not to use the pan control.
 */
    private static boolean usePanControl = true;

/**
 * Whether or not to use the sample rate control.
 */
    private static boolean useSampleRateControl = true;

    
/**
 * Constructor: Instantiates the source map, buffer map and listener 
 * information.  Also sets the library type to 
 * SoundSystemConfig.LIBRARY_JAVASOUND
 */
    public LibraryJavaSound() throws SoundSystemException
    {
        super();
        instance = this;
    }
    
 /**
 * Initializes Javasound.
 */
    @Override
    public void init() throws SoundSystemException
    {
        MixerRanking mixerRanker = null;
        // Check if a mixer has already been defined:
        if( myMixer == null )
        {
            // Nope, try the default Java Sound mixer first:
            for( Mixer.Info mixerInfo : AudioSystem.getMixerInfo() )
            {
                if( mixerInfo.getName().equals( "Java Sound Audio Engine" ) )
                {
                    // Found it, make sure it measures up to standards
                    mixerRanker = new MixerRanking();
                    try
                    {
                        mixerRanker.rank( mixerInfo );
                    }
                    catch( LibraryJavaSound.Exception ljse )
                    {
                        // Serious problem, don't use it!
                        break;
                    }
                    if( mixerRanker.rank < 14 )
                        break;  // Minor problem, see if there is a better mixer
                    // It's a good mixer, let's use it:
                    myMixer = AudioSystem.getMixer( mixerInfo );
                    mixerRanking( SET, mixerRanker );
                    break;
                }
            }
            // See if we have a mixer yet:
            if( myMixer == null )
            {
                // Nope, rank all the available mixers
                MixerRanking bestRankedMixer = mixerRanker;
                for( Mixer.Info mixerInfo : AudioSystem.getMixerInfo() )
                {
                    mixerRanker = new MixerRanking();
                    try
                    {
                        // See how good it is
                        mixerRanker.rank( mixerInfo );
                    }
                    catch( LibraryJavaSound.Exception ljse )
                    {}
                    // If this one is better, save it:
                    if( bestRankedMixer == null || mixerRanker.rank >
                                                          bestRankedMixer.rank )
                        bestRankedMixer = mixerRanker;
                }
                // Check if didn't find any useable mixers at all:
                if( bestRankedMixer == null )
                    throw new LibraryJavaSound.Exception( "No useable mixers " +
                                                "found!" , new MixerRanking() );
                try
                {
                    // Use the best available mixer
                    myMixer = AudioSystem.getMixer( bestRankedMixer.mixerInfo );
                    mixerRanking( SET, bestRankedMixer );
                }
                catch( java.lang.Exception e )
                {
                    // Why did we arive here?  Better be prepared for anything
                    throw new LibraryJavaSound.Exception( "No useable mixers " +
                                            "available!" , new MixerRanking() );
                }
            }
        }

        // Start out at full volume:
        setMasterVolume( 1.0f );
        
        // Let the user know if everything is ok:
        message( "JavaSound initialized." );
        
        super.init();
    }

/**
 * Checks if the JavaSound library type is compatible.
 * @return True or false.
 */
    public static boolean libraryCompatible()
    {
        // No real "loading" for the JavaSound library, just grab the Mixer:
        for( Mixer.Info mixerInfo : AudioSystem.getMixerInfo() )
        {
            if( mixerInfo.getName().equals( "Java Sound Audio Engine" ) )
                return true;
        }
        return false;
    }

/**
 * Creates a new channel of the specified type (normal or streaming).  Possible 
 * values for channel type can be found in the 
 * {@link paulscode.sound.SoundSystemConfig SoundSystemConfig} class.
 * @param type Type of channel.
 */
    @Override
    protected Channel createChannel( int type )
    {
        return new ChannelJavaSound( type, myMixer );
    }
    
/**
 * Stops all sources, and removes references to all instantiated objects.
 */
    @Override
    public void cleanup()
    {
        super.cleanup();
        instance = null;
        myMixer = null;
        myMixerRanking = null;
    }
    
/**
 * Pre-loads a sound into memory.
 * @param filenameURL Filename/URL of a sound file to load.
 * @return True if the sound loaded properly.
 */
    @Override
    public boolean loadSound( FilenameURL filenameURL )
    {
        // Make sure the buffer map exists:
        if( bufferMap == null )
        {
            bufferMap = new HashMap<String, SoundBuffer>();
            importantMessage( "Buffer Map was null in method 'loadSound'" );
        }
        
        // make sure they gave us a filename:
        if( errorCheck( filenameURL == null,
                          "Filename/URL not specified in method 'loadSound'" ) )
            return false;
        
        // check if it is already loaded:        
        if( bufferMap.get( filenameURL.getFilename() ) != null )
            return true;
        
        ICodec codec = SoundSystemConfig.getCodec( filenameURL.getFilename() );
        if( errorCheck( codec == null, "No codec found for file '" +
                                       filenameURL.getFilename() +
                                       "' in method 'loadSound'" ) )
            return false;
        URL url = filenameURL.getURL();

        if( errorCheck( url == null, "Unable to open file '" +
                                     filenameURL.getFilename() +
                                     "' in method 'loadSound'" ) )
            return false;

        codec.initialize( url );
        SoundBuffer buffer = codec.readAll();
        codec.cleanup();
        codec = null;
        if( buffer != null )
            bufferMap.put( filenameURL.getFilename(), buffer );
        else
            errorMessage( "Sound buffer null in method 'loadSound'" );
        
        return true;
    }

/**
 * Saves the specified sample data, under the specified identifier.  This
 * identifier can be later used in place of 'filename' parameters to reference
 * the sample data.
 * @param buffer the sample data and audio format to save.
 * @param identifier What to call the sample.
 * @return True if there weren't any problems.
 */
    @Override
    public boolean loadSound( SoundBuffer buffer, String identifier )
    {
        // Make sure the buffer map exists:
        if( bufferMap == null )
        {
            bufferMap = new HashMap<String, SoundBuffer>();
            importantMessage( "Buffer Map was null in method 'loadSound'" );
        }

        // make sure they gave us an identifier:
        if( errorCheck(identifier == null,
                          "Identifier not specified in method 'loadSound'" ) )
            return false;

        // check if it is already loaded:
        if( bufferMap.get( identifier ) != null )
            return true;

        // save it for later:
        if( buffer != null )
            bufferMap.put( identifier, buffer );
        else
            errorMessage( "Sound buffer null in method 'loadSound'" );

        return true;
    }
    
 /**
 * Sets the overall volume to the specified value, affecting all sources.
 * @param value New volume, float value ( 0.0f - 1.0f ).
 */ 
    @Override
    public void setMasterVolume( float value )
    {
        super.setMasterVolume( value );
        
        Set<String> keys = sourceMap.keySet();
        Iterator<String> iter = keys.iterator();        
        String sourcename;
        Source source;
        
        // loop through and update the volume of all sources:
        while( iter.hasNext() )
        {
            sourcename = iter.next();
            source = sourceMap.get( sourcename );
            if( source != null )
                source.positionChanged();
        }
    }
    
/**
 * Creates a new source and places it into the source map.
 * @param priority Setting this to true will prevent other sounds from overriding this one.
 * @param toStream Setting this to true will load the sound in pieces rather than all at once.
 * @param toLoop Should this source loop, or play only once.
 * @param sourcename A unique identifier for this source.  Two sources may not use the same sourcename.
 * @param filenameURL Filename/URL of the sound file to play at this source.
 * @param x X position for this source.
 * @param y Y position for this source.
 * @param z Z position for this source.
 * @param attModel Attenuation model to use.
 * @param distOrRoll Either the fading distance or rolloff factor, depending on the value of "attmodel".
 */
    @Override
    public void newSource( boolean priority, boolean toStream, boolean toLoop,
                           String sourcename, FilenameURL filenameURL, float x,
                           float y, float z, int attModel, float distOrRoll )
    {
        SoundBuffer buffer = null;
        
        if( !toStream )
        {
            // Grab the audio data for this file:
            buffer = bufferMap.get( filenameURL.getFilename() );
            // if not found, try loading it:
            if( buffer == null )
            {
                if( !loadSound( filenameURL ) )
                {
                    errorMessage( "Source '" + sourcename + "' was not created "
                                  + "because an error occurred while loading "
                                  + filenameURL.getFilename() );
                    return;
                }
            }
            // try and grab the sound buffer again:
            buffer = bufferMap.get( filenameURL.getFilename() );
            // see if it was there this time:
            if( buffer == null )
            {
                errorMessage( "Source '" + sourcename + "' was not created "
                              + "because audio data was not found for "
                              + filenameURL.getFilename() );
                return;
            }
        }
        
        if( !toStream && buffer != null )
            buffer.trimData( maxClipSize );
        
        sourceMap.put( sourcename,
                       new SourceJavaSound( listener, priority, toStream,
                                            toLoop, sourcename, filenameURL,
                                            buffer, x, y, z, attModel,
                                            distOrRoll, false ) );
    }

/**
 * Opens a direct line for streaming audio data.
 * @param audioFormat Format that the data will be in.
 * @param priority Setting this to true will prevent other sounds from overriding this one.
 * @param sourcename A unique identifier for this source.  Two sources may not use the same sourcename.
 * @param x X position for this source.
 * @param y Y position for this source.
 * @param z Z position for this source.
 * @param attModel Attenuation model to use.
 * @param distOrRoll Either the fading distance or rolloff factor, depending on the value of "attmodel".
 */
    @Override
    public void rawDataStream( AudioFormat audioFormat, boolean priority,
                               String sourcename, float x, float y,
                               float z, int attModel, float distOrRoll )
    {
        sourceMap.put( sourcename,
                       new SourceJavaSound( listener, audioFormat, priority,
                                            sourcename, x, y, z, attModel,
                                            distOrRoll ) );
    }
    
/**
 * Creates and immediately plays a new source.
 * @param priority Setting this to true will prevent other sounds from overriding this one.
 * @param toStream Setting this to true will load the sound in pieces rather than all at once.
 * @param toLoop Should this source loop, or play only once.
 * @param sourcename A unique identifier for this source.  Two sources may not use the same sourcename.
 * @param filenameURL Filename/URL of the sound file to play at this source.
 * @param x X position for this source.
 * @param y Y position for this source.
 * @param z Z position for this source.
 * @param attModel Attenuation model to use.
 * @param distOrRoll Either the fading distance or rolloff factor, depending on the value of "attmodel".
 * @param temporary Whether or not this source should be removed after it finishes playing.
 */
    @Override
    public void quickPlay( boolean priority, boolean toStream, boolean toLoop,
                           String sourcename, FilenameURL filenameURL, float x,
                           float y, float z, int attModel, float distOrRoll,
                           boolean temporary )
    {
        SoundBuffer buffer = null;
        
        if( !toStream )
        {
            // Grab the audio data for this file:
            buffer = bufferMap.get( filenameURL.getFilename() );
            // if not found, try loading it:
            if( buffer == null )
            {
                if( !loadSound( filenameURL ) )
                {
                    errorMessage( "Source '" + sourcename + "' was not created "
                                  + "because an error occurred while loading "
                                  + filenameURL.getFilename() );
                    return;
                }
            }
            // try and grab the sound buffer again:
            buffer = bufferMap.get( filenameURL.getFilename() );
            // see if it was there this time:
            if( buffer == null )
            {
                errorMessage( "Source '" + sourcename + "' was not created "
                              + "because audio data was not found for "
                              + filenameURL.getFilename() );
                return;
            }
        }
        
        if( !toStream && buffer != null)
            buffer.trimData( maxClipSize );
        
        sourceMap.put( sourcename,
                       new SourceJavaSound( listener, priority, toStream,
                                            toLoop, sourcename, filenameURL,
                                            buffer, x, y, z, attModel,
                                            distOrRoll, temporary ) );
    }
    
/**
 * Creates sources based on the source map provided.
 * @param srcMap Sources to copy.
 */
    @Override
    public void copySources( HashMap<String, Source> srcMap )
    {
        if( srcMap == null )
            return;
        Set<String> keys = srcMap.keySet();
        Iterator<String> iter = keys.iterator();        
        String sourcename;
        Source source;
        
        // Make sure the buffer map exists:
        if( bufferMap == null )
        {
            bufferMap = new HashMap<String, SoundBuffer>();
            importantMessage( "Buffer Map was null in method 'copySources'" );
        }
        
        // remove any existing sources before starting:
        sourceMap.clear();
        
        SoundBuffer buffer;
        // loop through and copy all the sources:
        while( iter.hasNext() )
        {
            sourcename = iter.next();
            source = srcMap.get( sourcename );
            if( source != null )
            {
                buffer = null;
                if( !source.toStream )
                {
                    loadSound( source.filenameURL );
                    buffer = bufferMap.get( source.filenameURL.getFilename() );
                }
                if( !source.toStream && buffer != null )
                {
                    buffer.trimData( maxClipSize );
                }
                if( source.toStream || buffer != null )
                {
                    sourceMap.put( sourcename, new SourceJavaSound( listener,
                                                             source, buffer ) );
                }
            }
        }
    }

/**
 * Sets the listener's velocity, for use in Doppler effect.
 * @param x Velocity along world x-axis.
 * @param y Velocity along world y-axis.
 * @param z Velocity along world z-axis.
 */
    @Override
    public void setListenerVelocity( float x, float y, float z )
    {
        super.setListenerVelocity( x, y, z );

        listenerMoved();
    }
    
/**
 * The Doppler parameters have changed.
 */
    @Override
    public void dopplerChanged()
    {
        super.dopplerChanged();

        listenerMoved();
    }
/**
 * Returns a handle to the current JavaSound mixer, or null if no mixer has
 * been set yet.
 * @return Handle to the mixer or null.
 */
    public static Mixer getMixer()
    {
        return mixer( GET, null );
    }

/**
 * Sets the current mixer.  If this method is not called, the
 * "Java Sound Audio Engine" mixer will be used by default.
 * @param m New mixer.
 */
    public static void setMixer( Mixer m ) throws SoundSystemException
    {
        mixer( SET, m );
        SoundSystemException e = SoundSystem.getLastException();
        SoundSystem.setException( null );
        if( e != null )
            throw e;
    }

/**
 * Either sets or returns the current mixer.
 * @param action GET or SET.
 * @param m New mixer or null.
 * @return Handle to the mixer.
 */
    private static synchronized Mixer mixer( boolean action, Mixer m )
    {
        if( action == SET )
        {
            if( m == null )
                return myMixer;

            MixerRanking mixerRanker = new MixerRanking();
            try
            {
                mixerRanker.rank( m.getMixerInfo() );
            }
            catch( LibraryJavaSound.Exception ljse )
            {
                SoundSystemConfig.getLogger().printStackTrace( ljse, 1 );
                SoundSystem.setException( ljse );
            }
            myMixer = m;
            mixerRanking( SET, mixerRanker );
            ChannelJavaSound c;
            if( instance != null )
            {
                ListIterator<Channel> itr =
                                          instance.normalChannels.listIterator();
                SoundSystem.setException( null );
                while( itr.hasNext() )
                {
                    c = (ChannelJavaSound) itr.next();
                    c.newMixer( m );
                }
                itr = instance.streamingChannels.listIterator();
                while( itr.hasNext() )
                {
                    c = (ChannelJavaSound) itr.next();
                    c.newMixer( m );
                }
            }
        }
        return myMixer;
    }

    public static MixerRanking getMixerRanking()
    {
        return mixerRanking( GET, null );
    }

    private static synchronized MixerRanking mixerRanking( boolean action,
                                                          MixerRanking value )
    {
        if( action == SET )
            myMixerRanking = value;
        return myMixerRanking;
    }
    
    public static void setMinSampleRate( int value )
    {
        minSampleRate( SET, value );
    }
    private static synchronized int minSampleRate( boolean action, int value )
    {
        if( action == SET )
            minSampleRate = value;
        return minSampleRate;
    }
    public static void setMaxSampleRate( int value )
    {
        maxSampleRate( SET, value );
    }
    private static synchronized int maxSampleRate( boolean action, int value )
    {
        if( action == SET )
            maxSampleRate = value;
        return maxSampleRate;
    }
    public static void setLineCount( int value )
    {
        lineCount( SET, value );
    }
    private static synchronized int lineCount( boolean action, int value )
    {
        if( action == SET )
            lineCount = value;
        return lineCount;
    }
    public static void useGainControl( boolean value )
    {
        useGainControl( SET, value );
    }
    private static synchronized boolean useGainControl( boolean action,
                                                        boolean value )
    {
        if( action == SET )
            useGainControl = value;
        return useGainControl;
    }
    public static void usePanControl( boolean value )
    {
        usePanControl( SET, value );
    }
    private static synchronized boolean usePanControl( boolean action,
                                                       boolean value )
    {
        if( action == SET )
            usePanControl = value;
        return usePanControl;
    }
    public static void useSampleRateControl( boolean value )
    {
        useSampleRateControl( SET, value );
    }
    private static synchronized boolean useSampleRateControl( boolean action,
                                                              boolean value )
    {
        if( action == SET )
            useSampleRateControl = value;
        return useSampleRateControl;
    }
    
/**
 * Returns the short title of this library type.
 * @return A short title.
 */
    public static String getTitle()
    {
        return "Java Sound";
    }

/**
 * Returns a longer description of this library type.
 * @return A longer description.
 */
    public static String getDescription()
    {
        return "The Java Sound API.  For more information, see " +
               "http://java.sun.com/products/java-media/sound/";
    }

/**
 * Returns the name of the class.
 * @return "Library" + library title.
 */
    @Override
    public String getClassName()
    {
        return "LibraryJavaSound";
    }

/**
 * The MixerRanking class is used to determine the capabilities of a mixer,
 * and to give it a ranking based on the priority of those capabilities.
 */
    public static class MixerRanking
    {
        /**
         * A priority of HIGH means the Mixer is not usable if the capability
         * is not available.
         */
        public static final int HIGH = 1;
        /**
         * A priority of MEDIUM means the Mixer is usable without the
         * capability, but functionality is greatly limited.
         */
        public static final int MEDIUM = 2;
        /**
         * A priority of LOW means the Mixer is usable without the capability,
         * but functionality may be somewhat limited.
         */
        public static final int LOW = 3;
        /**
         * A priority of NONE means the Mixer is fully functional, and loss of
         * this capability does not affect the overall ranking.  Used to ignore
         * capibilities not used by a particular application.
         */
        public static final int NONE = 4;
        /**
         * Priority for the Mixer existing.  Should always be HIGH.
         */
        public static int MIXER_EXISTS_PRIORITY = HIGH;
        /**
         * Priority for the desired minimum sample-rate compatibility.  By
         * default, this is HIGH.
         */
        public static int MIN_SAMPLE_RATE_PRIORITY = HIGH;
        /**
         * Priority for the desired maximum sample-rate compatibility.  By
         * default, this is HIGH.
         */
        public static int MAX_SAMPLE_RATE_PRIORITY = HIGH;
        /**
         * Priority for the desired maximum line-count.  By default, this is
         * HIGH.
         */
        public static int LINE_COUNT_PRIORITY = HIGH;
        /**
         * Priority for the gain control.  By default, this is MEDIUM.
         */
        public static int GAIN_CONTROL_PRIORITY = MEDIUM;
        /**
         * Priority for the pan control.  By default, this is MEDIUM.
         */
        public static int PAN_CONTROL_PRIORITY = MEDIUM;
        /**
         * Priority for the sample-rate control.  By default, this is LOW.
         */
        public static int SAMPLE_RATE_CONTROL_PRIORITY = LOW;
        /**
         * Standard information about the Mixer.
         */
        public Mixer.Info mixerInfo = null;
        /**
         * The Mixer's overall ranking.  Maximum is 14, meaning fully
         * functional.  Minimum is 0, meaning not usable.
         */
        public int rank = 0;
        /**
         * Indicates whether or not the Mixer exists.
         */
        public boolean mixerExists = false;
        /**
         * Indicates whether or not the desired minimum sample-rate is
         * compatible on the Mixer.
         */
        public boolean minSampleRateOK = false;
        /**
         * Indicates whether or not the desired maximum sample-rate is
         * compatible on the Mixer.
         */
        public boolean maxSampleRateOK = false;
        /**
         * Indicates whether or not the desired number of lines can be created
         * on the Mixer.
         */
        public boolean lineCountOK = false;
        /**
         * Indicates whether or not gain controls are possible on the Mixer.
         */
        public boolean gainControlOK = false;
        /**
         * Indicates whether or not pan controls are possible on the Mixer.
         */
        public boolean panControlOK = false;
        /**
         * Indicates whether or not sample-rate controls are possible on the
         * Mixer.
         */
        public boolean sampleRateControlOK = false;

        /**
         * Indicates the minimum sample rate possible for the Mixer, or -1 if
         * no sample rate is possible.
         */
        public int minSampleRatePossible = -1;
        /**
         * Indicates the maximum sample rate possible for the Mixer, or -1 if
         * no sample rate is possible.
         */
        public int maxSampleRatePossible = -1;
        /**
         * Indicates the maximum number of output lines the Mixer can handle.
         */
        public int maxLinesPossible = 0;

        /**
         * Constructor: Instantiates a Mixer ranking with default initial
         * values.
         */
        public MixerRanking()
        {}

        /**
         * Constructor: Instantiates a Mixer ranking with specified initial
         * values.
         * @param i Standard information about the mixer.
         * @param r Overall ranking of the mixer.
         * @param e Whether or not the mixer exists.
         * @param mnsr Whether or not minimum sample-rate is compatible.
         * @param mxsr Whether or not maximum sample-rate is compatible.
         * @param lc Whether or not number of lines are compatible.
         * @param gc Whether or not gain controls are compatible.
         * @param pc Whether or not pan controls are compatible.
         * @param src Whether or not sample-rate controls are compatible.
         */
        public MixerRanking( Mixer.Info i, int r, boolean e, boolean mnsr,
                             boolean mxsr, boolean lc, boolean gc, boolean pc,
                             boolean src )
        {
            mixerInfo = i;
            rank = r;
            mixerExists = e;
            minSampleRateOK = mnsr;
            maxSampleRateOK = mxsr;
            lineCountOK = lc;
            gainControlOK = gc;
            panControlOK = pc;
            sampleRateControlOK = src;
        }

        /**
         * Looks up the specified Mixer, tests its capabilities, and calculates
         * its overall ranking.
         * @param i Standard information about the mixer.
         */
        public void rank( Mixer.Info i ) throws LibraryJavaSound.Exception
        {
            // STEP 1: Determine if the Mixer exists
            if( i == null )
                throw new LibraryJavaSound.Exception( "No Mixer info " +
                             "specified in method 'MixerRanking.rank'" , this );
            mixerInfo = i;
            Mixer m;
            try
            {
                m = AudioSystem.getMixer( mixerInfo );
            }
            catch( java.lang.Exception e )
            {
                throw new LibraryJavaSound.Exception( "Unable to acquire the " +
                       "specified Mixer in method 'MixerRanking.rank'" , this );
            }
            if( m == null )
                throw new LibraryJavaSound.Exception( "Unable to acquire the " +
                       "specified Mixer in method 'MixerRanking.rank'" , this );
            mixerExists = true;
            
            // STEP 2: Check if the desired sample-rate range is possible
            AudioFormat format;
            DataLine.Info lineInfo;
            try
            {
                format = new AudioFormat( minSampleRate( GET, XXX ), 16, 2, true,
                                          false );
                lineInfo = new DataLine.Info( SourceDataLine.class, format );
            }
            catch( java.lang.Exception e )
            {
                throw new LibraryJavaSound.Exception( "Invalid minimum " +
                 "sample-rate specified in method 'MixerRanking.rank'" , this );
            }
            if( !AudioSystem.isLineSupported( lineInfo ) )
            {
                if( MIN_SAMPLE_RATE_PRIORITY == HIGH )
                    throw new LibraryJavaSound.Exception( "Specified minimum " +
                                        "sample-rate not possible for Mixer '" +
                                        mixerInfo.getName() + "'" , this );
            }
            else
            {
                minSampleRateOK = true;
            }
            try
            {
                format = new AudioFormat( maxSampleRate( GET, XXX ), 16, 2,
                                          true, false );
                lineInfo = new DataLine.Info( SourceDataLine.class, format );
            }
            catch( java.lang.Exception e )
            {
                throw new LibraryJavaSound.Exception( "Invalid maximum " +
                 "sample-rate specified in method 'MixerRanking.rank'" , this );
            }
            if( !AudioSystem.isLineSupported( lineInfo ) )
            {
                if( MAX_SAMPLE_RATE_PRIORITY == HIGH )
                    throw new LibraryJavaSound.Exception( "Specified maximum " +
                                        "sample-rate not possible for Mixer '" +
                                        mixerInfo.getName() + "'" , this );
            }
            else
            {
                maxSampleRateOK = true;
            }

            // STEP 3: If desired range is not possible, figure out what is
            int lL;
            int uL;
            int testSampleRate;
            if( minSampleRateOK )
            {
                minSampleRatePossible = minSampleRate( GET, XXX );
            }
            else
            {
                // Find the lower limit:
                lL = minSampleRate( GET, XXX );
                uL = maxSampleRate( GET, XXX );
                while( uL - lL > 1 )
                {
                    testSampleRate = lL + (uL - lL ) / 2;
                    format = new AudioFormat( testSampleRate, 16, 2, true, false );
                    lineInfo = new DataLine.Info( SourceDataLine.class, format );
                    if( AudioSystem.isLineSupported( lineInfo ) )
                    {
                        minSampleRatePossible = testSampleRate;
                        uL = testSampleRate;
                    }
                    else
                    {
                        lL = testSampleRate;
                    }
                }
            }
            if( maxSampleRateOK )
            {
                maxSampleRatePossible = maxSampleRate( GET, XXX );
            }
            else if( minSampleRatePossible != -1 )
            {
                // Find the upper limit:
                uL = maxSampleRate( GET, XXX );
                lL = minSampleRatePossible;
                while( uL - lL > 1 )
                {
                    testSampleRate = lL + (uL - lL ) / 2;
                    format = new AudioFormat( testSampleRate, 16, 2, true, false );
                    lineInfo = new DataLine.Info( SourceDataLine.class, format );
                    if( AudioSystem.isLineSupported( lineInfo ) )
                    {
                        maxSampleRatePossible = testSampleRate;
                        lL = testSampleRate;
                    }
                    else
                    {
                        uL = testSampleRate;
                    }
                }
            }
            // Make sure we have some range of possible sample-rates:
            if( minSampleRatePossible == -1 || maxSampleRatePossible == -1 )
                throw new LibraryJavaSound.Exception( "No possible " +
                                             "sample-rate found for Mixer '" +
                                             mixerInfo.getName() + "'" , this );

            //STEP 4: Determine if the specified number of lines is possible:
            format = new AudioFormat( minSampleRatePossible, 16, 2, true, false );
            Clip clip = null;
            try
            {
                DataLine.Info clipLineInfo = new DataLine.Info( Clip.class,
                                                               format );
                clip = (Clip) m.getLine( clipLineInfo );
                byte[] buffer = new byte[10];
                clip.open( format, buffer, 0, buffer.length );
            }
            catch( java.lang.Exception e )
            {
                throw new LibraryJavaSound.Exception( "Unable to attach an " +
                                                      "actual audio buffer " +
                                                      "to an actual Clip... " +
                                                      "Mixer '" +
                                                      mixerInfo.getName() +
                                                      "' is unuseable.", this );
            }
            maxLinesPossible = 1;
            lineInfo = new DataLine.Info( SourceDataLine.class, format );
            SourceDataLine[] lines = new SourceDataLine[lineCount( GET, XXX )
                                                        - 1];
            int c = 0;
            int x;
            for( x = 1; x < lines.length + 1; x++ )
            {
                try
                {
                    lines[x-1] = (SourceDataLine) m.getLine( lineInfo );
                }
                catch( java.lang.Exception e )
                {
                    if( x == 0 )
                        throw new LibraryJavaSound.Exception( "No output " +
                                             "lines possible for Mixer '" +
                                             mixerInfo.getName() + "'" , this );
                    else if( LINE_COUNT_PRIORITY == HIGH )
                        throw new LibraryJavaSound.Exception( "Specified " +
                            "maximum number of lines not possible for Mixer '" +
                            mixerInfo.getName() + "'" , this );
                    break;
                }
                maxLinesPossible = x + 1;
            }
            try
            {
                clip.close();
            }
            catch( java.lang.Exception e )
            {}
            clip = null;
            if( maxLinesPossible == lineCount( GET, XXX ) )
            {
                lineCountOK = true;
            }

            //STEP 5: Check which controls are available:
            if( !useGainControl( GET, false ) )
            {
                GAIN_CONTROL_PRIORITY = NONE;
            }
            else if( !lines[0].isControlSupported(
                                               FloatControl.Type.MASTER_GAIN ) )
            {
                if( GAIN_CONTROL_PRIORITY == HIGH )
                    throw new LibraryJavaSound.Exception( "Gain control " +
                                             "not available for Mixer '" +
                                             mixerInfo.getName() + "'" , this );
            }
            else
            {
                gainControlOK = true;
            }
            if( !usePanControl( GET, false ) )
            {
                PAN_CONTROL_PRIORITY = NONE;
            }
            else if( !lines[0].isControlSupported( FloatControl.Type.PAN ) )
            {
                if( PAN_CONTROL_PRIORITY == HIGH )
                    throw new LibraryJavaSound.Exception( "Pan control " +
                                             "not available for Mixer '" +
                                             mixerInfo.getName() + "'" , this );
            }
            else
            {
                panControlOK = true;
            }
            if( !useSampleRateControl( GET, false ) )
            {
                SAMPLE_RATE_CONTROL_PRIORITY = NONE;
            }
            else if( !lines[0].isControlSupported(
                                               FloatControl.Type.SAMPLE_RATE ) )
            {
                if( SAMPLE_RATE_CONTROL_PRIORITY == HIGH )
                    throw new LibraryJavaSound.Exception( "Sample-rate " +
                                             "control not available for " +
                                             "Mixer '" +
                                             mixerInfo.getName() + "'" , this );
            }
            else
            {
                sampleRateControlOK = true;
            }

            //STEP 6: Calculate the Mixer's rank:
            rank += getRankValue( mixerExists, MIXER_EXISTS_PRIORITY );
            rank += getRankValue( minSampleRateOK, MIN_SAMPLE_RATE_PRIORITY );
            rank += getRankValue( maxSampleRateOK, MAX_SAMPLE_RATE_PRIORITY );
            rank += getRankValue( lineCountOK, LINE_COUNT_PRIORITY );
            rank += getRankValue( gainControlOK, GAIN_CONTROL_PRIORITY );
            rank += getRankValue( panControlOK, PAN_CONTROL_PRIORITY );
            rank += getRankValue( sampleRateControlOK,
                                  SAMPLE_RATE_CONTROL_PRIORITY );

            //STEP 7: Clean up:
            m = null;
            format = null;
            lineInfo = null;
            lines = null;
        }

        /**
         * Calculates the value of the specified property (or lack thereof).
         * @param property Whether or not the property is available.
         * @param priority The priority of the specified property.
         * @return value to add to the Mixer's rank.
         */
        private int getRankValue( boolean property, int priority )
        {
            // Maximum value if the propery is available:
            if( property )
                return 2;
            // Property isn't available..
            // Full value if the property has no priority:
            if( priority == NONE )
                return 2;
            // Half-value if the priority is low:
            if( priority == LOW )
                return 1;
            // Otherwise, no value:
            return 0;
        }
    }

/**
 * The LibraryJavaSound.Exception class provides library-specific error
 * information.
 */
    public static class Exception extends SoundSystemException
    {
        /**
         * Global identifier for a problem with the mixer.
         */
        public static final int MIXER_PROBLEM               = 101;

        /**
         * If there is a mixer problem, this will hold additional information.
         */
        public static MixerRanking mixerRanking = null;

        /**
         * Constructor: Generates a standard "unknown error" exception with the
         * specified message.
         * @param message A brief description of the problem that occurred.
         */
        public Exception( String message )
        {
            super( message );
        }

        /**
         * Constructor: Generates an exception of the specified type, with the
         * specified message.
         * @param message A brief description of the problem that occurred.
         * @param type Identifier indicating they type of error.
         */
        public Exception( String message, int type )
        {
            super( message, type );
        }

        /**
         * Constructor: Generates a "Mixer Problem" exception with the specified
         * message.  Also, the mixer ranking is stored, containing additional
         * information about the problem.
         * @param message A brief description of the problem that occurred.
         * @param rank Ranking of the mixer involved.
         */
        public Exception( String message, MixerRanking rank )
        {
            super( message, MIXER_PROBLEM );
            mixerRanking = rank;
        }
        
    }
}
