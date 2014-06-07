package de.lessvoid.nifty.controls.listbox;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;

public class ListBoxFocusElementTest {
  private static final int TOO_LARGE_INDEX = 4;
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
  public void testGetFocusItemDefault() {
    replay(viewMock);
    assertEquals(0, listBox.getFocusItemIndex());
    assertEquals(o1, listBox.getFocusItem());
  }

  @Test
  public void testSetFocusItemByIndexInvalid() {
    replay(viewMock);
    listBox.setFocusItemByIndex(-1);
    assertEquals(0, listBox.getFocusItemIndex());
  }

  @Test
  public void testSetFocusItemByIndexTooLarge() {
    replay(viewMock);
    listBox.setFocusItemByIndex(TOO_LARGE_INDEX);
    assertEquals(0, listBox.getFocusItemIndex());
  }

  @Test
  public void testSetFocusItemByIndex0() {
    viewMock.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);
    listBox.setFocusItemByIndex(0);
    assertEquals(0, listBox.getFocusItemIndex());
  }

  @Test
  public void testSetFocusItemByIndex1() {
    viewMock.display(ListBoxTestTool.buildValues(o1, o2), 1, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);
    listBox.setFocusItemByIndex(1);
    assertEquals(1, listBox.getFocusItemIndex());
  }

  @Test
  public void testSetFocusItemByIndex2() {
    viewMock.display(ListBoxTestTool.buildValues(o2, o3), 1, ListBoxTestTool.buildValuesSelection());
    viewMock.scrollTo(1);
    replay(viewMock);
    listBox.setFocusItemByIndex(2);
    assertEquals(2, listBox.getFocusItemIndex());
  }

  @Test
  public void testSetFocusItemByIndex3() {
    viewMock.display(ListBoxTestTool.buildValues(o3, o4), 1, ListBoxTestTool.buildValuesSelection());
    viewMock.scrollTo(2);
    replay(viewMock);
    listBox.setFocusItemByIndex(3);
    assertEquals(3, listBox.getFocusItemIndex());
  }

  @Test
  public void testSetFocusItem() {
    viewMock.display(ListBoxTestTool.buildValues(o1, o2), 1, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);
    listBox.setFocusItem(o2);
    assertEquals(o2, listBox.getFocusItem());
    assertEquals(1, listBox.getFocusItemIndex());
  }

  @Test
  public void testSetFocusItemUnknown() {
    replay(viewMock);
    listBox.setFocusItem(new TestItem("test"));
  }

  @Test
  public void testSetFocusScroll() {
    replay(viewMock);
    listBox.setFocusItem(new TestItem("test"));
    assertEquals(o1, listBox.getFocusItem());
    assertEquals(0, listBox.getFocusItemIndex());
  }

  @Test
  public void testFocusItemAfterDelete() {
    viewMock.updateTotalCount(3);
    viewMock.display(ListBoxTestTool.buildValues(o2, o3), 0, ListBoxTestTool.buildValuesSelection());
    viewMock.publish(isA(ListBoxSelectionChangedEvent.class));
    replay(viewMock);

    listBox.removeItem(o1);

    assertEquals(0, listBox.getFocusItemIndex());
    assertEquals(o2, listBox.getFocusItem());
  }

  @Test
  public void testRemoveFocusItemBeingTheLastItem() {
    viewMock.scrollTo(2);
    viewMock.display(ListBoxTestTool.buildValues(o3, o4), 1, ListBoxTestTool.buildValuesSelection());
    viewMock.updateTotalCount(3);
    viewMock.scrollTo(1);
    viewMock.display(ListBoxTestTool.buildValues(o2, o3), 1, ListBoxTestTool.buildValuesSelection());
    viewMock.publish(isA(ListBoxSelectionChangedEvent.class));
    viewMock.scrollTo(1);
    viewMock.display(ListBoxTestTool.buildValues(o2, o3), 1, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);

    listBox.setFocusItem(o4);
    listBox.removeItem(o4);

    assertEquals(o3, listBox.getFocusItem());
    assertEquals(2, listBox.getFocusItemIndex());
  }
}
