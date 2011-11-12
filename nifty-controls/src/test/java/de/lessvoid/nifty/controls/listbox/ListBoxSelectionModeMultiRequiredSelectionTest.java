package de.lessvoid.nifty.controls.listbox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class ListBoxSelectionModeMultiRequiredSelectionTest {
  private ListBoxSelectionModeMulti<Object> selectionMode = new ListBoxSelectionModeMulti<Object>();
  private Object o1 = new Object();
  private Object o2 = new Object();

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
  public void testRemoveMultiInList() {
    selectionMode.add(o1);
    selectionMode.add(o2);
    selectionMode.remove(o1);
    selectionMode.remove(o2);
    assertSelection(o2);
  }

  @Test
  public void testRemoveMultiInListReverse() {
    selectionMode.add(o1);
    selectionMode.add(o2);
    selectionMode.remove(o2);
    selectionMode.remove(o1);
    assertSelection(o1);
  }

  @Test
  public void testRemoveForcedMultiInList() {
    selectionMode.add(o1);
    selectionMode.add(o2);
    selectionMode.removeForced(o1);
    selectionMode.removeForced(o2);
    assertSelection();
  }

  @Test
  public void testRemoveForcedMultiInListReverse() {
    selectionMode.add(o1);
    selectionMode.add(o2);
    selectionMode.removeForced(o2);
    selectionMode.removeForced(o1);
    assertSelection();
  }

  @Test
  public void testClear() {
    selectionMode.add(o1);
    selectionMode.clear();
    assertSelection();
  }

  @Test
  public void testClearMulti() {
    selectionMode.add(o1);
    selectionMode.add(o2);
    selectionMode.clear();
    assertSelection();
  }

  private void assertSelection(final Object ... expected) {
    List<Object> selection = selectionMode.getSelection();
    assertEquals(expected.length, selection.size());

    int i=0;
    for (Object o : selection) {
      assertEquals(o, expected[i++]);
    }
  }
}
