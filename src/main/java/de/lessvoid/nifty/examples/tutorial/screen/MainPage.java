package de.lessvoid.nifty.examples.tutorial.screen;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.LabelCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectOnHoverAttributes;
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
  private ArrayList <String> chapterCaption = new ArrayList <String> ();
  private Map <String, Integer> chapterPageMap = new Hashtable <String, Integer> ();
  private int pageIndex = 0;
  private int lastPageIndex = 0;
  private boolean lastPageWasActive = false;
  private Element chapterSelectPopup;
  
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

    chapterCaption.add("Welcome");
    chapterPageMap.put("0", pages.indexOf("pageWelcome"));

    chapterCaption.add("What's new in Nifty 1.2"); // ""
    chapterPageMap.put("1", pages.indexOf("pageChapterWhatsNewInNifty1.2"));

    chapterCaption.add("Nifty Basics"); // ""
    chapterPageMap.put("2", pages.indexOf("pageChapterI"));

    chapterCaption.add("Nifty Effects"); // ""
    chapterPageMap.put("3", pages.indexOf("pageChapterII"));

    chapterCaption.add("Connecting Java and Nifty XML"); // ""
    chapterPageMap.put("4", pages.indexOf("pageChapterIII"));

    chapterCaption.add("Advanced Nifty, Styles"); // ""
    chapterPageMap.put("5", pages.indexOf("pageChapterIV"));

    chapterCaption.add("Advanced Nifty, Controls"); // ""
    chapterPageMap.put("6", pages.indexOf("pageChapterV"));

    chapterCaption.add("Advanced Nifty, Custom Effects"); // ""
    chapterPageMap.put("7", pages.indexOf("pageChapterVI"));

    chapterCaption.add("Advanced Nifty, Slick2d Integration"); // ""
    chapterPageMap.put("8", pages.indexOf("pageChapterVII"));

    chapterCaption.add("The End"); // ""
    chapterPageMap.put("9", pages.indexOf("pageChapterVIII"));

    ControlEffectOnHoverAttributes textColorEffect = new ControlEffectOnHoverAttributes();
    textColorEffect.setName("textColor");
    textColorEffect.setAttribute("color", "#a22f");

    chapterSelectPopup = nifty.createPopup("chapterSelectPopup");
    Element chapterSelectElement = chapterSelectPopup.findElementByName("chapterSelect");
    int idx = 0;
    for (String label : chapterCaption) {
      LabelCreator createLabel = new LabelCreator(label);
      createLabel.setAlign("center");
      createLabel.setTextVAlign("center");
      createLabel.setTextHAlign("center");
      createLabel.setColor("#ccce");
      createLabel.setStyle("menuFont");
      createLabel.setVisibleToMouse("true");
      createLabel.addEffectsOnHover(textColorEffect);
      createLabel.setInteractOnClick("chapterSelect(" + idx + ")");
      createLabel.create(nifty, screen, chapterSelectElement);
      idx++;
    }

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

  public void showChapterSelect() {
    nifty.showPopup(screen, "chapterSelectPopup", null);
  }

  private void updateBackButtonVisibility() {
    Element backButtonElement = screen.findElementByName("backButton");
    if (pageIndex == 0) {
      if (!lastPageWasActive) {
        backButtonElement.setVisible(false);
      }
    } else if (pageIndex == 1) {
      showBackButton();
    }

    Element currentPageElement = screen.findElementByName("curPage");
    currentPageElement.startEffect(EffectEventId.onCustom);
    currentPageElement.getRenderer(TextRenderer.class).changeText(String.valueOf(pageIndex + 1) + " / " + pages.size());
  }

  private void showBackButton() {
    Element backButtonElement = screen.findElementByName("backButton");
    if (!backButtonElement.isVisible()) {
      backButtonElement.setVisible(true);
    }
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

  public void chapterSelect(final String chapterLabel) {
    nifty.closePopup(chapterSelectPopup.getId(), new EndNotify() {
      @Override
      public void perform() {
        pageIndex = chapterPageMap.get(chapterLabel);
        showBackButton();
        updatePage();
        updateBackButtonVisibility();
      }
    });
  }

  public void openLink(final String url) {
    if (!java.awt.Desktop.isDesktopSupported()) {
      System.err.println("Desktop is not supported (Can't open link)");
      return;
    }

    java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
    if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
      System.err.println("Desktop (BROWSE) is not supported (Can't open link)");
      return;
    }

    try {
      java.net.URI uri = new java.net.URI(url);
      desktop.browse(uri);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}