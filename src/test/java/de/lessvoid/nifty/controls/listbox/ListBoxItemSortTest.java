package de.lessvoid.nifty.controls.listbox;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ListBoxItemSortTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>(null);
  private TestItem a = new TestItem("a");
  private TestItem c = new TestItem("c");
  private TestItem b = new TestItem("b");

  @Before
  public void before() {
    listBox.addItem(a);
    listBox.addItem(c);
    listBox.addItem(b);
  }

  @Test
  public void testSortWithoutComparator() {
    listBox.sortItems(null);
    List<TestItem> items = listBox.getItems();
    assertEquals(a, items.get(0));
    assertEquals(b, items.get(1));
    assertEquals(c, items.get(2));
  }

  @Test
  public void testSortWithComparator() {
    listBox.sortItems(new Comparator<TestItem>() {
      @Override
      public int compare(final TestItem o1, final TestItem o2) {
        return o1.getLabel().compareTo(o2.getLabel());
      }
    });
    List<TestItem> items = listBox.getItems();
    assertEquals(a, items.get(0));
    assertEquals(b, items.get(1));
    assertEquals(c, items.get(2));
  }
}
