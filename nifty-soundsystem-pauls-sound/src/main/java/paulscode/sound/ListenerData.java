package paulscode.sound;

/**
 * The listenerData class is used to store information about the 
 * listener's position and orientation.  A ListenerData object can be obtained 
 * using SoundSystem's getListenerData() method.  See 
 * {@link paulscode.sound.Vector3D Vector3D} for more information about 3D 
 * coordinates and vectors.
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
public class ListenerData
{
/**
 * Listener's position in 3D space
 */
    public Vector3D position;
/**
 * A normalized vector indicating the direction the listener is facing
 */
    public Vector3D lookAt;
/**
 * A normalized vector indicating the up direction
 */
    public Vector3D up;
/**
 * Listener's velocity in world-space
 */
    public Vector3D velocity;
    
/**
 * Used for easy rotation along the x/z plane (for use in a first-person 
 * shooter type of application).
 */
    public float angle = 0.0f;
    
/**
 * Constructor: Set this listener data to the origin facing along the z-axis
 */
    public ListenerData()
    {
        position = new Vector3D( 0.0f, 0.0f, 0.0f );
        lookAt = new Vector3D( 0.0f, 0.0f, -1.0f );
        up = new Vector3D( 0.0f, 1.0f, 0.0f );
        velocity = new Vector3D( 0.0f, 0.0f, 0.0f );
        angle = 0.0f;
    }
    
/**
 * Constructor: Set this listener data to the specified values for position and 
 * orientation.
 * @param pX Listener's X coordinate.
 * @param pY Listener's Y coordinate.
 * @param pZ Listener's Z coordinate.
 * @param lX X element of the look-at direction.
 * @param lY Y element of the look-at direction.
 * @param lZ Z element of the look-at direction.
 * @param uX X element of the up direction.
 * @param uY Y element of the up direction.
 * @param uZ Z element of the up direction.
 * @param a Angle in radians that the listener is turned counterclockwise around the y-axis.
 */
    public ListenerData( float pX, float pY, float pZ, float lX, float lY,
                         float lZ, float uX, float uY, float uZ, float a )
    {
        position = new Vector3D( pX, pY, pZ );
        lookAt = new Vector3D( lX, lY, lZ );
        up = new Vector3D( uX, uY, uZ );
        velocity = new Vector3D( 0.0f, 0.0f, 0.0f );
        angle = a;
    }
    
/**
 * Constructor: Set this listener data to the specified values for position and 
 * orientation.
 * @param p Position of the listener in 3D space.
 * @param l Normalized vector indicating the direction which the listener is facing.
 * @param u Normalized vector indicating the up direction.
 * @param a Angle in radians that the listener is turned counterclockwise around the y-axis.
 */
    public ListenerData( Vector3D p, Vector3D l, Vector3D u, float a )
    {
        position = p.clone();
        lookAt = l.clone();
        up = u.clone();
        velocity = new Vector3D( 0.0f, 0.0f, 0.0f );
        angle = a;
    }
    
/**
 * Change this listener data using the specified coordinates for position and 
 * orientation.
 * @param pX Listener's X coordinate.
 * @param pY Listener's Y coordinate.
 * @param pZ Listener's Z coordinate.
 * @param lX X element of the look-at direction.
 * @param lY Y element of the look-at direction.
 * @param lZ Z element of the look-at direction.
 * @param uX X element of the up direction.
 * @param uY Y element of the up direction.
 * @param uZ Z element of the up direction.
 * @param a Angle in radians that the listener is turned counterclockwise around the y-axis.
 */
    public void setData(  float pX, float pY, float pZ, float lX, float lY,
                         float lZ, float uX, float uY, float uZ, float a )
    {
        position.x = pX;
        position.y = pY;
        position.z = pZ;
        lookAt.x = lX;
        lookAt.y = lY;
        lookAt.z = lZ;
        up.x = uX;
        up.y = uY;
        up.z = uZ;
        angle = a;
    }
    
/**
 * Change this listener data using the specified 3D vectors for position and 
 * orientation.
 * @param p Position of the listener in 3D space.
 * @param l Normalized vector indicating the direction which the listener is facing.
 * @param u Normalized vector indicating the up direction.
 * @param a Angle in radians that the listener is turned counterclockwise around the y-axis.
 */
    public void setData( Vector3D p, Vector3D l, Vector3D u, float a )
    {
        position.x = p.x;
        position.y = p.y;
        position.z = p.z;
        lookAt.x = l.x;
        lookAt.y = l.y;
        lookAt.z = l.z;
        up.x = u.x;
        up.y = u.y;
        up.z = u.z;
        angle = a;
    }
    
/**
 * Change this listener data to match the specified listener data.
 * @param l Listener data to use.
 */
    public void setData( ListenerData l )
    {
        position.x = l.position.x;
        position.y = l.position.y;
        position.z = l.position.z;
        lookAt.x = l.lookAt.x;
        lookAt.y = l.lookAt.y;
        lookAt.z = l.lookAt.z;
        up.x = l.up.x;
        up.y = l.up.y;
        up.z = l.up.z;
        angle = l.angle;
    }
    
/**
 * Change this listener's position using the specified coordinates.
 * @param x Listener's X coordinate.
 * @param y Listener's Y coordinate.
 * @param z Listener's Z coordinate.
 */
    public void setPosition( float x, float y, float z )
    {
        position.x = x;
        position.y = y;
        position.z = z;
    }
    
/**
 * Change this listener's position using the specified vector.
 * @param p New position.
 */
    public void setPosition( Vector3D p )
    {
        position.x = p.x;
        position.y = p.y;
        position.z = p.z;
    }
    
/**
 * Changes the listeners orientation using the specified coordinates.
 * @param lX X element of the look-at direction.
 * @param lY Y element of the look-at direction.
 * @param lZ Z element of the look-at direction.
 * @param uX X element of the up direction.
 * @param uY Y element of the up direction.
 * @param uZ Z element of the up direction.
 */
    public void setOrientation( float lX, float lY, float lZ,
                                float uX, float uY, float uZ )
    {
        lookAt.x = lX;
        lookAt.y = lY;
        lookAt.z = lZ;
        up.x = uX;
        up.y = uY;
        up.z = uZ;
    }
    
/**
 * Changes the listeners orientation using the specified vectors.
 * @param l Normalized vector representing the look-at direction.
 * @param u Normalized vector representing the up direction.
 */
    public void setOrientation( Vector3D l, Vector3D u )
    {
        lookAt.x = l.x;
        lookAt.y = l.y;
        lookAt.z = l.z;
        up.x = u.x;
        up.y = u.y;
        up.z = u.z;
    }

/**
 * Change this listener's velocity in world-space.
 * @param v New velocity.
 */
    public void setVelocity( Vector3D v )
    {
        velocity.x = v.x;
        velocity.y = v.y;
        velocity.z = v.z;
    }

/**
 * Change this listener's velocity in world-space.
 * @param x New velocity along world x-axis.
 * @param y New velocity along world y-axis.
 * @param z New velocity along world z-axis.
 */
    public void setVelocity( float x, float y, float z )
    {
        velocity.x = x;
        velocity.y = y;
        velocity.z = z;
    }
    
/**
 * Sets the listener's angle counterclockwise around the y-axis.
 * @param a Angle in radians.
 */
    public void setAngle( float a )
    {
        angle = a;
        lookAt.x = -1.0f * (float) Math.sin( angle );
        lookAt.z = -1.0f * (float) Math.cos( angle );
    }
}
