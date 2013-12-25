package de.lessvoid.nifty.controls.listbox;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class ListBoxViewWidthTest {
  private static final int WIDTH_100 = 100;
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>(null);
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private TestItem o3 = new TestItem("o3");
  private ListBoxView<TestItem> view;

  @Before
  public void before() {
    view = createMock(ListBoxView.class);
    assertEquals(0, listBox.bindToView(view, 2));
  }

  @After
  public void after() {
    verify(view);
  }

  @Test
  public void testUpdateViewWithEmptyList() {
    view.display(ListBoxTestTool.buildValues(null, null), -1, ListBoxTestTool.buildValuesSelection());
    replay(view);

    listBox.updateView(0);
  }

  @Test
  public void testSingleEntry() {
    expect(view.getWidth(o1)).andReturn(WIDTH_100);
    view.updateTotalWidth(WIDTH_100);
    view.updateTotalCount(1);
    view.display(ListBoxTestTool.buildValues(o1, null), 0, ListBoxTestTool.buildValuesSelection());
    replay(view);

    listBox.addItem(o1);
  }

  @Test
  public void testTwoEntries() {
    expect(view.getWidth(o1)).andReturn(WIDTH_100);
    view.updateTotalWidth(WIDTH_100);
    view.updateTotalCount(1);
    view.display(ListBoxTestTool.buildValues(o1, null), 0, ListBoxTestTool.buildValuesSelection());
    view.scrollTo(0);

    expect(view.getWidth(o2)).andReturn(WIDTH_100);
    view.updateTotalCount(2);
    view.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    view.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    replay(view);

    listBox.addItem(o1);
    listBox.addItem(o2);
  }

  @Test
  public void testThreeEntries() {
    expect(view.getWidth(o1)).andReturn(WIDTH_100);
    view.updateTotalWidth(WIDTH_100);
    view.updateTotalCount(1);
    view.display(ListBoxTestTool.buildValues(o1, null), 0, ListBoxTestTool.buildValuesSelection());
    view.scrollTo(0);
    view.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    expect(view.getWidth(o2)).andReturn(WIDTH_100);
    view.updateTotalCount(2);
    view.scrollTo(0);
    view.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    expect(view.getWidth(o3)).andReturn(WIDTH_100);
    view.updateTotalCount(3);
    view.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    view.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    replay(view);

    listBox.addItem(o1);
    listBox.addItem(o2);
    listBox.addItem(o3);
  }
}
