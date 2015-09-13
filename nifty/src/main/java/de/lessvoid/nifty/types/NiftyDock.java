package de.lessvoid.nifty.types;

/**
 * These enumeration is used to define at what border in a {@link de.lessvoid.nifty.node.DockLayoutNode} the
 * {@link de.lessvoid.nifty.node.DockLayoutChildNode} is attached to.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public enum NiftyDock {
  /**
   * Docking north.
   * <p>
   *   This value attaches the node to the top border of it's parent. It will use the desired height of the element
   *   but stretch the width to the remaining width available in the parent element.
   * </p>
   */
  North,

  /**
   * Docking south.
   * <p>
   *   This value attaches the node to the bottom border of it's parent. It will use the desired height of the element
   *   but stretch the width to the remaining width available in the parent element.
   * </p>
   */
  South,

  /**
   * Docking right.
   * <p>
   *   This value attaches the node to the right border of it's parent. It will use the desired width of the element
   *   but stretch the height to the remaining height available in the parent element.
   * </p>
   */
  East,

  /**
   * Docking left.
   * <p>
   *   This value attaches the node to the left border of it's parent. It will use the desired width of the element
   *   but stretch the height to the remaining height available in the parent element.
   * </p>
   */
  West
}
