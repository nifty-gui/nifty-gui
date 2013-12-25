package de.lessvoid.nifty.controls.listbox;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;


public class ListBoxSelectionModeSingleRequiredSelectionTest {
  private ListBoxSelectionModeSingle<Object> selectionMode = new ListBoxSelectionModeSingle<Object>();
  private Object o1 = new Object();

  @Before
  public void before() {
    selectionMode.enableRequiresSelection(true);
  }

  @Test
  public void testRequiresSelectionDefault() {
    assertTrue(selectionMode.requiresAutoSelection());
  }

  @Test
  public void testRequiresSelectionWithSingleItem() {
    selectionMode.add(o1);
    assertFalse(selectionMode.requiresAutoSelection());
  }

  @Test
  public void testRemoveInList() {
    selectionMode.add(o1);
    selectionMode.remove(o1);
    assertSelection(o1);
  }

  @Test
  public void testRemoveForcedInList() {
    selectionMode.add(o1);
    selectionMode.removeForced(o1);
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
    assertEquals(expected.length, selection.size());

    int i = 0;
    for (Object o : selection) {
      assertEquals(o, expected[i++]);
    }
  }
}
