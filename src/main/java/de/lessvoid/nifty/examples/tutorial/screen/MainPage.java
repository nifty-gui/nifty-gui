package de.lessvoid.nifty.examples.tutorial.screen;

import java.util.ArrayList;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class MainPage implements ScreenController, KeyInputHandler {
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

    pages.add("pageWelcome");
    pages.add("pageIntro");
    pages.add("pageChapterWhatsNewInNifty1.2");
    pages.add("pageNifty1.2");
    pages.add("pageChapterI");
    pages.add("pageBasicsJava1");
    pages.add("pageBasicsJava1b");
    pages.add("pageBasicsJava1c");
    pages.add("pageBasicsJava2");
    pages.add("pageBasicsXML1");
    pages.add("pageBasicsXML2");
    pages.add("pageBasicsXMLLayout1");
    pages.add("pageBasicsXMLLayout2");
    pages.add("pageBasicsXMLLayout3");
    pages.add("pageChapterII");
    pages.add("pageEffects1");
    pages.add("pageEffects2");
    pages.add("pageEffects3");
    pages.add("pageChapterIII");
    pages.add("pageConnecting1");
    pages.add("pageConnecting2");
    pages.add("pageBasicsEnd");
    pages.add("pageChapterIV");
    pages.add("pageAdvancedStyles1");
    pages.add("pageAdvancedStyles2");
    pages.add("pageAdvancedStyles3");
    pages.add("pageChapterV");
    pages.add("pageAdvancedControls1");
    pages.add("pageAdvancedControls2");
    pages.add("pageAdvancedControls3");
    pages.add("pageAdvancedControls4");
    pages.add("pageAdvancedControls5");
    pages.add("pageAdvancedControls6");
    pages.add("pageAdvancedControls7");
    pages.add("pageAdvancedControls8");
    pages.add("pageAdvancedControls9");
    pages.add("pageChapterVI");
    pages.add("pageCustomEffects1");
    pages.add("pageChapterVII");
    pages.add("pageSlick1");
    pages.add("pageSlick2");
    pages.add("pageSlick3");
    pages.add("pageChapterVIII");
    pages.add("pageTheEnd");

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
    String pageName = pages.get(pageIndex);
    CreateButtonControl buttonControl = new CreateButtonControl(pageName, "page_" + pageIndex);
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

  public boolean keyEvent(final NiftyInputEvent inputEvent) {
    if (NiftyInputEvent.MoveCursorRight.equals(inputEvent)) {
      screen.findElementByName("nextButton").onClick();
      return true;
    } else if (NiftyInputEvent.MoveCursorLeft.equals(inputEvent)) {
      screen.findElementByName("backButton").onClick();
      return true;
    }
    return false;
  }
}