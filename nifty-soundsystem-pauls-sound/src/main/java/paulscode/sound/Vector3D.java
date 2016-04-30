package paulscode.sound;

/**
 * The Vector3D class contains methods to simplify common 3D vector functions,
 * such as cross and dot product, normalize, etc.
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
public class Vector3D
{
    
/**
 * The vector's X coordinate.
 */
    public float x;
    
/**
 * The vector's Y coordinate.
 */
    public float y;
    
/**
 * The vector's Z coordinate.
 */
    public float z;

/**
 * Constructor:  Places the vector at the origin.
 */
    public Vector3D()
    {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
    }
    
/**
 * Constructor:  Places the vector at the specified 3D coordinates.
 * @param nx X coordinate for the new vector.
 * @param ny Y coordinate for the new vector.
 * @param nz Z coordinate for the new vector.
 */
    public Vector3D( float nx, float ny, float nz )
    {
        x = nx;
        y = ny;
        z = nz;
    }
    
/**
 * Returns a new instance containing the same information as this one.
 * @return A new Vector3D.
 */
    @Override
    public Vector3D clone()
    {
        return new Vector3D( x, y, z );
    }
    
/**
 * Returns a vector containing the cross-product: A cross B.
 * @param A First vector in the cross product.
 * @param B Second vector in the cross product.
 * @return A new Vector3D.
 */
    public Vector3D cross( Vector3D A, Vector3D B )
    {
        return new Vector3D(
                                A.y * B.z - B.y * A.z,
                                A.z * B.x - B.z * A.x,
                                A.x * B.y - B.x * A.y );
    }
    
/**
 * Returns a vector containing the cross-product: (this) cross B.
 * @param B Second vector in the cross product.
 * @return A new Vector3D.
 */
    public Vector3D cross( Vector3D B )
    {
        return new Vector3D(
                                y * B.z - B.y * z,
                                z * B.x - B.z * x,
                                x * B.y - B.x * y );

    }
    
/**
 * Returns the dot-product result of: A dot B.
 * @param A First vector in the dot product.
 * @param B Second vector in the dot product.
 * @return Dot product.
 */
    public float dot( Vector3D A, Vector3D B )
    {
        return( (A.x * B.x) + (A.y * B.y) + (A.z * B.z) );
    }
    
/**
 * Returns the dot-product result of: (this) dot B.
 * @param B Second vector in the dot product.
 * @return Dot product.
 */
    public float dot( Vector3D B )
    {
        return( (x * B.x) + (y * B.y) + (z * B.z) );
    }
    
/**
 * Returns the vector represented by: A + B.
 * @param A First vector.
 * @param B Vector to add to A.
 * @return A new Vector3D.
 */
    public Vector3D add( Vector3D A, Vector3D B )
    {
        return new Vector3D( A.x + B.x, A.y + B.y, A.z + B.z );
    }
    
/**
 * Returns the vector represented by: (this) + B.
 * @param B Vector to add to this one.
 * @return A new Vector3D.
 */
    public Vector3D add( Vector3D B )
    {
        return new Vector3D( x + B.x, y + B.y, z + B.z );
    }
    
/**
 * Returns the vector represented by: A - B.
 * @param A First vector.
 * @param B Vector to subtract from A.
 * @return A new Vector3D.
 */
    public Vector3D subtract( Vector3D A, Vector3D B )
    {
        return new Vector3D( A.x - B.x, A.y - B.y, A.z - B.z );
    }
    
/**
 * Returns the vector represented by: (this) - B.
 * @param B Vector to subtract from this one.
 * @return A new Vector3D.
 */
    public Vector3D subtract( Vector3D B )
    {
        return new Vector3D( x - B.x, y - B.y, z - B.z );
    }

/**
 * Returns the length of this vector.
 * @return Length.
 */
    public float length()
    {
        return (float) Math.sqrt( x * x + y * y + z * z );
    }
    
/**
 * Changes the length of this vector to 1.0.
 */
    public void normalize()
    {
        double t = Math.sqrt( x*x + y*y + z*z );
        x = (float) (x / t);
        y = (float) (y / t);
        z = (float) (z / t);
    }

/**
 * Returns a string depicting this vector.
 * @return "Vector3D (x, y, z)".
 */
    @Override
    public String toString()
    {
        return "Vector3D (" + x + ", " + y + ", " + z + ")";
    }
}
