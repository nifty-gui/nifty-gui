package de.lessvoid.nifty.internal;

import java.util.List;

/**
 * InternalLayoutNone doesn't do anything. It will be used when no childLayout is set. 
 * @author void
 */
public class InternalLayoutNone implements InternalLayout {

  @Override
  public void layoutElements(final InternalLayoutable rootElement, final List <? extends InternalLayoutable> elements) {
    // Does nothing.
  }
}
