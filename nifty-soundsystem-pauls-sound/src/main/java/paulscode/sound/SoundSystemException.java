package paulscode.sound;

/**
 * The SoundSystemException class is used to provide information about serious
 * errors.
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
public class SoundSystemException extends Exception
{
/**
 * Global identifier for no problem.
 */
    public static final int ERROR_NONE                              =  0;
/**
 * Global identifier for a generic exception.
 */
    public static final int UNKNOWN_ERROR                          =  1;
/**
 * Global identifier for a null parameter.
 */
    public static final int NULL_PARAMETER                          =  2;
/**
 * Global identifier for a class type mismatch.
 */
    public static final int CLASS_TYPE_MISMATCH                     =  3;
/**
 * Global identifier for the sound library does not exist.
 */
    public static final int LIBRARY_NULL                            =  4;
/**
 * Global identifier for the sound library does not exist.
 */
    public static final int LIBRARY_TYPE                            =  5;
    
/**
 * Holds a global identifier indicating the type of exception.
 */
    private int myType = UNKNOWN_ERROR;
    
/**
 * Constructor: Generic exception.  Specify the error message.
 */
    public SoundSystemException( String message )
    {
        super( message );
    }
    
/**
 * Constructor: Specify the error message and type of exception.
 * @param message Description of the problem.
 * @param type Global identifier for type of exception.
 */
    public SoundSystemException( String message, int type )
    {
        super( message );
        myType = type;
    }
    
    public int getType()
    {
        return myType;
    }
}
