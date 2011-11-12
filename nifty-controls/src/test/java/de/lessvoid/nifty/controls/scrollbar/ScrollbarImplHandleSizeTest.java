package de.lessvoid.nifty.controls.scrollbar;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the ScrollbarImpl.
 * @author void
 */
public class ScrollbarImplHandleSizeTest {
  private ScrollbarImpl scrollbar = new ScrollbarImpl();
  private ScrollbarView view;
  private float viewSize = 10.f;

  @Before
  public void before() {
    view = createMock(ScrollbarView.class);
    expect(view.getAreaSize()).andReturn((int) viewSize).anyTimes();
    expect(view.getMinHandleSize()).andReturn(5).anyTimes();
  }

  @After
  public void after() {
    verify(view);
  }

  @Test
  public void testNoEntries() {
    view.setHandle(0, 5);
    view.valueChanged(0.f);
    replay(view);

    scrollbar.bindToView(view, 0.f, 100.f, viewSize, 1.f, 4.f);
  }
}
