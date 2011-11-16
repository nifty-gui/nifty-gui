package de.lessvoid.nifty.controls;

import static org.junit.Assert.assertEquals;
import de.lessvoid.nifty.controls.listbox.ListBoxImpl;
import de.lessvoid.nifty.controls.listbox.TestItem;

public class SelectionCheck {
  private final ListBoxImpl<TestItem> listBox;

  public SelectionCheck(final ListBoxImpl<TestItem> listBox) {
    this.listBox = listBox;
  }

  public void assertSelection(final Object ... selection) {
    assertEquals(selection.length, listBox.getSelection().size());
    int i = 0;
    for (Object o : selection) {
      assertEquals(o, listBox.getSelection().get(i));
      i++;
    }
  }

  public void assertSelectionIndices(final int ... selection) {
    assertEquals(selection.length, listBox.getSelectedIndices().size());
    int i = 0;
    for (int o : selection) {
      assertEquals(o, listBox.getSelectedIndices().get(i).intValue());
      i++;
    }
  }

  public void assertChangedEventSelection(final ListBoxSelectionChangedEvent<TestItem> selectionChangedEvent, final TestItem ... expected) {
    assertEquals(expected.length, selectionChangedEvent.getSelection().size());
    int i = 0;
    for (TestItem item : expected) {
      assertEquals(item, selectionChangedEvent.getSelection().get(i++));
    }
  }

  public void assertChangedEventSelectionIndices(final ListBoxSelectionChangedEvent<TestItem> selectionChangedEvent, final int ... expected) {
    assertEquals(expected.length, selectionChangedEvent.getSelectionIndices().size());
    int i = 0;
    for (int item : expected) {
      assertEquals(item, selectionChangedEvent.getSelectionIndices().get(i++).intValue());
    }
  }

}
