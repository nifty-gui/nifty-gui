package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNode;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FixedSizeLayoutNode implements NiftyNode {
  @Nonnull
  private final FixedSizeLayoutNodeImpl implementation;

  public FixedSizeLayoutNode(final float width, final float height) {
    this(new FixedSizeLayoutNodeImpl(width, height));
  }

  FixedSizeLayoutNode(@Nonnull final FixedSizeLayoutNodeImpl implementation) {
    this.implementation = implementation;
  }

  float getWidth() {
    return implementation.getWidth();
  }

  void setWidth(float width) {
    implementation.setWidth(width);
  }

  float getHeight() {
    return implementation.getHeight();
  }

  void setHeight(float height) {
    implementation.setHeight(height);
  }
}
