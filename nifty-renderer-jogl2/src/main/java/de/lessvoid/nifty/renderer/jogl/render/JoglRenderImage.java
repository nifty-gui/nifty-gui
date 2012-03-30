package de.lessvoid.nifty.renderer.jogl.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.logging.Logger;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.media.opengl.glu.GLU;

import com.jogamp.common.nio.Buffers;

import de.lessvoid.nifty.renderer.jogl.render.io.ImageData;
import de.lessvoid.nifty.renderer.jogl.render.io.ImageIOImageData;
import de.lessvoid.nifty.renderer.jogl.render.io.TGAImageData;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * @author Julien Gouesse
 */
public class JoglRenderImage implements RenderImage {

    private Logger log = Logger.getLogger(JoglRenderImage.class.getName());

    private int width;

    private int height;

    private int textureWidth;

    private int textureHeight;

    private int textureId;

    private GLU glu;

    public JoglRenderImage(final String name, final boolean filterParam, final NiftyResourceLoader resourceLoader) {
        try {
            log.fine("loading image: " + name);
            glu = new GLU();
            ImageData imageLoader;
            if (name.endsWith(".tga")) {
                imageLoader = new TGAImageData();
            }
            else {
                imageLoader = new ImageIOImageData();
            }
            ByteBuffer imageData = imageLoader.loadImage(resourceLoader.getResourceAsStream(name));
            imageData.rewind();
            width = imageLoader.getWidth();
            height = imageLoader.getHeight();
            textureWidth = imageLoader.getTexWidth();
            textureHeight = imageLoader.getTexHeight();
            createTexture(imageData, textureWidth, textureHeight, 0,
                    imageLoader.getDepth() == 32 ? GL.GL_RGBA : GL.GL_RGB);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

    public void dispose() {
        // do nothing
    }

    private void createTexture(final ByteBuffer textureBuffer, final int width, final int height,
            final int filter, final int srcPixelFormat) throws Exception {
        final GL gl = GLContext.getCurrentGL();
        textureId = createTextureID();
        int minFilter = GL.GL_NEAREST;
        int magFilter = GL.GL_NEAREST;
        bind();

        int componentCount = 1;

        IntBuffer temp = Buffers.newDirectIntBuffer(16);
        gl.glGetIntegerv(GL.GL_MAX_TEXTURE_SIZE, temp);
        checkGLError();

        int max = temp.get(0);
        if ((width > max) || (height > max)) {
            throw new Exception("Attempt to allocate a texture to big for the current hardware");
        }

        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, minFilter);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, magFilter);
        checkGLError();

        if (minFilter == GL.GL_LINEAR_MIPMAP_NEAREST) {
            glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, componentCount, width, height, srcPixelFormat,
                    GL.GL_UNSIGNED_BYTE, textureBuffer);
        }
        else {
            gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 4, width, height, 0, srcPixelFormat,
                    GL.GL_UNSIGNED_BYTE, textureBuffer);
        }
        checkGLError();
    }

    private int createTextureID() {
        IntBuffer tmp = createIntBuffer(1);
        final GL gl = GLContext.getCurrentGL();
        gl.glGenTextures(1, tmp);
        checkGLError();
        return tmp.get(0);
    }

    private IntBuffer createIntBuffer(final int size) {
        ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
        temp.order(ByteOrder.nativeOrder());
        return temp.asIntBuffer();
    }

    public void bind() {
        final GL gl = GLContext.getCurrentGL();
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);
        checkGLError();
    }

    private void checkGLError() {
        final GL gl = GLContext.getCurrentGL();
        int error = gl.glGetError();
        if (error != GL.GL_NO_ERROR) {
            String glerrmsg = glu.gluErrorString(error);
            log.warning("OpenGL Error: (" + error + ") " + glerrmsg);
        }
    }
}
