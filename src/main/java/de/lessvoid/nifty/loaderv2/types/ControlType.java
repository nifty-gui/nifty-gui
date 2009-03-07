package de.lessvoid.nifty.loaderv2.types;

import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.loaderv2.types.resolver.parameter.ParameterResolver;
import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolver;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

public class ControlType extends ElementType {
  private Logger log = Logger.getLogger(ControlType.class.getName());

  public ControlType() {
    super();
  }

  public ControlType(final Attributes attributes) {
    super(attributes);
  }

  protected ElementRenderer[] createElementRenderer(final Nifty nifty) {
    return null;
  }

  public Element create(
      final Element parent,
      final Nifty nifty,
      final Screen screen,
      final LayoutPart layoutPart,
      final StyleResolver styleResolver,
      final ParameterResolver parameterResolver,
      final Attributes attrib,
      final Object[] controllers) {
    ControlDefinitionType controlDefinition = nifty.resolveControlDefinition(getName());
    if (controlDefinition == null) {
      log.warning("controlDefinition [" + getName() + "] missing.");
      return null;
    }

    ElementType elementType = controlDefinition.getControlElementType();
    Attributes controlDefinitionAttrib = controlDefinition.getAttributes();
    Element control = elementType.createControl(
        parent,
        nifty,
        screen,
        layoutPart,
        styleResolver,
        parameterResolver,
        elementType.getAttributes(),
        controlDefinitionAttrib,
        attrib,
        controllers);
    applyEffects(control, screen, nifty, parameterResolver);
    applyInteract(nifty, control, controllers, screen.getScreenController());

    String childRootId = controlDefinitionAttrib.get("childRootId");
    Element childRootElement = control.findElementByName(childRootId);
    if (childRootElement != null) {
      applyChildren(childRootElement, screen, nifty, styleResolver, parameterResolver, controllers);
    }

    return control;
  }

  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<control> " + super.output(offset + 1);
  }

  private String getName() {
    return getAttributes().get("name");
  }
}
