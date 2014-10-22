package de.lessvoid.nifty.controls.chatcontrol;

import java.util.logging.Logger;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;

/**
 * Handles the displaying of the items in the ChatBox.
 *
 * @author Mark
 * @version 0.1
 */
public class ChatBoxViewConverter implements ListBoxViewConverter<ChatEntryModelClass> {
  @Nonnull
  private static final Logger log = Logger.getLogger(ChatBoxViewConverter.class.getName());
  @Nonnull
  private static final String CHAT_LINE_ICON = "#chat-line-icon";
  @Nonnull
  private static final String CHAT_LINE_TEXT = "#chat-line-text";

  /**
   * Default constructor.
   */
  public ChatBoxViewConverter() {
  }

  @Override
  public final void display(@Nonnull final Element listBoxItem, @Nonnull final ChatEntryModelClass item) {
    final Element text = listBoxItem.findElementById(CHAT_LINE_TEXT);
    if (text == null) {
      log.severe("Failed to locate text part of chat line! Can't display entry.");
      return;
    }
    final Element icon = listBoxItem.findElementById(CHAT_LINE_ICON);
    if (icon == null) {
      log.severe("Failed to locate icon part of chat line! Can't display entry.");
      return;
    }
    final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
    if (textRenderer == null) {
      log.severe("Text entry of the chat line does not contain the required text renderer.");
      return;
    }
    final ImageRenderer iconRenderer = icon.getRenderer(ImageRenderer.class);
    if (iconRenderer == null) {
      log.severe("Icon entry of the chat line does not contain the required image renderer.");
      return;
    }
    if (item.getStyle() != null && !item.getStyle().equals("")) {
      text.setStyle(item.getStyle());
    } else {
      text.setStyle("default");
    }
    // setStyle will reset the text to initial value (null), so setText/setImage should be called after it
    textRenderer.setText(item.toString());
    iconRenderer.setImage(item.getIcon());
  }

  @Override
  public final int getWidth(@Nonnull final Element listBoxItem, @Nonnull final ChatEntryModelClass item) {
    final Element text = listBoxItem.findElementById(CHAT_LINE_TEXT);
    if (text == null) {
      log.severe("Failed to locate text part of chat line! Can't display entry.");
      return 0;
    }
    final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
    if (textRenderer == null) {
      log.severe("Text entry of the chat line does not contain the required text renderer.");
      return 0;
    }
    return ((textRenderer.getFont() == null) ? 0 : textRenderer.getFont().getWidth(item.getLabel()))
        + ((item.getIcon() == null) ? 0 : item.getIcon().getWidth());
  }
}
