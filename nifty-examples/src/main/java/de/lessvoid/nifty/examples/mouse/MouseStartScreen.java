package de.lessvoid.nifty.examples.mouse;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.*;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

/**
 * @author void
 */
public class MouseStartScreen implements ScreenController, NiftyExample {
  private Nifty nifty;
  private Screen screen;
  @Nullable
  private Label mousePrimaryText;
  @Nullable
  private Label mouseSecondaryText;
  @Nullable
  private Label mouseTertiaryText;
  @Nullable
  private Label mouseMoveText;
  @Nullable
  private Label mouseWheelText;

  @Nullable
  private Label mouseEventsPrimaryText;
  @Nullable
  private Label mouseEventsSecondaryText;
  @Nullable
  private Label mouseEventsTertiaryText;
  @Nullable
  private Label mouseEventsMoveText;
  @Nullable
  private Label mouseEventsWheelText;
  @Nullable
  private Label mouseEventsText;

  @Override
  public void bind(@Nonnull final Nifty newNifty, @Nonnull final Screen newScreen) {
    this.nifty = newNifty;
    this.screen = newScreen;
    this.mousePrimaryText = screen.findNiftyControl("mousePrimaryText", Label.class);
    this.mouseSecondaryText = screen.findNiftyControl("mouseSecondaryText", Label.class);
    this.mouseTertiaryText = screen.findNiftyControl("mouseTertiaryText", Label.class);
    this.mouseMoveText = screen.findNiftyControl("mouseMoveText", Label.class);
    this.mouseWheelText = screen.findNiftyControl("mouseWheelText", Label.class);

    this.mouseEventsPrimaryText = screen.findNiftyControl("mouseEventsPrimaryText", Label.class);
    this.mouseEventsSecondaryText = screen.findNiftyControl("mouseEventsSecondaryText", Label.class);
    this.mouseEventsTertiaryText = screen.findNiftyControl("mouseEventsTertiaryText", Label.class);
    this.mouseEventsMoveText = screen.findNiftyControl("mouseEventsMoveText", Label.class);
    this.mouseEventsWheelText = screen.findNiftyControl("mouseEventsWheelText", Label.class);
    this.mouseEventsText = screen.findNiftyControl("mouseEventsText", Label.class);
  }

  @Override
  public void onStartScreen() {
    System.out.println(screen.debugOutput());
  }

  @Override
  public void onEndScreen() {
  }

  public void quit() {
    nifty.setAlternateKeyForNextLoadXml("fade");
    nifty.fromXml("all/intro.xml", "menu");
  }

  // the old regular interact kinda way

  // primay

  public void primaryClick() {
    mousePrimaryText.setText("clicked");
  }

  public void primaryRelease() {
    mousePrimaryText.setText("");
  }

  public void primaryClickMouseMoved() {
    mousePrimaryText.setText("clicked mouse moved");
  }

  // secondary

  public void secondaryClick() {
    mouseSecondaryText.setText("clicked");
  }

  public void secondaryClickMouseMove() {
    mouseSecondaryText.setText("clicked mouse moved");
  }

  public void secondaryRelease() {
    mouseSecondaryText.setText("");
  }

  // tertiary

  public void tertiaryClick() {
    mouseTertiaryText.setText("clicked");
  }

  public void tertiaryClickMouseMove() {
    mouseTertiaryText.setText("clicked mouse moved");
  }

  public void tertiaryRelease() {
    mouseTertiaryText.setText("");
  }

  // mouse over

  public void mouseOver(final Element element, @Nonnull final NiftyMouseInputEvent event) {
    mouseMoveText.setText(event.getMouseX() + ", " + event.getMouseY());
  }

  // mouse wheel

  public void mouseWheel(final Element element, @Nonnull final NiftyMouseInputEvent event) {
    mouseWheelText.setText(String.valueOf(event.getMouseWheel()));
  }

  // the new event subscriber way. please note that these methods really could be anywhere as long as
  // you call nifty.processAnnotations() on the class that implements them.

  // primary

  @NiftyEventSubscriber(id = "mouseEvents")
  public void onElementPrimaryClick(final String id, final NiftyMousePrimaryClickedEvent event) {
    mouseEventsPrimaryText.setText("clicked");
  }

  @NiftyEventSubscriber(id = "mouseEvents")
  public void onElementPrimaryClickMove(final String id, final NiftyMousePrimaryClickedMovedEvent event) {
    mouseEventsPrimaryText.setText("clicked mouse move");
  }

  @NiftyEventSubscriber(id = "mouseEvents")
  public void onElementPrimaryRelease(final String id, final NiftyMousePrimaryReleaseEvent event) {
    mouseEventsPrimaryText.setText("");
  }

  // secondary

  @NiftyEventSubscriber(id = "mouseEvents")
  public void onElementSecondaryClick(final String id, final NiftyMouseSecondaryClickedEvent event) {
    mouseEventsSecondaryText.setText("clicked");
  }

  @NiftyEventSubscriber(id = "mouseEvents")
  public void onElementSecondaryClickMove(final String id, final NiftyMouseSecondaryClickedMovedEvent event) {
    mouseEventsSecondaryText.setText("clicked mouse moved");
  }

  @NiftyEventSubscriber(id = "mouseEvents")
  public void onElementSecondaryRelease(final String id, final NiftyMouseSecondaryReleaseEvent event) {
    mouseEventsSecondaryText.setText("");
  }

  // Tertiary

  @NiftyEventSubscriber(id = "mouseEvents")
  public void onElementTertiaryClick(final String id, final NiftyMouseTertiaryClickedEvent event) {
    mouseEventsTertiaryText.setText("clicked");
  }

  @NiftyEventSubscriber(id = "mouseEvents")
  public void onElementTertiaryClickMove(final String id, final NiftyMouseTertiaryClickedMovedEvent event) {
    mouseEventsTertiaryText.setText("clicked mouse moved");
  }

  @NiftyEventSubscriber(id = "mouseEvents")
  public void onElementTertiaryRelease(final String id, final NiftyMouseTertiaryReleaseEvent event) {
    mouseEventsTertiaryText.setText("");
  }

  // mouse move

  @NiftyEventSubscriber(id = "mouseEvents")
  public void onElementMouseMove(final String id, @Nonnull final NiftyMouseMovedEvent event) {
    mouseEventsMoveText.setText(event.getMouseX() + ", " + event.getMouseY());
  }

  // mouse wheel

  @NiftyEventSubscriber(id = "mouseEvents")
  public void onElementMouseWheel(final String id, @Nonnull final NiftyMouseWheelEvent event) {
    mouseEventsWheelText.setText(String.valueOf(event.getMouseWheel()));
  }

  // general

  @NiftyEventSubscriber(id = "mouseEvents")
  public void onElementMouse(final String id, @Nonnull final NiftyMouseEvent event) {
    mouseEventsText.setText(
        id + " -> " + event.getMouseX() + ", " + event.getMouseY() + ", " + event.getMouseWheel() + "\n" +
            event.isButton0Down() + ", " + event.isButton1Down() + ", " + event.isButton2Down() + "\n" +
            event.isButton0Release() + ", " + event.isButton1Release() + ", " + event.isButton2Release());
  }

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "mouse/mouse.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Mouse Control Example";
  }

  @Override
  public void prepareStart(@Nonnull Nifty nifty) {
    // get the NiftyMouse interface that gives us access to all mouse cursor related stuff
    NiftyMouse niftyMouse = nifty.getNiftyMouse();

    try {
      // register/load a mouse cursor (this would be done somewhere at the beginning)
      niftyMouse.registerMouseCursor("mouseId", "nifty-cursor.png", 0, 0);

      // change the cursor to the one we've loaded before
      niftyMouse.enableMouseCursor("mouseId");
    } catch (IOException e) {
      System.err.println("Failed to load mouse cursor!");
    }

    // we could set the position like so
    niftyMouse.setMousePosition(20, 20);
  }
}
