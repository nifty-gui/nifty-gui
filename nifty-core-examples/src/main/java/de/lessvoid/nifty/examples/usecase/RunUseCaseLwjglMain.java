package de.lessvoid.nifty.examples.usecase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import de.lessvoid.coregl.CoreLwjglSetup;
import de.lessvoid.coregl.CoreLwjglSetup.RenderLoopCallback;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.renderer.lwjgl.NiftyRenderDeviceLwgl;

public class RunUseCaseLwjglMain {
  private static Logger log = Logger.getLogger(RunUseCaseLwjglMain.class.getName());

  public static void main(final String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println("Expected use case classname (without package name) to run.");
      System.err.println("Example: " + RunUseCaseLwjglMain.class.getName() + " UseCase_0001_FullScreenColorNode");
      System.exit(1);
    }

    // init LWJGL using some helper class
    CoreLwjglSetup setup = new CoreLwjglSetup();
    setup.initializeLogging("/logging.properties");
    setup.initialize("Hello Nifty 2.0", 1024, 768);

    // create nifty instance
    final Nifty nifty = createNifty();

    final UseCase useCase = loadUseCase(args[0], nifty);
    logScene(nifty);

    setup.renderLoop(new RenderLoopCallback() {
      @Override
      public boolean render(final float deltaTime) {
        useCase.update(nifty);
        nifty.update();
        nifty.render();
/*
        NumberFormat format = NumberFormat.getInstance();
        format.setMinimumFractionDigits(4);
        format.setMaximumFractionDigits(4);
        List<Integer> frameTimes = new ArrayList<Integer>();
        nifty.getStatistics().getUpdateTime(frameTimes);
        StringBuilder stuff = new StringBuilder();
        for (Integer i : frameTimes) {
          stuff.append(format.format(i / 100000.f));
          stuff.append(", ");
        }
        System.out.println(stuff.toString());
*/
        return false;
      }
    });
  }

  private static UseCase loadUseCase(final String clazzName, final Nifty nifty) throws Exception {
    Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(
        RunUseCaseLwjglMain.class.getPackage().getName() + "." + clazzName);
    log.info("loaded class [" + clazz + "]");

    return (UseCase) clazz.getConstructor(Nifty.class).newInstance(nifty);
  }

  private static void logScene(final Nifty nifty) {
    log.info(nifty.getSceneInfoLog());
  }

  private static Nifty createNifty() {
    return new Nifty(new NiftyRenderDeviceLwgl());
  }
}
