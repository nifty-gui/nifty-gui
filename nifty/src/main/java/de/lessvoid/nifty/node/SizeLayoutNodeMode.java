package de.lessvoid.nifty.node;

/**
 * The possible values how the size of a {@link SizeLayoutNode} is handled.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public enum SizeLayoutNodeMode {
  /**
   * The size is fixed. The node will always report the size set, regardless of the size the child nodes report.
   */
  Fixed,

  /**
   * The size will be considered the maximal size the node is allowed to grow to. In case the child elements request a
   * size smaller than the one set, the smaller size will be reported instead.
   */
  Maximal,

  /**
   * The size will be considered the minimal size the node is allowed to shrink to. In case the child nodes request a
   * size bigger than the one set, the bigger size will be reported instead.
   */
  Minimal
}
