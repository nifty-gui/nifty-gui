package de.lessvoid.nifty.controls.listbox;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.controls.listbox.ListBoxImpl;
import de.lessvoid.nifty.controls.listbox.ListBoxView;

public class ListBoxFocusElementTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>();
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
    listBox.setFocusItemByIndex(4);
    assertEquals(0, listBox.getFocusItemIndex());
  }

  @Test
  public void testSetFocusItemByIndex() {
    viewMock.display(buildValues(o1, o2), 0, buildValuesSelection());
    viewMock.display(buildValues(o1, o2), 1, buildValuesSelection());
    viewMock.display(buildValues(o1, o2), -1, buildValuesSelection());
    viewMock.scrollTo(1);
    viewMock.display(buildValues(o2, o3), 1, buildValuesSelection());
    viewMock.display(buildValues(o2, o3), -1, buildValuesSelection());
    viewMock.scrollTo(2);
    viewMock.display(buildValues(o3, o4), 1, buildValuesSelection());
    replay(viewMock);
    listBox.setFocusItemByIndex(0);
    listBox.setFocusItemByIndex(1);
    listBox.setFocusItemByIndex(2);
    listBox.updateView(1);
    listBox.setFocusItemByIndex(3);
    listBox.updateView(2);
  }

  @Test
  public void testSetFocusItem() {
    viewMock.display(buildValues(o1, o2), 1, buildValuesSelection());
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
