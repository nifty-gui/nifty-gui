package paulscode.sound.libraries;

import java.util.LinkedList;
import javax.sound.sampled.AudioFormat;

import paulscode.sound.Channel;
import paulscode.sound.FilenameURL;
import paulscode.sound.ListenerData;
import paulscode.sound.SoundBuffer;
import paulscode.sound.Source;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.Vector3D;

/**
 * The SourceJavaSound class provides an interface to the JavaSound library.  
 * For more information about the Java Sound API, please visit 
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
public class SourceJavaSound extends Source
{
/**
 * The source's basic Channel type-cast to a ChannelJavaSound.
 */
    protected ChannelJavaSound channelJavaSound = (ChannelJavaSound) channel;
    
/**
 * Handle to the listener information.  
 */
    public ListenerData listener;
    
/**
 * Panning between left and right speaker (float between -1.0 and 1.0).  
 */
    private float pan = 0.0f;
    
/**
 * Constructor:  Creates a new source using the specified parameters.
 * @param listener Handle to information about the listener.
 * @param priority Setting this to true will prevent other sounds from overriding this one.
 * @param toStream Setting this to true will create a streaming source.
 * @param toLoop Should this source loop, or play only once.
 * @param sourcename A unique identifier for this source.  Two sources may not use the same sourcename.
 * @param filenameURL Filename/URL of the sound file to play at this source.
 * @param soundBuffer Sound buffer to use if creating a new normal source.
 * @param x X position for this source.
 * @param y Y position for this source.
 * @param z Z position for this source.
 * @param attModel Attenuation model to use.
 * @param distOrRoll Either the fading distance or rolloff factor, depending on the value of 'att'.
 * @param temporary Whether or not to remove this source after it finishes playing.
 */
    public SourceJavaSound( ListenerData listener, boolean priority,
                            boolean toStream, boolean toLoop, String sourcename,
                            FilenameURL filenameURL, SoundBuffer soundBuffer,
                            float x, float y, float z, int attModel,
                            float distOrRoll, boolean temporary )
    {
        super( priority, toStream, toLoop, sourcename, filenameURL, soundBuffer,
               x, y, z, attModel, distOrRoll, temporary );
        libraryType = LibraryJavaSound.class;
        
        // point handle to the listener information:
        this.listener = listener;
        positionChanged();
    }
    
/**
 * Constructor:  Creates a new source matching the specified source.
 * @param listener Handle to information about the listener.
 * @param old Source to copy information from.
 * @param soundBuffer Sound buffer to use if creating a new normal source.
 */
    public SourceJavaSound( ListenerData listener, Source old,
                            SoundBuffer soundBuffer )
    {
        super( old, soundBuffer );
        libraryType = LibraryJavaSound.class;

        // point handle to the listener information:
        this.listener = listener;
        positionChanged();
    }

/**
 * Constructor:  Creates a new streaming source that will be directly fed with
 * raw audio data.
 * @param listener Handle to information about the listener.
 * @param audioFormat Format that the data will be in.
 * @param priority Setting this to true will prevent other sounds from overriding this one.
 * @param sourcename A unique identifier for this source.  Two sources may not use the same sourcename.
 * @param x X position for this source.
 * @param y Y position for this source.
 * @param z Z position for this source.
 * @param attModel Attenuation model to use.
 * @param distOrRoll Either the fading distance or rolloff factor, depending on the value of 'att'.
 */
    public SourceJavaSound( ListenerData listener, AudioFormat audioFormat,
                            boolean priority, String sourcename, float x,
                            float y, float z, int attModel, float distOrRoll )
    {
        super( audioFormat, priority, sourcename, x, y, z, attModel,
               distOrRoll );
        libraryType = LibraryJavaSound.class;

        // point handle to the listener information:
        this.listener = listener;
        positionChanged();
    }
    
    
/**
 * Shuts the source down and removes references to all instantiated objects.
 */
    @Override
    public void cleanup()
    {
        super.cleanup();        
    }
    
/**
 * Changes the peripheral information about the source using the specified 
 * parameters.
 * @param priority Setting this to true will prevent other sounds from overriding this one.
 * @param toStream Setting this to true will create a streaming source.
 * @param toLoop Should this source loop, or play only once.
 * @param sourcename A unique identifier for this source.  Two sources may not use the same sourcename.
 * @param filenameURL Filename/URL of the sound file to play at this source.
 * @param soundBuffer Sound buffer to use if creating a new normal source.
 * @param x X position for this source.
 * @param y Y position for this source.
 * @param z Z position for this source.
 * @param attModel Attenuation model to use.
 * @param distOrRoll Either the fading distance or rolloff factor, depending on the value of 'att'.
 * @param temporary Whether or not to remove this source after it finishes playing.
 */
    @Override
    public void changeSource( boolean priority, boolean toStream,
                              boolean toLoop, String sourcename,
                              FilenameURL filenameURL, SoundBuffer soundBuffer,
                              float x, float y, float z, int attModel,
                              float distOrRoll, boolean temporary )
    {
        super.changeSource( priority, toStream, toLoop, sourcename, filenameURL,
                            soundBuffer, x, y, z, attModel, distOrRoll,
                            temporary );
        if( channelJavaSound != null )
            channelJavaSound.setLooping( toLoop );
        positionChanged();
    }
    
/**
 * Called every time the listener's position or orientation changes.
 */
    @Override
    public void listenerMoved()
    {
        positionChanged();
    }

/**
 * Sets this source's velocity, for use in Doppler effect.
 * @param x Velocity along world x-axis.
 * @param y Velocity along world y-axis.
 * @param z Velocity along world z-axis.
 */
    @Override
    public void setVelocity( float x, float y, float z )
    {
        super.setVelocity( x, y, z );
        positionChanged();
    }
    
/**
 * Moves the source to the specified position.
 * @param x X coordinate to move to.
 * @param y Y coordinate to move to.
 * @param z Z coordinate to move to.
 */
    @Override
    public void setPosition( float x, float y, float z )
    {
        super.setPosition( x, y, z );
        positionChanged();
    }
    
/**
 * Updates the pan and gain.
 */
    @Override
    public void positionChanged()
    {
        calculateGain();
        calculatePan();
        calculatePitch();
    }

/**
 * Manually sets this source's pitch.
 * @param value A float value ( 0.5f - 2.0f ).
 */
    @Override
    public void setPitch( float value )
    {
        super.setPitch( value );
        calculatePitch();
    }

/**
 * Sets this source's attenuation model.
 * @param model Attenuation model to use.
 */
    @Override
    public void setAttenuation( int model )
    {
        super.setAttenuation( model );
        calculateGain();
    }
    
/**
 * Sets this source's fade distance or rolloff factor, depending on the 
 * attenuation model.
 * @param dr New value for fade distance or rolloff factor.
 */
    @Override
    public void setDistOrRoll( float dr)
    {
        super.setDistOrRoll( dr );
        calculateGain();
    }
    
/**
 * Plays the source on the specified channel.
 * @param c Channel to play on.
 */
    @Override
    public void play( Channel c )
    {
        if( !active() )
        {
            if( toLoop )
                toPlay = true;
            return;
        }
        
        if( c == null )
        {
            errorMessage( "Unable to play source, because channel was null" );
            return;
        }
        
        boolean newChannel = (channel != c);
        if( channel != null && channel.attachedSource != this )
            newChannel = true;

        boolean wasPaused = paused();
        boolean wasStopped = stopped();
        
        super.play( c );
        
        channelJavaSound = (ChannelJavaSound) channel;
        
        // Make sure the channel exists:
        // check if we are already on this channel:
        if( newChannel )
        {
            if( channelJavaSound != null )
                channelJavaSound.setLooping( toLoop );
            
            if( !toStream )
            {
                // This is not a streaming source, so make sure there is
                // a sound buffer loaded to play:
                if( soundBuffer == null )
                {
                    errorMessage( "No sound buffer to play" );
                    return;
                }
                
                channelJavaSound.attachBuffer( soundBuffer );
            }
        }
        positionChanged();  // set new pan and gain
        
        // See if we are already playing:
        if( wasStopped || !playing() )
        {
            if( toStream && !wasPaused )
            {
                preLoad = true;
            }
            channel.play();
        }
    }
    
/**
 * Queues up the initial stream-buffers for the stream.
 * @return False if the end of the stream was reached.
 */
    @Override
    public boolean preLoad()
    {
        if( codec == null )
        {
            return false;
        }

        boolean noNextBuffers = false;
        synchronized( soundSequenceLock )
        {
            if( nextBuffers == null || nextBuffers.isEmpty() )
                noNextBuffers = true;
        }

        LinkedList<byte[]> preLoadBuffers = new LinkedList<byte[]>();
        if( nextCodec != null && !noNextBuffers )
        {
            codec = nextCodec;
            nextCodec = null;
            synchronized( soundSequenceLock )
            {
                while( !nextBuffers.isEmpty() )
                {
                    soundBuffer = nextBuffers.remove( 0 );
                    if( soundBuffer != null && soundBuffer.audioData != null )
                        preLoadBuffers.add( soundBuffer.audioData );
                }
            }
        }
        else
        {
            codec.initialize( filenameURL.getURL() );

            for( int i = 0; i < SoundSystemConfig.getNumberStreamingBuffers(); i++ )
            {
                soundBuffer = codec.read();

                if( soundBuffer == null || soundBuffer.audioData == null )
                    break;

                preLoadBuffers.add( soundBuffer.audioData );
            }
            channelJavaSound.resetStream( codec.getAudioFormat() );
        }
        positionChanged();

        channel.preLoadBuffers( preLoadBuffers );

        preLoad = false;
        return true;
    }
    
/**
 * Calculates the gain for this source based on its attenuation model and 
 * distance from the listener.
 */
    public void calculateGain()
    {
        float distX = position.x - listener.position.x;
        float distY = position.y - listener.position.y;
        float distZ = position.z - listener.position.z;
        
        distanceFromListener = (float) Math.sqrt( distX*distX + distY*distY
                                                  + distZ*distZ );
        
        // Calculate the source's gain using the specified attenuation model:
        switch( attModel )
        {
            case SoundSystemConfig.ATTENUATION_LINEAR:
                if( distanceFromListener <= 0 )
                {
                    gain = 1.0f;
                }
                else if( distanceFromListener >= distOrRoll )
                {
                    gain = 0.0f;
                }
                else
                {
                    gain = 1.0f - ( distanceFromListener / distOrRoll );
                }
                break;
            case SoundSystemConfig.ATTENUATION_ROLLOFF:
                if( distanceFromListener <= 0 )
                {
                    gain = 1.0f;
                }
                else
                {
                    float tweakFactor = 0.0005f;
                    float attenuationFactor = distOrRoll * distanceFromListener
                                              * distanceFromListener 
                                              * tweakFactor;
                    // Make sure we don't do a division by zero:
                    // (rolloff should NEVER be negative)
                    if( attenuationFactor < 0 )
                        attenuationFactor = 0;
                    
                    gain = 1.0f / ( 1 + attenuationFactor );
                }
                break;
            default:
                gain = 1.0f;
                break;
        }
        // make sure gain is between 0 and 1:
        if( gain > 1.0f )
            gain = 1.0f;
        if( gain < 0.0f )
            gain = 0.0f;
        
        gain *= sourceVolume * SoundSystemConfig.getMasterGain()
                * (float) Math.abs( fadeOutGain ) * fadeInGain;

        // update the channel's gain:
        if( channel != null && channel.attachedSource == this &&
            channelJavaSound != null )
            channelJavaSound.setGain( gain );
    }
    
/**
 * Calculates the panning for this source based on its position in relation to 
 * the listener.
 */
    public void calculatePan()
    {
        Vector3D side = listener.up.cross( listener.lookAt );
        side.normalize();
        float x = position.dot( position.subtract( listener.position ), side );
        float z = position.dot( position.subtract( listener.position ),
                                listener.lookAt );
        side = null;        
        float angle = (float) Math.atan2( x, z );
        pan = (float) - Math.sin( angle );
        
        if( channel != null && channel.attachedSource == this &&
            channelJavaSound != null )
        {
            if( attModel == SoundSystemConfig.ATTENUATION_NONE )
                channelJavaSound.setPan( 0 );
            else
                channelJavaSound.setPan( pan );
        }
    }

/**
 * Calculates the pitch for this source based on its position in relation to
 * the listener.
 */
    public void calculatePitch()
    {
        if( channel != null && channel.attachedSource == this &&
            channelJavaSound != null )
        {
            // If not using Doppler effect, save some calculations:
            if( SoundSystemConfig.getDopplerFactor() == 0 )
            {
                channelJavaSound.setPitch( pitch );
            }
            else
            {
                float SS = 343.3f;

                Vector3D SV = velocity;
                Vector3D LV = listener.velocity;
                float DV = SoundSystemConfig.getDopplerVelocity();
                float DF = SoundSystemConfig.getDopplerFactor();
                Vector3D SL = listener.position.subtract( position );

                float vls = SL.dot( LV ) / SL.length();
                float vss = SL.dot( SV ) / SL.length();

                vss = min( vss, SS / DF );
                vls = min( vls, SS / DF );
                float newPitch = pitch * ( SS * DV - DF * vls ) /
                                         (SS * DV - DF * vss );
                
                if( newPitch < 0.5f )
                    newPitch = 0.5f;
                else if( newPitch > 2.0f )
                    newPitch = 2.0f;

                channelJavaSound.setPitch( newPitch );
            }
        }
    }

    public float min( float a, float b )
    {
        if( a < b )
            return a;
        return b;
    }
}
