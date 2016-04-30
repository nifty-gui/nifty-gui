package paulscode.sound.codecs;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import paulscode.sound.ICodec;
import paulscode.sound.SoundBuffer;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemLogger;

/**
 * The CodecWav class provides an ICodec interface for reading from .wav files.
 *<br><br>
 *<b><i>   SoundSystem CodecWav License:</b></i><br><b><br>
 *    You are free to use this class for any purpose, commercial or otherwise.
 *    You may modify this class or source code, and distribute it any way you
 *    like, provided the following conditions are met:
 *<br>
 *    1) You may not falsely claim to be the author of this class or any
 *    unmodified portion of it.
 *<br>
 *    2) You may not copyright this class or a modified version of it and then
 *    sue me for copyright infringement.
 *<br>
 *    3) If you modify the source code, you must clearly document the changes
 *    made before redistributing the modified source code, so other users know
 *    it is not the original code.
 *<br>
 *    4) You are not required to give me credit for this class in any derived
 *    work, but if you do, you must also mention my website:
 *    http://www.paulscode.com
 *<br>
 *    5) I the author will not be responsible for any damages (physical,
 *    financial, or otherwise) caused by the use if this class or any portion
 *    of it.
 *<br>
 *    6) I the author do not guarantee, warrant, or make any representations,
 *    either expressed or implied, regarding the use of this class or any
 *    portion of it.
 * <br><br>
 *    Author: Paul Lamb
 * <br>
 *    http://www.paulscode.com
 * </b>
 */
public class CodecWav implements ICodec
{
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
 * True if there is no more data to read in.
 */
    private boolean endOfStream = false;

/**
 * True if the stream has finished initializing.
 */
    private boolean initialized = false;

/**
 * Input stream to use for reading in pcm data.
 */
    private AudioInputStream myAudioInputStream = null;

/**
 * This method is ignored by CodecWav, because it produces "nice" data.
 * @param b True if the calling audio library requires byte-reversal from certain codecs
 */
    public void reverseByteOrder( boolean b )
    {}

/**
 * Processes status messages, warnings, and error messages.
 */
    private SoundSystemLogger logger;

/**
 * Constructor:  Grabs a handle to the logger.
 */
    public CodecWav()
    {
        logger = SoundSystemConfig.getLogger();
    }

/**
 * Prepares an audio stream to read from.  If another stream is already opened,
 * it will be closed and a new audio stream opened in its place.
 * @param url URL to an audio file to stream from.
 * @return False if an error occurred or if end of stream was reached.
 */
    public boolean initialize( URL url )
    {
        initialized( SET, false );
        cleanup();
        
        if( url == null )
        {
            errorMessage( "url null in method 'initialize'" );
            cleanup();
            return false;
        }

        try
        {
            myAudioInputStream = AudioSystem.getAudioInputStream(
                                  new BufferedInputStream( url.openStream() ) );
        }
        catch( UnsupportedAudioFileException uafe )
        {
            errorMessage( "Unsupported audio format in method 'initialize'" );
            printStackTrace( uafe );
            return false;
        }
        catch( IOException ioe )
        {
            errorMessage( "Error setting up audio input stream in method " +
                          "'initialize'" );
            printStackTrace( ioe );
            return false;
        }

        endOfStream( SET, false );
        initialized( SET, true );
        return true;
    }

/**
 * Returns false if the stream is busy initializing.
 * @return True if steam is initialized.
 */
    public boolean initialized()
    {
        return initialized( GET, XXX );
    }

/**
 * Reads in one stream buffer worth of audio data.  See
 * {@link paulscode.sound.SoundSystemConfig SoundSystemConfig} for more
 * information about accessing and changing default settings.
 * @return The audio data wrapped into a SoundBuffer context.
 */
    public SoundBuffer read()
    {
        if( myAudioInputStream == null )
            return null;

        // Get the format for the audio data:
        AudioFormat audioFormat = myAudioInputStream.getFormat();

        // Check to make sure there is an audio format:
        if( audioFormat == null )
        {
            errorMessage( "Audio Format null in method 'read'" );
            return null;
        }

        // Varriables used when reading from the audio input stream:
        int bytesRead = 0, cnt = 0;

        // Allocate memory for the audio data:
        byte[] streamBuffer =
                           new byte[SoundSystemConfig.getStreamingBufferSize()];

        try
        {
            // Read until buffer is full or end of stream is reached:
            while( ( !endOfStream( GET, XXX ) )
                   && ( bytesRead < streamBuffer.length ) )
            {
                if( ( cnt = myAudioInputStream.read( streamBuffer, bytesRead,
                                      streamBuffer.length - bytesRead ) )
                     <= 0 )
                {
                    endOfStream( SET, true );
                    break;
                }
                // keep track of how many bytes were read:
                bytesRead += cnt;
            }
        }
        catch( IOException ioe )
        {
// TODO: See if setting endOfStream is needed here
            endOfStream( SET, true );
            return null;
        }

        // Return null if no data was read:
        if( bytesRead <= 0 )
            return null;

        // If we didn't fill the stream buffer entirely, trim it down to size:
        if( bytesRead < streamBuffer.length )
            streamBuffer = trimArray( streamBuffer, bytesRead );


        // Insert the converted data into a ByteBuffer:
        byte[] data = convertAudioBytes( streamBuffer,
                                      audioFormat.getSampleSizeInBits() == 16 );

        // Wrap the data into a SoundBuffer:
        SoundBuffer buffer = new SoundBuffer( data, audioFormat );

        // Return the result:
        return buffer;
    }

/**
 * Reads in all the audio data from the stream (up to the default
 * "maximum file size".  See
 * {@link paulscode.sound.SoundSystemConfig SoundSystemConfig} for more
 * information about accessing and changing default settings.
 * @return the audio data wrapped into a SoundBuffer context.
 */
    public SoundBuffer readAll()
    {
        // Check to make sure there is an audio format:
        if( myAudioInputStream == null )
        {
            errorMessage( "Audio input stream null in method 'readAll'" );
            return null;
        }
        AudioFormat myAudioFormat = myAudioInputStream.getFormat();

        // Check to make sure there is an audio format:
        if( myAudioFormat == null )
        {
            errorMessage( "Audio Format null in method 'readAll'" );
            return null;
        }

        // Array to contain the audio data:
        byte[] fullBuffer = null;

        // Determine how much data will be read in:
        int fileSize = myAudioFormat.getChannels()
                        * (int) myAudioInputStream.getFrameLength()
                        * myAudioFormat.getSampleSizeInBits() / 8;
        if( fileSize > 0 )
        {
            // Allocate memory for the audio data:
            fullBuffer = new byte[ myAudioFormat.getChannels()
                                   * (int) myAudioInputStream.getFrameLength()
                                   * myAudioFormat.getSampleSizeInBits()
                                   / 8 ];
            int read = 0, total = 0;
            try
            {
                // Read until the end of the stream is reached:
                while( ( read = myAudioInputStream.read( fullBuffer, total,
                                                         fullBuffer.length
                                                               - total ) ) != -1
                       && total < fullBuffer.length )
                {
                        total += read;
                }
            }
            catch( IOException e )
            {
                errorMessage( "Exception thrown while reading from the " +
                              "AudioInputStream (location #1)." );
                printStackTrace( e );
                return null;
            }
        }
        else
        {
            // Total file size unknown.

            // Varriables used when reading from the audio input stream:
            int totalBytes = 0, bytesRead = 0, cnt = 0;
            byte[] smallBuffer = null;

            // Allocate memory for a chunk of data:
            smallBuffer = new byte[ SoundSystemConfig.getFileChunkSize() ];

            // Read until end of file or maximum file size is reached:
            while( (!endOfStream(GET, XXX))  &&
                   (totalBytes < SoundSystemConfig.getMaxFileSize()) )
            {
                bytesRead = 0;
                cnt = 0;

                try
                {
                    // Read until small buffer is filled or end of file reached:
                    while( bytesRead < smallBuffer.length )
                    {
                        if( ( cnt = myAudioInputStream.read( smallBuffer,
                                     bytesRead, smallBuffer.length-bytesRead ) )
                             <= 0 )
                        {
                            endOfStream( SET, true );
                            break;
                        }
                        bytesRead += cnt;
                    }
                }
                catch( IOException e )
                {
                    errorMessage( "Exception thrown while reading from the " +
                                  "AudioInputStream (location #2)." );
                    printStackTrace( e );
                    return null;
                }

                // Keep track of the total number of bytes read:
                totalBytes += bytesRead;

                // Append the small buffer to the full buffer:
                fullBuffer = appendByteArrays( fullBuffer, smallBuffer,
                                               bytesRead );
            }
        }

        // Insert the converted data into a ByteBuffer
        byte[] data = convertAudioBytes( fullBuffer,
                                    myAudioFormat.getSampleSizeInBits() == 16 );

        // Wrap the data into an SoundBuffer:
        SoundBuffer soundBuffer = new SoundBuffer( data, myAudioFormat );

        // Close the audio input stream
        try
        {
            myAudioInputStream.close();
        }
        catch( IOException e )
        {}

        // Return the result:
        return soundBuffer;
    }

/**
 * Returns false if there is still more data available to be read in.
 * @return True if end of stream was reached.
 */
    public boolean endOfStream()
    {
        return endOfStream( GET, XXX );
    }

/**
 * Closes the audio stream and remove references to all instantiated objects.
 */
    public void cleanup()
    {
        if( myAudioInputStream != null )
        try
        {
            myAudioInputStream.close();
        }
        catch( Exception e )
        {}
        myAudioInputStream = null;
    }

/**
 * Returns the audio format of the data being returned by the read() and
 * readAll() methods.
 * @return Information wrapped into an AudioFormat context.
 */
    public AudioFormat getAudioFormat()
    {
        if( myAudioInputStream == null )
            return null;
        return myAudioInputStream.getFormat();
    }

/**
 * Internal method for synchronizing access to the boolean 'initialized'.
 * @param action GET or SET.
 * @param value New value if action == SET, or XXX if action == GET.
 * @return True if steam is initialized.
 */
    private synchronized boolean initialized( boolean action, boolean value )
    {
        if( action == SET )
            initialized = value;
        return initialized;
    }

/**
 * Internal method for synchronizing access to the boolean 'endOfStream'.
 * @param action GET or SET.
 * @param value New value if action == SET, or XXX if action == GET.
 * @return True if end of stream was reached.
 */
    private synchronized boolean endOfStream( boolean action, boolean value )
    {
        if( action == SET )
            endOfStream = value;
        return endOfStream;
    }

/**
 * Trims down the size of the array if it is larger than the specified
 * maximum length.
 * @param array Array containing audio data.
 * @param maxLength Maximum size this array may be.
 * @return New array.
 */
    private static byte[] trimArray( byte[] array, int maxLength )
    {
        byte[] trimmedArray = null;
        if( array != null && array.length > maxLength )
        {
            trimmedArray = new byte[maxLength];
            System.arraycopy( array, 0, trimmedArray, 0, maxLength );
        }
        return trimmedArray;
    }

/**
 * Converts sound bytes to little-endian format.
 * @param audio_bytes The original wave data
 * @param two_bytes_data For stereo sounds.
 * @return byte array containing the converted data.
 */
    private static byte[] convertAudioBytes( byte[] audio_bytes,
                                             boolean two_bytes_data )
    {
        ByteBuffer dest = ByteBuffer.allocateDirect( audio_bytes.length );
        dest.order( ByteOrder.nativeOrder() );
        ByteBuffer src = ByteBuffer.wrap( audio_bytes );
        src.order( ByteOrder.LITTLE_ENDIAN );
        if( two_bytes_data )
        {
            ShortBuffer dest_short = dest.asShortBuffer();
            ShortBuffer src_short = src.asShortBuffer();
            while( src_short.hasRemaining() )
            {
                dest_short.put(src_short.get());
            }
        }
        else
        {
            while( src.hasRemaining() )
            {
                dest.put( src.get() );
            }
        }
        dest.rewind();

        if( !dest.hasArray() )
        {
            byte[] arrayBackedBuffer = new byte[dest.capacity()];
            dest.get( arrayBackedBuffer );
            dest.clear();

            return arrayBackedBuffer;
        }

        return dest.array();
    }

/**
 * Creates a new array with the second array appended to the end of the first
 * array.
 * @param arrayOne The first array.
 * @param arrayTwo The second array.
 * @param length How many bytes to append from the second array.
 * @return Byte array containing information from both arrays.
 */
    private static byte[] appendByteArrays( byte[] arrayOne, byte[] arrayTwo,
                                            int length )
    {
        byte[] newArray;
        if( arrayOne == null && arrayTwo == null )
        {
            // no data, just return
            return null;
        }
        else if( arrayOne == null )
        {
            // create the new array, same length as arrayTwo:
            newArray = new byte[ length ];
            // fill the new array with the contents of arrayTwo:
            System.arraycopy( arrayTwo, 0, newArray, 0, length );
            arrayTwo = null;
        }
        else if( arrayTwo == null )
        {
            // create the new array, same length as arrayOne:
            newArray = new byte[ arrayOne.length ];
            // fill the new array with the contents of arrayOne:
            System.arraycopy( arrayOne, 0, newArray, 0, arrayOne.length );
            arrayOne = null;
        }
        else
        {
            // create the new array large enough to hold both arrays:
            newArray = new byte[ arrayOne.length + length ];
            System.arraycopy( arrayOne, 0, newArray, 0, arrayOne.length );
            // fill the new array with the contents of both arrays:
            System.arraycopy( arrayTwo, 0, newArray, arrayOne.length,
                              length );
            arrayOne = null;
            arrayTwo = null;
        }

        return newArray;
    }

/**
 * Prints an error message.
 * @param message Message to print.
 */
    private void errorMessage( String message )
    {
        logger.errorMessage( "CodecWav", message, 0 );
    }

/**
 * Prints an exception's error message followed by the stack trace.
 * @param e Exception containing the information to print.
 */
    private void printStackTrace( Exception e )
    {
        logger.printStackTrace( e, 1 );
    }
}
