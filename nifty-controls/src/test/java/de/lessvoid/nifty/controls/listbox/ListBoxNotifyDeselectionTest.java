package de.lessvoid.nifty.controls.listbox;

import static org.easymock.EasyMock.capture;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import org.easymock.Capture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.SelectionCheck;

public class ListBoxNotifyDeselectionTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>(null);
  private ListBoxView<TestItem> view;
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private Capture<ListBoxSelectionChangedEvent<TestItem>> lastEvent = new Capture<ListBoxSelectionChangedEvent<TestItem>>();
  private SelectionCheck selectionCheck = new SelectionCheck(listBox);

  @Before
  public void before() {
    listBox.bindToView(new ListBoxViewNull<TestItem>(), 2);
    listBox.addItem(o1);
    listBox.addItem(o2);
    listBox.selectItem(o1);

    view = createMock(ListBoxView.class);
    listBox.bindToView(view, 2);
  }

  @After
  public void after() {
    verify(view);
  }

  @Test
  public void testDeselectItem() {
    view.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    view.publish(capture(lastEvent));
    replay(view);

    listBox.deselectItem(o1);

    selectionCheck.assertChangedEventSelection(lastEvent.getValue());
    selectionCheck.assertChangedEventSelectionIndices(lastEvent.getValue());
  }

  @Test
  public void testDeselectByIndex() {
    view.display(ListBoxTestTool.buildValues(o1, o2), 0, ListBoxTestTool.buildValuesSelection());
    view.publish(capture(lastEvent));
    replay(view);

    listBox.deselectItemByIndex(0);

    selectionCheck.assertChangedEventSelection(lastEvent.getValue());
    selectionCheck.assertChangedEventSelectionIndices(lastEvent.getValue());
  }

  @Test
  public void testRemoveSelectedItem() {
    view.scrollTo(0);
    view.updateTotalCount(1);
    view.display(ListBoxTestTool.buildValues(o2, null), 0, ListBoxTestTool.buildValuesSelection());
    view.publish(capture(lastEvent));
    replay(view);

    listBox.removeItem(o1);

    selectionCheck.assertChangedEventSelection(lastEvent.getValue());
    selectionCheck.assertChangedEventSelectionIndices(lastEvent.getValue());
  }

  @Test
  public void testRemoveSelectedItemByIndex() {
    view.scrollTo(0);
    view.updateTotalCount(1);
    view.display(ListBoxTestTool.buildValues(o2, null), 0, ListBoxTestTool.buildValuesSelection());
    view.publish(capture(lastEvent));
    replay(view);

    listBox.removeItemByIndex(0);

    selectionCheck.assertChangedEventSelection(lastEvent.getValue());
    selectionCheck.assertChangedEventSelectionIndices(lastEvent.getValue());
  }

  @Test
  public void testRemoveSelectionByClear() {
    view.updateTotalWidth(0);
    view.updateTotalCount(0);
    view.display(ListBoxTestTool.buildValues(null, null), -1, ListBoxTestTool.buildValuesSelection());
    view.publish(capture(lastEvent));
    replay(view);

    listBox.clear();

    selectionCheck.assertChangedEventSelection(lastEvent.getValue());
    selectionCheck.assertChangedEventSelectionIndices(lastEvent.getValue());
  }
}
