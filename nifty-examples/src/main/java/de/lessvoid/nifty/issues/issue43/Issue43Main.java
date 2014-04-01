package de.lessvoid.nifty.issues.issue43;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.checkbox.builder.CheckboxBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public class Issue43Main {

  private Issue43Main() {
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
            panel(new PanelBuilder() {{
              alignLeft();
              childLayoutHorizontal();
              padding("10px");
              control(new CheckboxBuilder("redPanelVisible"));
              text(new TextBuilder() {{
                marginLeft("10px");
                text("Hide Red Panel");
                style("base-font");
                alignCenter();
                valignCenter();
              }});
            }});
            panel(new PanelBuilder() {{
              alignLeft();
              childLayoutHorizontal();
              padding("10px");
              control(new CheckboxBuilder("greenPanelVisible"));
              text(new TextBuilder() {{
                marginLeft("10px");
                text("Hide Green Panel");
                style("base-font");
                alignCenter();
                valignCenter();
              }});
            }});
            control(new LabelBuilder("redPanelLabel", "---") {{
              alignLeft();
              marginLeft("10px");
              width("100%");
            }});
            control(new LabelBuilder("greenPanelLabel", "---") {{
              alignLeft();
              marginLeft("10px");
              width("100%");
            }});
          }});
          panel(new PanelBuilder("redPanel") {{
            backgroundColor("#a00f");
            width("50%");
            height("100%");
            childLayoutCenter();
            panel(new PanelBuilder("greenPanel") {{
              backgroundColor("#0a0f");
              width("50%");
              height("50%");
              childLayoutCenter();
            }});
          }});
        }});
      }});
    }}.build(nifty);
  }

  public static class MyScreenController extends DefaultScreenController {
    @Override
    public void onStartScreen() {
      update("redPanel", "redPanelLabel", false);
      update("greenPanel", "greenPanelLabel", false);
    }
    @NiftyEventSubscriber(id="redPanelVisible")
    public void redPanelVisible(final String id, final CheckBoxStateChangedEvent event) {
      update("redPanel", "redPanelLabel", event.isChecked());
    }
    @NiftyEventSubscriber(id="greenPanelVisible")
    public void greenPanelVisible(final String id, final CheckBoxStateChangedEvent event) {
      update("greenPanel", "greenPanelLabel", event.isChecked());
    }
    private void update(final String panelId, final String labelId, final boolean checked) {
      Element panel = nifty.getCurrentScreen().findElementById(panelId);
      panel.setVisible(!checked);
      nifty.getCurrentScreen().findNiftyControl(labelId, Label.class).setText("Visible: " + panel.isVisible());
    }
  }
}
