package de.lessvoid.nifty.elements;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.lessvoid.nifty.tools.NiftyIdGenerator;

/**
 * The Nifty core Element class is at the core of Niftys scenegraph. It's a hierarchical
 * @author void
 *
 */
public class Element {
  private final List<Element> children = new CopyOnWriteArrayList<Element>();
  private final String id;

  public Element() {
    this.id = NiftyIdGenerator.generate();
  }

  public Element(final String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void addElement(final Element element) {
    children.add(element);
  }

  public void insertElement(final int index, final Element element) {
    children.add(index, element);
  }

  public void removeElement(final Element element) {
    children.remove(element);
  }

  public void removeElement(final int index) {
    children.remove(index);
  }

  public List<Element> getChildren() {
    return Collections.unmodifiableList(children);
  }
}
