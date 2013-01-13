package de.lessvoid.niftyimpl.layout.manager;

import java.util.List;

import de.lessvoid.niftyimpl.layout.Box;
import de.lessvoid.niftyimpl.layout.BoxConstraints;
import de.lessvoid.niftyimpl.layout.Layoutable;

/**
 * AbsolutPositionLayout doesn't layout things. It just absolute positions it according to the constraints.
 *
 * @author void
 */
public class AbsolutePositionLayout implements LayoutManager {
  private static final DefaultPostProcess defaultPostProcess = new DefaultPostProcess();
  private PostProcess post;

  /**
   * Create a regular AbsolutePositionLayout.
   */
  public AbsolutePositionLayout() {
    this.post = defaultPostProcess;
  }

  /**
   * Create a AbsolutePositionLayout with a PostProcess (probably for the use in AbsolutePositionInside).
   */
  public AbsolutePositionLayout(final PostProcess post) {
    this.post = post;
  }

  /**
   * layoutElements.
   * @param rootElement @see {@link LayoutManager}
   * @param elements @see {@link LayoutManager}
   */
  @Override
  public void layoutElements(final Layoutable rootElement, final List < Layoutable > elements) {
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
      Layoutable p = elements.get(i);
      Box box = p.getLayoutPos();
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

  private int getRootBoxX(final Layoutable root) {
    return
        root.getLayoutPos().getX() +
        root.getBoxConstraints().getPaddingLeft().getValueAsInt(root.getLayoutPos().getWidth());
  }

  private int getRootBoxY(final Layoutable root) {
    return
        root.getLayoutPos().getY() +
        root.getBoxConstraints().getPaddingTop().getValueAsInt(root.getLayoutPos().getHeight());
  }

  private int getRootBoxWidth(final Layoutable root) {
    return
        root.getLayoutPos().getWidth() -
        root.getBoxConstraints().getPaddingLeft().getValueAsInt(root.getLayoutPos().getWidth()) -
        root.getBoxConstraints().getPaddingRight().getValueAsInt(root.getLayoutPos().getWidth());
  }

  private int getRootBoxHeight(final Layoutable root) {
    return
        root.getLayoutPos().getHeight() -
        root.getBoxConstraints().getPaddingTop().getValueAsInt(root.getLayoutPos().getHeight()) -
        root.getBoxConstraints().getPaddingBottom().getValueAsInt(root.getLayoutPos().getHeight());
  }

  public interface PostProcess {
    void process(int rootBoxX, int rootBoxY, int rootBoxWidth, int rootBoxHeight, Box box);
  }

  public static class DefaultPostProcess implements PostProcess {
    @Override
    public void process(
        final int rootBoxX,
        final int rootBoxY,
        final int rootBoxWidth,
        
        int rootBoxHeight, final Box box) {
    }
  }

  public static class KeepInsidePostProcess implements PostProcess {
    @Override
    public void process(
        final int rootBoxX,
        final int rootBoxY,
        final int rootBoxWidth,
        final int rootBoxHeight,
        final Box box) {
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
