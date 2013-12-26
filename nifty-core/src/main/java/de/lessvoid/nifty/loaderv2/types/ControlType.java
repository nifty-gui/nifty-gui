package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loaderv2.types.helper.NullElementRendererCreator;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

public class ControlType extends ElementType {
  private static final Logger log = Logger.getLogger(ControlType.class.getName());

  public ControlType() {
    super();
  }

  public ControlType(@Nonnull final ControlType src) {
    super(src);
  }

  public ControlType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  @Override
  @Nonnull
  public ElementType copy() {
    return new ControlType(this);
  }

  @Override
  protected void makeFlat() {
    super.makeFlat();
    setTagName("<control>");
    setElementRendererCreator(new NullElementRendererCreator());
  }

  @Nullable
  private String getType() {
    String type = getAttributes().get("type");
    if (type != null) {
      return type;
    }

    return getAttributes().get("name");
  }

  @Override
  void internalApplyControl(@Nonnull final Nifty nifty) {
    ControlDefinitionType controlDefinition = nifty.resolveControlDefinition(getType());
    if (controlDefinition == null) {
      log.warning("controlDefinition [" + getType() + "] missing.");
      return;
    }

    Collection<ElementType> childCopy = new ArrayList<ElementType>();
    childCopy.addAll(elements);

    mergeFromElementType(controlDefinition);

    String childRootId = getAttributes().get("childRootId");
    if (childRootId != null) {
      if (!addChildrenToChildRoot(this, childRootId, childCopy)) {
        log.warning("childRootId [" + childRootId + "] could not be found in any childs of [" + this + "]");
      }
    }
  }

  @Override
  void makeFlatControlsInternal() {
    if (!elements.isEmpty()) {
      mergeFromElementType(elements.get(0));
    }
    resolveIds(this, getAttributes().get("id"));
  }

  private void resolveIds(@Nonnull final ElementType parent, @Nullable final String grantParentId) {
    for (ElementType element : parent.elements) {
      String id = element.getAttributes().get("id");
      if (id != null && id.startsWith("#")) {
        String elementId;
        // only get the last segment of the ID, as this is the actual ID of the element. Anything else causes IDs
        // that are too long.
        int lastSharpIndex = id.lastIndexOf('#');
        if (lastSharpIndex <= 0 - 1) {
          elementId = id;
        } else {
          elementId = id.substring(lastSharpIndex);
        }
        String parentId = parent.getAttributes().get("id");
        if (parentId != null) {
          element.getAttributes().set("id", parentId + elementId);
        } else if (grantParentId != null) {
          element.getAttributes().set("id", grantParentId + elementId);
        }
      }
      resolveIds(element, grantParentId);
    }
  }

  private boolean addChildrenToChildRoot(
      @Nonnull final ElementType elementType,
      @Nonnull final String childRootId,
      @Nonnull final Collection<ElementType> children) {
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
