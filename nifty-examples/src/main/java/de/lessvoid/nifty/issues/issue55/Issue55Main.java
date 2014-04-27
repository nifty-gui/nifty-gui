package de.lessvoid.nifty.issues.issue55;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventAnnotationProcessor;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public class Issue55Main {

  private Issue55Main() {
  }

  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Issue 43")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new LwjglRenderDevice(),
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new AccurateTimeProvider());

    nifty.loadStyleFile("nifty-default-styles.xml");
    nifty.loadControlFile("nifty-default-controls.xml");
    createScreen(nifty);
    nifty.gotoScreen("start");

    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }

  static Screen createScreen(@Nonnull final Nifty nifty) {
    return new ScreenBuilder("start") {{
      controller(new MyScreenController());
      layer(new LayerBuilder("layer") {{
        backgroundColor("#55d5");
        childLayoutCenter();
        panel(new PanelBuilder("redPanel") {{
          backgroundColor("#c00f");
          alignCenter();
          valignCenter();
          childLayoutHorizontal();
          width("50%");
          height("50%");
          visibleToMouse();
        }});
      }});
    }}.build(nifty);
  }

  public static class MyScreenController extends DefaultScreenController {
    @Override
    public void onStartScreen() {
      NiftyEventAnnotationProcessor.process(this);
    }
    @NiftyEventSubscriber(id = "redPanel")
    public void onMouse(final String id, @Nonnull final NiftyMousePrimaryClickedEvent e) {
      // without the fix you would get this twice
      System.out.println("you should see this line only once when you click! " + e.getMouseX() + ", " + e.getMouseY());
    }
  }
}
