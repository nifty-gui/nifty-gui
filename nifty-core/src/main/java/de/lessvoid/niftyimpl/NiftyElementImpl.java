package de.lessvoid.niftyimpl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.lessvoid.nifty.NiftyElement;
import de.lessvoid.nifty.layout.SizeValue;
import de.lessvoid.niftyimpl.layout.Box;
import de.lessvoid.niftyimpl.layout.BoxConstraints;
import de.lessvoid.niftyimpl.layout.Layoutable;

public class NiftyElementImpl implements NiftyElement, Layoutable {

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
  private final List<NiftyElementImpl> children = new CopyOnWriteArrayList<NiftyElementImpl>();

  /**
   * Create new Element.
   */
  public NiftyElementImpl() {
    this(NiftyIdGenerator.generate());
  }

  /**
   * Create new Element with the given id.
   * @param id the id
   */
  public NiftyElementImpl(final String id) {
    this(id, new Box(), new BoxConstraints());
  }

  /**
   * Create new Element with the given id, Box and BoxConstraints.
   * @param id the id
   * @param newBox the box
   * @param newBoxConstraints the box constraints
   */
  public NiftyElementImpl(final String id, final Box newBox, final BoxConstraints newBoxConstraints) {
    this.id = id;
    this.layoutPos = newBox;
    this.constraints = newBoxConstraints;
  }

  @Override
  public Box getLayoutPos() {
    return layoutPos;
  }

  @Override
  public BoxConstraints getBoxConstraints() {
    return constraints;
  }
}
