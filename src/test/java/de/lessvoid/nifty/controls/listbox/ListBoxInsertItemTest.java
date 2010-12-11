package de.lessvoid.nifty.controls.listbox;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ListBoxInsertItemTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>();
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");

  @Test
  public void testInsertItemLast() {
    listBox.addItem(o1);
    listBox.insertItem(o2, 1);
    assertListBoxContent(o1, o2);
  }

  @Test
  public void testInsertItemFirst() {
    listBox.addItem(o1);
    listBox.insertItem(o2, 0);
    assertListBoxContent(o2, o1);
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
