package de.lessvoid.nifty.controls;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.lessvoid.nifty.controls.ListBoxSelectionModeDisabled;
import de.lessvoid.nifty.controls.listbox.TestItem;


public class ListBoxDisabledSelectionModeTest {
  private ListBoxSelectionModeDisabled<TestItem> selectionMode = new ListBoxSelectionModeDisabled<TestItem>();
  private TestItem o1 = new TestItem("o1");

  @Test
  public void testDefault() {
    assertEmptySelection();
  }

  @Test
  public void testClear() {
    selectionMode.clear();
    assertEmptySelection();
  }

  @Test
  public void testRemove() {
    selectionMode.remove(o1);
    assertEmptySelection();
  }

  @Test
  public void testAdd() {
    selectionMode.add(o1);
    assertEmptySelection();
  }

  private void assertEmptySelection() {
    assertTrue(selectionMode.getSelection().isEmpty());
  }
}
