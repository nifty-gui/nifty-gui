package de.lessvoid.nifty.node;

import de.lessvoid.nifty.NiftyLayout;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;
import de.lessvoid.nifty.types.NiftyPoint;
import de.lessvoid.nifty.types.NiftyRect;
import de.lessvoid.nifty.types.NiftySize;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@SuppressWarnings("ConstantConditions")
@RunWith(EasyMockRunner.class)
public class StackLayoutNodeImplTest {
  private StackLayoutNodeImpl testInstance;

  @Mock
  private NiftyLayout layout;

  @Before
  public void prepare() {
    testInstance = new StackLayoutNodeImpl(Orientation.Vertical, false);
  }

  @Test
  public void testOrientation() throws Exception {
    replay(layout);
    assertEquals(Orientation.Vertical, testInstance.getOrientation());
    for (Orientation orientation : Orientation.values()) {
      testInstance.setOrientation(orientation);
      assertEquals(orientation, testInstance.getOrientation());
    }
    verify(layout);
  }

  @Test
  public void testStretchLast() throws Exception {
    replay(layout);
    assertFalse(testInstance.isStretchLast());
    testInstance.setStretchLast(true);
    assertTrue(testInstance.isStretchLast());
    testInstance.setStretchLast(false);
    assertFalse(testInstance.isStretchLast());
    verify(layout);
  }

  @Test
  public void testHorizontalNoStretch() throws Exception {
    NiftySize availableSize = new NiftySize(100, 100);
    NiftyNodeImpl<?> firstChildMock = EasyMock.createMock(NiftyNodeImpl.class);
    NiftyNodeImpl<?> secondChildMock = EasyMock.createMock(NiftyNodeImpl.class);

    layout.reportMeasureInvalid(testInstance);
    expectLastCall();
    layout.reportArrangeInvalid(testInstance);
    expectLastCall();

    expect(layout.getDirectChildren(testInstance))
        .andReturn(Arrays.asList(firstChildMock, secondChildMock))
        .anyTimes();
    expect(layout.measure(firstChildMock, availableSize))
        .andReturn(new NiftySize(10, 5))
        .anyTimes();
    expect(layout.measure(secondChildMock, new NiftySize(availableSize.getWidth() - 10, availableSize.getHeight())))
        .andReturn(new NiftySize(20, 15))
        .anyTimes();
    expect(layout.getDesiredSize(firstChildMock))
        .andReturn(new NiftySize(10, 5))
        .anyTimes();
    expect(layout.getDesiredSize(secondChildMock))
        .andReturn(new NiftySize(20, 15))
        .anyTimes();

    layout.arrange(firstChildMock, new NiftyRect(new NiftyPoint(0, 0), new NiftySize(10, availableSize.getHeight())));
    EasyMock.expectLastCall().once();
    layout.arrange(secondChildMock, new NiftyRect(new NiftyPoint(10, 0), new NiftySize(20, availableSize.getHeight())));
    EasyMock.expectLastCall().once();
    layout.reportChangedArrangement(testInstance);
    expectLastCall();

    replay(layout, firstChildMock, secondChildMock);

    testInstance.setOrientation(Orientation.Horizontal);
    testInstance.setStretchLast(false);
    testInstance.onAttach(layout);
    assertEquals(new NiftySize(30, 15), testInstance.measure(availableSize));

    testInstance.arrange(new NiftyRect(new NiftyPoint(0, 0), availableSize));

    verify(layout, firstChildMock, secondChildMock);
  }

  @Test
  public void testHorizontalStretch() throws Exception {
    NiftySize availableSize = new NiftySize(100, 100);
    NiftyNodeImpl<?> firstChildMock = EasyMock.createMock(NiftyNodeImpl.class);
    NiftyNodeImpl<?> secondChildMock = EasyMock.createMock(NiftyNodeImpl.class);

    layout.reportMeasureInvalid(testInstance);
    expectLastCall();
    layout.reportArrangeInvalid(testInstance);
    expectLastCall();

    expect(layout.getDirectChildren(testInstance))
        .andReturn(Arrays.asList(firstChildMock, secondChildMock))
        .anyTimes();
    expect(layout.measure(firstChildMock, availableSize))
        .andReturn(new NiftySize(10, 5))
        .anyTimes();
    expect(layout.measure(secondChildMock, new NiftySize(availableSize.getWidth() - 10, availableSize.getHeight())))
        .andReturn(new NiftySize(20, 15))
        .anyTimes();
    expect(layout.getDesiredSize(firstChildMock))
        .andReturn(new NiftySize(10, 5))
        .anyTimes();
    expect(layout.getDesiredSize(secondChildMock))
        .andReturn(new NiftySize(20, 15))
        .anyTimes();

    layout.arrange(firstChildMock, new NiftyRect(new NiftyPoint(0, 0), new NiftySize(10, availableSize.getHeight())));
    EasyMock.expectLastCall().once();
    layout.arrange(secondChildMock, new NiftyRect(new NiftyPoint(10, 0),
        new NiftySize(availableSize.getWidth() - 10, availableSize.getHeight())));
    EasyMock.expectLastCall().once();
    layout.reportChangedArrangement(testInstance);
    expectLastCall();

    replay(layout, firstChildMock, secondChildMock);

    testInstance.setOrientation(Orientation.Horizontal);
    testInstance.setStretchLast(true);
    testInstance.onAttach(layout);
    assertEquals(new NiftySize(30, 15), testInstance.measure(availableSize));

    testInstance.arrange(new NiftyRect(new NiftyPoint(0, 0), availableSize));

    verify(layout, firstChildMock, secondChildMock);
  }

  @Test
  public void testVerticalNoStretch() throws Exception {
    NiftySize availableSize = new NiftySize(100, 100);
    NiftyNodeImpl<?> firstChildMock = EasyMock.createMock(NiftyNodeImpl.class);
    NiftyNodeImpl<?> secondChildMock = EasyMock.createMock(NiftyNodeImpl.class);

    layout.reportMeasureInvalid(testInstance);
    expectLastCall();
    layout.reportArrangeInvalid(testInstance);
    expectLastCall();

    expect(layout.getDirectChildren(testInstance))
        .andReturn(Arrays.asList(firstChildMock, secondChildMock))
        .anyTimes();
    expect(layout.measure(firstChildMock, availableSize))
        .andReturn(new NiftySize(10, 5))
        .anyTimes();
    expect(layout.measure(secondChildMock, new NiftySize(availableSize.getWidth(), availableSize.getHeight() - 5)))
        .andReturn(new NiftySize(20, 15))
        .anyTimes();
    expect(layout.getDesiredSize(firstChildMock))
        .andReturn(new NiftySize(10, 5))
        .anyTimes();
    expect(layout.getDesiredSize(secondChildMock))
        .andReturn(new NiftySize(20, 15))
        .anyTimes();

    layout.arrange(firstChildMock, new NiftyRect(new NiftyPoint(0, 0), new NiftySize(availableSize.getWidth(), 5)));
    EasyMock.expectLastCall().once();
    layout.arrange(secondChildMock, new NiftyRect(new NiftyPoint(0, 5), new NiftySize(availableSize.getWidth(), 15)));
    EasyMock.expectLastCall().once();
    layout.reportChangedArrangement(testInstance);
    expectLastCall();

    replay(layout, firstChildMock, secondChildMock);

    testInstance.setOrientation(Orientation.Vertical);
    testInstance.setStretchLast(false);
    testInstance.onAttach(layout);
    assertEquals(new NiftySize(20, 20), testInstance.measure(availableSize));

    testInstance.arrange(new NiftyRect(new NiftyPoint(0, 0), availableSize));

    verify(layout, firstChildMock, secondChildMock);
  }

  @Test
  public void testVerticalStretch() throws Exception {
    NiftySize availableSize = new NiftySize(100, 100);
    NiftyNodeImpl<?> firstChildMock = EasyMock.createMock(NiftyNodeImpl.class);
    NiftyNodeImpl<?> secondChildMock = EasyMock.createMock(NiftyNodeImpl.class);

    layout.reportMeasureInvalid(testInstance);
    expectLastCall();
    layout.reportArrangeInvalid(testInstance);
    expectLastCall();

    expect(layout.getDirectChildren(testInstance))
        .andReturn(Arrays.asList(firstChildMock, secondChildMock))
        .anyTimes();
    expect(layout.measure(firstChildMock, availableSize))
        .andReturn(new NiftySize(10, 5))
        .anyTimes();
    expect(layout.measure(secondChildMock, new NiftySize(availableSize.getWidth(), availableSize.getHeight() - 5)))
        .andReturn(new NiftySize(20, 15))
        .anyTimes();
    expect(layout.getDesiredSize(firstChildMock))
        .andReturn(new NiftySize(10, 5))
        .anyTimes();
    expect(layout.getDesiredSize(secondChildMock))
        .andReturn(new NiftySize(20, 15))
        .anyTimes();
    layout.reportChangedArrangement(testInstance);
    expectLastCall();

    layout.arrange(firstChildMock, new NiftyRect(new NiftyPoint(0, 0), new NiftySize(availableSize.getWidth(), 5)));
    EasyMock.expectLastCall().once();
    layout.arrange(secondChildMock, new NiftyRect(new NiftyPoint(0, 5),
        new NiftySize(availableSize.getWidth(), availableSize.getHeight() - 5)));
    EasyMock.expectLastCall().once();

    replay(layout, firstChildMock, secondChildMock);

    testInstance.setOrientation(Orientation.Vertical);
    testInstance.setStretchLast(true);
    testInstance.onAttach(layout);
    assertEquals(new NiftySize(20, 20), testInstance.measure(availableSize));

    testInstance.arrange(new NiftyRect(new NiftyPoint(0, 0), availableSize));

    verify(layout, firstChildMock, secondChildMock);
  }

  @Test
  public void testMeasureNoChildren() throws Exception {
    layout.reportMeasureInvalid(testInstance);
    expectLastCall();
    layout.reportArrangeInvalid(testInstance);
    expectLastCall();

    expect(layout.getDirectChildren(testInstance))
        .andReturn(Collections.<NiftyNodeImpl<?>>emptyList())
        .anyTimes();
    replay(layout);
    testInstance.onAttach(layout);
    assertEquals(NiftySize.ZERO, testInstance.measure(NiftySize.INFINITE));
    verify(layout);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMeasureInvalidSize() throws Exception {
    replay(layout);
    testInstance.measure(NiftySize.INVALID);
    verify(layout);
  }

  @Test
  public void testArrangeNoChildren() throws Exception {
    layout.reportMeasureInvalid(testInstance);
    expectLastCall();
    layout.reportArrangeInvalid(testInstance);
    expectLastCall();

    expect(layout.getDirectChildren(testInstance))
        .andReturn(Collections.<NiftyNodeImpl<?>>emptyList())
        .anyTimes();
    layout.reportChangedArrangement(testInstance);
    expectLastCall();
    replay(layout);
    testInstance.onAttach(layout);
    testInstance.arrange(new NiftyRect(new NiftyPoint(0, 0), new NiftySize(10, 20)));
    verify(layout);
  }
}