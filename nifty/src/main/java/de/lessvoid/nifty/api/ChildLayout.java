package de.lessvoid.nifty.api;

import de.lessvoid.nifty.internal.layout.InternalLayout;
import de.lessvoid.nifty.internal.layout.InternalLayoutAbsolute;
import de.lessvoid.nifty.internal.layout.InternalLayoutAbsolute.KeepInsidePostProcess;
import de.lessvoid.nifty.internal.layout.InternalLayoutCenter;
import de.lessvoid.nifty.internal.layout.InternalLayoutHorizontal;
import de.lessvoid.nifty.internal.layout.InternalLayoutNone;
import de.lessvoid.nifty.internal.layout.InternalLayoutOverlay;
import de.lessvoid.nifty.internal.layout.InternalLayoutVertical;

/**
 * The ChildLayout enumeration will define how this NiftyNode will layout its
 * children.
 * 
 * @author void
 */
public enum ChildLayout {
  /**
   * A layout that does nothing. This is the default layout for a Node without any parents.
   */
  None(new InternalLayoutNone()),

  /**
   * Absolute doesn't really layout the child nodes. It just absolute
   * positions them according to their constraints.
   */
  Absolute(new InternalLayoutAbsolute()),

  /**
   * This works the same as Absolute but automatically ensures that child
   * elements are not positioned outside of the their parent node. If
   * necessary the constrained positions will be automatically corrected to
   * force the child elements inside of the parent.
   */
  AbsoluteInside(new InternalLayoutAbsolute(new KeepInsidePostProcess())),

  /**
   * CenterLayout centers all child elements. If there are more than one child
   * elements all elements will be centered (and will overlay each other
   * consequently). Remember that center probably makes only sense if the
   * centered element has some width and height constraints set.
   */
  Center(new InternalLayoutCenter()),

  /**
   * The child nodes are arranged horizontally in relation to the root
   * element.
   */
  Horizontal(new InternalLayoutHorizontal()),

  /**
   * OverlayLayout doesn't layout things. It just forwards the size of the
   * root box to the children.
   */
  Overlay(new InternalLayoutOverlay()),

  /**
   * The child nodes are arranged vertically in relation to the root element.
   */
  Vertical(new InternalLayoutVertical());

  private InternalLayout layout;

  private ChildLayout(final InternalLayout layout) {
    this.layout = layout;
  }

  public InternalLayout getLayout() {
    return layout;
  }
}
