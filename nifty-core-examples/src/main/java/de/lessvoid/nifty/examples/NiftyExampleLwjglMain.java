package de.lessvoid.nifty.examples;

import java.util.logging.Logger;

import de.lessvoid.coregl.CoreLwjglSetup;
import de.lessvoid.coregl.CoreLwjglSetup.RenderLoopCallback;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.examples.usecase.UseCase_0001_FullScreenColorNode;
import de.lessvoid.nifty.examples.usecase.UseCase_0002_QuarterRootNodeWithTwoHorizontalChildNodes;
import de.lessvoid.nifty.renderer.lwjgl.NiftyRenderDeviceLwgl;

public class NiftyExampleLwjglMain {

  public static void main(final String[] args) throws Exception {
    // init LWJGL using some helper class
    CoreLwjglSetup setup = new CoreLwjglSetup();
    setup.initializeLogging();
    setup.initialize("Hello Nifty 2.0", 1024, 768);

    // create nifty instance
    final Nifty nifty = createNifty();

    // FIXME allow example class to be given by commandline, so that we can use this code to run multiple examples 
    new UseCase_0001_FullScreenColorNode(nifty);
    new UseCase_0002_QuarterRootNodeWithTwoHorizontalChildNodes(nifty);

    logScene(nifty);

    setup.renderLoop(new RenderLoopCallback() {
      @Override
      public boolean render(final float deltaTime) {
        nifty.update();
        nifty.render();
        return false;
      }
    });
  }

  private static void logScene(final Nifty nifty) {
    Logger log = Logger.getLogger(NiftyExampleLwjglMain.class.getName());
    log.info(nifty.getSceneInfoLog());
  }

  private static Nifty createNifty() {
    return new Nifty(new NiftyRenderDeviceLwgl());
  }
}
