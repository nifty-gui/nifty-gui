package de.lessvoid.nifty.controls.chatcontrol;

import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;

/**
 * Handles the displaying of the items in the ChatBox.
 * @author Mark
 * @version 0.1
 */
public class ChatBoxViewConverter implements ListBoxViewConverter<ChatEntryModelClass> {
    
    private static final String CHAT_LINE_TEXT = "chat-line-text";

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
        final TextRenderer renderer = text.getRenderer(TextRenderer.class);
        if (item != null) {
            renderer.setText(item.toString());
        } else {
            renderer.setText("");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getWidth(final Element listBoxItem, final ChatEntryModelClass item) {
        final Element text = listBoxItem.findElementByName(CHAT_LINE_TEXT);
        final TextRenderer renderer = text.getRenderer(TextRenderer.class);
        return renderer.getFont().getWidth(item.toString());
    }

}
