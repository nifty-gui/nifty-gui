package de.lessvoid.nifty.node;

import de.lessvoid.nifty.NiftyLayout;
import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.types.Point;
import de.lessvoid.nifty.types.Rect;
import de.lessvoid.nifty.types.Size;
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
    Size availableSize = new Size(100, 100);
    NiftyNode firstChildMock = EasyMock.createMock(NiftyNode.class);
    NiftyNode secondChildMock = EasyMock.createMock(NiftyNode.class);

    expect(layout.getDirectChildren(testInstance))
        .andReturn(Arrays.asList(firstChildMock, secondChildMock))
        .anyTimes();
    expect(layout.measure(firstChildMock, new Size(availableSize.getWidth() / 2.f, availableSize.getHeight())))
        .andReturn(new Size(10, 5))
        .anyTimes();
    expect(layout.measure(secondChildMock, new Size(availableSize.getWidth() / 2.f, availableSize.getHeight())))
        .andReturn(new Size(20, 15))
        .anyTimes();
    expect(layout.getDesiredSize(firstChildMock))
        .andReturn(new Size(10, 5))
        .anyTimes();
    expect(layout.getDesiredSize(secondChildMock))
        .andReturn(new Size(20, 15))
        .anyTimes();

    layout.arrange(firstChildMock, new Rect(new Point(0, 0), new Size(50, availableSize.getHeight())));
    EasyMock.expectLastCall().once();
    layout.arrange(secondChildMock, new Rect(new Point(50, 0), new Size(50, availableSize.getHeight())));
    EasyMock.expectLastCall().once();

    replay(layout, firstChildMock, secondChildMock);

    testInstance.setOrientation(Orientation.Horizontal);
    testInstance.onAttach(layout);
    assertEquals(new Size(40, 15), testInstance.measure(availableSize));

    testInstance.arrange(new Rect(new Point(0, 0), availableSize));

    verify(layout, firstChildMock, secondChildMock);
  }

  @Test
  public void testHorizontalInfiniteWidth() throws Exception {
    Size availableSize = new Size(Float.POSITIVE_INFINITY, 100);
    NiftyNode firstChildMock = EasyMock.createMock(NiftyNode.class);
    NiftyNode secondChildMock = EasyMock.createMock(NiftyNode.class);

    expect(layout.getDirectChildren(testInstance))
        .andReturn(Arrays.asList(firstChildMock, secondChildMock))
        .anyTimes();
    expect(layout.measure(firstChildMock, new Size(availableSize.getWidth(), availableSize.getHeight())))
        .andReturn(new Size(10, 5))
        .anyTimes();
    expect(layout.measure(secondChildMock, new Size(availableSize.getWidth(), availableSize.getHeight())))
        .andReturn(new Size(20, 15))
        .anyTimes();
    expect(layout.getDesiredSize(firstChildMock))
        .andReturn(new Size(10, 5))
        .anyTimes();
    expect(layout.getDesiredSize(secondChildMock))
        .andReturn(new Size(20, 15))
        .anyTimes();

    layout.arrange(firstChildMock, new Rect(new Point(0, 0), new Size(50, availableSize.getHeight())));
    EasyMock.expectLastCall().once();
    layout.arrange(secondChildMock, new Rect(new Point(50, 0), new Size(50, availableSize.getHeight())));
    EasyMock.expectLastCall().once();

    replay(layout, firstChildMock, secondChildMock);

    testInstance.setOrientation(Orientation.Horizontal);
    testInstance.onAttach(layout);
    assertEquals(new Size(40, 15), testInstance.measure(availableSize));

    testInstance.arrange(new Rect(new Point(0, 0), new Size(100, 100)));

    verify(layout, firstChildMock, secondChildMock);
  }

  @Test
  public void testVertical() throws Exception {
    Size availableSize = new Size(100, 100);
    NiftyNode firstChildMock = EasyMock.createMock(NiftyNode.class);
    NiftyNode secondChildMock = EasyMock.createMock(NiftyNode.class);

    expect(layout.getDirectChildren(testInstance))
        .andReturn(Arrays.asList(firstChildMock, secondChildMock))
        .anyTimes();
    expect(layout.measure(firstChildMock, new Size(availableSize.getWidth(), availableSize.getHeight() / 2.f)))
        .andReturn(new Size(10, 5))
        .anyTimes();
    expect(layout.measure(secondChildMock, new Size(availableSize.getWidth(), availableSize.getHeight() / 2.f)))
        .andReturn(new Size(20, 15))
        .anyTimes();
    expect(layout.getDesiredSize(firstChildMock))
        .andReturn(new Size(10, 5))
        .anyTimes();
    expect(layout.getDesiredSize(secondChildMock))
        .andReturn(new Size(20, 15))
        .anyTimes();

    layout.arrange(firstChildMock, new Rect(new Point(0, 0), new Size(availableSize.getWidth(), 50)));
    EasyMock.expectLastCall().once();
    layout.arrange(secondChildMock, new Rect(new Point(0, 50), new Size(availableSize.getWidth(), 50)));
    EasyMock.expectLastCall().once();

    replay(layout, firstChildMock, secondChildMock);

    testInstance.setOrientation(Orientation.Vertical);
    testInstance.onAttach(layout);
    assertEquals(new Size(20, 30), testInstance.measure(availableSize));

    testInstance.arrange(new Rect(new Point(0, 0), availableSize));

    verify(layout, firstChildMock, secondChildMock);
  }

  @Test
  public void testVerticalInfiniteHeight() throws Exception {
    Size availableSize = new Size(100, Float.POSITIVE_INFINITY);
    NiftyNode firstChildMock = EasyMock.createMock(NiftyNode.class);
    NiftyNode secondChildMock = EasyMock.createMock(NiftyNode.class);

    expect(layout.getDirectChildren(testInstance))
        .andReturn(Arrays.asList(firstChildMock, secondChildMock))
        .anyTimes();
    expect(layout.measure(firstChildMock, new Size(availableSize.getWidth(), availableSize.getHeight())))
        .andReturn(new Size(10, 5))
        .anyTimes();
    expect(layout.measure(secondChildMock, new Size(availableSize.getWidth(), availableSize.getHeight())))
        .andReturn(new Size(20, 15))
        .anyTimes();
    expect(layout.getDesiredSize(firstChildMock))
        .andReturn(new Size(10, 5))
        .anyTimes();
    expect(layout.getDesiredSize(secondChildMock))
        .andReturn(new Size(20, 15))
        .anyTimes();

    layout.arrange(firstChildMock, new Rect(new Point(0, 0), new Size(availableSize.getWidth(), 50)));
    EasyMock.expectLastCall().once();
    layout.arrange(secondChildMock, new Rect(new Point(0, 50), new Size(availableSize.getWidth(), 50)));
    EasyMock.expectLastCall().once();

    replay(layout, firstChildMock, secondChildMock);

    testInstance.setOrientation(Orientation.Vertical);
    testInstance.onAttach(layout);
    assertEquals(new Size(20, 30), testInstance.measure(availableSize));

    testInstance.arrange(new Rect(new Point(0, 0), new Size(100, 100)));

    verify(layout, firstChildMock, secondChildMock);
  }

  @Test
  public void testMeasureNoChildren() throws Exception {
    expect(layout.getDirectChildren(testInstance))
        .andReturn(Collections.<NiftyNode>emptyList())
        .anyTimes();
    replay(layout);
    testInstance.onAttach(layout);
    assertEquals(Size.ZERO, testInstance.measure(Size.INFINITE));
    verify(layout);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMeasureInvalidSize() throws Exception {
    replay(layout);
    testInstance.measure(Size.INVALID);
    verify(layout);
  }

  @Test
  public void testArrangeNoChildren() throws Exception {
    expect(layout.getDirectChildren(testInstance))
        .andReturn(Collections.<NiftyNode>emptyList())
        .anyTimes();
    replay(layout);
    testInstance.onAttach(layout);
    testInstance.arrange(new Rect(new Point(0, 0), new Size(10, 20)));
    verify(layout);
  }
}