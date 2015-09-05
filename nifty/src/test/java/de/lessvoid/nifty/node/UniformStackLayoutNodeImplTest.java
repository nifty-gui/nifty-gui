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
public class UniformStackLayoutNodeImplTest {
  private UniformStackLayoutNodeImpl testInstance;

  @Mock
  private NiftyLayout layout;

  @Before
  public void prepare() {
    testInstance = new UniformStackLayoutNodeImpl(Orientation.Vertical);
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
  public void testHorizontal() throws Exception {
    NiftySize availableSize = new NiftySize(100, 100);
    NiftyNodeImpl<?> firstChildMock = EasyMock.createMock(NiftyNodeImpl.class);
    NiftyNodeImpl<?> secondChildMock = EasyMock.createMock(NiftyNodeImpl.class);

    expect(layout.getDirectChildren(testInstance))
        .andReturn(Arrays.asList(firstChildMock, secondChildMock))
        .anyTimes();
    expect(layout.measure(firstChildMock, new NiftySize(availableSize.getWidth() / 2.f, availableSize.getHeight())))
        .andReturn(new NiftySize(10, 5))
        .anyTimes();
    expect(layout.measure(secondChildMock, new NiftySize(availableSize.getWidth() / 2.f, availableSize.getHeight())))
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

    layout.arrange(firstChildMock, new NiftyRect(new NiftyPoint(0, 0), new NiftySize(50, availableSize.getHeight())));
    expectLastCall().once();
    layout.arrange(secondChildMock, new NiftyRect(new NiftyPoint(50, 0), new NiftySize(50, availableSize.getHeight())));
    expectLastCall().once();

    replay(layout, firstChildMock, secondChildMock);

    testInstance.setOrientation(Orientation.Horizontal);
    testInstance.onAttach(layout);
    assertEquals(new NiftySize(40, 15), testInstance.measure(availableSize));

    testInstance.arrange(new NiftyRect(new NiftyPoint(0, 0), availableSize));

    verify(layout, firstChildMock, secondChildMock);
  }

  @Test
  public void testHorizontalInfiniteWidth() throws Exception {
    NiftySize availableSize = new NiftySize(Float.POSITIVE_INFINITY, 100);
    NiftyNodeImpl<?> firstChildMock = EasyMock.createMock(NiftyNodeImpl.class);
    NiftyNodeImpl<?> secondChildMock = EasyMock.createMock(NiftyNodeImpl.class);

    expect(layout.getDirectChildren(testInstance))
        .andReturn(Arrays.asList(firstChildMock, secondChildMock))
        .anyTimes();
    expect(layout.measure(firstChildMock, new NiftySize(availableSize.getWidth(), availableSize.getHeight())))
        .andReturn(new NiftySize(10, 5))
        .anyTimes();
    expect(layout.measure(secondChildMock, new NiftySize(availableSize.getWidth(), availableSize.getHeight())))
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

    layout.arrange(firstChildMock, new NiftyRect(new NiftyPoint(0, 0), new NiftySize(50, availableSize.getHeight())));
    expectLastCall().once();
    layout.arrange(secondChildMock, new NiftyRect(new NiftyPoint(50, 0), new NiftySize(50, availableSize.getHeight())));
    expectLastCall().once();

    replay(layout, firstChildMock, secondChildMock);

    testInstance.setOrientation(Orientation.Horizontal);
    testInstance.onAttach(layout);
    assertEquals(new NiftySize(40, 15), testInstance.measure(availableSize));

    testInstance.arrange(new NiftyRect(new NiftyPoint(0, 0), new NiftySize(100, 100)));

    verify(layout, firstChildMock, secondChildMock);
  }

  @Test
  public void testVertical() throws Exception {
    NiftySize availableSize = new NiftySize(100, 100);
    NiftyNodeImpl<?> firstChildMock = EasyMock.createMock(NiftyNodeImpl.class);
    NiftyNodeImpl<?> secondChildMock = EasyMock.createMock(NiftyNodeImpl.class);

    expect(layout.getDirectChildren(testInstance))
        .andReturn(Arrays.asList(firstChildMock, secondChildMock))
        .anyTimes();
    expect(layout.measure(firstChildMock, new NiftySize(availableSize.getWidth(), availableSize.getHeight() / 2.f)))
        .andReturn(new NiftySize(10, 5))
        .anyTimes();
    expect(layout.measure(secondChildMock, new NiftySize(availableSize.getWidth(), availableSize.getHeight() / 2.f)))
        .andReturn(new NiftySize(20, 15))
        .anyTimes();
    expect(layout.getDesiredSize(firstChildMock))
        .andReturn(new NiftySize(10, 5))
        .anyTimes();
    expect(layout.getDesiredSize(secondChildMock))
        .andReturn(new NiftySize(20, 15))
        .anyTimes();

    layout.arrange(firstChildMock, new NiftyRect(new NiftyPoint(0, 0), new NiftySize(availableSize.getWidth(), 50)));
    expectLastCall().once();
    layout.arrange(secondChildMock, new NiftyRect(new NiftyPoint(0, 50), new NiftySize(availableSize.getWidth(), 50)));
    expectLastCall().once();
    layout.reportChangedArrangement(testInstance);
    expectLastCall();

    replay(layout, firstChildMock, secondChildMock);

    testInstance.setOrientation(Orientation.Vertical);
    testInstance.onAttach(layout);
    assertEquals(new NiftySize(20, 30), testInstance.measure(availableSize));

    testInstance.arrange(new NiftyRect(new NiftyPoint(0, 0), availableSize));

    verify(layout, firstChildMock, secondChildMock);
  }

  @Test
  public void testVerticalInfiniteHeight() throws Exception {
    NiftySize availableSize = new NiftySize(100, Float.POSITIVE_INFINITY);
    NiftyNodeImpl<?> firstChildMock = EasyMock.createMock(NiftyNodeImpl.class);
    NiftyNodeImpl<?> secondChildMock = EasyMock.createMock(NiftyNodeImpl.class);

    expect(layout.getDirectChildren(testInstance))
        .andReturn(Arrays.asList(firstChildMock, secondChildMock))
        .anyTimes();
    expect(layout.measure(firstChildMock, new NiftySize(availableSize.getWidth(), availableSize.getHeight())))
        .andReturn(new NiftySize(10, 5))
        .anyTimes();
    expect(layout.measure(secondChildMock, new NiftySize(availableSize.getWidth(), availableSize.getHeight())))
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

    layout.arrange(firstChildMock, new NiftyRect(new NiftyPoint(0, 0), new NiftySize(availableSize.getWidth(), 50)));
    expectLastCall().once();
    layout.arrange(secondChildMock, new NiftyRect(new NiftyPoint(0, 50), new NiftySize(availableSize.getWidth(), 50)));
    expectLastCall().once();

    replay(layout, firstChildMock, secondChildMock);

    testInstance.setOrientation(Orientation.Vertical);
    testInstance.onAttach(layout);
    assertEquals(new NiftySize(20, 30), testInstance.measure(availableSize));

    testInstance.arrange(new NiftyRect(new NiftyPoint(0, 0), new NiftySize(100, 100)));

    verify(layout, firstChildMock, secondChildMock);
  }

  @Test
  public void testMeasureNoChildren() throws Exception {
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