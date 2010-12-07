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

public class ListBoxItemRemoveTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>();
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private ListBoxView<TestItem> viewMock;

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

  @Test
  public void testClear() {
    viewMock.updateTotalCount(0);
    viewMock.display(buildValues(null, null), -1, buildValuesSelection());
    replay(viewMock);

    listBox.clear();
    assertListBoxCount(0);
  }

  @Test
  public void testRemoveItemByIndex() {
    viewMock.updateTotalCount(1);
    viewMock.display(buildValues(o2, null), 0, buildValuesSelection());
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

  @Test
  public void testRemoveAllItem() {
    viewMock.updateTotalCount(1);
    viewMock.display(buildValues(o2, null), 0, buildValuesSelection());
    viewMock.updateTotalCount(0);
    viewMock.display(buildValues(null, null), -1, buildValuesSelection());
    replay(viewMock);

    listBox.removeItem(o1);
    listBox.removeItem(o2);
    assertListBoxCount(0);
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
