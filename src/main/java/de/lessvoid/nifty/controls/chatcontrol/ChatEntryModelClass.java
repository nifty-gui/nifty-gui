package de.lessvoid.nifty.controls.chatcontrol;

import de.lessvoid.nifty.render.NiftyImage;

/**
 * Handles a line in the chat controller. This can be either a chat line or an
 * entry in the list of players.
 * 
 * @author Mark
 * @version 0.1
 */
public final class ChatEntryModelClass {
    private String label;
    private NiftyImage icon;

    /**
     * Constructor excepting the line and the icon.
     * @param labelParam The label to put in the entry. This can be either a chat line or a player name.
     * @param iconParam The icon to display in the entry, this one is optional.
     */
    public ChatEntryModelClass(final String labelParam, final NiftyImage iconParam) {
        this.label = labelParam;
        this.icon = iconParam;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return label;
    }

    /**
     * Return the supplied label. This can be either a chat line or a player name.
     * @return The supplied label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Return the supplied icon.
     * @return The supplied icon.
     */
    public NiftyImage getIcon() {
        return icon;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof ChatEntryModelClass)) {
            return false;
        }
        return this.getLabel().equalsIgnoreCase(((ChatEntryModelClass) obj).getLabel());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.getLabel().hashCode();
    }

}
