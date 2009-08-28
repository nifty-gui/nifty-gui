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

//  public Element create(
//      final Element parent,
//      final Nifty nifty,
//      final Screen screen,
//      final LayoutPart layoutPart,
//      final StyleResolver styleResolver,
//      final ParameterResolver parameterResolver,
//      final Attributes attrib,
//      final Object[] controllers) {
//    ControlDefinitionType controlDefinition = nifty.resolveControlDefinition(getName());
//    if (controlDefinition == null) {
//      log.warning("controlDefinition [" + getName() + "] missing.");
//      return null;
//    }
//
//    ElementType elementType = controlDefinition.getControlElementType();
//    Attributes controlDefinitionAttrib = controlDefinition.getAttributes();
//    Element control = elementType.createControl(
//        parent,
//        nifty,
//        screen,
//        layoutPart,
//        styleResolver,
//        parameterResolver,
//        elementType.getAttributes(),
//        controlDefinitionAttrib,
//        attrib,
//        controllers);
//    applyEffects(control, screen, nifty, parameterResolver);
//    applyInteract(nifty, control, controllers, screen.getScreenController());
//
//    String childRootId = controlDefinitionAttrib.get("childRootId");
//    Element childRootElement = control.findElementByName(childRootId);
//    if (childRootElement != null) {
//      applyChildren(childRootElement, screen, nifty, styleResolver, parameterResolver, controllers);
//    }
//
//    return control;
//  }

  private String getName() {
    return getAttributes().get("name");
  }

  void internalApplyControl(final Nifty nifty) {
    ControlDefinitionType controlDefinition = nifty.resolveControlDefinition(getName());
    if (controlDefinition == null) {
      log.warning("controlDefinition [" + getName() + "] missing.");
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
    mergeFromElementType(elements.iterator().next());
  }

//    mergeFromElementType(controlDefinition);
//    String baseStyle = getAttributes().get("style");
//    mergeFromElementType(controlDefinition.getControlElementType());
//    setTagName("(control) " + tagName);
//
//    String style = controlDefinition.getControlElementType().getAttributes().get("style");
//    if (style != null) {
//      if (style.startsWith("#")) {
//        getAttributes().set("style", baseStyle + style);
//      }
//    }
//  }

  private boolean addChildrenToChildRoot(
      final ElementType elementType,
      final String childRootId,
      final Collection < ElementType > children) {
    for (ElementType element : elementType.elements) {
      if (childRootId.equals(element.getAttributes().get("id"))) {
        element.elements.addAll(children);
        return true;
      } else {
        if (addChildrenToChildRoot(element, childRootId, children)) {
          return true;
        }
      }
    }
    return false;
  }
}
