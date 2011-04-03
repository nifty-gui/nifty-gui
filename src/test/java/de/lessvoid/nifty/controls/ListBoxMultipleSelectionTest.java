package de.lessvoid.nifty.controls;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.controls.ListBox.SelectionMode;
import de.lessvoid.nifty.controls.listbox.ListBoxImpl;
import de.lessvoid.nifty.controls.listbox.TestItem;

public class ListBoxMultipleSelectionTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>();
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private SelectionCheck selectionCheck = new SelectionCheck(listBox);

  @Before
  public void before() {
    listBox.addItem(o1);
    listBox.addItem(o2);
    listBox.changeSelectionMode(SelectionMode.Multiple, false);
  }

  @Test
  public void testDefault() {
    selectionCheck.assertSelection();
    selectionCheck.assertSelectionIndices();
  }

  @Test
  public void testSelectWithItemIndexTooLarge() {
    listBox.selectItemByIndex(2);
    selectionCheck.assertSelection();
    selectionCheck.assertSelectionIndices();
  }

  @Test
  public void testSelectWithNegativeIndex() {
    listBox.selectItemByIndex(-1);
    selectionCheck.assertSelection();
    selectionCheck.assertSelectionIndices();
  }

  @Test
  public void testSelectFirstItemByIndex() {
    listBox.selectItemByIndex(0);
    selectionCheck.assertSelection(o1);
    selectionCheck.assertSelectionIndices(0);
  }

  @Test
  public void testSelectFirstItem() {
    listBox.selectItem(o1);
    selectionCheck.assertSelection(o1);
    selectionCheck.assertSelectionIndices(0);
  }

  @Test
  public void testMultipleSelection() {
    listBox.selectItemByIndex(0);
    listBox.selectItemByIndex(1);
    selectionCheck.assertSelection(o1, o2);
    selectionCheck.assertSelectionIndices(0, 1);
  }

  @Test
  public void testMultipleSelectionWithItem() {
    listBox.selectItem(o1);
    listBox.selectItem(o2);
    selectionCheck.assertSelection(o1, o2);
    selectionCheck.assertSelectionIndices(0, 1);
  }

  @Test
  public void testClearWithSelection() {
    listBox.selectItemByIndex(0);
    listBox.clear();
    selectionCheck.assertSelection();
    selectionCheck.assertSelectionIndices();
  }

  @Test
  public void testRemoveItemWithSelection() {
    listBox.selectItemByIndex(0);
    listBox.removeItemByIndex(0);
    selectionCheck.assertSelection();
    selectionCheck.assertSelectionIndices();
  }

  @Test
  public void testRemoveItemWithAnItemNotPartOfTheList() {
    listBox.selectItem(o1);
    listBox.removeItem(o2);
    selectionCheck.assertSelection(o1);
    selectionCheck.assertSelectionIndices(0);
  }

  @Test
  public void testRemoveUnselectedIndex() {
    listBox.removeItemByIndex(0);
    selectionCheck.assertSelection();
    selectionCheck.assertSelectionIndices();
  }

  @Test
  public void testDeselectionByIndex() {
    listBox.selectItemByIndex(0);
    listBox.deselectItemByIndex(0);
    selectionCheck.assertSelection();
    selectionCheck.assertSelectionIndices();
  }

  @Test
  public void testDeselectionByIndexTooLarge() {
    listBox.selectItemByIndex(0);
    listBox.deselectItemByIndex(1);
    selectionCheck.assertSelection(o1);
    selectionCheck.assertSelectionIndices(0);
  }

  @Test
  public void testDeselectionByNegativIndex() {
    listBox.selectItemByIndex(0);
    listBox.deselectItemByIndex(-1);
    selectionCheck.assertSelection(o1);
    selectionCheck.assertSelectionIndices(0);
  }

  @Test
  public void testDeselection() {
    listBox.selectItem(o1);
    listBox.deselectItem(o1);
    selectionCheck.assertSelection();
    selectionCheck.assertSelectionIndices();
  }

  @Test
  public void testDeselectionFirst() {
    listBox.selectItem(o1);
    listBox.selectItem(o2);
    listBox.deselectItem(o1);
    selectionCheck.assertSelection(o2);
    selectionCheck.assertSelectionIndices(1);
  }

  @Test
  public void testDeselectionByNotAddedItem() {
    listBox.selectItem(o1);
    listBox.deselectItem(o2);
    selectionCheck.assertSelection(o1);
    selectionCheck.assertSelectionIndices(0);
  }

  @Test
  public void testDeselectionWithoutSelection() {
    listBox.deselectItem(o1);
    selectionCheck.assertSelection();
    selectionCheck.assertSelectionIndices();
  }

  @Test
  public void testChangeToForceSelectionWithItems() {
    listBox.changeSelectionMode(SelectionMode.Multiple, false);
    listBox.addItem(o1);
    listBox.addItem(o2);
    listBox.changeSelectionMode(SelectionMode.Multiple, true);
    selectionCheck.assertSelection(o1);
    selectionCheck.assertSelectionIndices(0);
  }
}
