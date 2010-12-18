package de.lessvoid.nifty.controls.listbox;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ListBoxShowItemLessThanViewCountTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>();
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private ListBoxView<TestItem> viewMock;

  @SuppressWarnings("unchecked")
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
