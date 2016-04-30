package paulscode.sound;

import javax.sound.sampled.AudioFormat;

/**
 * The SoundBuffer class is used to wrap audio data along with the format in
 * which the data is stored.
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
public class SoundBuffer
{
/**
 * The actual audio data.
 */
    public byte[] audioData;
/**
 * The audio format in which the data is stored.
 */
    public AudioFormat audioFormat;

/**
 * Constructor: Wraps the specified data with the specified audio format.
 *
 * @param audioData The actual audio data.
 * @param audioFormat The audio format in which the data is stored.
 */
    public SoundBuffer( byte[] audioData, AudioFormat audioFormat )
    {
        this.audioData = audioData;
        this.audioFormat = audioFormat;
    }
    
/**
 * Removes handles to all instantiated objects.
 */
    public void cleanup()
    {
        audioData = null;
        audioFormat = null;
    }

/**
 * Trims down the size of the audio data if it is larger than the specified
 * maximum length.
 *
 * @param maxLength Maximum size this buffer may be.
 */
    public void trimData( int maxLength )
    {
        if( audioData == null || maxLength == 0 )
            audioData = null;
        else if( audioData.length > maxLength )
        {
            byte[] trimmedArray = new byte[maxLength];
            System.arraycopy( audioData, 0, trimmedArray, 0,
                              maxLength );
            audioData = trimmedArray;
        }
    }
}
