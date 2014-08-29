package de.lessvoid.nifty.examples.all;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;

/**
 * @author void
 */
public class MenuController implements ScreenController {
  private Nifty nifty;
  private Screen screen;

  @Override
  public void bind(@Nonnull final Nifty niftyParam, @Nonnull final Screen screenParam) {
    this.nifty = niftyParam;
    this.screen = screenParam;
    hideIfThere("thumbHelloWorld");
    hideIfThere("thumbHint");
    hideIfThere("thumbMouse");
    hideIfThere("thumbMenu");
    hideIfThere("thumbDragAndDrop");
    hideIfThere("thumbTextAlign");
    hideIfThere("thumbTextField");
    hideIfThere("thumbDropDownList");
    hideIfThere("thumbScrollpanel");
    hideIfThere("thumbMultiplayer");
    hideIfThere("thumbConsole");
    hideIfThere("thumbCredits");
    hideIfThere("thumbExit");
  }

  private void hideIfThere(final String elementName) {
    Element element = screen.findElementById(elementName);
    if (element != null) {
      element.hide();
    }
  }

  @Override
  public final void onStartScreen() {
  }

  @Override
  public final void onEndScreen() {
  }

  public void helloWorld() {
    nifty.fromXml("helloworld/helloworld.xml", "start");
  }

  public void hint() {
    nifty.fromXml("hint/hint.xml", "start");
  }

  public void mouse() {
    nifty.fromXml("mouse/mouse.xml", "start");
  }

  public void menu() {
    nifty.fromXml("menu/menu.xml", "start");
  }

  public void dragAndDrop() {
    nifty.fromXml("dragndrop/dragndrop.xml", "start");
  }

  public void textfield() {
    nifty.fromXml("textfield/textfield.xml", "start");
  }

  public void textalign() {
    nifty.fromXml("textalign/textalign.xml", "start");
  }

  public void multiplayer() {
    nifty.fromXml("multiplayer/multiplayer.xml", "start");
  }

  public void console() {
    nifty.fromXml("console/console.xml", "start");
  }

  public void dropDown() {
    nifty.fromXml("controls/controls.xml", "start");
  }

  public void scrollpanel() {
    nifty.fromXml("scroll/scroll.xml", "start");
  }

  public void credits() {
    nifty.gotoScreen("outro");
  }

  public void exit() {
    nifty.createPopupWithId("popupExit", "popupExit");
    nifty.showPopup(screen, "popupExit", null);
  }

  public void popupExit(final String exit) {
    nifty.closePopup("popupExit", new EndNotify() {
      @Override
      public void perform() {
        if ("yes".equals(exit)) {
          nifty.setAlternateKey("fade");
          nifty.exit();
        }
      }
    }
    );
  }
}
