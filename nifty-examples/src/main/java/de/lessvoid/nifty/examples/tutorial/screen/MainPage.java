package de.lessvoid.nifty.examples.tutorial.screen;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainPage implements ScreenController, KeyInputHandler {
  private Nifty nifty;
  private Screen screen;
  private boolean mute = false;

  @Nonnull
  private final ArrayList <String> pages = new ArrayList <String>();
  @Nonnull
  private final ArrayList <String> chapterCaption = new ArrayList <String>();
  @Nonnull
  private final Map <String, Integer> chapterPageMap = new HashMap <String, Integer>();
  private int pageIndex = 0;
  private int lastPageIndex = 0;
  private boolean lastPageWasActive = false;
  private Element chapterSelectPopup;
  
  @Override
  public void bind(@Nonnull final Nifty nifty, @Nonnull final Screen screen) {
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

    HoverEffectBuilder hoverEffect = new HoverEffectBuilder("textColor");
    hoverEffect.effectValue("color", "#a22f");

    chapterSelectPopup = nifty.createPopup(screen, "chapterSelectPopup");
    Element chapterSelectElement = chapterSelectPopup.findElementById("#chapterSelect");
    if (chapterSelectElement == null) {
      throw new IllegalStateException("Binding of page failed! Popup not properly created.");
    }
    int idx = 0;
    for (String label : chapterCaption) {
      LabelBuilder createLabel = new LabelBuilder(label);
      createLabel.align(ElementBuilder.Align.Left);
      createLabel.textVAlign(ElementBuilder.VAlign.Center);
      createLabel.textHAlign(ElementBuilder.Align.Left);
      createLabel.color("#ccce");
      createLabel.style("menuFont");
      createLabel.visibleToMouse(true);
      createLabel.onHoverEffect(hoverEffect);
      createLabel.interactOnClick("chapterSelect(" + idx + ")");
      createLabel.build(nifty, screen, chapterSelectElement);
      idx++;
    }

    pageIndex = 0;
    lastPageIndex = -1;
    updatePage();
    updateBackButtonVisibility();
  }

  private void addChapter(final String chapter, final String page) {
    chapterCaption.add(chapter);
    chapterPageMap.put(String.valueOf(chapterCaption.size() - 1), pages.indexOf(page));
  }
  
  @Override
  public void onStartScreen() {
  }
  
  @Override
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
    Element backButtonElement = screen.findElementById("backButton");
    if (pageIndex == 0) {
      if (!lastPageWasActive) {
        backButtonElement.setVisible(false);
      }
    } else if (pageIndex == 1) {
      showBackButton();
    }

    Element currentPageElement = screen.findElementById("curPage");
    currentPageElement.startEffect(EffectEventId.onCustom);
    currentPageElement.getRenderer(TextRenderer.class).setText(String.valueOf(pageIndex + 1) + " / " + pages.size());
  }

  private void showBackButton() {
    Element backButtonElement = screen.findElementById("backButton");
    if (!backButtonElement.isVisible()) {
      backButtonElement.setVisible(true);
    }
  }

  public void toggleMute() {
    System.out.println("toggleMute");
    mute = !mute;
    if (mute) {
      screen.findElementById("muteButton").setStyle("muteButtonSoundOff");
      screen.findElementById("muteButton").startEffect(EffectEventId.onCustom);
    } else {
      screen.findElementById("muteButton").setStyle("muteButtonSoundOn");
      screen.findElementById("muteButton").startEffect(EffectEventId.onCustom);
    }
  }

  private void updatePage() {
    String pageName = pages.get(pageIndex);
    CreatePageControl createControl = new CreatePageControl(pageName, "page_" + pageIndex);
    createControl.create(nifty, screen, screen.findElementById("pp"));

    Element element = screen.findElementById("page_" + lastPageIndex);
    if (element != null) {
      nifty.removeElement(screen, element, new EndNotify() {
        @Override
        public void perform() {
        }
      });
    }
    lastPageIndex = pageIndex;
  }

  public class CreatePageControl extends ControlAttributes {
    public CreatePageControl(@Nonnull final String name, @Nonnull final String id) {
      setId(id);
      setName(name);
    }

    public void create(
        @Nonnull final Nifty nifty,
        @Nonnull final Screen screen,
        @Nonnull final Element parent) {
      nifty.addControl(screen, parent, getStandardControl());
    }

    @Nullable
    @Override
    public ElementType createType() {
      return null;
    }
  }

  @Override
  public boolean keyEvent(@Nonnull final NiftyInputEvent inputEvent) {
    if (NiftyStandardInputEvent.MoveCursorRight.equals(inputEvent)) {
      screen.findElementById("nextButton").onClickAndReleasePrimaryMouseButton();
      return true;
    } else if (NiftyStandardInputEvent.MoveCursorLeft.equals(inputEvent)) {
      screen.findElementById("backButton").onClickAndReleasePrimaryMouseButton();
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

  public void openLink(@Nonnull final String url) {
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
  public void onDragStart(final String id, @Nonnull final DraggableDragStartedEvent event) {
    changeInfoText("dragStart() for source: " + getDroppableId(event.getSource()) + " with draggable: " + getDraggableId(event.getDraggable()));
  }

  @NiftyEventSubscriber(id="draggable")
  public void onDragCancel(final String id, @Nonnull final DraggableDragCanceledEvent event) {
    changeInfoText("dragCancel() for source: " + getDroppableId(event.getSource()) + " with draggable: " + getDraggableId(event.getDraggable()));
  }

  @NiftyEventSubscriber(pattern="droppable.") // this is a regexp matching both droppable1 and droppable2
  public void onDropped(final String id, @Nonnull final DroppableDroppedEvent event) {
    changeInfoText("drop() for source: " + getDroppableId(event.getSource()) + " with draggable: " + getDraggableId(event.getDraggable()) + " on target: " + getDroppableId(event.getTarget()));
  }

  @Nullable
  private String getDroppableId(@Nullable final Droppable source) {
    if (source == null) {
      return "null";
    }
    if (source.getElement() == null) {
      return "null element";
    }
    return source.getElement().getId();
  }

  @Nullable
  private String getDraggableId(@Nullable final Draggable draggable) {
    if (draggable == null) {
      return "null";
    }
    if (draggable.getElement() == null) {
      return "null element";
    }
    return draggable.getElement().getId();
  }

  private void changeInfoText(final String text) {
    Element infoText = screen.findElementById("DragAndDropInfoText");
    if (infoText != null) {
      infoText.getRenderer(TextRenderer.class).setText(text);
    }
  }
}