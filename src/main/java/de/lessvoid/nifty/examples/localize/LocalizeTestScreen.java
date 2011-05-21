package de.lessvoid.nifty.examples.localize;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class LocalizeTestScreen implements ScreenController {
  private Screen screen;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    this.screen = newScreen;
  }

  public void onStartScreen() {
    // #3147118: Apply Special XML Markup replacement ${} on dynamic changes (http://sourceforge.net/tracker/?func=detail&aid=3147118&group_id=223898&atid=1059825)
    screen.findNiftyControl("label", Label.class).setText("${dialog.hello}");
  }

  public void onEndScreen() {
  }

  public String method1() {
    return "no param";
  }

  public String method2(final String param) {
    return "param: " + param;
  }

  public String sound() {
    return "outro";
  }
}
