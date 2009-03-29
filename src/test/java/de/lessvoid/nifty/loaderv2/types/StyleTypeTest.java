package de.lessvoid.nifty.loaderv2.types;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.xml.xpp3.Attributes;

public class StyleTypeTest {
  private StyleType styleType = new StyleType();

  @Before
  public void setUp() {
    AttributesType attributesType = new AttributesType();
    Attributes attributes = new Attributes();
    attributes.set("height", "10%");
    attributesType.initFromAttributes(attributes);
    styleType.setAttributes(attributesType);

    EffectsType effectType = new EffectsType();
    EffectType effect = new EffectType();
    attributes = new Attributes();
    attributes.set("name", "move");
    effect.initFromAttributes(attributes);
    effectType.addOnStartScreen(effect);
    styleType.setEffect(effectType);

    InteractType interactType = new InteractType();
    attributes = new Attributes();
    attributes.set("onClick", "onClickMethod()");
    interactType.initFromAttributes(attributes);
    styleType.setInteract(interactType);
  }

  @Test
  public void testApply() {
    ElementType elementType = new PanelType();
    styleType.applyTo(elementType, null);

    assertEquals("10%", elementType.getAttributes().get("height"));

    assertEquals(1, elementType.getEffects().onStartScreen.size());
    assertEquals("move", elementType.getEffects().onStartScreen.iterator().next().getAttributes().get("name"));

    assertEquals("onClickMethod()", elementType.interact.getAttributes().get("onClick"));
  }
}
