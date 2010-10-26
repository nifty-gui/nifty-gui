/*
 * Created on 20.02.2005
 */
package de.lessvoid.nifty.renderer.jogl.render.font;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.media.opengl.glu.GLU;

import com.jogamp.common.nio.Buffers;

/**
 * @author void
 */
public class Tools {
    private static Logger log = Logger.getLogger(Tools.class.getName());

    private static GLU glu;

    /**
     * @param fkt
     */
    public static void checkGLError(String fkt) {
        // return;
        final GL gl = GLContext.getCurrentGL();
        int error = gl.glGetError();
        if (error != GL.GL_NO_ERROR) {
            if (glu == null) {
                glu = new GLU();
            }
            String glerrmsg = glu.gluErrorString(error);
            log.warning("OpenGL Error: (" + error + ") " + glerrmsg + ", " + fkt);
        }

    }

    public static ByteBuffer toByteString(String str, boolean isNullTerminated) {
        int length = str.length();
        if (isNullTerminated) {
            length++;
        }
        ByteBuffer buff = Buffers.newDirectByteBuffer(length);
        buff.put(str.getBytes());

        if (isNullTerminated) {
            buff.put((byte) 0);
        }

        buff.flip();
        return buff;
    }
}
