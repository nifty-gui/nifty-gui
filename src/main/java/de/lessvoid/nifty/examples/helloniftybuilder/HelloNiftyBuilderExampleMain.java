package de.lessvoid.nifty.examples.helloniftybuilder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectOnHoverAttributes;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * The Nifty Hello World. This time using the Nifty Builder classes
 * to dynamically create Nifty screens WITHOUT the xml.
 * @author void
 */
public class HelloNiftyBuilderExampleMain implements ScreenController {
  private Nifty nifty;

  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Hello Nifty Builder World")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new LwjglRenderDevice(),
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new TimeProvider());

    // create a screen
    Screen screen = new ScreenBuilder("start") {{
      controller(new HelloNiftyBuilderExampleMain());

      layer(new LayerBuilder("layer") {{
        backgroundColor("#003f");
        childLayoutCenter();

        panel(new PanelBuilder() {{
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

          onStartScreenEffect(new ControlEffectAttributes() {{
            setAttribute("name", "move");
            setAttribute("mode", "in");
            setAttribute("direction", "top");
            setAttribute("length", "300");
            setAttribute("startDelay", "0");
            setAttribute("inherit", "true");
          }});

          onEndScreenEffect(new ControlEffectAttributes() {{
            setAttribute("name", "move");
            setAttribute("mode", "out");
            setAttribute("direction", "bottom");
            setAttribute("length", "300");
            setAttribute("startDelay", "0");
            setAttribute("inherit", "true");
          }});

          onHoverEffect(new ControlEffectOnHoverAttributes() {{
            setAttribute("name", "pulsate");
            setAttribute("scaleFactor", "0.008");
            setAttribute("startColor", "#f600");
            setAttribute("endColor", "#ffff");
            setAttribute("post", "true");
          }});

          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            alignCenter();
            valignCenter();
            width("100%");

            image(new ImageBuilder() {{
              filename("nifty-logo-150x150.png");
            }});

            text(new TextBuilder() {{
              text("Hello Nifty Builder World!!!");
              font("aurulent-sans-17.fnt");
              color("#000f");
              width("*");
              alignCenter();
              valignCenter();
            }});
          }});
        }});
      }});
    }}.build(nifty);

    nifty.addScreen("start", screen);
    nifty.gotoScreen("start");

    // render
    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
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
  }
}
