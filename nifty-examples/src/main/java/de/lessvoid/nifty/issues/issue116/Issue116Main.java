package de.lessvoid.nifty.issues.issue116;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.checkbox.builder.CheckboxBuilder;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public class Issue116Main {

  private Issue116Main() {
  }

  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Issue 116")) {
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
        panel(new PanelBuilder() {{
          backgroundColor("#f00f");
          alignCenter();
          valignCenter();
          childLayoutVertical();
          width("50%");
          height("25%");
          panel(new PanelBuilder() {{
            alignLeft();
            childLayoutHorizontal();
            padding("10px");
            control(new CheckboxBuilder("enableDebugMode"));
            text(new TextBuilder() {{
              marginLeft("10px");
              text("nifty.setNiftyMethodInvokerDebugEnabled(true)");
              style("base-font");
              alignCenter();
              valignCenter();
            }});
          }});
          panel(new PanelBuilder() {{
            height("40px");
          }});
          text(new TextBuilder() {{
            text("Click me to crash and burn!");
            style("base-font");
            alignCenter();
            valignCenter();
            interactOnClick("crash()");
          }});
        }});
      }});
    }}.build(nifty);
  }

  public static class MyScreenController extends DefaultScreenController {
    @Override
    public void onStartScreen() {
    }

    @NiftyEventSubscriber(id="enableDebugMode")
    public void greenPanelVisible(final String id, final CheckBoxStateChangedEvent event) {
      nifty.setNiftyMethodInvokerDebugEnabled(event.isChecked());
    }

    public void crash() {
      throw new RuntimeException("CRASH");
    }
  }
}
