package de.lessvoid.nifty.loaderv2.types;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolver;
import de.lessvoid.xml.xpp3.Attributes;
import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

public class ElementTypeTest {
  private ElementType elementType = new PanelType();
  private ElementType elementTypeChild = new PanelType();
  private StyleResolver styleResolver;

  @Before
  public void setUp() {
    Attributes attr = new Attributes();
    attr.set("name", "value");
    elementType.initFromAttributes(attr);

    InteractType interact = new InteractType();
    attr = new Attributes();
    attr.set("onClick", "onClickMethod()");
    interact.initFromAttributes(attr);
    elementType.setInteract(interact);

    EffectsType effects = new EffectsType();
    EffectType effect = new EffectType();
    attr = new Attributes();
    attr.set("name", "move");
    effect.initFromAttributes(attr);
    effects.addOnStartScreen(effect);
    elementType.setEffect(effects);

    attr = new Attributes();
    attr.set("name2", "value2");
    elementTypeChild.initFromAttributes(attr);
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
    assertEquals(1, effects.onStartScreen.size());

    EffectType effect = effects.onStartScreen.iterator().next();
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
