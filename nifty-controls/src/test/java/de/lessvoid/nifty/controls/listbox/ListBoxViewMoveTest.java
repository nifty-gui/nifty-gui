package de.lessvoid.nifty.controls.listbox;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ListBoxViewMoveTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>(null);
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private TestItem o3 = new TestItem("o3");
  private ListBoxView<TestItem> view;

  @Before
  public void before() {
    view = createMock(ListBoxView.class);
    listBox.addItem(o1);
    listBox.addItem(o2);
    listBox.addItem(o3);
    listBox.bindToView(view, 2);
  }

  @After
  public void after() {
    verify(view);
  }

  @Test
  public void testUpdateView0() {
    view.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    replay(view);
    listBox.updateView(0);
  }

  @Test
  public void testUpdateView1() {
    view.display(ListBoxTestTool.buildValues(o2, o3), -1, ListBoxTestTool.buildValuesSelection());
    replay(view);
    listBox.updateView(1);
  }

  @Test
  public void testUpdateView2() {
    view.display(ListBoxTestTool.buildValues(o3, null), -1, ListBoxTestTool.buildValuesSelection());
    replay(view);
    listBox.updateView(2);
  }

  @Test
  public void testUpdateViewTooBig() {
    replay(view);
    listBox.updateView(3);
  }
}
