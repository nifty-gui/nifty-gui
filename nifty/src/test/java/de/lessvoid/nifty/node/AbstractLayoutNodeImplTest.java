package de.lessvoid.nifty.node;

import de.lessvoid.nifty.NiftyLayout;
import de.lessvoid.nifty.types.NiftyRect;
import de.lessvoid.nifty.types.NiftySize;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static de.lessvoid.nifty.types.NiftyPoint.newNiftyPoint;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@RunWith(EasyMockRunner.class)
public class AbstractLayoutNodeImplTest {
  private AbstractLayoutNodeImpl<?> testInstance;

  @Mock
  private NiftyLayout layout;

  @Before
  public void prepare() {
    testInstance = createMockBuilder(AbstractLayoutNodeImpl.class)
        .addMockedMethod("measureInternal")
        .addMockedMethod("arrangeInternal")
        .withConstructor()
        .createMock();
  }

  @Test
  public void testAttach() throws Exception {
    layout.reportMeasureInvalid(testInstance);
    expectLastCall();
    layout.reportArrangeInvalid(testInstance);
    expectLastCall();

    replay(layout, testInstance);
    testInstance.onAttach(layout);
    verify(layout, testInstance);
  }

  @Test(expected = IllegalStateException.class)
  public void testAttachException() throws Exception {
    layout.reportMeasureInvalid(testInstance);
    expectLastCall();
    layout.reportArrangeInvalid(testInstance);
    expectLastCall();

    replay(layout, testInstance);
    testInstance.onAttach(layout);
    testInstance.onAttach(layout);
    verify(layout, testInstance);
  }

  @Test
  public void testDetach() throws Exception {
    layout.reportMeasureInvalid(testInstance);
    expectLastCall();
    layout.reportArrangeInvalid(testInstance);
    expectLastCall();

    layout.reportRemoval(testInstance);
    EasyMock.expectLastCall();
    replay(layout, testInstance);
    testInstance.onAttach(layout);
    testInstance.onDetach(layout);
    verify(layout, testInstance);
  }

  @Test(expected = IllegalStateException.class)
  public void testDetachException1() throws Exception {
    replay(layout, testInstance);
    testInstance.onDetach(layout);
    verify(layout, testInstance);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDetachException2() throws Exception {
    layout.reportMeasureInvalid(testInstance);
    expectLastCall();
    layout.reportArrangeInvalid(testInstance);
    expectLastCall();

    NiftyLayout secondLayout = EasyMock.createMock(NiftyLayout.class);
    replay(layout, secondLayout, testInstance);
    testInstance.onAttach(layout);
    testInstance.onDetach(secondLayout);
    verify(layout, secondLayout, testInstance);
  }

  @Test
  public void testIsMeasureValid() throws Exception {
    layout.reportMeasureInvalid(testInstance);
    expectLastCall();
    layout.reportArrangeInvalid(testInstance);
    expectLastCall();

    assertFalse(testInstance.isMeasureValid());
    replay(layout, testInstance);
    testInstance.onAttach(layout);
    verify(layout, testInstance);
    assertFalse(testInstance.isMeasureValid());
  }

  @Test
  public void testIsArrangeValid() throws Exception {
    layout.reportMeasureInvalid(testInstance);
    expectLastCall();
    layout.reportArrangeInvalid(testInstance);
    expectLastCall();

    assertFalse(testInstance.isArrangeValid());
    replay(layout, testInstance);
    testInstance.onAttach(layout);
    verify(layout, testInstance);
    assertFalse(testInstance.isArrangeValid());
  }

  @Test
  public void testInvalidateMeasure() throws Exception {
    /* Setup Test */
    EasyMock.expect(testInstance.measureInternal(NiftySize.INFINITE)).andReturn(NiftySize.ZERO);
    layout.reportMeasureInvalid(testInstance);
    expectLastCall().times(2);
    layout.reportArrangeInvalid(testInstance);
    expectLastCall().once();
    replay(layout, testInstance);

    /* Execute Test */
    testInstance.onAttach(layout);
    testInstance.measure(NiftySize.INFINITE);

    assertTrue(testInstance.isMeasureValid());
    testInstance.invalidateMeasure();
    verify(layout, testInstance);
    assertFalse(testInstance.isMeasureValid());
  }

  @Test
  public void testInvalidateArrange() throws Exception {
    /* Setup Test */
    NiftySize tempSize = new NiftySize(10, 10);
    NiftyRect tempRect = NiftyRect.newNiftyRect(newNiftyPoint(0, 0), tempSize);

    expect(testInstance.measureInternal(tempSize)).andReturn(tempSize);
    layout.reportMeasureInvalid(testInstance);
    layout.reportArrangeInvalid(testInstance);
    layout.reportArrangeInvalid(testInstance);
    layout.reportChangedArrangement(testInstance);
    testInstance.arrangeInternal(tempRect);
    replay(layout, testInstance);

    /* Execute Test */
    testInstance.onAttach(layout);
    testInstance.measure(tempSize);
    testInstance.arrange(tempRect);

    assertTrue(testInstance.isArrangeValid());
    testInstance.invalidateArrange();

    verify(layout, testInstance);
    assertFalse(testInstance.isArrangeValid());
  }

  @Test
  public void testGetDesiredSize() throws Exception {
    /* Setup Test */
    NiftySize tempSize = new NiftySize(10, 10);
    layout.reportMeasureInvalid(testInstance);
    layout.reportArrangeInvalid(testInstance);
    expect(testInstance.measureInternal(NiftySize.INFINITE)).andReturn(tempSize);
    replay(layout, testInstance);

    /* Execute Test */
    testInstance.onAttach(layout);
    testInstance.measure(NiftySize.INFINITE);

    assertTrue(testInstance.isMeasureValid());
    assertEquals(tempSize, testInstance.getDesiredSize());
    verify(layout, testInstance);
  }

  @Test
  public void testGetArrangedRect() throws Exception {
    /* Setup Test */
    NiftySize tempSize = new NiftySize(10, 10);
    NiftyRect tempRect = NiftyRect.newNiftyRect(newNiftyPoint(0, 0), tempSize);

    layout.reportMeasureInvalid(testInstance);
    expectLastCall();
    layout.reportArrangeInvalid(testInstance);
    expectLastCall();
    
    expect(testInstance.measureInternal(tempSize)).andReturn(tempSize);
    testInstance.arrangeInternal(tempRect);
    EasyMock.expectLastCall().once();
    layout.reportChangedArrangement(testInstance);
    expectLastCall();

    replay(layout, testInstance);

    /* Execute Test */
    testInstance.onAttach(layout);
    testInstance.measure(tempSize);
    testInstance.arrange(tempRect);

    assertTrue(testInstance.isArrangeValid());
    assertEquals(tempRect, testInstance.getArrangedRect());

    verify(layout, testInstance);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetLayoutException() throws Exception {
    layout.reportMeasureInvalid(testInstance);
    expectLastCall();
    layout.reportArrangeInvalid(testInstance);
    expectLastCall();

    replay(layout, testInstance);
    testInstance.getLayout();
    verify(layout, testInstance);
  }

  @Test
  public void testGetLayout() throws Exception {
    layout.reportMeasureInvalid(testInstance);
    expectLastCall();
    layout.reportArrangeInvalid(testInstance);
    expectLastCall();

    replay(layout, testInstance);
    testInstance.onAttach(layout);
    assertEquals(layout, testInstance.getLayout());
    verify(layout, testInstance);
  }
}