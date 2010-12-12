package de.lessvoid.nifty.controls;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.easymock.Capture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.controls.listbox.ListBoxImpl;
import de.lessvoid.nifty.controls.listbox.ListBoxView;
import de.lessvoid.nifty.controls.listbox.TestItem;
import static org.easymock.EasyMock.capture;
import static org.easymock.classextension.EasyMock.*;

public class ListBoxMultipleSelectionViewTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>();
  private ListBoxView<TestItem> viewMock;
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private Capture<ListBoxSelectionChangedEvent<TestItem>> lastEvent = new Capture<ListBoxSelectionChangedEvent<TestItem>>();

  @Before
  public void before() {
    listBox.addItem(o1);
    listBox.addItem(o2);
    listBox.changeSelectionMode(new ListBoxSelectionModeMulti<TestItem>());
    listBox.selectItem(o1);
    listBox.selectItem(o2);

    viewMock = createMock(ListBoxView.class);
    listBox.bindToView(viewMock, 2);
  }

  @After
  public void after() {
    verify(viewMock);
  }

  @Test
  public void testRemoveAllItemsWithSelection() {
    viewMock.updateTotalCount(0);
    viewMock.display(buildValues(null, null), -1, buildValuesSelection());
    viewMock.publish(capture(lastEvent));
    replay(viewMock);

    List<TestItem> itemsToRemove = new ArrayList<TestItem>();
    itemsToRemove.add(o1);
    itemsToRemove.add(o2);
    listBox.removeAllItems(itemsToRemove);

    assertTrue(listBox.getItems().isEmpty());
    assertTrue(listBox.getSelection().isEmpty());
    assertEquals(0, lastEvent.getValue().getSelection().size());
  }

  private List<TestItem> buildValues(final TestItem ... values) {
    List<TestItem> result = new ArrayList<TestItem>();
    for (int i=0; i<values.length; i++) {
      result.add(values[i]);
    }
    return result;
  }

  private List<Integer> buildValuesSelection(final Integer ... values) {
    List<Integer> result = new ArrayList<Integer>();
    for (int i=0; i<values.length; i++) {
      result.add(values[i]);
    }
    return result;
  }

}
