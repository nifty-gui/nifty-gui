package de.lessvoid.nifty.controls.listbox;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    expect(viewMock.getWidth(o1)).andReturn(100);
    viewMock.updateTotalWidth(100);
    viewMock.updateTotalCount(1);
    viewMock.display(ListBoxTestTool.buildValues(o1, null), 0, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);
    listBox.addItem(o1);
    assertEquals(0, listBox.getFocusItemIndex());
    assertEquals(o1, listBox.getFocusItem());
  }

  @Test
  public void testGetFocusAfterTwoAddItems() {
    expect(viewMock.getWidth(o1)).andReturn(100);
    viewMock.updateTotalWidth(100);
    viewMock.updateTotalCount(1);
    viewMock.display(ListBoxTestTool.buildValues(o1, null), 0, ListBoxTestTool.buildValuesSelection());
    expect(viewMock.getWidth(o2)).andReturn(100);
    viewMock.updateTotalCount(2);
    viewMock.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);
    listBox.addItem(o1);
    listBox.addItem(o2);
    assertEquals(0, listBox.getFocusItemIndex());
    assertEquals(o1, listBox.getFocusItem());
  }

  @Test
  public void testGetFocusAfterInsertItem() {
    expect(viewMock.getWidth(o1)).andReturn(100);
    viewMock.updateTotalWidth(100);
    viewMock.updateTotalCount(1);
    viewMock.display(ListBoxTestTool.buildValues(o1, null), 0, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);
    listBox.insertItem(o1, 0);
    assertEquals(0, listBox.getFocusItemIndex());
    assertEquals(o1, listBox.getFocusItem());
  }

  @Test
  public void testGetFocusAfterSecondInsertItem() {
    expect(viewMock.getWidth(o1)).andReturn(100);
    viewMock.updateTotalWidth(100);
    viewMock.updateTotalCount(1);
    viewMock.display(ListBoxTestTool.buildValues(o1, null), 0, ListBoxTestTool.buildValuesSelection());
    expect(viewMock.getWidth(o2)).andReturn(100);
    viewMock.updateTotalCount(2);
    viewMock.display(ListBoxTestTool.buildValues(o2, o1), 0, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);
    listBox.insertItem(o1, 0);
    listBox.insertItem(o2, 0);
    assertEquals(0, listBox.getFocusItemIndex());
    assertEquals(o2, listBox.getFocusItem());
  }

  @Test
  public void testGetFocusAfterClear() {
    expect(viewMock.getWidth(o1)).andReturn(100);
    viewMock.updateTotalWidth(100);
    viewMock.updateTotalCount(1);
    viewMock.display(ListBoxTestTool.buildValues(o1, null), 0, ListBoxTestTool.buildValuesSelection());
    expect(viewMock.getWidth(o2)).andReturn(100);
    viewMock.updateTotalCount(2);
    viewMock.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    viewMock.updateTotalCount(0);
    viewMock.updateTotalWidth(0);
    viewMock.display(ListBoxTestTool.buildValues(null, null), -1, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);
    listBox.addItem(o1);
    listBox.addItem(o2);
    listBox.clear();
    assertEquals(-1, listBox.getFocusItemIndex());
    assertNull(listBox.getFocusItem());
  }

  @Test
  public void testGetFocusAfterRemoveItem() {
    expect(viewMock.getWidth(o1)).andReturn(100);
    viewMock.updateTotalWidth(100);
    viewMock.updateTotalCount(1);
    viewMock.display(ListBoxTestTool.buildValues(o1, null), 0, ListBoxTestTool.buildValuesSelection());
    viewMock.updateTotalWidth(0);
    viewMock.updateTotalCount(0);
    viewMock.display(ListBoxTestTool.buildValues(null, null), -1, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);
    listBox.addItem(o1);
    listBox.removeItem(o1);
    assertEquals(-1, listBox.getFocusItemIndex());
    assertNull(listBox.getFocusItem());
  }

  @Test
  public void testGetFocusAfterRemoveItemByIndex() {
    expect(viewMock.getWidth(o1)).andReturn(100);
    viewMock.updateTotalWidth(100);
    viewMock.updateTotalCount(1);
    viewMock.display(ListBoxTestTool.buildValues(o1, null), 0, ListBoxTestTool.buildValuesSelection());
    viewMock.updateTotalWidth(0);
    viewMock.updateTotalCount(0);
    viewMock.display(ListBoxTestTool.buildValues(null, null), -1, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);
    listBox.addItem(o1);
    listBox.removeItemByIndex(0);
    assertEquals(-1, listBox.getFocusItemIndex());
    assertNull(listBox.getFocusItem());
  }
}
