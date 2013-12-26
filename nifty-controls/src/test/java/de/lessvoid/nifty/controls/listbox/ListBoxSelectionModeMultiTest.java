package de.lessvoid.nifty.controls.listbox;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class ListBoxSelectionModeMultiTest {
  private ListBoxSelectionModeMulti<Object> selectionMode = new ListBoxSelectionModeMulti<Object>();
  private Object o1 = new Object();
  private Object o2 = new Object();

  @Test
  public void testEmptyDefaultSelection() {
    assertSelection();
  }

  @Test
  public void testRequiresSelectionDefault() {
    assertFalse(selectionMode.requiresAutoSelection());
  }

  @Test
  public void testAdd() {
    selectionMode.add(o1);
    assertSelection(o1);
  }

  @Test
  public void testAddTwice() {
    selectionMode.add(o1);
    selectionMode.add(o2);
    assertSelection(o1, o2);
  }

  @Test
  public void testRemoveNotInList() {
    selectionMode.remove(o1);
    assertSelection();
  }

  @Test
  public void testRemoveInList() {
    selectionMode.add(o1);
    selectionMode.remove(o1);
    assertSelection();
  }

  @Test
  public void testClear() {
    selectionMode.add(o1);
    selectionMode.clear();
    assertSelection();
  }

  private void assertSelection(final Object... expected) {
    List<Object> selection = selectionMode.getSelection();
    assertEquals(selection.size(), expected.length);

    int i = 0;
    for (Object o : selection) {
      assertEquals(o, expected[i++]);
    }
  }
}
