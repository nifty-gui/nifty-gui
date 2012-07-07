package de.lessvoid.nifty.elements;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.render.RenderBuffer;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.NiftyIdGenerator;

/**
 * The Nifty core Element class is at the core of Niftys scenegraph. It's a hierarchical
 * @author void
 *
 */
public class Element {
  private final List<Element> children = new CopyOnWriteArrayList<Element>();
  private final String id;
  private final LayoutPart layoutPart = new LayoutPart();
  private final Matrix4f local = new Matrix4f();
  private float angle = 0;
  private float scale = 0;
  private float angleInc;
  private float x = 0;
  private final Color backgroundColor;

  public Element() {
    this(NiftyIdGenerator.generate());
  }

  public Element(final String id) {
    this.id = id;
    this.backgroundColor = Color.randomColor();
    this.layoutPart.getBox().setX((int)(Math.random() * 1000.f));
    this.layoutPart.getBox().setY((int)(Math.random() * 700.f));
    this.layoutPart.getBox().setWidth(150);
    this.layoutPart.getBox().setHeight(150);
    this.angleInc = (float) (Math.random() * 0.001f);
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

  public void update() {
    Box box = layoutPart.getBox();

//    local.setIdentity();
//    Matrix4f.mul(local, new Matrix4f().translate(new Vector2f(box.getX(), box.getY())), local);
    scale = (float) Math.sin(x) / 4.0f + 1.0f;
//    Matrix4f.mul(local, new Matrix4f().scale(new Vector3f(box.getWidth() * scale, box.getHeight() * scale, 1.0f)), local);
//    Matrix4f.mul(local, new Matrix4f().rotate(angle, new Vector3f(0.f, 0.f, 1.f)), local);
//    Matrix4f.mul(local, new Matrix4f().translate(new Vector2f(-0.5f, -0.5f)), local);

    angle += angleInc;
    x += 0.0001;
    for (int i=0; i<children.size(); i++) {
      children.get(i).update();
    }
  }

  public void render(final RenderBuffer renderBuffer) {
    renderBuffer.append(backgroundColor, scale, angle, layoutPart.getBox());

    for (int i=0; i<children.size(); i++) {
      children.get(i).render(renderBuffer);
    }
  }
}
