package de.lessvoid.nifty.examples.helloniftybuilder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

/**
 * The Nifty Hello World. This time using the Nifty Builder classes
 * to dynamically create Nifty screens WITHOUT the xml.
 * @author void
 */
public class HelloNiftyBuilderExampleMain implements ScreenController, NiftyExample {
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
        new AccurateTimeProvider());

    HelloNiftyBuilderExampleMain screenController = new HelloNiftyBuilderExampleMain();
    screenController.prepareStart(nifty);

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

  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return null;
  }

  @Override
  public String getTitle() {
    return "Hello Nifty Builder World";
  }

  @Override
  public void prepareStart(final Nifty nifty) {
    // create a screen
    Screen screen = new ScreenBuilder("start") {{
      controller(HelloNiftyBuilderExampleMain.this);

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

          onStartScreenEffect(new EffectBuilder("move") {{
            effectParameter("mode", "in");
            effectParameter("direction", "top");
            length(300);
            startDelay(0);
            inherit(true);
          }});

          onEndScreenEffect(new EffectBuilder("move") {{
            effectParameter("mode", "out");
            effectParameter("direction", "bottom");
            length(300);
            startDelay(0);
            inherit(true);
          }});

          onHoverEffect(new HoverEffectBuilder("pulsate") {{
            effectParameter("scaleFactor", "0.008");
            effectParameter("startColor", "#f600");
            effectParameter("endColor", "#ffff");
            post(true);
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
  }
}
