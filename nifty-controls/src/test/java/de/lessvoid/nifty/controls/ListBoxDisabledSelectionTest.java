package de.lessvoid.nifty.controls;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.controls.ListBox.SelectionMode;
import de.lessvoid.nifty.controls.listbox.ListBoxImpl;
import de.lessvoid.nifty.controls.listbox.TestItem;

public class ListBoxDisabledSelectionTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>(null);
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private SelectionCheck selectionCheck = new SelectionCheck(listBox);

  @Before
  public void before() {
    listBox.addItem(o1);
    listBox.addItem(o2);
    listBox.changeSelectionMode(SelectionMode.Disabled, false);
  }

  @Test
  public void testSelectWithItemIndexTooLarge() {
    listBox.selectItemByIndex(0);
    selectionCheck.assertSelection();
    selectionCheck.assertSelectionIndices();
  }

  @Test
  public void testSelectFirstItem() {
    listBox.selectItem(o1);
    selectionCheck.assertSelection();
    selectionCheck.assertSelectionIndices();
  }
}
