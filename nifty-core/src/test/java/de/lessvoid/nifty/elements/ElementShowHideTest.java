package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.spi.time.TimeProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
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

    e1 = createMockBuilder(Element.class)
        .withConstructor(
            Nifty.class,
            ElementType.class,
            String.class,
            Element.class,
            FocusHandler.class,
            boolean.class,
            TimeProvider.class,
            ElementRenderer[].class)
        .withArgs(
            niftyMock, null, "e1", null, focusHandler, false, null, null
        )
        .createMock();

    e2 = createMockBuilder(Element.class)
        .withConstructor(
            Nifty.class,
            ElementType.class,
            String.class,
            Element.class,
            FocusHandler.class,
            boolean.class,
            TimeProvider.class,
            ElementRenderer[].class)
        .withArgs(
            niftyMock, null, "e1", null, focusHandler, false, null, null
        ).createMock();

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
  public void testDefaults() {
    assertTrue(e1.isVisible());
    assertTrue(e2.isVisible());
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
    assertTrue(e2.isVisible());
  }

  @Test
  public void testSimpleParentHideWithChildHidden() {
    e2.hide();
    e1.hide();

    assertFalse(e1.isVisible());
    assertFalse(e2.isVisible());

    e1.show();
    assertTrue(e1.isVisible());
    assertFalse(e2.isVisible());

    e2.show();
    assertTrue(e2.isVisible());
  }

  @Test
  public void testChildHiddenAndParentShow() {
    e2.hide();
    e1.show();

    assertTrue(e1.isVisible());
    assertFalse(e2.isVisible());
  }
}
