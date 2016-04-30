package paulscode.sound;

/**
 * The CommandObject class is used to store arguments in the SoundSystem's
 * Command Queue.  Queued CommandObjects are then processed by the 
 * {@link paulscode.sound.CommandThread CommandThread}.  Commands are queued 
 * and executed in the background, so it is unlikely that the user will ever 
 * need to use this class.  
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
public class CommandObject
{
/**
 * Global identifier for the command to initialize the current sound library.
 */
    public static final int INITIALIZE                  =  1;
/**
 * Global identifier for the command to pre-load a sound file.
 */
    public static final int LOAD_SOUND                  =  2;
/**
 * Global identifier for the command to pre-load a sound file.
 */
    public static final int LOAD_DATA                   =  3;
/**
 * Global identifier for the command to remove a sound file from memory.
 */
    public static final int UNLOAD_SOUND                =  4;
/**
 * Global identifier for the command to queue a sound file.
 */
    public static final int QUEUE_SOUND                 =  5;
/**
 * Global identifier for the command to dequeue a sound file.
 */
    public static final int DEQUEUE_SOUND               =  6;
/**
 * Global identifier for the command to fade-out transition a source.
 */
    public static final int FADE_OUT                    =  7;
/**
 * Global identifier for the command to fade-out/in transition a source.
 */
    public static final int FADE_OUT_IN                 =  8;
/**
 * Global identifier for the command to check volume levels of fading sources.
 */
    public static final int CHECK_FADE_VOLUMES          =  9;
/**
 * Global identifier for the command to create a new source.
 */
    public static final int NEW_SOURCE                  = 10;
/**
 * Global identifier for the command to create a new raw data stream.
 */
    public static final int RAW_DATA_STREAM             = 11;
/**
 * Global identifier for the command to create a source and immediately play it.
 */
    public static final int QUICK_PLAY                  = 12;
/**
 * Global identifier for the command to set a source's position in 3D space.
 */
    public static final int SET_POSITION                = 13;
/**
 * Global identifier for the command to change a source's volume.
 */
    public static final int SET_VOLUME                  = 14;
/**
 * Global identifier for the command to change a source's pitch.
 */
    public static final int SET_PITCH                   = 15;
/**
 * Global identifier for the command to change a source's priority.
 */
    public static final int SET_PRIORITY                = 16;
/**
 * Global identifier for the command to tell a source whether or not to loop.
 */
    public static final int SET_LOOPING                 = 17;
/**
 * Global identifier for the command to set a source's attenuation model.
 */
    public static final int SET_ATTENUATION             = 18;
/**
 * Global identifier for the command to set a source's fade distance or rolloff 
 * factor.
 */
    public static final int SET_DIST_OR_ROLL             = 19;
/**
 * Global identifier for the command to change the Doppler factor.
 */
    public static final int CHANGE_DOPPLER_FACTOR       = 20;
/**
 * Global identifier for the command to change the Doppler velocity.
 */
    public static final int CHANGE_DOPPLER_VELOCITY     = 21;
/**
 * Global identifier for the command to set a source's velocity.
 */
    public static final int SET_VELOCITY                 = 22;
/**
 * Global identifier for the command to set a source's velocity.
 */
    public static final int SET_LISTENER_VELOCITY         = 23;
/**
 * Global identifier for the command to play a source.
 */
    public static final int PLAY                         = 24;
/**
 * Global identifier for the command to play a source.
 */
    public static final int FEED_RAW_AUDIO_DATA         = 25;
/**
 * Global identifier for the command to pause a source.
 */
    public static final int PAUSE                       = 26;
/**
 * Global identifier for the command to stop a source.
 */
    public static final int STOP                        = 27;
/**
 * Global identifier for the command to rewind a source.
 */
    public static final int REWIND                      = 28;
/**
 * Global identifier for the command to flush all queued data.
 */
    public static final int FLUSH                       = 29;
/**
 * Global identifier for the command to cull a source.
 */
    public static final int CULL                        = 30;
/**
 * Global identifier for the command to activate a source.
 */
    public static final int ACTIVATE                    = 31;
/**
 * Global identifier for the command to set a source as permanant or temporary.
 */
    public static final int SET_TEMPORARY               = 32;
/**
 * Global identifier for the command to delete a source.
 */
    public static final int REMOVE_SOURCE               = 33;
/**
 * Global identifier for the command to move the listner.
 */
    public static final int MOVE_LISTENER               = 34;
/**
 * Global identifier for the command to set the listener's position.
 */
    public static final int SET_LISTENER_POSITION       = 35;
/**
 * Global identifier for the command to turn the listener.
 */
    public static final int TURN_LISTENER               = 36;
/**
 * Global identifier for the command to set the listener's turn angle.
 */
    public static final int SET_LISTENER_ANGLE          = 37;
/**
 * Global identifier for the command to change the listener's orientation.
 */
    public static final int SET_LISTENER_ORIENTATION    = 38;
/**
 * Global identifier for the command to change the master volume.
 */
    public static final int SET_MASTER_VOLUME           = 39;
/**
 * Global identifier for the command to create a new library.
 */
    public static final int NEW_LIBRARY                 = 40;
    
/**
 * Any buffer required for a command.
 */
    public byte[]          buffer;
/**
 * Any int arguments required for a command.
 */
    public int[]          intArgs;
/**
 * Any float arguments required for a command.
 */
    public float[]        floatArgs;
/**
 * Any long arguments required for a command.
 */
    public long[]         longArgs;
/**
 * Any boolean arguments required for a command.
 */
    public boolean[]      boolArgs;
/**
 * Any String arguments required for a command.
 */
    public String[]       stringArgs;

/**
 * Any Class arguments required for a command.
 */
    public Class[]        classArgs;

/**
 * Any Object arguments required for a command.
 */
    public Object[]        objectArgs;
    
/**
 * Which command to execute.
 */
    public int Command;

/**
 * Constructor used to create a command which doesn't require any arguments.
 * @param cmd Which command to execute.
 */
    public CommandObject( int cmd )
    {
        Command = cmd;
    }
/**
 * Constructor used to create a command which requires one integer argument.
 * @param cmd Which command to execute.
 * @param i The integer argument needed to execute this command.
 */
    public CommandObject( int cmd, int i )
    {
        Command = cmd;
        intArgs = new int[1];
        intArgs[0] = i;
    }
/**
 * Constructor used to create a command which requires one Library Class
 * argument.
 * @param cmd Which command to execute.
 * @param c The Library Class argument needed to execute this command.
 */
    public CommandObject( int cmd, Class c )
    {
        Command = cmd;
        classArgs = new Class[1];
        classArgs[0] = c;
    }
/**
 * Constructor used to create a command which requires one float argument.
 * @param cmd Which command to execute.
 * @param f The float argument needed to execute this command.
 */
    public CommandObject( int cmd, float f )
    {
        Command = cmd;
        floatArgs = new float[1];
        floatArgs[0] = f;
    }
/**
 * Constructor used to create a command which requires one String argument.
 * @param cmd Which command to execute.
 * @param s The String argument needed to execute this command.
 */
    public CommandObject( int cmd, String s )
    {
        Command = cmd;
        stringArgs = new String[1];
        stringArgs[0] = s;
    }
/**
 * Constructor used to create a command which requires one Object argument.
 * @param cmd Which command to execute.
 * @param o The Object argument needed to execute this command.
 */
    public CommandObject( int cmd, Object o )
    {
        Command = cmd;
        objectArgs = new Object[1];
        objectArgs[0] = o;
    }
/**
 * Constructor used to create a command which requires one String argument and
 * one Object argument.
 * @param cmd Which command to execute.
 * @param s The String argument needed to execute this command.
 * @param o The Object argument needed to execute this command.
 */
    public CommandObject( int cmd, String s, Object o )
    {
        Command = cmd;
        stringArgs = new String[1];
        stringArgs[0] = s;
        objectArgs = new Object[1];
        objectArgs[0] = o;
    }
/**
 * Constructor used to create a command which requires one String argument and
 * one byte buffer argument.
 * @param cmd Which command to execute.
 * @param s The String argument needed to execute this command.
 * @param buff The byte buffer argument needed to execute this command.
 */
    public CommandObject( int cmd, String s, byte[] buff )
    {
        Command = cmd;
        stringArgs = new String[1];
        stringArgs[0] = s;
        buffer = buff;
    }
/**
 * Constructor used to create a command which requires one String argument, one
 * Object argument, and one long argument.
 * @param cmd Which command to execute.
 * @param s The String argument needed to execute this command.
 * @param o The Object argument needed to execute this command.
 * @param l The long argument needed to execute this command.
 */
    public CommandObject( int cmd, String s, Object o, long l )
    {
        Command = cmd;
        stringArgs = new String[1];
        stringArgs[0] = s;
        objectArgs = new Object[1];
        objectArgs[0] = o;
        longArgs = new long[1];
        longArgs[0] = l;
    }
/**
 * Constructor used to create a command which requires one String argument, one
 * Object argument, and two long arguments.
 * @param cmd Which command to execute.
 * @param s The String argument needed to execute this command.
 * @param o The Object argument needed to execute this command.
 * @param l1 The first long argument needed to execute this command.
 * @param l2 The second long argument needed to execute this command.
 */
    public CommandObject( int cmd, String s, Object o, long l1, long l2 )
    {
        Command = cmd;
        stringArgs = new String[1];
        stringArgs[0] = s;
        objectArgs = new Object[1];
        objectArgs[0] = o;
        longArgs = new long[2];
        longArgs[0] = l1;
        longArgs[1] = l2;
    }
/**
 * Constructor used to create a command which requires two String arguments.
 * @param cmd Which command to execute.
 * @param s1 The first String argument needed to execute this command.
 * @param s2 The second String argument needed to execute this command.
 */
    public CommandObject( int cmd, String s1, String s2 )
    {
        Command = cmd;
        stringArgs = new String[2];
        stringArgs[0] = s1;
        stringArgs[1] = s2;
    }
/**
 * Constructor used to create a command which requires a String and an int as 
 * arguments.
 * @param cmd Which command to execute.
 * @param s The String argument needed to execute this command.
 * @param i The integer argument needed to execute this command.
 */
    public CommandObject( int cmd, String s, int i )
    {
        Command = cmd;
        intArgs = new int[1];
        stringArgs = new String[1];
        intArgs[0] = i;
        stringArgs[0] = s;
    }
/**
 * Constructor used to create a command which requires a String and a float as 
 * arguments.
 * @param cmd Which command to execute.
 * @param s The String argument needed to execute this command.
 * @param f The float argument needed to execute this command.
 */
    public CommandObject( int cmd, String s, float f )
    {
        Command = cmd;
        floatArgs = new float[1];
        stringArgs = new String[1];
        floatArgs[0] = f;
        stringArgs[0] = s;
    }
/**
 * Constructor used to create a command which requires a String and a boolean 
 * as arguments.
 * @param cmd Which command to execute.
 * @param s The String argument needed to execute this command.
 * @param b The boolean argument needed to execute this command.
 */
    public CommandObject( int cmd, String s, boolean b )
    {
        Command = cmd;
        boolArgs = new boolean[1];
        stringArgs = new String[1];
        boolArgs[0] = b;
        stringArgs[0] = s;
    }
/**
 * Constructor used to create a command which requires three float arguments.
 * @param cmd Which command to execute.
 * @param f1 The first float argument needed to execute this command.
 * @param f2 The second float argument needed to execute this command.
 * @param f3 The third float argument needed to execute this command.
 */
    public CommandObject( int cmd, float f1, float f2, float f3 )
    {
        Command = cmd;
        floatArgs = new float[3];
        floatArgs[0] = f1;
        floatArgs[1] = f2;
        floatArgs[2] = f3;
    }
/**
 * Constructor used to create a command which a String and three float 
 * arguments.
 * @param cmd Which command to execute.
 * @param s The String argument needed to execute this command.
 * @param f1 The first float argument needed to execute this command.
 * @param f2 The second float argument needed to execute this command.
 * @param f3 The third float argument needed to execute this command.
 */
    public CommandObject( int cmd, String s, float f1, float f2, float f3 )
    {
        Command = cmd;
        floatArgs = new float[3];
        stringArgs = new String[1];
        floatArgs[0] = f1;
        floatArgs[1] = f2;
        floatArgs[2] = f3;
        stringArgs[0] = s;
    }
/**
 * Constructor used to create a command which requires six float arguments.
 * @param cmd Which command to execute.
 * @param f1 The first float argument needed to execute this command.
 * @param f2 The second float argument needed to execute this command.
 * @param f3 The third float argument needed to execute this command.
 * @param f4 The fourth float argument needed to execute this command.
 * @param f5 The fifth float argument needed to execute this command.
 * @param f6 The sixth float argument needed to execute this command.
 */
    public CommandObject( int cmd, float f1, float f2, float f3, float f4,
                          float f5, float f6 )
    {
        Command = cmd;
        floatArgs = new float[6];
        floatArgs[0] = f1;
        floatArgs[1] = f2;
        floatArgs[2] = f3;
        floatArgs[3] = f4;
        floatArgs[4] = f5;
        floatArgs[5] = f6;
    }
/**
 * Constructor used to create a command which requires several arguments.
 * @param cmd Which command to execute.
 * @param b1 The first boolean argument needed to execute this command.
 * @param b2 The second boolean argument needed to execute this command.
 * @param b3 The third boolean argument needed to execute this command.
 * @param s The String argument needed to execute this command.
 * @param o The Object argument needed to execute this command.
 * @param f1 The first float argument needed to execute this command.
 * @param f2 The second float argument needed to execute this command.
 * @param f3 The third float argument needed to execute this command.
 * @param i The integer argument needed to execute this command.
 * @param f4 The fourth float argument needed to execute this command.
 */
    public CommandObject( int cmd,
                          boolean b1, boolean b2, boolean b3,
                          String s, Object o,
                          float f1, float f2, float f3,
                          int i, float f4 )
    {
        Command = cmd;
        intArgs = new int[1];
        floatArgs = new float[4];
        boolArgs = new boolean[3];
        stringArgs = new String[1];
        objectArgs = new Object[1];
        intArgs[0] = i;
        floatArgs[0] = f1;
        floatArgs[1] = f2;
        floatArgs[2] = f3;
        floatArgs[3] = f4;
        boolArgs[0] = b1;
        boolArgs[1] = b2;
        boolArgs[2] = b3;
        stringArgs[0] = s;
        objectArgs[0] = o;
    }
/**
 * Constructor used to create a command which requires several arguments.
 * @param cmd Which command to execute.
 * @param b1 The first boolean argument needed to execute this command.
 * @param b2 The second boolean argument needed to execute this command.
 * @param b3 The third boolean argument needed to execute this command.
 * @param s The String argument needed to execute this command.
 * @param o The Object argument needed to execute this command.
 * @param f1 The first float argument needed to execute this command.
 * @param f2 The second float argument needed to execute this command.
 * @param f3 The third float argument needed to execute this command.
 * @param i The integer argument needed to execute this command.
 * @param f4 The fourth float argument needed to execute this command.
 * @param b4 The fourth boolean argument needed to execute this command.
 */
    public CommandObject( int cmd,
                          boolean b1, boolean b2, boolean b3,
                          String s,
                          Object o,
                          float f1, float f2, float f3,
                          int i, float f4,  boolean b4 )
    {
        Command = cmd;
        intArgs = new int[1];
        floatArgs = new float[4];
        boolArgs = new boolean[4];
        stringArgs = new String[1];
        objectArgs = new Object[1];
        intArgs[0] = i;
        floatArgs[0] = f1;
        floatArgs[1] = f2;
        floatArgs[2] = f3;
        floatArgs[3] = f4;
        boolArgs[0] = b1;
        boolArgs[1] = b2;
        boolArgs[2] = b3;
        boolArgs[3] = b4;
        stringArgs[0] = s;
        objectArgs[0] = o;
    }
/**
 * Constructor used to create a command which requires several arguments.
 * @param cmd Which command to execute.
 * @param o The Object argument needed to execute this command.
 * @param b The first boolean argument needed to execute this command.
 * @param s The String argument needed to execute this command.
 * @param f1 The first float argument needed to execute this command.
 * @param f2 The second float argument needed to execute this command.
 * @param f3 The third float argument needed to execute this command.
 * @param i The integer argument needed to execute this command.
 * @param f4 The fourth float argument needed to execute this command.
 */
    public CommandObject( int cmd,
                          Object o,
                          boolean b,
                          String s,
                          float f1, float f2, float f3,
                          int i,
                          float f4 )
    {
        Command = cmd;
        intArgs = new int[1];
        floatArgs = new float[4];
        boolArgs = new boolean[1];
        stringArgs = new String[1];
        objectArgs = new Object[1];
        intArgs[0] = i;
        floatArgs[0] = f1;
        floatArgs[1] = f2;
        floatArgs[2] = f3;
        floatArgs[3] = f4;
        boolArgs[0] = b;
        stringArgs[0] = s;
        objectArgs[0] = o;
    }
}
