package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolver;
import de.lessvoid.xml.xpp3.Attributes;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class ElementTypeTest {
  private ElementType elementType;
  private StyleResolver styleResolver;

  @Before
  public void setUp() {
    Attributes attr = new Attributes();
    attr.set("name", "value");
    elementType = new PanelType(attr);

    attr = new Attributes();
    attr.set("onClick", "onClickMethod()");
    elementType.setInteract(new InteractType(attr));

    EffectsType effects = new EffectsType();
    attr = new Attributes();
    attr.set("name", "move");
    EffectType effect = new EffectType(attr);
    effects.addOnStartScreen(effect);
    elementType.setEffect(effects);

    attr = new Attributes();
    attr.set("name2", "value2");
    ElementType elementTypeChild = new PanelType(attr);
    elementType.elements.add(elementTypeChild);
  }

  @Test
  public void testApplyStyleInternalNoStyle() {
    styleResolver = createMock(StyleResolver.class);
    replay(styleResolver);

    elementType.applyStyleInternal(styleResolver);
    assertEquals("value", elementType.getAttributes().get("name"));

    InteractType interact = elementType.getInteract();
    assertEquals("onClickMethod()", interact.getAttributes().get("onClick"));

    EffectsType effects = elementType.getEffects();
    assertEquals(1, effects.getEventEffectTypes(EffectEventId.onStartScreen).size());

    EffectType effect = effects.getEventEffectTypes(EffectEventId.onStartScreen).iterator().next();
    assertEquals("move", effect.getAttributes().get("name"));

    assertEquals(1, elementType.elements.size());
    ElementType child = elementType.elements.iterator().next();
    assertEquals("value2", child.getAttributes().get("name2"));
  }

  @Test
  public void testApplyStyleInternalWithStyle() {
    styleResolver = createMock(StyleResolver.class);

    StyleType myStyle = createMock(StyleType.class);
    myStyle.applyTo(elementType, styleResolver);
    replay(myStyle);

    expect(styleResolver.resolve("myStyle")).andReturn(myStyle);
    replay(styleResolver);

    elementType.getAttributes().set("style", "myStyle");
    elementType.applyStyleInternal(styleResolver);

    verify(myStyle);
    verify(styleResolver);
  }
}
