package de.lessvoid.nifty.controls;

import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.controls.listbox.ListBoxImpl;
import de.lessvoid.nifty.controls.listbox.ListBoxTestTool;
import de.lessvoid.nifty.controls.listbox.ListBoxView;
import de.lessvoid.nifty.controls.listbox.TestItem;

public class ListBoxSingleSelectionListViewTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>();
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private TestItem o3 = new TestItem("o3");
  private TestItem o4 = new TestItem("o4");
  private ListBoxView<TestItem> viewMock;

  @SuppressWarnings("unchecked")
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

  @SuppressWarnings("unchecked")
  @Test
  public void testSelectFirstItem() {
    viewMock.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection(0));
    viewMock.publish(isA(ListBoxSelectionChangedEvent.class));
    replay(viewMock);
    listBox.selectItemByIndex(0);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSelectDoubleItem() {
    viewMock.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection(0));
    viewMock.publish(isA(ListBoxSelectionChangedEvent.class));
    viewMock.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection(1));
    viewMock.publish(isA(ListBoxSelectionChangedEvent.class));
    replay(viewMock);
    listBox.selectItemByIndex(0);
    listBox.selectItemByIndex(1);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSelectOutOfDisplayItem() {
    viewMock.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    viewMock.publish(isA(ListBoxSelectionChangedEvent.class));
    replay(viewMock);
    listBox.selectItemByIndex(2);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testClearWithSelection() {
    viewMock.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection(0));
    viewMock.publish(isA(ListBoxSelectionChangedEvent.class));
    viewMock.updateTotalCount(0);
    viewMock.display(ListBoxTestTool.buildValues(null, null), -1, ListBoxTestTool.buildValuesSelection());
    viewMock.publish(isA(ListBoxSelectionChangedEvent.class));
    replay(viewMock);
    listBox.selectItemByIndex(0);
    listBox.clear();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testRemoveItemWithSelection() {
    viewMock.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection(0));
    viewMock.publish(isA(ListBoxSelectionChangedEvent.class));
    viewMock.updateTotalCount(3);
    viewMock.display(ListBoxTestTool.buildValues(o2, o3), 0, ListBoxTestTool.buildValuesSelection());
    viewMock.publish(isA(ListBoxSelectionChangedEvent.class));
    replay(viewMock);
    listBox.selectItemByIndex(0);
    listBox.removeItemByIndex(0);
  }
}
