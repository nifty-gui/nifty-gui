package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;
import javax.annotation.Nonnull;

/**
 * This event occurs when user click more than one time on an element . For example
 * a double click . NOTICE : every multi-click fire one instance of this class , i.e
 * click-click-click will generate : NiftyMouseEvent - NiftyMouseMultiClickEvent(clickcount = 2) then 
 * NiftyMouseMultiClickEvent(clickcount = 3).
 * 
 */
public class NiftyMousePrimaryMultiClickedEvent extends NiftyMouseBaseEvent {

    private final int clickCount;

    public NiftyMousePrimaryMultiClickedEvent(Element element) {
        super(element);
        clickCount = 0;
    }

    public NiftyMousePrimaryMultiClickedEvent(final Element element, @Nonnull final NiftyMouseInputEvent source, int clickCount) {
        super(element, source);
        this.clickCount = clickCount;
    }

    /**
     * How many click
     *
     * @return the clickCount
     */
    public int getClickCount() {
        return clickCount;
    }
}
