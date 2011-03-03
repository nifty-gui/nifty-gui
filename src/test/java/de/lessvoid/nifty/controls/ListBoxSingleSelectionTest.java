package de.lessvoid.nifty.controls;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.lessvoid.nifty.controls.listbox.ListBoxImpl;
import de.lessvoid.nifty.controls.listbox.TestItem;

public class ListBoxSingleSelectionTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>();
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");

  @Test
  public void testDefault() {
    assertSelection();
  }

  @Test
  public void testAddItem() {
    listBox.addItem(o1);
    assertSelection();
  }

  @Test
  public void testSelectWithoutItems() {
    listBox.selectItemByIndex(0);
    assertSelection();
  }

  @Test
  public void testSelectWithItemIndexTooLarge() {
    listBox.addItem(o1);
    listBox.selectItemByIndex(1);
    assertSelection();
  }

  @Test
  public void testSelectWithNegativeIndex() {
    listBox.selectItemByIndex(-1);
    assertSelection();
  }

  @Test
  public void testSelectFirstItemByIndex() {
    listBox.addItem(o1);
    listBox.selectItemByIndex(0);
    assertSelection(o1);
  }

  @Test
  public void testSelectFirstItem() {
    listBox.addItem(o1);
    listBox.selectItem(o1);
    assertSelection(o1);
  }

  @Test
  public void testSingleSelection() {
    listBox.addItem(o1);
    listBox.addItem(o2);
    listBox.selectItemByIndex(0);
    listBox.selectItemByIndex(1);
    assertSelection(o2);
  }

  @Test
  public void testSingleSelectionWithItem() {
    listBox.addItem(o1);
    listBox.addItem(o2);
    listBox.selectItem(o1);
    listBox.selectItem(o2);
    assertSelection(o2);
  }

  @Test
  public void testClearWithSelection() {
    listBox.addItem(o1);
    listBox.selectItemByIndex(0);
    listBox.clear();
    assertListBoxCount(0);
    assertSelection();
  }

  @Test
  public void testRemoveItemWithSelection() {
    listBox.addItem(o1);
    listBox.selectItemByIndex(0);
    listBox.removeItemByIndex(0);
    assertListBoxCount(0);
  }

  @Test
  public void testRemoveSelectedIndex() {
    listBox.addItem(o1);
    listBox.selectItemByIndex(0);

    listBox.removeItemByIndex(0);
    assertSelection();
    assertListBoxCount(0);
  }

  @Test
  public void testRemoveUnselectedIndex() {
    listBox.addItem(o1);
    listBox.removeItemByIndex(0);
    assertSelection();
    assertListBoxCount(0);
  }

  @Test
  public void testRemoveItem() {
    listBox.addItem(o1);
    listBox.selectItem(o1);
    listBox.removeItem(o1);
    assertSelection();
  }

  @Test
  public void testRemoveItemWithAnItemNotPartOfTheList() {
    listBox.addItem(o1);
    listBox.selectItem(o1);
    listBox.removeItem(o2);
    assertSelection(o1);
  }

  @Test
  public void testRemoveChangesSelectionIndex() {
    listBox.addItem(o1);
    listBox.addItem(o2);
    listBox.selectItem(o1);
    listBox.removeItem(o1);
    assertSelection();
  }

  @Test
  public void testDeselectionByIndex() {
    listBox.addItem(o1);
    listBox.selectItemByIndex(0);
    listBox.deselectItemByIndex(0);
    assertSelection();
  }

  @Test
  public void testDeselectionByIndexTooLarge() {
    listBox.addItem(o1);
    listBox.selectItemByIndex(0);
    listBox.deselectItemByIndex(1);
    assertSelection(o1);
  }

  @Test
  public void testDeselectionByNegativIndex() {
    listBox.addItem(o1);
    listBox.selectItemByIndex(0);
    listBox.deselectItemByIndex(-1);
    assertSelection(o1);
  }

  @Test
  public void testDeselection() {
    listBox.addItem(o1);
    listBox.selectItem(o1);
    listBox.deselectItem(o1);
    assertSelection();
  }

  @Test
  public void testDeselectionByNotAddedItem() {
    listBox.addItem(o1);
    listBox.selectItem(o1);
    listBox.deselectItem(o2);
    assertSelection(o1);
  }

  @Test
  public void testDeselectionWithoutSelection() {
    listBox.addItem(o1);
    listBox.deselectItem(o1);
    assertSelection();
  }

  private void assertListBoxCount(final int expected) {
    assertEquals(expected, listBox.itemCount());
  }
  
  private void assertSelection(final Object ... selection) {
    assertEquals(selection.length, listBox.getSelection().size());
    int i = 0;
    for (Object o : selection) {
      assertEquals(o, listBox.getSelection().get(i));
      i++;
    }
  }
}
