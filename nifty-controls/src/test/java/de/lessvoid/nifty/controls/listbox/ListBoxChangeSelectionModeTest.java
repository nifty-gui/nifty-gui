package de.lessvoid.nifty.controls.listbox;

import de.lessvoid.nifty.controls.ListBox.SelectionMode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ListBoxChangeSelectionModeTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>(null);
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");

  @Before
  public void before() {
    listBox.addItem(o1);
    listBox.addItem(o2);
  }

  @Test
  public void testChangeFromDefaultToMultipleWithoutSelection() {
    listBox.changeSelectionMode(SelectionMode.Multiple, false);
    assertSelection();
  }

  @Test
  public void testChangeFromDefaultToMultipleWithSingleSelection() {
    listBox.selectItemByIndex(0);
    listBox.changeSelectionMode(SelectionMode.Multiple, false);
    assertSelection(o1);
  }

  @Test
  public void testChangeFromMultipleToSingleWithoutSelection() {
    listBox.changeSelectionMode(SelectionMode.Multiple, false);
    listBox.changeSelectionMode(SelectionMode.Single, false);
    assertSelection();
  }

  @Test
  public void testChangeFromMultipleToSingleWithSingleSelection() {
    listBox.changeSelectionMode(SelectionMode.Multiple, false);
    listBox.selectItem(o1);
    listBox.changeSelectionMode(SelectionMode.Single, false);
    assertSelection(o1);
  }

  @Test
  public void testChangeFromMultipleToSingleWithMultipleSelection() {
    listBox.changeSelectionMode(SelectionMode.Multiple, false);
    listBox.selectItem(o1);
    listBox.selectItem(o2);
    listBox.changeSelectionMode(SelectionMode.Single, false);
    assertSelection(o1);
  }

  private void assertSelection(final Object... selection) {
    assertEquals(selection.length, listBox.getSelection().size());
    int i = 0;
    for (Object o : selection) {
      assertEquals(o, listBox.getSelection().get(i));
      i++;
    }
  }
}
