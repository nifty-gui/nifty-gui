package de.lessvoid.nifty.layout.manager;

import java.util.List;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * AbsolutPositionLayout doesn't layout things. It just
 * absolute position it according to the constraints.
 *
 * @author void
 */
public class AbsolutePositionLayout implements LayoutManager {
  private static final DefaultPostProcess defaultPostProcess = new DefaultPostProcess();
  private PostProcess post;

  public AbsolutePositionLayout() {
    this.post = defaultPostProcess;
  }

  public AbsolutePositionLayout(final PostProcess post) {
    this.post = post;
  }

  /**
   * layoutElements.
   * @param rootElement @see {@link LayoutManager}
   * @param elements @see {@link LayoutManager}
   */
  public void layoutElements(
      final LayoutPart rootElement,
      final List < LayoutPart > elements) {

    // make the params any sense?
    if (rootElement == null || elements == null || elements.size() == 0) {
      return;
    }

    // get the root box
    int rootBoxX = getRootBoxX(rootElement);
    int rootBoxY = getRootBoxY(rootElement);
    int rootBoxWidth = getRootBoxWidth(rootElement);
    int rootBoxHeight = getRootBoxHeight(rootElement);

    // now do the layout
    for (int i = 0; i < elements.size(); i++) {
      LayoutPart p = elements.get(i);
      Box box = p.getBox();
      BoxConstraints cons = p.getBoxConstraints();

      // makes only sense with constraints given
      if (cons != null) {
        if (cons.getX() != null) {
          box.setX(rootBoxX + cons.getX().getValueAsInt(rootBoxWidth));
        }

        if (cons.getY() != null) {
          box.setY(rootBoxY + cons.getY().getValueAsInt(rootBoxHeight));
        }

        if (cons.getWidth() != null && cons.getWidth().hasHeightSuffix()) {
          if (cons.getHeight() != null) {
            box.setHeight(cons.getHeight().getValueAsInt(rootBoxHeight));
          }
          box.setWidth(cons.getWidth().getValueAsInt(box.getHeight()));
        } else if (cons.getHeight() != null && cons.getHeight().hasWidthSuffix()) {
          if (cons.getWidth() != null) {
            box.setWidth(cons.getWidth().getValueAsInt(rootBoxWidth));
          }
          box.setHeight(cons.getHeight().getValueAsInt(box.getWidth()));
        } else {
          if (cons.getWidth() != null) {
            box.setWidth(cons.getWidth().getValueAsInt(rootBoxWidth));
          }
          if (cons.getHeight() != null) {
            box.setHeight(cons.getHeight().getValueAsInt(rootBoxHeight));
          }
        }

        post.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
      }
    }
  }

  /**
   * @param children children elements of the root element
   * @return new calculated SizeValue
   */
  public final SizeValue calculateConstraintWidth(final LayoutPart root, final List < LayoutPart > children) {
    return null;
  }

  /**
   * @param children children elements of the root element
   * @return new calculated SizeValue
   */
  public final SizeValue calculateConstraintHeight(final LayoutPart root, final List < LayoutPart > children) {
    return null;
  }

  private int getRootBoxX(final LayoutPart root) {
    return root.getBox().getX() + root.getBoxConstraints().getPaddingLeft().getValueAsInt(root.getBox().getWidth());
  }

  private int getRootBoxY(final LayoutPart root) {
    return root.getBox().getY() + root.getBoxConstraints().getPaddingTop().getValueAsInt(root.getBox().getHeight());
  }

  private int getRootBoxWidth(final LayoutPart root) {
    return root.getBox().getWidth() - root.getBoxConstraints().getPaddingLeft().getValueAsInt(root.getBox().getWidth()) - root.getBoxConstraints().getPaddingRight().getValueAsInt(root.getBox().getWidth());
  }

  private int getRootBoxHeight(final LayoutPart root) {
    return root.getBox().getHeight() - root.getBoxConstraints().getPaddingTop().getValueAsInt(root.getBox().getHeight()) - root.getBoxConstraints().getPaddingBottom().getValueAsInt(root.getBox().getHeight());
  }

  public interface PostProcess {
    void process(int rootBoxX, int rootBoxY, int rootBoxWidth, int rootBoxHeight, Box box);
  }

  public static class DefaultPostProcess implements PostProcess {
    @Override
    public void process(final int rootBoxX, final int rootBoxY, final int rootBoxWidth, final int rootBoxHeight, final Box box) {
    }
  }

  public static class KeepInsidePostProcess implements PostProcess {
    @Override
    public void process(final int rootBoxX, final int rootBoxY, final int rootBoxWidth, final int rootBoxHeight, final Box box) {
      // first make sure width and height fit into the root box
      if (box.getWidth() > rootBoxWidth) {
        box.setWidth(rootBoxWidth);
      }
      if (box.getHeight() > rootBoxHeight) {
        box.setHeight(rootBoxHeight);
      }

      // and now make sure the box fits the rootbox
      if (box.getX() < rootBoxX) {
        box.setX(rootBoxX);
      }
      if (box.getY() < rootBoxY) {
        box.setY(rootBoxY);
      }
      if ((box.getX() + box.getWidth()) > (rootBoxX + rootBoxWidth)) {
        box.setX(rootBoxX + rootBoxWidth - box.getWidth());
      }
      if ((box.getY() + box.getHeight()) > (rootBoxY + rootBoxHeight)) {
        box.setY(rootBoxY + rootBoxHeight - box.getHeight());
      }
    }
  }
}
