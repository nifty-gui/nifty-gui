package de.lessvoid.nifty.tools;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class TargetElementResolverTest {

  private Element baseElement = null;

  @Test
  public void testResolveWithNull() {
    TargetElementResolver resolver = new TargetElementResolver(null, null);
    assertNull(resolver.resolve(null));
  }

  @Test
  public void testResolveDirectIdFound() {
    Element expected = createMock(Element.class);
    replay(expected);

    Screen screen = createMock(Screen.class);
    expect(screen.findElementByName("id")).andReturn(expected);
    replay(screen);

    TargetElementResolver resolver = new TargetElementResolver(screen, baseElement);
    assertEquals(expected, resolver.resolve("id"));

    verify(screen);
    verify(expected);
  }

  @Test
  public void testResolveDirectIdNotFound() {
    Screen screen = createMock(Screen.class);
    expect(screen.findElementByName("id")).andReturn(null);
    replay(screen);

    TargetElementResolver resolver = new TargetElementResolver(screen, baseElement);
    assertNull(resolver.resolve("id"));

    verify(screen);
  }

  @Test
  public void testResolveParentWithIdFound() {
    Element expected = createMock(Element.class);
    replay(expected);

    Element parent = createMock(Element.class);
    expect(parent.findElementById("id")).andReturn(expected);
    replay(parent);

    baseElement = createMock(Element.class);
    expect(baseElement.getParent()).andReturn(parent);
    replay(baseElement);

    TargetElementResolver resolver = new TargetElementResolver(null, baseElement);
    assertEquals(expected, resolver.resolve("#parent#id"));

    verify(expected);
    verify(parent);
    verify(baseElement);
  }

  @Test
  public void testResolveParentWithIdNotFound() {
    Element parent = createMock(Element.class);
    expect(parent.findElementById("id")).andReturn(null);
    replay(parent);

    baseElement = createMock(Element.class);
    expect(baseElement.getParent()).andReturn(parent);
    replay(baseElement);

    TargetElementResolver resolver = new TargetElementResolver(null, baseElement);
    assertNull(resolver.resolve("#parent#id"));

    verify(parent);
    verify(baseElement);
  }

  @Test
  public void testResolveDirectParent() {
    Element parent = createMock(Element.class);
    replay(parent);

    baseElement = createMock(Element.class);
    expect(baseElement.getParent()).andReturn(parent);
    replay(baseElement);

    TargetElementResolver resolver = new TargetElementResolver(null, baseElement);
    assertEquals(parent, resolver.resolve("#parent"));

    verify(parent);
    verify(baseElement);
  }
}
