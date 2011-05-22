package de.lessvoid.nifty.controls;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.controls.ListBox.SelectionMode;
import de.lessvoid.nifty.controls.listbox.ListBoxImpl;
import de.lessvoid.nifty.controls.listbox.ListBoxTestTool;
import de.lessvoid.nifty.controls.listbox.TestItem;

public class ListBoxSingleSelectionRequiresSelectionListViewTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>(null);
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");

  @Before
  public void before() {
    listBox.changeSelectionMode(SelectionMode.Single, true);
  }

  @After
  public void after() {
  }

  @Test
  public void testAutoSelectedAdd() {
    listBox.addItem(o1);
    assertSelection(o1);
  }

  @Test
  public void testAutoSelectedAddMultipl() {
    listBox.addItem(o1);
    listBox.addItem(o2);
    assertSelection(o1);
  }

  @Test
  public void testAutoSelectedInsert() {
    listBox.insertItem(o1, 0);
    assertSelection(o1);
  }

  @Test
  public void testAutoSelectedInsertMultipl() {
    listBox.insertItem(o1, 0);
    listBox.insertItem(o2, 0);
    assertSelection(o1);
  }

  @Test
  public void testAutoSelectedAddAllEmpty() {
    listBox.addAllItems(ListBoxTestTool.buildValues());
    assertSelection();
  }

  @Test
  public void testAutoSelectedAddAllSingle() {
    listBox.addAllItems(ListBoxTestTool.buildValues(o1));
    assertSelection(o1);
  }

  @Test
  public void testAutoSelectedAddAllMultiple() {
    listBox.addAllItems(ListBoxTestTool.buildValues(o1, o2));
    assertSelection(o1);
  }

  @Test
  public void testChangeToForceSelectionWithItems() {
    listBox.changeSelectionMode(SelectionMode.Single, false);
    listBox.addItem(o1);
    listBox.changeSelectionMode(SelectionMode.Single, true);
    assertSelection(o1);
  }

  @Test
  public void testRemoveSelection() {
    listBox.addItem(o1);
    listBox.addItem(o2);
    listBox.removeItem(o1);
    assertSelection(o2);
  }

  private void assertSelection(final TestItem ... expected) {
    List<TestItem> selection = listBox.getSelection();
    assertEquals(selection.size(), expected.length);

    int i=0;
    for (TestItem o : selection) {
      assertEquals(o, expected[i++]);
    }
  }
}
