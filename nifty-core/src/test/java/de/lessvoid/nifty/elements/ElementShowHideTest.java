package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.spi.time.TimeProvider;
import org.easymock.classextension.ConstructorArgs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ElementShowHideTest {
  private Nifty niftyMock;
  private Element e1;
  private Element e2;
  private FocusHandler focusHandler = new FocusHandler();

  @Before
  public void before() throws Exception {
    niftyMock = createNiceMock(Nifty.class);
    expect(niftyMock.getAlternateKey()).andReturn(null).times(2);
    replay(niftyMock);

    e1 = createMock(
        Element.class,
        new ConstructorArgs(
            Element.class.getConstructor(
                Nifty.class, ElementType.class, String.class, Element.class, FocusHandler.class, boolean.class,
                TimeProvider.class, ElementRenderer[].class),
            niftyMock, null, "e1", null, focusHandler, false, null, null));

    e2 = createMock(
        Element.class,
        new ConstructorArgs(
            Element.class.getConstructor(
                Nifty.class, ElementType.class, String.class, Element.class, FocusHandler.class, boolean.class,
                TimeProvider.class, ElementRenderer[].class),
            niftyMock, null, "e1", null, focusHandler, false, null, null));
    e1.addChild(e2);
    replay(e1);
    replay(e2);
  }

  @After
  public void after() {
    verify(niftyMock);
    verify(e1);
    verify(e2);
  }

  @Test
  public void testSimpleShow() {
    e2.show();

    assertTrue(e1.isVisible());
    assertTrue(e2.isVisible());
  }

  @Test
  public void testSimpleHide() {
    e2.hide();

    assertTrue(e1.isVisible());
    assertFalse(e2.isVisible());
  }

  @Test
  public void testSimpleHideTwice() {
    e2.hide();
    e2.hide();

    assertTrue(e1.isVisible());
    assertFalse(e2.isVisible());
  }

  @Test
  public void testHideMutipleAndShow() {
    e2.hide();
    e2.hide();
    e2.hide();
    e2.hide();
    e2.hide();
    e2.show();

    assertTrue(e1.isVisible());
    assertTrue(e2.isVisible());
  }

  @Test
  public void testSimpleParentHide() {
    e1.hide();

    assertFalse(e1.isVisible());
    assertFalse(e2.isVisible());
  }

  @Test
  public void testSimpleParentHideWithChildHidden() {
    e2.hide();
    e1.hide();

    assertFalse(e1.isVisible());
    assertFalse(e2.isVisible());

    e1.show();
    assertTrue(e1.isVisible());
    assertTrue(e2.isVisible()); // visible will currently override all child elements!

    e2.show();
    assertTrue(e2.isVisible());
  }

  @Test
  public void testChildHiddenAndParentShow() {
    e2.hide();
    e1.show();

    assertTrue(e1.isVisible());
    assertTrue(e2.isVisible()); // visible will currently override all child elements!
  }

}
