package de.lessvoid.nifty.controls.chatcontrol;

import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;

/**
 * Handles the displaying of the items in the ChatBox.
 * @author Mark
 * @version 0.1
 */
public class ChatBoxViewConverter implements ListBoxViewConverter<ChatEntryModelClass> {
    private static final String CHAT_LINE_ICON = "#chat-line-icon";
    private static final String CHAT_LINE_TEXT = "#chat-line-text";

    /**
     * Default constructor.
     */
    public ChatBoxViewConverter() {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final void display(final Element listBoxItem, final ChatEntryModelClass item) {
        final Element text = listBoxItem.findElementByName(CHAT_LINE_TEXT);
        final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
        final Element icon = listBoxItem.findElementByName(CHAT_LINE_ICON);
        final ImageRenderer iconRenderer = icon.getRenderer(ImageRenderer.class);
        if (item != null) {
            textRenderer.setText(item.toString());
            iconRenderer.setImage(item.getIcon());
            if (item.getStyle() != null && !item.getStyle().equals("")) {
                text.setStyle(item.getStyle());
            } else {
                text.setStyle("default");
            }
        } else {
            textRenderer.setText("");
            iconRenderer.setImage(null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getWidth(final Element listBoxItem, final ChatEntryModelClass item) {
        final Element text = listBoxItem.findElementByName(CHAT_LINE_TEXT);
        final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
        return ((textRenderer.getFont() == null) ? 0 : textRenderer.getFont().getWidth(item.getLabel()))
                + ((item.getIcon() == null) ? 0 : item.getIcon().getWidth());
    }
}
