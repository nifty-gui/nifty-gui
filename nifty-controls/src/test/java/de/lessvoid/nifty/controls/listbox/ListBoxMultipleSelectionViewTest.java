package de.lessvoid.nifty.controls.listbox;

import de.lessvoid.nifty.controls.ListBox.SelectionMode;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import org.easymock.Capture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

public class ListBoxMultipleSelectionViewTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>(null);
  private ListBoxView<TestItem> viewMock;
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private Capture<ListBoxSelectionChangedEvent<TestItem>> lastEvent = Capture.newInstance();
  private SelectionCheck selectionCheck = new SelectionCheck(listBox);

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
