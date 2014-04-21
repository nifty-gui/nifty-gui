package de.lessvoid.nifty.issues.issue83;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.events.NiftyMouseEvent;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public class Issue83Main {

  private Issue83Main() {
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
        panel(new PanelBuilder() {{
          backgroundColor("#000f");
          alignCenter();
          valignCenter();
          childLayoutHorizontal();
          width("75%");
          height("50%");
          panel(new PanelBuilder() {{
            width("50%");
            height("100%");
            childLayoutVertical();
            valignCenter();
            control(new LabelBuilder("redPanelLabel", "---\n\n\n") {{
              color("#a00f");
              alignLeft();
              valignCenter();
              marginLeft("10px");
              width("100%");
            }});
            control(new LabelBuilder("greenPanelLabel", "---\n\n\n") {{
              color("#0a0f");
              alignLeft();
              valignCenter();
              marginLeft("10px");
              width("100%");
            }});
          }});
          panel(new PanelBuilder("redPanel") {{
            backgroundColor("#a00f");
            width("50%");
            height("100%");
            childLayoutCenter();
            visibleToMouse();
            panel(new PanelBuilder("greenPanel") {{
              backgroundColor("#0a0f");
              width("50%");
              height("50%");
              childLayoutCenter();
              visibleToMouse();
            }});
          }});
        }});
      }});
    }}.build(nifty);
  }

  public static class MyScreenController extends DefaultScreenController {
    @Override
    public void onStartScreen() {
      updateText("redPanelLabel", "---");
      updateText("greenPanelLabel", "---");
    }
    @NiftyEventSubscriber(pattern = ".*Panel")
    public void onMouse(final String id, @Nonnull final NiftyMouseEvent event) {
      updateText(id + "Label", event.getMouseX() + ", " + event.getMouseY() + ", " + event.getMouseWheel() + "\n" +
              event.isButton0Down() + ", " + event.isButton1Down() + ", " + event.isButton2Down() + "\n" +
              event.isButton0Release() + ", " + event.isButton1Release() + ", " + event.isButton2Release());
    }
    private void updateText(final String labelId, final String text) {
      nifty.getCurrentScreen().findNiftyControl(labelId, Label.class).setText(text);
    }
  }
}
