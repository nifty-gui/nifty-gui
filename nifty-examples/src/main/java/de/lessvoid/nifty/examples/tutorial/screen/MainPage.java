package de.lessvoid.nifty.examples.tutorial.screen;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Draggable;
import de.lessvoid.nifty.controls.DraggableDragCanceledEvent;
import de.lessvoid.nifty.controls.DraggableDragStartedEvent;
import de.lessvoid.nifty.controls.Droppable;
import de.lessvoid.nifty.controls.DroppableDroppedEvent;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectOnHoverAttributes;
import de.lessvoid.nifty.controls.label.builder.CreateLabelControl;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.loaderv2.types.ElementType;
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
    pages.add("pageBasicsLoadXml");
    pages.add("pageChapterII");
    pages.add("pageBasicsXML1");
    pages.add("pageBasicsXML2");
    pages.add("pageBasicsXMLLayout1");
    pages.add("pageBasicsXMLLayout2");
    pages.add("pageBasicsXMLLayout3");
    pages.add("pageBasicsXMLMarkup");
    pages.add("pageBasicsXMLLocalization");
    pages.add("pageChapterIII");
    pages.add("pageEffects1");
    pages.add("pageEffects2");
    pages.add("pageEffects3");
    pages.add("pageCustomEffects1");
    pages.add("pageChapterIV");
    pages.add("pageConnecting1");
    pages.add("pageConnecting2");
    pages.add("pageConnecting3");
    pages.add("pageBasicsEnd");
    pages.add("pageChapterV");
    pages.add("pageAdvancedStyles1");
    pages.add("pageAdvancedStyles2");
    pages.add("pageAdvancedStyles3");
    pages.add("pageChapterVI");
    pages.add("pageAdvancedControls1");
    pages.add("pageAdvancedControls2");
    pages.add("pageAdvancedControls3");
    pages.add("pageAdvancedControls4");
    pages.add("pageAdvancedControls5");
    pages.add("pageAdvancedControls6");
    pages.add("pageAdvancedControls7");
    pages.add("pageAdvancedControls8");
    pages.add("pageAdvancedControls9");
    pages.add("pageChapterVII");
    pages.add("pageDnD1");
    pages.add("pageDnD2");
    pages.add("pageDnD3");
    pages.add("pageDnD4");
    pages.add("pageChapterVIII");
    pages.add("pageSlick1");
    pages.add("pageSlick2");
    pages.add("pageSlick3");
    pages.add("pageTheEndTeaser");
    pages.add("pageTheEnd");
    pages.add("pageCredits");

    addChapter("Welcome", "pageWelcome");
    addChapter("What's new in Nifty 1.2", "pageChapterWhatsNewInNifty1.2");
    addChapter("Nifty Basics: Java", "pageChapterI");
    addChapter("Nifty Basics: Writing XML", "pageChapterII");
    addChapter("Nifty Basics: Effects", "pageChapterIII");
    addChapter("Nifty Basics: Connect Java with Nifty XML", "pageChapterIV");
    addChapter("Advanced Nifty: Styles", "pageChapterV");
    addChapter("Advanced Nifty: Controls", "pageChapterVI");
    addChapter("Advanced Nifty: Drag and Drop", "pageChapterVII");
    addChapter("Integrate Nifty with: Slick2d", "pageChapterVIII");
    addChapter("The End", "pageTheEndTeaser");
    addChapter("Credits", "pageCredits");

    ControlEffectOnHoverAttributes textColorEffect = new ControlEffectOnHoverAttributes();
    textColorEffect.setName("textColor");
    textColorEffect.setAttribute("color", "#a22f");

    chapterSelectPopup = nifty.createPopup("chapterSelectPopup");
    Element chapterSelectElement = chapterSelectPopup.findElementByName("#chapterSelect");
    int idx = 0;
    for (String label : chapterCaption) {
      CreateLabelControl createLabel = new CreateLabelControl(label);
      createLabel.setAlign("left");
      createLabel.setTextVAlign("center");
      createLabel.setTextHAlign("left");
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
    nifty.addControls();
  }

  private void addChapter(final String chapter, final String page) {
    chapterCaption.add(chapter);
    chapterPageMap.put(String.valueOf(chapterCaption.size() - 1), pages.indexOf(page));
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
    nifty.showPopup(screen, chapterSelectPopup.getId(), null);
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
    currentPageElement.getRenderer(TextRenderer.class).setText(String.valueOf(pageIndex + 1) + " / " + pages.size());
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
    CreatePageControl createControl = new CreatePageControl(pageName, "page_" + pageIndex);
    createControl.create(nifty, screen, screen.findElementByName("pp"));

    Element element = screen.findElementByName("page_" + lastPageIndex);
    if (element != null) {
      nifty.removeElement(screen, element, new EndNotify() {
        public void perform() {
        }
      });
    }
    lastPageIndex = pageIndex;
  }

  public class CreatePageControl extends ControlAttributes {
    public CreatePageControl(final String name, final String id) {
      setAutoId(id);
      setName(name);
    }

    public void create(
        final Nifty nifty,
        final Screen screen,
        final Element parent) {
      nifty.addControl(screen, parent, getStandardControl());
    }

    @Override
    public ElementType createType() {
      return null;
    }
  }

  public boolean keyEvent(final NiftyInputEvent inputEvent) {
    if (NiftyStandardInputEvent.MoveCursorRight.equals(inputEvent)) {
      screen.findElementByName("nextButton").onClick();
      return true;
    } else if (NiftyStandardInputEvent.MoveCursorLeft.equals(inputEvent)) {
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

  @NiftyEventSubscriber(id="draggable")
  public void onDragStart(final String id, final DraggableDragStartedEvent event) {
    changeInfoText("dragStart() for source: " + getDroppableId(event.getSource()) + " with draggable: " + getDraggableId(event.getDraggable()));
  }

  @NiftyEventSubscriber(id="draggable")
  public void onDragCancel(final String id, final DraggableDragCanceledEvent event) {
    changeInfoText("dragCancel() for source: " + getDroppableId(event.getSource()) + " with draggable: " + getDraggableId(event.getDraggable()));
  }

  @NiftyEventSubscriber(pattern="droppable.") // this is a regexp matching both droppable1 and droppable2
  public void onDropped(final String id, final DroppableDroppedEvent event) {
    changeInfoText("drop() for source: " + getDroppableId(event.getSource()) + " with draggable: " + getDraggableId(event.getDraggable()) + " on target: " + getDroppableId(event.getTarget()));
  }

  private String getDroppableId(final Droppable source) {
    if (source == null) {
      return "null";
    }
    if (source.getElement() == null) {
      return "null element";
    }
    return source.getElement().getId();
  }

  private String getDraggableId(final Draggable draggable) {
    if (draggable == null) {
      return "null";
    }
    if (draggable.getElement() == null) {
      return "null element";
    }
    return draggable.getElement().getId();
  }

  private void changeInfoText(final String text) {
    Element infoText = screen.findElementByName("DragAndDropInfoText");
    if (infoText != null) {
      infoText.getRenderer(TextRenderer.class).setText(text);
    }
  }
}