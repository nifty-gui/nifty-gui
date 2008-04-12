/*
 * Created on 20.02.2005
 */
package de.lessvoid.font;

import java.nio.ByteBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.glu.GLU;

/**
 * @author void
 *
 */
public class Tools
{
  private static Log log = LogFactory.getLog( Tools.class );
  
  /**
   * 
   * @param fkt
   */
  public static void checkGLError( String fkt )
  {
     // return;
     
    int error= GL11.glGetError();
    if( error != GL11.GL_NO_ERROR )
    {
      String glerrmsg= GLU.gluErrorString( error );
      log.error( "OpenGL Error: (" + error + ") " + glerrmsg + ", " + fkt );
    }
    
  }

  public static ByteBuffer toByteString(String str, boolean isNullTerminated)
  {
    int length = str.length();
    if (isNullTerminated)
      length++;
    ByteBuffer buff = BufferUtils.createByteBuffer(length);
    buff.put( str.getBytes() );
    
    if (isNullTerminated)
      buff.put( (byte)0 );

    buff.flip();
    return buff;
  }
}
