package de.lessvoid.nifty.controls.listbox;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.controls.ListBox.SelectionMode;

public class ListBoxItemNextPreviousEmptyTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>();

  @Before
  public void before() {
    listBox.changeSelectionMode(SelectionMode.Single, true);
  }

  @Test
  public void testNext() {
    listBox.selectNext();
    assertTrue(listBox.getSelection().isEmpty());
  }

  @Test
  public void testPrevious() {
    listBox.selectPrevious();
    assertTrue(listBox.getSelection().isEmpty());
  }
}
