package de.lessvoid.nifty.examples.tutorial.screen;

import java.util.ArrayList;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class MainPage implements ScreenController {
  private Nifty nifty;
  private Screen screen;
  private boolean mute = false;

  private ArrayList <String> pages = new ArrayList <String> ();
  private int pageIndex = 0;
  private int lastPageIndex = 0;
  private boolean lastPageWasActive = false;
  
  public void bind(final Nifty nifty, final Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
    /*
    pages.add("page1");
    pages.add("page2");
    pages.add("page3");
    pages.add("page4");
    pages.add("page5");
    pages.add("page6");
    pages.add("page7");
    pages.add("page8");
    pages.add("page9");
    pages.add("page10");
    pages.add("page11");
    pages.add("page12");
    pages.add("page13");
    pages.add("page14");
    pages.add("page15");
    pages.add("page16");
    pages.add("page17");
    pages.add("page18");
    */
    pages.add("page19");
    pages.add("page20");
    pages.add("page21");
    pages.add("page22");
    pages.add("page23");
    pages.add("page24");

    pageIndex = 0;
    lastPageIndex = -1;
    updatePage();
    updateBackButtonVisibility();
  }
  
  public void onStartScreen() {
  }
  
  public void onEndScreen() {
  }

  public void back() {
    pageIndex--;
    if (pageIndex < 0) {
      pageIndex = pages.size() - 1;
    }
    updateBackButtonVisibility();

    nifty.setAlternateKey("back");
    updatePage();
  }

  public void next() {
    pageIndex++;
    if (pageIndex >= pages.size()) {
      lastPageWasActive = true;
      pageIndex = 0;
    }
    updateBackButtonVisibility();

    nifty.setAlternateKey(null);
    updatePage();
  }

  private void updateBackButtonVisibility() {
    Element backButtonElement = screen.findElementByName("backButton");
    if (pageIndex == 0) {
      if (!lastPageWasActive) {
        backButtonElement.setVisible(false);
      }
    } else if (pageIndex == 1) {
      if (!backButtonElement.isVisible()) {
        backButtonElement.setVisible(true);
      }
    }

    Element currentPageElement = screen.findElementByName("curPage");
    currentPageElement.startEffect(EffectEventId.onCustom);
    currentPageElement.getRenderer(TextRenderer.class).changeText(String.valueOf(pageIndex + 1) + " / " + pages.size());
  }

  public void toggleMute() {
    System.out.println("toggleMute");
    mute = !mute;
    if (mute) {
      screen.findElementByName("muteButton").setStyle("muteButtonSoundOff");
      screen.findElementByName("muteButton").startEffect(EffectEventId.onCustom);
    } else {
      screen.findElementByName("muteButton").setStyle("muteButtonSoundOn");
      screen.findElementByName("muteButton").startEffect(EffectEventId.onCustom);
    }
  }

  private void updatePage() {
    CreateButtonControl buttonControl = new CreateButtonControl(pages.get(pageIndex), "page_" + pageIndex);
    buttonControl.create(nifty, screen, screen.findElementByName("pp"));

    Element element = screen.findElementByName("page_" + lastPageIndex);
    if (element != null) {
      nifty.removeElement(screen, element, new EndNotify() {
        public void perform() {
        }
      });
    }
    lastPageIndex = pageIndex;
  }

  public class CreateButtonControl extends ControlAttributes {
    public CreateButtonControl(final String name, final String id) {
      setId(id);
      setName(name);
    }

    public void create(
        final Nifty nifty,
        final Screen screen,
        final Element parent) {
      nifty.addControl(screen, parent, getStandardControl());
    }
  }
}