package de.lessvoid.nifty.examples.helloworld;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.Animator;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.renderer.jogl.input.JoglInputSystem;
import de.lessvoid.nifty.renderer.jogl.render.JoglRenderDevice;
import de.lessvoid.nifty.renderer.jogl.sound.SoundDeviceNullImpl;
import de.lessvoid.nifty.tools.TimeProvider;

public class HelloWorldExampleJoglMain {

    private Nifty nifty;

    private Frame frame;

    private Animator animator;

    /**
     * Prevent instantiation of this class.
     */
    private HelloWorldExampleJoglMain() {
        GLCanvas canvas = new GLCanvas();
        canvas.addGLEventListener(new GLEventListener() {

            @Override
            public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

            }

            @Override
            public void init(GLAutoDrawable drawable) {
                final GL2 gl = GLContext.getCurrentGL().getGL2();
                IntBuffer viewportBuffer = Buffers.newDirectIntBuffer(4);
                gl.glGetIntegerv(GL.GL_VIEWPORT, viewportBuffer);
                int viewportWidth = viewportBuffer.get(2);
                int viewportHeight = viewportBuffer.get(3);

                // GL11.glViewport(0, 0, Display.getDisplayMode().getWidth(),
                // Display.getDisplayMode().getHeight());
                gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
                gl.glLoadIdentity();
                gl.glOrtho(0, viewportWidth, viewportHeight, 0, -9999, 9999);

                gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
                gl.glLoadIdentity();

                // Prepare Rendermode
                gl.glDisable(GL.GL_DEPTH_TEST);
                gl.glEnable(GL.GL_BLEND);
                gl.glDisable(GL.GL_CULL_FACE);

                gl.glEnable(GL2ES1.GL_ALPHA_TEST);
                gl.glAlphaFunc(GL.GL_NOTEQUAL, 0);

                gl.glDisable(GLLightingFunc.GL_LIGHTING);
                gl.glDisable(GL.GL_TEXTURE_2D);

                gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                gl.glClear(GL.GL_COLOR_BUFFER_BIT);
                gl.glEnable(GL.GL_TEXTURE_2D);

                if (nifty == null) {
                    // create nifty
                    nifty = new Nifty(new JoglRenderDevice(), new SoundDeviceNullImpl(),
                            new JoglInputSystem(), new TimeProvider());
                    nifty.fromXml("helloworld/helloworld.xml", "start");
                }
            }

            @Override
            public void dispose(GLAutoDrawable drawable) {
            }

            @Override
            public void display(GLAutoDrawable drawable) {
                if (nifty != null) {
                    nifty.render(true);
                }
            }
        });
        frame = new Frame("NiftyGUI with JOGL");
        animator = new Animator(canvas);
        frame.add(canvas);
        frame.setSize(640, 480);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });
        frame.setVisible(true);
        animator.start();
        canvas.requestFocus();
    }

    /**
     * Main method.
     * 
     * @param args arguments
     */
    public static void main(final String[] args) {
        new HelloWorldExampleJoglMain();
    }

    public void quit() {
        nifty.exit();
        // animator.stop();
        // frame.dispose();
        System.exit(0);
    }
}
