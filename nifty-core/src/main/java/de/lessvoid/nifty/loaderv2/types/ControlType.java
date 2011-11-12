package de.lessvoid.nifty.loaderv2.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loaderv2.types.helper.NullElementRendererCreator;
import de.lessvoid.xml.xpp3.Attributes;

public class ControlType extends ElementType {
  private Logger log = Logger.getLogger(ControlType.class.getName());

  public ControlType() {
    super();
  }

  public ControlType(final ControlType src) {
    super(src);
  }

  public ControlType(final Attributes attributes) {
    super(attributes);
  }

  public ElementType copy() {
    return new ControlType(this);
  }

  protected void makeFlat() {
    super.makeFlat();
    setTagName("<control>");
    setElementRendererCreator(new NullElementRendererCreator());
  }

  private String getType() {
    String type = getAttributes().get("type");
    if (type != null) {
      return type;
    }

    return getAttributes().get("name");
  }

  @Override
  void internalApplyControl(final Nifty nifty) {
    ControlDefinitionType controlDefinition = nifty.resolveControlDefinition(getType());
    if (controlDefinition == null) {
      log.warning("controlDefinition [" + getType() + "] missing.");
      return;
    }

    Collection < ElementType > childCopy = new ArrayList < ElementType > ();
    childCopy.addAll(elements);

    mergeFromElementType(controlDefinition);

    String childRootId = getAttributes().get("childRootId");
    if (childRootId != null) {
      if (!addChildrenToChildRoot(this, childRootId, childCopy)) {
        log.warning("childRootId [" + childRootId + "] could not be found in any childs of [" + this + "]");
      }
    }
  }

  void makeFlatControlsInternal() {
    if (!elements.isEmpty()) {
      mergeFromElementType(elements.iterator().next());
    }
    resolveIds(this, getAttributes().get("id"));
  }

  private void resolveIds(final ElementType parent, final String grantParentId) {
    for (ElementType element : parent.elements) {
      String id = element.getAttributes().get("id");
      if (id != null && id.startsWith("#")) {
        String parentId = parent.getAttributes().get("id");
        if (parentId != null) {
          element.getAttributes().set("id", parentId + id);
        } else if (grantParentId != null) {
          element.getAttributes().set("id", grantParentId + id);
        }
      }
      resolveIds(element, grantParentId);
    }
  }

  private boolean addChildrenToChildRoot(
      final ElementType elementType,
      final String childRootId,
      final Collection < ElementType > children) {
    if (children.isEmpty()) {
      return true;
    }
    for (ElementType element : elementType.elements) {
      if (childRootId.equals(element.getAttributes().get("id"))) {
        element.elements.clear();
        element.elements.addAll(children);
        return true;
      } else if (addChildrenToChildRoot(element, childRootId, children)) {
          return true;
      }
    }
    return false;
  }
}
