/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;
import javax.annotation.Nonnull;


public class NiftyMouseSecondaryMultiClickedEvent extends NiftyMouseBaseEvent {
    
    private final int clickCount;

    public NiftyMouseSecondaryMultiClickedEvent(Element element) {
        super(element);
        clickCount = 0;
    }

    public NiftyMouseSecondaryMultiClickedEvent(final Element element, @Nonnull final NiftyMouseInputEvent source, int clickCount) {
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
