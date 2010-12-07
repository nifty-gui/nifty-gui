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

public class ListBoxAddItemTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>();
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private ListBoxView<TestItem> viewMock;

  @Before
  public void before() {
    viewMock = createMock(ListBoxView.class);
    assertEquals(0, listBox.bindToView(viewMock, 2));
  }

  @After
  public void after() {
    verify(viewMock);
  }

  @Test
  public void testAddItem() {
    viewMock.updateTotalCount(1);
    viewMock.display(buildValues(o1, null), 0, buildValuesSelection());
    replay(viewMock);

    listBox.addItem(o1);
    assertListBoxContent(o1);
  }

  @Test
  public void testAddItemTwice() {
    viewMock.updateTotalCount(1);
    viewMock.display(buildValues(o1, null), 0, buildValuesSelection());
    viewMock.updateTotalCount(2);
    viewMock.display(buildValues(o1, o2), 0, buildValuesSelection());
    replay(viewMock);

    listBox.addItem(o1);
    listBox.addItem(o2);
    assertListBoxContent(o1, o2);
  }

  @Test
  public void testInsertItem() {
    viewMock.updateTotalCount(1);
    viewMock.display(buildValues(o1, null), 0, buildValuesSelection());
    replay(viewMock);

    listBox.insertItem(o1, 0);
    assertListBoxContent(o1);
  }

  private void assertListBoxContent(final TestItem ... expected) {
    assertEquals(expected.length, listBox.getItems().size());
    int i = 0;
    for (TestItem e : expected) {
      assertEquals(e, listBox.getItems().get(i));
      i++;
    }
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
