package de.lessvoid.nifty.loader.xpp3.processor;


import java.util.Hashtable;
import java.util.Map;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.SubstitutionGroup;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.elements.RegisterControlDefinitionType;

/**
 * Regîster a control.
 */
public class RegisterControlDefinitionTypeProcessor implements XmlElementProcessor {

  /**
   * registered effects.
   */
  private Map < String, RegisterControlDefinitionType > registeredControls =
    new Hashtable < String, RegisterControlDefinitionType >();

  public RegisterControlDefinitionTypeProcessor() {
    
  }

  /**
   * process.
   * @param xmlParser xmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    String name = attributes.get("name");
    String controller = attributes.get("controller");
    String inputMapping = attributes.get("inputMapping");
    final RegisterControlDefinitionType registerControl = new RegisterControlDefinitionType(
        name,
        controller,
        inputMapping);
    registeredControls.put(name, registerControl);

    ElementType pseudoElement = new ElementType() {
      public void addElementType(final ElementType elementType) {
        registerControl.addElement(elementType);
      }
    };

    xmlParser.nextTag();
    xmlParser.optional("interact", new InteractTypeProcessor(pseudoElement));
    xmlParser.optional("hover", new HoverTypeProcessor(pseudoElement));
    xmlParser.optional("effect", new EffectsTypeProcessor(pseudoElement));
    xmlParser.zeroOrMore(
          new SubstitutionGroup().
            add("panel", new PanelTypeProcessor(pseudoElement)).
            add("text", new TextTypeProcessor(pseudoElement)).
            add("image", new ImageTypeProcessor(pseudoElement)).
            add("menu", new MenuTypeProcessor(pseudoElement))
            );
  }

  /**
   * get registered effects.
   * @return registered effects
   */
  public Map < String, RegisterControlDefinitionType > getRegisteredControls() {
    return registeredControls;
  }
}
