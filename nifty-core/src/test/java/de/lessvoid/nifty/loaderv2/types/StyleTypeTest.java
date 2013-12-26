package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolver;
import de.lessvoid.xml.xpp3.Attributes;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.classextension.EasyMock.createNiceMock;
import static org.junit.Assert.assertEquals;

public class StyleTypeTest {
  private StyleType styleType = new StyleType();

  @Before
  public void setUp() {
    styleType.getAttributes().set("id", "my-style");

    Attributes attributes = new Attributes();
    attributes.set("height", "10%");
    AttributesType attributesType = new AttributesType(attributes);
    styleType.setAttributes(attributesType);

    EffectsType effectType = new EffectsType();
    attributes = new Attributes();
    attributes.set("name", "move");
    EffectType effect = new EffectType(attributes);
    effectType.addOnStartScreen(effect);
    styleType.setEffect(effectType);

    attributes = new Attributes();
    attributes.set("onClick", "onClickMethod()");
    InteractType interactType = new InteractType(attributes);
    styleType.setInteract(interactType);
  }

  @Test
  public void testApply() {
    StyleResolver styleResolver = createNiceMock(StyleResolver.class);

    ElementType elementType = new PanelType();
    styleType.applyTo(elementType, styleResolver);

    assertEquals("10%", elementType.getAttributes().get("height"));

    assertEquals(1, elementType.getEffects().getEventEffectTypes(EffectEventId.onStartScreen).size());
    assertEquals("move", elementType.getEffects().getEventEffectTypes(EffectEventId.onStartScreen).iterator().next()
        .getAttributes().get("name"));

    assertEquals("onClickMethod()", elementType.interact.getAttributes().get("onClick"));
  }
}
