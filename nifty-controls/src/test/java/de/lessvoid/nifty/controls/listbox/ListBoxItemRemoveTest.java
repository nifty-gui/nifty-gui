package de.lessvoid.nifty.controls.listbox;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;

public class ListBoxItemRemoveTest {
  private static final int WIDTH_100 = 100;
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>(null);
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private ListBoxView<TestItem> viewMock;

  @SuppressWarnings("unchecked")
  @Before
  public void before() {
    listBox.addItem(o1);
    listBox.addItem(o2);
    viewMock = createMock(ListBoxView.class);
    listBox.bindToView(viewMock, 2);
  }

  @After
  public void after() {
    verify(viewMock);
  }

  @SuppressWarnings ("unchecked")
  @Test
  public void testClear() {
    viewMock.updateTotalCount(0);
    viewMock.updateTotalWidth(0);
    viewMock.display(ListBoxTestTool.buildValues(null, null), -1, ListBoxTestTool.buildValuesSelection());
    viewMock.publish(isA(ListBoxSelectionChangedEvent.class));
    replay(viewMock);

    listBox.clear();
    assertListBoxCount(0);
  }

  @SuppressWarnings ("unchecked")
  @Test
  public void testRemoveItemByIndex() {
    viewMock.updateTotalCount(1);
    viewMock.display(ListBoxTestTool.buildValues(o2, null), 0, ListBoxTestTool.buildValuesSelection());
    viewMock.publish(isA(ListBoxSelectionChangedEvent.class));
    viewMock.scrollTo(0);
    replay(viewMock);

    listBox.removeItemByIndex(0);
    assertListBoxCount(1);
  }

  @Test
  public void testRemoveItemByNegativeIndex() {
    replay(viewMock);

    listBox.removeItemByIndex(-1);
    assertListBoxCount(2);
  }

  @Test
  public void testRemoveItemByIndexTooLarge() {
    replay(viewMock);

    listBox.removeItemByIndex(4);
    assertListBoxCount(2);
  }

  @SuppressWarnings ("unchecked")
  @Test
  public void testRemoveAllItemManual() {
    viewMock.updateTotalCount(1);
    viewMock.display(ListBoxTestTool.buildValues(o2, null), 0, ListBoxTestTool.buildValuesSelection());
    viewMock.scrollTo(0);
    viewMock.updateTotalCount(0);
    viewMock.display(ListBoxTestTool.buildValues(null, null), -1, ListBoxTestTool.buildValuesSelection());
    viewMock.publish(isA(ListBoxSelectionChangedEvent.class));
    viewMock.publish(isA(ListBoxSelectionChangedEvent.class));
    replay(viewMock);

    listBox.removeItem(o1);
    listBox.removeItem(o2);
    assertListBoxCount(0);
  }

  @SuppressWarnings ("unchecked")
  @Test
  public void testRemoveAllItems() {
    viewMock.updateTotalCount(0);
    viewMock.display(ListBoxTestTool.buildValues(null, null), -1, ListBoxTestTool.buildValuesSelection());
    viewMock.publish(isA(ListBoxSelectionChangedEvent.class));
    replay(viewMock);

    List<TestItem> itemsToRemove = new ArrayList<TestItem>();
    itemsToRemove.add(o1);
    itemsToRemove.add(o2);
    listBox.removeAllItems(itemsToRemove);

    assertListBoxCount(0);
  }

  @SuppressWarnings ("unchecked")
  @Test
  public void testRemoveAllItemsAtBackOfList() {
    TestItem o3 = new TestItem("o3");
    TestItem o4 = new TestItem("o4");

    expect(viewMock.getWidth(o3)).andReturn(WIDTH_100);
    viewMock.updateTotalWidth(WIDTH_100);
    viewMock.updateTotalCount(3);
    viewMock.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    expect(viewMock.getWidth(o4)).andReturn(WIDTH_100);
    viewMock.updateTotalCount(4);
    viewMock.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    viewMock.scrollTo(2);
    viewMock.display(ListBoxTestTool.buildValues(o3, o4), -1, ListBoxTestTool.buildValuesSelection());
    viewMock.updateTotalCount(2);
    viewMock.scrollTo(0);
    viewMock.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    viewMock.publish(isA(ListBoxSelectionChangedEvent.class));
    viewMock.updateTotalWidth(0);
    replay(viewMock);

    listBox.addItem(o3);
    listBox.addItem(o4);
    listBox.showItem(o3);

    List<TestItem> itemsToRemove = new ArrayList<TestItem>();
    itemsToRemove.add(o3);
    itemsToRemove.add(o4);
    listBox.removeAllItems(itemsToRemove);
  }

  @Test
  public void testRemoveItemWithAnItemNotPartOfTheList() {
    replay(viewMock);

    listBox.removeItem(new TestItem("another"));
    assertListBoxCount(2);
  }

  private void assertListBoxCount(final int expected) {
    assertEquals(expected, listBox.itemCount());
  }
}
