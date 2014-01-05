package de.lessvoid.nifty.examples.usecase;

import java.util.logging.Logger;

import de.lessvoid.coregl.CoreFactory;
import de.lessvoid.coregl.CoreSetup;
import de.lessvoid.coregl.CoreSetup.RenderLoopCallback2;
import de.lessvoid.coregl.lwjgl.CoreFactoryLwjgl;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.renderer.lwjgl.NiftyRenderDeviceLwgl;

/**
 * A helper class that initializes the rendering subsystem and the main Nifty instance. It will then instantiate
 * the given class using the Nifty instance as it's only parameter.
 *
 * @author void
 */
public class UseCaseRunner {
  private static Logger log = Logger.getLogger(UseCaseRunner.class.getName());
  private static float time;

  static void run(final Class<?> useCaseClass, final String[] args) throws Exception {
    CoreFactory factory = CoreFactoryLwjgl.create();

    CoreSetup setup = factory.createSetup();
    setup.initializeLogging("/logging.properties");
    setup.initialize(caption(useCaseClass.getSimpleName()), 1024, 768);
    setup.enableVSync(false);

    // create nifty instance
    final Nifty nifty = createNifty();
    final Object useCase = useCaseClass.getConstructor(Nifty.class).newInstance(nifty);
    logScene(nifty);

    setup.renderLoop2(new RenderLoopCallback2() {
      @Override
      public boolean render(final float deltaTime) {
        updateUseCase(nifty, useCase, deltaTime);
        nifty.update();
        return nifty.render();
      }

      @Override
      public boolean shouldEnd() {
        return false;
      }

      private void updateUseCase(final Nifty nifty, final Object useCase, final float deltaTime) {
        if (!(useCase instanceof UseCaseUpdateable)) {
          return;
        }
        ((UseCaseUpdateable) useCase).update(nifty, deltaTime);
      }
    });
  }

  private static String caption(final String caption) {
    return "Nifty 2.0 (" + caption + ")";
  }

  private static void logScene(final Nifty nifty) {
    log.info(nifty.getSceneInfoLog());
  }

  private static Nifty createNifty() {
    return new Nifty(new NiftyRenderDeviceLwgl());
  }
}
