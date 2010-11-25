package de.lessvoid.nifty.examples.helloniftybuilder;

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
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.Animator;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.renderer.jogl.input.JoglInputSystem;
import de.lessvoid.nifty.renderer.jogl.render.JoglRenderDevice;
import de.lessvoid.nifty.renderer.jogl.sound.SoundDeviceNullImpl;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * The Nifty Hello World. This time using the Nifty Builder classes to dynamically create Nifty
 * screens WITHOUT the xml.
 * 
 * @author void
 */
public class HelloNiftyBuilderExampleJoglMain implements ScreenController {
    private Nifty nifty;

    private Frame frame;

    private Animator animator;

    private HelloNiftyBuilderExampleJoglMain() {
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

                    // create a screen
                    Screen screen = new ScreenBuilder("start") {
                        {
                            controller(HelloNiftyBuilderExampleJoglMain.this);

                            layer(new LayerBuilder("layer") {
                                {
                                    backgroundColor("#003f");
                                    childLayoutCenter();

                                    panel(new PanelBuilder() {
                                        {
                                            id("panel");
                                            childLayoutCenter();
                                            height("25%");
                                            width("80%");
                                            alignCenter();
                                            valignCenter();
                                            backgroundColor("#f60f");
                                            visibleToMouse();
                                            interactOnClick("quit()");
                                            padding("10px");

                                            onStartScreenEffect(new EffectBuilder("move") {
                                                {
                                                    parameter("mode", "in");
                                                    parameter("direction", "top");
                                                    length(300);
                                                    startDelay(0);
                                                    inherit(true);
                                                }
                                            });

                                            onEndScreenEffect(new EffectBuilder("move") {
                                                {
                                                    parameter("mode", "out");
                                                    parameter("direction", "bottom");
                                                    length(300);
                                                    startDelay(0);
                                                    inherit(true);
                                                }
                                            });

                                            onHoverEffect(new HoverEffectBuilder("pulsate") {
                                                {
                                                    parameter("scaleFactor", "0.008");
                                                    parameter("startColor", "#f600");
                                                    parameter("endColor", "#ffff");
                                                    post(true);
                                                }
                                            });

                                            panel(new PanelBuilder() {
                                                {
                                                    childLayoutHorizontal();
                                                    alignCenter();
                                                    valignCenter();
                                                    width("100%");

                                                    /*image(new ImageBuilder() {
                                                        {
                                                            filename("nifty-logo-150x150.png");
                                                        }
                                                    });*/

                                                    text(new TextBuilder() {
                                                        {
                                                            text("Hello Nifty Builder World!!!");
                                                            font("aurulent-sans-17.fnt");
                                                            color("#000f");
                                                            width("*");
                                                            alignCenter();
                                                            valignCenter();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    }.build(nifty);

                    nifty.addScreen("start", screen);
                    nifty.gotoScreen("start");
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

    public static void main(final String[] args) {
        GLProfile.initSingleton(true);
        new HelloNiftyBuilderExampleJoglMain();
    }

    @Override
    public void bind(final Nifty nifty, final Screen screen) {
        System.out.println("bind()");
        this.nifty = nifty;
    }

    @Override
    public void onStartScreen() {
        System.out.println("onStartScreen()");
    }

    @Override
    public void onEndScreen() {
        System.out.println("onEndScreen()");
    }

    public void quit() {
        nifty.exit();
        // animator.stop();
        // frame.dispose();
        System.exit(0);
    }
}
