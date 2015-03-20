package de.lessvoid.nifty.examples.test;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.input.mapping.DefaultInputMapping;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

public class ChatPanelController implements Controller, KeyInputHandler {
  @Nonnull
  private static final Logger log = Logger.getLogger(ChatPanelController.class.getName());
  @Nonnull
  private Nifty nifty;
  @Nonnull
  private Screen screen;
  @Nonnull
  private Element element;
  @Nullable
  private TextField chatsend;
  @Nullable
  public Element chatField;

  @Override
  public void bind(
      @Nonnull final Nifty niftyParam,
      @Nonnull final Screen screenParam,
      @Nonnull final Element newElement,
      @Nonnull final Parameters properties) {

    nifty = niftyParam;
    screen = screenParam;
    element = newElement;
    chatField = screen.findElementById("chatfield");

    if (chatField == null) {
      log.warning("Failed to locate chat field. Looked for: chatfield");
    } else {
      log.info("Setup chat field: " + chatField.getId());
    }
  }

  @Override
  public void init(@Nonnull final Parameters parameter) {
  }

  @Override
  public void onStartScreen() {
    chatsend = screen.findNiftyControl("chatsend", TextField.class);
    screen.addKeyboardInputHandler(new DefaultInputMapping(), this);

    if (chatsend == null) {
      log.warning("Failed to locate text field containing the text to send. Looked for: chatsend");
    }
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return true;
  }

  @Override
  public void onEndScreen() {

  }

  public void removePanel() {
    nifty.removeElement(screen, element);
  }

  @Override
  public boolean keyEvent(@Nonnull NiftyInputEvent inputEvent) {
    if (chatsend == null) {
      return false;
    }
    if (inputEvent == NiftyStandardInputEvent.Activate) {
      String message = chatsend.getRealText();
      if (message.length() >= 1000) {
        return false;
      }
      chatsend.setText("");
      return true;
    }
    return false;
  }
}