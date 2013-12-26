package de.lessvoid.nifty.controls.listbox;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ListBoxFocusItemTest {
  private ListBoxFocusItem focusItem = new ListBoxFocusItem();

  @Test
  public void testDefaultEmpty() {
    createList();
    assertEquals(-1, focusItem.resolve(-1, 0));
  }

  @Test
  public void testDefaultWithNoFocus() {
    createList();
    assertEquals(-1, focusItem.resolve(-1, 4));
  }

  @Test
  public void testDefaultWithNoFocusAndRemove() {
    createList(1, 2, 3);
    assertEquals(-1, focusItem.resolve(-1, 4));
  }

  @Test
  public void testDefaultWithEmptyRemove() {
    createList();
    assertEquals(2, focusItem.resolve(2, 4));
  }

  @Test
  public void testRemoveFocusElement() {
    createList(1);
    assertEquals(1, focusItem.resolve(1, 4));
  }

  @Test
  public void testWithRemoveElementBeforeFocus() {
    createList(0);
    assertEquals(0, focusItem.resolve(1, 4));
  }

  @Test
  public void testWithRemoveElementAfterFocus() {
    createList(2);
    assertEquals(1, focusItem.resolve(1, 4));
  }

  @Test
  public void testWithRemoveMultipleElementAfterFocus() {
    createList(2, 3);
    assertEquals(1, focusItem.resolve(1, 4));
  }

  @Test
  public void testWithRemoveMultipleElementBeforeFocus() {
    createList(0, 1, 2);
    assertEquals(0, focusItem.resolve(3, 4));
  }

  @Test
  public void testWithRemoveLastElementWithFocus() {
    createList(3);
    assertEquals(2, focusItem.resolve(3, 4));
  }

  @Test
  public void testWithRemoveLastElementsWithFocus() {
    createList(1, 2, 3);
    assertEquals(0, focusItem.resolve(3, 4));
  }

  @Test
  public void testWithRemoveAllElementWithFocus() {
    createList(0, 1, 2, 3);
    assertEquals(-1, focusItem.resolve(3, 4));
  }

  @Test
  public void testWithRemoveAllElementBeforeFocus() {
    createList(0, 1, 2);
    assertEquals(0, focusItem.resolve(3, 4));
  }

  private void createList(final Integer... indizes) {
    focusItem.prepare();
    for (Integer idx : indizes) {
      focusItem.registerIndex(idx);
    }
  }
}
