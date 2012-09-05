package de.lessvoid.nifty.controls.listbox;

import static org.easymock.EasyMock.expect;
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
  private static final int WIDTH_101 = 101;
  private static final int WIDTH_100 = 100;
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>(null);
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private ListBoxView<TestItem> viewMock;

  @SuppressWarnings("unchecked")
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
    expect(viewMock.getWidth(o1)).andReturn(WIDTH_100);
    viewMock.updateTotalWidth(WIDTH_100);
    viewMock.updateTotalCount(1);
    viewMock.display(ListBoxTestTool.buildValues(o1, null), 0, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);

    listBox.addItem(o1);
    assertListBoxContent(o1);
  }

  @Test
  public void testAddItemTwice() {
    expect(viewMock.getWidth(o1)).andReturn(WIDTH_100);
    viewMock.updateTotalWidth(WIDTH_100);
    viewMock.updateTotalCount(1);
    viewMock.display(ListBoxTestTool.buildValues(o1, null), 0, ListBoxTestTool.buildValuesSelection());
    expect(viewMock.getWidth(o2)).andReturn(WIDTH_100);
    viewMock.updateTotalCount(2);
    viewMock.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);

    listBox.addItem(o1);
    listBox.addItem(o2);
    assertListBoxContent(o1, o2);
  }

  @Test
  public void testInsertItem() {
    expect(viewMock.getWidth(o1)).andReturn(WIDTH_100);
    viewMock.updateTotalWidth(WIDTH_100);
    viewMock.updateTotalCount(1);
    viewMock.display(ListBoxTestTool.buildValues(o1, null), 0, ListBoxTestTool.buildValuesSelection());
    replay(viewMock);

    listBox.insertItem(o1, 0);
    assertListBoxContent(o1);
  }

  @Test
  public void testAddAllItems() {
    expect(viewMock.getWidth(o1)).andReturn(WIDTH_100);
    viewMock.updateTotalCount(2);
    viewMock.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    expect(viewMock.getWidth(o2)).andReturn(WIDTH_101);
    viewMock.updateTotalWidth(WIDTH_101);
    replay(viewMock);

    List<TestItem> itemsToAdd = new ArrayList<TestItem>();
    itemsToAdd.add(o1);
    itemsToAdd.add(o2);
    listBox.addAllItems(itemsToAdd);

    assertListBoxContent(o1, o2);
  }

  private void assertListBoxContent(final TestItem ... expected) {
    assertEquals(expected.length, listBox.getItems().size());
    int i = 0;
    for (TestItem e : expected) {
      assertEquals(e, listBox.getItems().get(i));
      i++;
    }
  }
}
