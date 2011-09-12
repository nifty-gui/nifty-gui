package de.lessvoid.nifty.loaderv2.types;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolver;
import de.lessvoid.xml.xpp3.Attributes;
import static org.easymock.classextension.EasyMock.*;

public class StyleTypeTest {
  private StyleType styleType = new StyleType();

  @Before
  public void setUp() {
    styleType.getAttributes().set("id", "my-style");

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
    StyleResolver styleResolver = createNiceMock(StyleResolver.class);

    ElementType elementType = new PanelType();
    styleType.applyTo(elementType, styleResolver);

    assertEquals("10%", elementType.getAttributes().get("height"));

    assertEquals(1, elementType.getEffects().onStartScreen.size());
    assertEquals("move", elementType.getEffects().onStartScreen.iterator().next().getAttributes().get("name"));

    assertEquals("onClickMethod()", elementType.interact.getAttributes().get("onClick"));
  }
}
