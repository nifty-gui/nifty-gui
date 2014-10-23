package de.lessvoid.nifty.examples.usecase;

import java.util.logging.Logger;

import de.lessvoid.coregl.lwjgl.CoreSetupLwjgl;
import de.lessvoid.coregl.lwjgl.LwjglCoreGL;
import de.lessvoid.coregl.spi.CoreGL;
import de.lessvoid.coregl.spi.CoreSetup;
import de.lessvoid.coregl.spi.CoreSetup.RenderLoopCallback;
import de.lessvoid.nifty.api.AccurateTimeProvider;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.renderer.opengl.NiftyInputDeviceOpenGL;
import de.lessvoid.nifty.renderer.opengl.NiftyRenderDeviceOpenGL;
import de.lessvoid.nifty.spi.NiftyInputDevice;

/**
 * A helper class that initializes the rendering subsystem and the main Nifty instance. It will then instantiate
 * the given class using the Nifty instance as it's only parameter.
 *
 * @author void
 */
public class UseCaseRunner {
  private static Logger log = Logger.getLogger(UseCaseRunner.class.getName());

  static void run(final Class<?> useCaseClass, final String[] args) throws Exception {
    CoreSetup setup = new CoreSetupLwjgl(new LwjglCoreGL());
    setup.initializeLogging("/logging.properties");
    setup.initialize(caption(useCaseClass.getSimpleName()), 1024, 768);
    setup.enableVSync(false);

    // create nifty instance
    final Nifty nifty = createNifty();
    final Object useCase = useCaseClass.getConstructor(Nifty.class).newInstance(nifty);
    logScene(nifty);

    setup.renderLoop(new RenderLoopCallback() {

      @Override
      public void init(final CoreGL gl) {
      }

      @Override
      public boolean render(final CoreGL gl, final float deltaTime) {
        nifty.update();
        return nifty.render();
      }

      @Override
      public boolean endLoop() {
        return false;
      }
    });
  }

  private static String caption(final String caption) {
    return "Nifty 2.0 (" + caption + ")";
  }

  private static void logScene(final Nifty nifty) {
    log.info(nifty.getSceneInfoLog());
  }

  private static Nifty createNifty() throws Exception {
    return new Nifty(createRenderDevice(), createInputDevice(), new AccurateTimeProvider());
  }

  private static NiftyRenderDeviceOpenGL createRenderDevice() throws Exception {
    return new NiftyRenderDeviceOpenGL(new LwjglCoreGL());
  }

  private static NiftyInputDevice createInputDevice() throws Exception {
    return new NiftyInputDeviceOpenGL();
  }
}
