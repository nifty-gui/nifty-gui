package de.lessvoid.nifty.controls.listbox;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ListBoxFocusElementEmptyTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>();
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private ListBoxView<TestItem> viewMock;

  @SuppressWarnings("unchecked")
  @Before
  public void before() {
    viewMock = createMock(ListBoxView.class);
    listBox.bindToView(viewMock, 2);
  }

  @After
  public void after() {
    verify(viewMock);
  }

  @Test
  public void testGetFocusItemDefault() {
    replay(viewMock);
    assertEquals(-1, listBox.getFocusItemIndex());
    assertNull(listBox.getFocusItem());
  }

  @Test
  public void testGetFocusAfterAddItem() {
    viewMock.updateTotalCount(1);
    viewMock.display(buildValues(o1, null), 0, buildValuesSelection());
    replay(viewMock);
    listBox.addItem(o1);
    assertEquals(0, listBox.getFocusItemIndex());
    assertEquals(o1, listBox.getFocusItem());
  }

  @Test
  public void testGetFocusAfterTwoAddItems() {
    viewMock.updateTotalCount(1);
    viewMock.display(buildValues(o1, null), 0, buildValuesSelection());
    viewMock.updateTotalCount(2);
    viewMock.display(buildValues(o1, o2), 0, buildValuesSelection());
    replay(viewMock);
    listBox.addItem(o1);
    listBox.addItem(o2);
    assertEquals(0, listBox.getFocusItemIndex());
    assertEquals(o1, listBox.getFocusItem());
  }

  @Test
  public void testGetFocusAfterInsertItem() {
    viewMock.updateTotalCount(1);
    viewMock.display(buildValues(o1, null), 0, buildValuesSelection());
    replay(viewMock);
    listBox.insertItem(o1, 0);
    assertEquals(0, listBox.getFocusItemIndex());
    assertEquals(o1, listBox.getFocusItem());
  }

  @Test
  public void testGetFocusAfterSecondInsertItem() {
    viewMock.updateTotalCount(1);
    viewMock.display(buildValues(o1, null), 0, buildValuesSelection());
    viewMock.updateTotalCount(2);
    viewMock.display(buildValues(o2, o1), 0, buildValuesSelection());
    replay(viewMock);
    listBox.insertItem(o1, 0);
    listBox.insertItem(o2, 0);
    assertEquals(0, listBox.getFocusItemIndex());
    assertEquals(o2, listBox.getFocusItem());
  }

  @Test
  public void testGetFocusAfterClear() {
    viewMock.updateTotalCount(1);
    viewMock.display(buildValues(o1, null), 0, buildValuesSelection());
    viewMock.updateTotalCount(2);
    viewMock.display(buildValues(o1, o2), 0, buildValuesSelection());
    viewMock.updateTotalCount(0);
    viewMock.display(buildValues(null, null), -1, buildValuesSelection());
    replay(viewMock);
    listBox.addItem(o1);
    listBox.addItem(o2);
    listBox.clear();
    assertEquals(-1, listBox.getFocusItemIndex());
    assertNull(listBox.getFocusItem());
  }

  @Test
  public void testGetFocusAfterRemoveItem() {
    viewMock.updateTotalCount(1);
    viewMock.display(buildValues(o1, null), 0, buildValuesSelection());
    viewMock.updateTotalCount(0);
    viewMock.display(buildValues(null, null), -1, buildValuesSelection());
    replay(viewMock);
    listBox.addItem(o1);
    listBox.removeItem(o1);
    assertEquals(-1, listBox.getFocusItemIndex());
    assertNull(listBox.getFocusItem());
  }

  @Test
  public void testGetFocusAfterRemoveItemByIndex() {
    viewMock.updateTotalCount(1);
    viewMock.display(buildValues(o1, null), 0, buildValuesSelection());
    viewMock.updateTotalCount(0);
    viewMock.display(buildValues(null, null), -1, buildValuesSelection());
    replay(viewMock);
    listBox.addItem(o1);
    listBox.removeItemByIndex(0);
    assertEquals(-1, listBox.getFocusItemIndex());
    assertNull(listBox.getFocusItem());
  }

  private List<TestItem> buildValues(final TestItem ... values) {
    List<TestItem> result = new ArrayList<TestItem>();
    for (int i=0; i<values.length; i++) {
      result.add(values[i]);
    }
    return result;
  }

  private List<Integer> buildValuesSelection(final Integer ... values) {
    List<Integer> result = new ArrayList<Integer>();
    for (int i=0; i<values.length; i++) {
      result.add(values[i]);
    }
    return result;
  }
}
