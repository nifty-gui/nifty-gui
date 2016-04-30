package paulscode.sound;

import java.net.URL;
import javax.sound.sampled.AudioFormat;

/**
 * The ICodec interface provides a common interface for SoundSystem to use
 * for accessing external codec libraries.
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
public interface ICodec
{
/**
 * Should tell derived classes when they may need to reverse the byte order of
 * the data before returning it in the read() and readAll() methods.  The
 * reason for the reversBytOrder method is because some external codec
 * libraries produce audio data in a format that some external audio libraries
 * require to be reversed.  Derivatives of the Library and Source classes for
 * audio libraries which require this type of data to be reversed should call
 * the reverseByteOrder() method for all instances of ICodec that they use.
 * Derivatives of the ICodec interface for codec libraries which which produce
 * this type of data should use the reverseByteOrder() method to know when the
 * data needs to be reversed before returning it in the read() and readAll()
 * methods.  If a particular codec library does not produce this type of data,
 * its derived ICodec class may disregard any calls to the reverseByteOrder()
 * method.
 * @param b True if the calling audio library requires byte-reversal by some codec libraries.
 */
    public void reverseByteOrder( boolean b );
    
/**
 * Should make any preperations required before reading from the audio stream.
 * If another stream is already opened, it should be closed and a new audio
 * stream opened in its place.  This method is used internally by SoundSystem
 * not only to initialize a stream, but also to rewind streams and to switch
 * stream sources on the fly.
 * @return False if an error occurred or if end of stream was reached.
 */
    public boolean initialize( URL url );

/**
 * Should return false if the stream is busy initializing.  To prevent bad
 * data from being returned by this method, derived classes should internally
 * synchronize with any elements used by both the initialized() and initialize()
 * methods.
 * @return True if steam is initialized.
 */
    public boolean initialized();

/**
 * Should read in one stream buffer worth of audio data.  See
 * {@link paulscode.sound.SoundSystemConfig SoundSystemConfig} for more
 * information about accessing and changing default settings.
 * @return The audio data wrapped into a SoundBuffer context.
 */
    public SoundBuffer read();

/**
 * Should read in all the audio data from the stream (up to the default
 * "maximum file size".  See
 * {@link paulscode.sound.SoundSystemConfig SoundSystemConfig} for more
 * information about accessing and changing default settings.
 * @return the audio data wrapped into a SoundBuffer context.
 */
    public SoundBuffer readAll();

/**
 * Should return false if there is still more data available to be read in.  To
 * prevent bad data from being returned by this method, derived classes should
 * internally synchronize with any elements used in both the endOfStream() and
 * the read() or readAll() methods.
 * @return True if end of stream was reached.
 */
    public boolean endOfStream();

/**
 * Should close any open streams and remove references to all instantiated
 * objects.
 */
    public void cleanup();

/**
 * Should return the audio format of the data being returned by the read() and
 * readAll() methods.
 * @return Information wrapped into an AudioFormat context.
 */
    public AudioFormat getAudioFormat();
}
