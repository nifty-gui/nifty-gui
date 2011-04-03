package de.lessvoid.nifty.controls;

import static org.easymock.EasyMock.capture;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.easymock.Capture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.controls.ListBox.SelectionMode;
import de.lessvoid.nifty.controls.listbox.ListBoxImpl;
import de.lessvoid.nifty.controls.listbox.ListBoxTestTool;
import de.lessvoid.nifty.controls.listbox.ListBoxView;
import de.lessvoid.nifty.controls.listbox.TestItem;

public class ListBoxMultipleSelectionViewTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>();
  private ListBoxView<TestItem> viewMock;
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private Capture<ListBoxSelectionChangedEvent<TestItem>> lastEvent = new Capture<ListBoxSelectionChangedEvent<TestItem>>();
  private SelectionCheck selectionCheck = new SelectionCheck(listBox);

  @SuppressWarnings("unchecked")
  @Before
  public void before() {
    listBox.addItem(o1);
    listBox.addItem(o2);
    listBox.changeSelectionMode(SelectionMode.Multiple, false);
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
    viewMock.display(ListBoxTestTool.buildValues(null, null), -1, ListBoxTestTool.buildValuesSelection());
    viewMock.publish(capture(lastEvent));
    replay(viewMock);

    List<TestItem> itemsToRemove = new ArrayList<TestItem>();
    itemsToRemove.add(o1);
    itemsToRemove.add(o2);
    listBox.removeAllItems(itemsToRemove);

    assertTrue(listBox.getItems().isEmpty());
    selectionCheck.assertSelection();
    selectionCheck.assertChangedEventSelection(lastEvent.getValue());
    selectionCheck.assertChangedEventSelectionIndices(lastEvent.getValue());
  }
}
