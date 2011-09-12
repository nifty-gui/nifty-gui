package de.lessvoid.nifty.controls.scrollbar;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the ScrollbarImpl.
 * @author void
 */
public class ScrollbarImplTest {
  private ScrollbarImpl scrollbar = new ScrollbarImpl();
  private ScrollbarView view;
  private float viewSize = 4.f;

  @Before
  public void before() {
    view = createMock(ScrollbarView.class);
    expect(view.getAreaSize()).andReturn(4).anyTimes();
    expect(view.getMinHandleSize()).andReturn(1).anyTimes();
  }

  @After
  public void after() {
    verify(view);
  }

  @Test
  public void testNoEntries() {
    view.setHandle(0, 4);
    view.valueChanged(0.f);
    replay(view);

    scrollbar.bindToView(view, 0.f, 4.f, viewSize, 1.f, 4.f);
    assertEquals(0.f, scrollbar.getValue());
  }

  @Test
  public void testMaxEqualView() {
    view.setHandle(0, 4);
    view.valueChanged(0.f);
    replay(view);

    scrollbar.bindToView(view, 0.f, 4.f, viewSize, 1.f, 4.f); 
  }

  @Test
  public void testMaxDoubleView() {
    view.setHandle(0, 2);
    view.valueChanged(0.f);
    replay(view);

    scrollbar.bindToView(view, 0.f, 8.f, viewSize, 1.f, 4.f); 
  }

  @Test
  public void testMaxView() {
    view.setHandle(0, 1);
    view.valueChanged(0.f);
    replay(view);

    scrollbar.bindToView(view, 0.f, 16.f, viewSize, 1.f, 4.f); 
  }

  @Test
  public void testCurrentValueTooBigValue() {
    view.setHandle(0, 4);
    view.valueChanged(0.f);
    replay(view);

    scrollbar.bindToView(view, 1.f, 4.f, viewSize, 1.f, 4.f); 
  }

  @Test
  public void testCurrentValueMaximumValue() {
    view.setHandle(0, 4);
    view.valueChanged(0.f);
    replay(view);

    scrollbar.bindToView(view, 4.f, 4.f, viewSize, 1.f, 4.f); 
  }

  @Test
  public void testMovingCurrentValue() {
    view.setHandle(2, 1);
    view.valueChanged(8.f);
    replay(view);

    scrollbar.bindToView(view, 8.f, 16.f, viewSize, 1.f, 4.f); 
  }

  @Test
  public void testStepUp() {
    view.setHandle(0, 2);
    view.valueChanged(0.f);
    view.setHandle(1, 2);
    view.valueChanged(2.f);
    replay(view);

    scrollbar.bindToView(view, 0.f, 8.f, viewSize, 2.f, 4.f);
    scrollbar.stepUp();
  }

  @Test
  public void testStepUpLimit() {
    view.setHandle(2, 2);
    view.valueChanged(4.f);
    view.setHandle(2, 2);
    replay(view);

    scrollbar.bindToView(view, 8.f, 8.f, viewSize, 1.f, 4.f);
    scrollbar.stepUp();
  }

  @Test
  public void testStepDownLimit() {
    view.setHandle(0, 2);
    view.valueChanged(0.f);
    view.setHandle(0, 2);
    replay(view);

    scrollbar.bindToView(view, 0.f, 8.f, viewSize, 2.f, 4.f);
    scrollbar.stepDown();
  }

  @Test
  public void testStepDown() {
    view.setHandle(2, 2);
    view.valueChanged(4.f);
    view.setHandle(1, 2);
    view.valueChanged(2.f);
    replay(view);

    scrollbar.bindToView(view, 4.f, 8.f, viewSize, 2.f, 4.f);
    scrollbar.stepDown();
  }

  @Test
  public void testPageUp() {
    view.setHandle(0, 2);
    view.valueChanged(0.f);
    view.setHandle(2, 2);
    view.valueChanged(4.f);
    replay(view);

    scrollbar.bindToView(view, 0.f, 8.f, viewSize, 2.f, 4.f);
    scrollbar.stepPageUp();
  }

  @Test
  public void testPageUpLimit() {
    view.setHandle(2, 2);
    view.valueChanged(4.f);
    view.setHandle(2, 2);
    replay(view);

    scrollbar.bindToView(view, 8.f, 8.f, viewSize, 1.f, 4.f);
    scrollbar.stepPageUp();
  }

  @Test
  public void testPageDownLimit() {
    view.setHandle(0, 2);
    view.valueChanged(0.f);
    view.setHandle(0, 2);
    replay(view);

    scrollbar.bindToView(view, 0.f, 8.f, viewSize, 2.f, 4.f);
    scrollbar.stepPageDown();
  }

  @Test
  public void testPageDown() {
    view.setHandle(2, 2);
    view.valueChanged(4.f);
    view.setHandle(0, 2);
    view.valueChanged(0.f);
    replay(view);

    scrollbar.bindToView(view, 4.f, 8.f, viewSize, 2.f, 4.f);
    scrollbar.stepPageDown();
  }

  @Test
  public void testSetValue() {
    view.setHandle(0, 4);
    view.valueChanged(0.f);
    view.setHandle(0, 4);
    replay(view);

    scrollbar.bindToView(view, 0.f, 4.f, viewSize, 1.f, 4.f);
    scrollbar.setValue(4.f);
    assertEquals(0.f, scrollbar.getValue());
  }

  @Test
  public void testSetValueMinLimit() {
    view.setHandle(0, 4);
    view.valueChanged(0.f);
    view.setHandle(0, 4);
    replay(view);

    scrollbar.bindToView(view, 0.f, 4.f, viewSize, 1.f, 4.f);
    scrollbar.setValue(-4.f);
    assertEquals(0.f, scrollbar.getValue());
  }

  @Test
  public void testSetValueMaxLimit() {
    view.setHandle(0, 4);
    view.valueChanged(0.f);
    view.setHandle(0, 4);
    replay(view);

    scrollbar.bindToView(view, 0.f, 4.f, viewSize, 1.f, 4.f);
    scrollbar.setValue(40.f);
    assertEquals(0.f, scrollbar.getValue());
  }

  @Test
  public void testSetup() {
    view.setHandle(0, 4);
    view.valueChanged(0.f);
    view.setHandle(2, 2);
    view.valueChanged(4.f);
    replay(view);

    scrollbar.bindToView(view, 0.f, 4.f, viewSize, 1.f, 4.f);
    scrollbar.setup(4.f, 8.f, viewSize, 2.f, 4.f);
    assertEquals(2.f, scrollbar.getButtonStepSize());
    assertEquals(4.f, scrollbar.getValue());
    assertEquals(4.f, scrollbar.getPageStepSize());
  }

  @Test
  public void testSetMax() {
    view.setHandle(0, 4);
    view.valueChanged(0.f);
    view.setHandle(0, 2);
    replay(view);

    scrollbar.bindToView(view, 0.f, 4.f, viewSize, 1.f, 4.f);
    scrollbar.setWorldMax(8.f);
    assertEquals(8.f, scrollbar.getWorldMax());
  }

  @Test
  public void testSetPageStepSize() {
    replay(view);
    scrollbar.setPageStepSize(12.f);
    assertEquals(12.f, scrollbar.getPageStepSize());
  }

  @Test
  public void testInteractionCantMove() {
    view.setHandle(0, 4);
    view.valueChanged(0.f);
    replay(view);

    scrollbar.bindToView(view, 0.f, 4.f, viewSize, 1.f, 4.f);
    scrollbar.interactionClick(3);
  }

  @Test
  public void testPageDownInteractionClick() {
    view.setHandle(0, 2);
    view.valueChanged(0);
    view.setHandle(2, 2);
    view.valueChanged(4);
    replay(view);

    scrollbar.bindToView(view, 0.f, 8.f, viewSize, 1.f, 4.f);
    scrollbar.interactionClick(3);
  }

  @Test
  public void testPageUpInteractionClick() {
    view.setHandle(2, 2);
    view.valueChanged(4.f);
    view.setHandle(0, 2);
    view.valueChanged(0.f);
    replay(view);

    scrollbar.bindToView(view, 4.f, 8.f, viewSize, 1.f, 4.f);
    scrollbar.interactionClick(1);
  }

  @Test
  public void testMoveHandle() {
    view.setHandle(2, 2);
    view.valueChanged(4.f);
    view.setHandle(0, 2);
    view.valueChanged(0.f);
    replay(view);

    scrollbar.bindToView(view, 4.f, 8.f, viewSize, 1.f, 4.f);
    scrollbar.interactionClick(3);
    scrollbar.interactionMove(0);
  }
}
