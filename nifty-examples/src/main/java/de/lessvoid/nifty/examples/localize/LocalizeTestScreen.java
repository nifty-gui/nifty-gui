package de.lessvoid.nifty.examples.localize;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;
import java.util.Properties;

public class LocalizeTestScreen implements ScreenController, NiftyExample {
  private Screen screen;

  @Override
  public void bind(@Nonnull final Nifty newNifty, @Nonnull final Screen newScreen) {
    this.screen = newScreen;
  }

  @Override
  public void onStartScreen() {
    // #3147118: Apply Special XML Markup replacement ${} on dynamic changes (http://sourceforge
    // .net/tracker/?func=detail&aid=3147118&group_id=223898&atid=1059825)
    screen.findNiftyControl("label", Label.class).setText("${dialog.hello}");
  }

  @Override
  public void onEndScreen() {
  }

  @Nonnull
  public String method1() {
    return "no param";
  }

  @Nonnull
  public String method2(final String param) {
    return "param: " + param;
  }

  @Nonnull
  public String sound() {
    return "outro";
  }

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "localize/localize.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Localize Example";
  }

  @Override
  public void prepareStart(@Nonnull Nifty nifty) {
    final Properties props = new Properties();
    props.put("void", ":)");
    nifty.setGlobalProperties(props);
  }
}
