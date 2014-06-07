package de.lessvoid.nifty.controls.listbox;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Arrays;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ListBoxViewDefaultTest {
  private static final int WIDTH_100 = 100;
  private ListBoxImpl<String> listBox = new ListBoxImpl<String>(null);
  private ListBoxView<String> view;

  @Before
  public void before() {
    view = createMock(ListBoxView.class);
    listBox.bindToView(view, 2);
  }

  @After
  public void after() {
    verify(view);
  }

  @Test
  public void testUpdateViewWithEmptyList() {
    expect(view.getWidth("a")).andReturn(WIDTH_100);
    view.updateTotalWidth(WIDTH_100);
    view.updateTotalCount(1);
    view.display(Arrays.asList("a"), 0, Collections.<Integer>emptyList());
    view.scrollTo(0);
    view.display(Arrays.asList("a", "b"), 0, Collections.<Integer>emptyList());
    expect(view.getWidth("b")).andReturn(WIDTH_100);
    view.updateTotalCount(2);
    view.display(Arrays.asList("a", "b"), 0, Collections.<Integer>emptyList());
    view.display(Arrays.asList("a", "b"), 0, Collections.<Integer>emptyList());
    replay(view);

    listBox.addItem("a");
    listBox.addItem("b");
    listBox.updateView(0);
  }
}
