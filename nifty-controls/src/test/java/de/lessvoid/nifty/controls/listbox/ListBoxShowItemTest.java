package de.lessvoid.nifty.controls.listbox;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.classextension.EasyMock.*;

public class ListBoxShowItemTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>(null);
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private TestItem o3 = new TestItem("o3");
  private TestItem o4 = new TestItem("o4");
  private ListBoxView<TestItem> viewMock;

  @Before
  public void before() {
    listBox.addItem(o1);
    listBox.addItem(o2);
    listBox.addItem(o3);
    listBox.addItem(o4);
    viewMock = createMock(ListBoxView.class);
    listBox.bindToView(viewMock, 2);
  }

  @After
  public void after() {
    verify(viewMock);
  }

  @Test
  public void testShowItemByInvalidIndex() {
    replay(viewMock);

    listBox.showItemByIndex(-1);
  }

  @Test
  public void testShowItemByInvalidIndexTooLarge() {
    replay(viewMock);

    listBox.showItemByIndex(4);
  }

  @Test
  public void testShowItemByIndex() {
    viewMock.scrollTo(0);
    viewMock.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);

    listBox.showItemByIndex(0);
  }

  @Test
  public void testShowItemByLastIndex() {
    viewMock.scrollTo(2);
    viewMock.display(ListBoxTestTool.buildValues(o3, o4), -1, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);

    listBox.showItemByIndex(3);
  }

  @Test
  public void testShowItem() {
    viewMock.scrollTo(2);
    viewMock.display(ListBoxTestTool.buildValues(o3, o4), -1, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);

    listBox.showItem(o4);
  }

  @Test
  public void testShowItemUnknownItem() {
    replay(viewMock);

    listBox.showItem(new TestItem("test"));
  }
}
