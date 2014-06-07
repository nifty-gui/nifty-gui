package de.lessvoid.nifty.controls.listbox;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ListBoxShowItemLessThanViewCountTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>(null);
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private ListBoxView<TestItem> viewMock;

  @Before
  public void before() {
    listBox.addItem(o1);
    listBox.addItem(o2);
    viewMock = createMock(ListBoxView.class);
    listBox.bindToView(viewMock, 4);
  }

  @After
  public void after() {
    verify(viewMock);
  }

  @Test
  public void testShowItemByIndexSmallerThanVisibleCount() {
    viewMock.scrollTo(0);
    viewMock.display(ListBoxTestTool.buildValues(o1, o2, null, null), 0, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);

    listBox.showItemByIndex(1);
  }

  @Test
  public void testShowItemByIndexEqualsVisibleCount() {
    listBox.bindToView(viewMock, 2);

    viewMock.scrollTo(0);
    viewMock.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);

    listBox.showItemByIndex(1);
  }
}
