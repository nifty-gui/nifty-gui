package de.lessvoid.nifty.controls.listbox;

import static org.easymock.EasyMock.capture;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.easymock.Capture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;

public class ListBoxNotifyDeselectionTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>();
  private ListBoxView<TestItem> view;
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private Capture<ListBoxSelectionChangedEvent<TestItem>> lastEvent = new Capture<ListBoxSelectionChangedEvent<TestItem>>();
  
  @SuppressWarnings("unchecked")
  @Before
  public void before() {
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
    view.display(buildValues(o1, o2), 0, buildValuesSelection());
    view.publish(capture(lastEvent));
    replay(view);

    listBox.deselectItem(o1);

    assertEquals(0, lastEvent.getValue().getSelection().size());
  }

  @Test
  public void testDeselectByIndex() {
    view.display(buildValues(o1, o2), 0, buildValuesSelection());
    view.publish(capture(lastEvent));
    replay(view);

    listBox.deselectItemByIndex(0);

    assertEquals(0, lastEvent.getValue().getSelection().size());
  }

  @Test
  public void testRemoveSelectedItem() {
    view.updateTotalCount(1);
    view.display(buildValues(o2, null), 0, buildValuesSelection());
    view.publish(capture(lastEvent));
    replay(view);

    listBox.removeItem(o1);

    assertEquals(0, lastEvent.getValue().getSelection().size());
  }

  @Test
  public void testRemoveSelectedItemByIndex() {
    view.updateTotalCount(1);
    view.display(buildValues(o2, null), 0, buildValuesSelection());
    view.publish(capture(lastEvent));
    replay(view);

    listBox.removeItemByIndex(0);

    assertEquals(0, lastEvent.getValue().getSelection().size());
  }

  @Test
  public void testRemoveSelectionByClear() {
    view.updateTotalCount(0);
    view.display(buildValues(null, null), -1, buildValuesSelection());
    view.publish(capture(lastEvent));
    replay(view);

    listBox.clear();

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
