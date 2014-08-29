package de.lessvoid.nifty.examples.helloniftybuilder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.*;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The Nifty Hello World. This time using the Nifty Builder classes
 * to dynamically create Nifty screens WITHOUT the xml.
 *
 * @author void
 */
public class HelloNiftyBuilderExample implements ScreenController, NiftyExample {
  private Nifty nifty;

  @Override
  public void bind(@Nonnull final Nifty nifty, @Nonnull final Screen screen) {
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

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nullable
  @Override
  public String getMainXML() {
    return null;
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Hello Nifty Builder World";
  }

  @Override
  public void prepareStart(@Nonnull final Nifty nifty) {
    // create a screen
    Screen screen = new ScreenBuilder("start") {{
      controller(HelloNiftyBuilderExample.this);

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
