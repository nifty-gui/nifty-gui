package de.lessvoid.nifty.controls.listbox;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ListBoxItemTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>(null);

  @Test
  public void testDefault() {
    assertListBoxCount(0);
  }

  @Test
  public void testRemoveItemByNegativeIndex() {
    listBox.removeItemByIndex(-1);
    assertListBoxCount(0);
  }

  @Test
  public void testRemoveItemByIndexTooLarge() {
    listBox.removeItemByIndex(0);
    assertListBoxCount(0);
  }

  private void assertListBoxCount(final int expected) {
    assertEquals(expected, listBox.itemCount());
  }
}
