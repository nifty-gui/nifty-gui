package de.lessvoid.nifty.controls.listbox;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.controls.ListBox.SelectionMode;

public class ListBoxItemNextPreviousWithMultipleSelectionTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>(null);
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");

  @Before
  public void before() {
    listBox.changeSelectionMode(SelectionMode.Multiple, true);
    listBox.addItem(o1);
    listBox.addItem(o2);
  }

  @Test
  public void testDefaultSelection() {
    assertEquals(o1, listBox.getSelection().get(0));
  }

  @Test
  public void testNext() {
    listBox.selectNext();
    assertEquals(o1, listBox.getSelection().get(0));
  }

  @Test
  public void testNextEnd() {
    listBox.selectItem(o2);
    listBox.selectNext();
    assertEquals(o1, listBox.getSelection().get(0));
  }

  @Test
  public void testPreviousAtBeginning() {
    listBox.selectPrevious();
    assertEquals(o1, listBox.getSelection().get(0));
  }

  @Test
  public void testPrevious() {
    listBox.selectItem(o2);
    listBox.selectPrevious();
    assertEquals(o1, listBox.getSelection().get(0));
  }
}
