package de.lessvoid.nifty.internal;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.layout.Layoutable;

public class NiftyNode implements de.lessvoid.nifty.api.NiftyNode, Layoutable {

  /**
   * The id of this element.
   */
  private final String id;

  /**
   * the box.
   */
  private final Box layoutPos;

  /**
   * the box constraints.
   */
  private final BoxConstraints constraints;

  /**
   * The child elements of this element.
   */
  private final List<NiftyNode> children = new CopyOnWriteArrayList<NiftyNode>();

  /**
   * Create new Element.
   */
  public NiftyNode() {
    this(NiftyIdGenerator.generate());
  }

  /**
   * Create new Element with the given id.
   * @param id the id
   */
  public NiftyNode(final String id) {
    this(id, new Box(), new BoxConstraints());
  }

  /**
   * Create new Element with the given id, Box and BoxConstraints.
   * @param id the id
   * @param newBox the box
   * @param newBoxConstraints the box constraints
   */
  public NiftyNode(final String id, final Box newBox, final BoxConstraints newBoxConstraints) {
    this.id = id;
    this.layoutPos = newBox;
    this.constraints = newBoxConstraints;
  }

  // Nifty API interface implementation

  @Override
  public void setWidthConstraint(final UnitValue px) {
  }

  @Override
  public void setHeightConstraint(final UnitValue px) {
  }

  // Layoutable Implementation

  @Override
  public Box getLayoutPos() {
    return layoutPos;
  }

  @Override
  public BoxConstraints getBoxConstraints() {
    return constraints;
  }
}
