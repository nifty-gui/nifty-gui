package de.lessvoid.nifty.controls.listbox;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBox.SelectionMode;

public class ListBoxItemNextPreviousWithDisabledSelectionTest {
  private ListBox<TestItem> listBox = new ListBoxImpl<TestItem>();
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");

  @Before
  public void before() {
    listBox.changeSelectionMode(SelectionMode.Disabled, true);
    listBox.addItem(o1);
    listBox.addItem(o2);
  }

  @Test
  public void testDefaultSelection() {
    assertTrue(listBox.getSelection().isEmpty());
  }

  @Test
  public void testNext() {
    listBox.selectNext();
    assertTrue(listBox.getSelection().isEmpty());
  }

  @Test
  public void testNextEnd() {
    listBox.selectItem(o2);
    listBox.selectNext();
    assertTrue(listBox.getSelection().isEmpty());
  }

  @Test
  public void testPreviousAtBeginning() {
    listBox.selectPrevious();
    assertTrue(listBox.getSelection().isEmpty());
  }

  @Test
  public void testPrevious() {
    listBox.selectItem(o2);
    listBox.selectPrevious();
    assertTrue(listBox.getSelection().isEmpty());
  }
}
