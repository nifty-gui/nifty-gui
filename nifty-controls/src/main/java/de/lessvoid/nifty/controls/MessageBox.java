package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * TODO: This thing is bad! Hardcoded images and it breaks the general controller conventions.
 */
@SuppressWarnings("ConstantConditions")
public class MessageBox extends AbstractController {
  @Nonnull
  private String[] buttonCaptions;

  @Nonnull
  private MessageType messageType = MessageType.INFO;

  @Nullable
  private NiftyImage icon;

  @Nonnull
  private String message;

  @Nonnull
  private final String buttonWidth = "100px";
  @Nonnull
  private final String buttonHeight = "25px";

  @Nullable
  private Nifty nifty;

  @Nullable
  private Element messageboxPopup;
  @Nullable
  private MessageBox msgBox;

  public MessageBox() {
  }

  public MessageBox(
      @Nonnull Nifty nifty, @Nonnull final MessageType messageType, @Nonnull final String message,
      final String buttonCaption, final String icon) {
    this.nifty = nifty;
    messageboxPopup = nifty.createPopup("niftyPopupMessageBox");

    if (messageboxPopup == null) {
      return;
    }

    msgBox = messageboxPopup.findNiftyControl("#messagebox", MessageBox.class);
    if (msgBox != null) {
      msgBox.setMessageType(messageType);
      msgBox.setMessage(message);
      msgBox.setButtonCaption(buttonCaption);
      msgBox.setIcon(icon);
      msgBox.setupMessageBox();
    }
  }

  public MessageBox(
      @Nonnull Nifty nifty, @Nonnull MessageType messageType, @Nonnull String message,
      String buttonCaption) {
    this(nifty, messageType, message, buttonCaption, null);
  }

  public MessageBox(
      @Nonnull Nifty nifty, @Nonnull final MessageType messageType, @Nonnull final String message,
      final String[] buttonCaptions, final String icon) {
    this.nifty = nifty;
    messageboxPopup = nifty.createPopup("niftyPopupMessageBox");
    if (messageboxPopup == null) {
      return;
    }

    msgBox = messageboxPopup.findNiftyControl("#messagebox", MessageBox.class);
    if (msgBox != null) {
      msgBox.setMessageType(messageType);
      msgBox.setMessage(message);
      msgBox.setButtonCaptions(buttonCaptions);
      msgBox.setIcon(icon);
      msgBox.setupMessageBox();
    }
  }

  public MessageBox(
      @Nonnull Nifty nifty, @Nonnull MessageType messageType, @Nonnull String message,
      String... buttonCaptions) {
    this(nifty, messageType, message, buttonCaptions, null);
  }

  @Override
  public void bind(
      @Nonnull Nifty nifty,
      @Nonnull Screen screen,
      @Nonnull Element element,
      @Nonnull Parameters parameter) {
    messageboxPopup = element;
    this.nifty = nifty;
    if (parameter.isSet("buttonCaptions")) {
      setButtonCaptions(parameter.getWithDefault("buttonCaptions", "").split(","));
    } else if (parameter.isSet("buttonCaption")) {
      setButtonCaption(parameter.get("buttonCaption"));
    }

    if (messageType != MessageType.CUSTOM) {
      setIcon("messagebox/" + messageType.name() + ".png");
    }
  }

  @Override
  public void onStartScreen() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean inputEvent(@Nonnull NiftyInputEvent inputEvent) {
    // TODO Auto-generated method stub
    return false;
  }

  public void show() {
    nifty.showPopup(nifty.getCurrentScreen(), messageboxPopup.getId(), null);
  }

  public void close(String command) {
    closeMessageBox();
    nifty.closePopup(messageboxPopup.getParent().getId());
  }

  public void setIcon(@Nullable String icon) {
    if (icon != null) {
      this.icon = nifty.createImage(icon, false);
    }
  }

  public void setMessage(@Nonnull String message) {
    this.message = message;
  }

  public void setButtonCaption(String buttonCaption) {
    this.buttonCaptions = new String[] { buttonCaption };
  }

  public void setButtonCaptions(@Nonnull String... buttonCaptions) {
    this.buttonCaptions = Arrays.copyOf(buttonCaptions, buttonCaptions.length);
  }

  public void setButtonCaptions(@Nonnull String buttonCaptions) {
    this.buttonCaptions = buttonCaptions.split(",");
  }

  public void setMessageType(@Nonnull String messageType) {
    this.messageType = MessageType.valueOf(messageType);
  }

  public void setMessageType(@Nonnull MessageType messageType) {
    this.messageType = messageType;
  }

  private void setupMessageBox() {
    final Element imgIcon = messageboxPopup.findElementById("#messagebox").findElementById("#message-icon");
    final ImageRenderer iconRenderer = imgIcon.getRenderer(ImageRenderer.class);
    iconRenderer.setImage(icon);
    final Element text = messageboxPopup.findElementById("#messagebox").findElementById("#message-text");
    final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
    textRenderer.setText(message);
    int i = 0;
    for (String buttonCaption : buttonCaptions) {
      i++;
      createButton(buttonCaption, buttonCaption, "button_" + i);
    }
    messageboxPopup.findElementById("#messagebox").layoutElements();
    nifty.getCurrentScreen().layoutLayers();
  }

  private void closeMessageBox() {
    clearButtons();
    //		messageboxPopup.findElementById("#messagebox").findElementById("#buttons");
    nifty.getCurrentScreen().layoutLayers();
  }

  private void createButton(@Nonnull final String buttonCaption, final String command, final String buttonId) {
    Element buttonPanel = messageboxPopup.findElementById("#messagebox").findElementById("#buttons");
    if (buttonPanel.findElementById("#" + buttonId) == null) {
      new ButtonBuilder("#" + buttonId) {{
        style("nifty-button");
        childLayout(ChildLayoutType.Horizontal);
        interactOnClick("close(" + command + ")");
        if (buttonWidth != null) {
          width(buttonWidth);
        }
        if (buttonHeight != null) {
          height(buttonHeight);
        } else {
          height("25px");
        }
        label(buttonCaption);
      }}.build(nifty, nifty.getCurrentScreen(), buttonPanel);
    }
  }

  private void clearButtons() {
    List<Element> buttons = messageboxPopup.findElementById("#messagebox").findElementById("#buttons").getChildren();
    for (Element button : buttons) {
      button.markForRemoval();
    }
  }

  @Nullable
  protected Element getMessageBoxPopup() {
    return messageboxPopup;
  }

  public enum MessageType {
    CUSTOM, INFO, WARNING, ERROR
  }

}
