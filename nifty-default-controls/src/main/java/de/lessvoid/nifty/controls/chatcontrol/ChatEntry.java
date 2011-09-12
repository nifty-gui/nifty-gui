package de.lessvoid.nifty.controls.chatcontrol;

import de.lessvoid.nifty.render.NiftyImage;

/**
 *
 * @author ractoc
 */
final class ChatEntry {
    
    private String label;
    private NiftyImage icon;
    private String style;
    
    public ChatEntry(String label, NiftyImage icon) {
        setLabel(label);
        setIcon(icon);
    }

    public ChatEntry(String label, NiftyImage icon, String style) {
        setLabel(label);
        setIcon(icon);
        setStyle(style);
    }

    public NiftyImage getIcon() {
        return icon;
    }

    public void setIcon(NiftyImage icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    
}
