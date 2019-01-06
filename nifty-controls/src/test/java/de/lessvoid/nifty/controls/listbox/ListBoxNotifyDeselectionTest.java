package de.lessvoid.nifty.controls.listbox;

import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import org.easymock.Capture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

public class ListBoxNotifyDeselectionTest {
  private ListBoxImpl<TestItem> listBox;
  private ListBoxView<TestItem> view;
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private Capture<ListBoxSelectionChangedEvent<TestItem>> lastEvent = Capture.newInstance();
  private SelectionCheck selectionCheck = new SelectionCheck(listBox);

  @Before
  public void before() {
    listBox = new ListBoxImpl<TestItem>(createMock(ListBox.class));
    view = createMock(ListBoxView.class);
    expect(view.getWidth(o1)).andReturn(10);
    view.updateTotalWidth(10);
    expectLastCall();
    view.updateTotalCount(1);
    expectLastCall();
    view.display(Arrays.asList(o1), 0, Collections.<Integer>emptyList());
    expectLastCall();

    expect(view.getWidth(o2)).andReturn(20);
    view.updateTotalWidth(20);
    expectLastCall();
    view.updateTotalCount(2);
    expectLastCall();
    view.display(Arrays.asList(o1, o2), 0, Collections.<Integer>emptyList());
    expectLastCall();
    view.display(Arrays.asList(o1, o2), 0, Collections.<Integer>emptyList());
    expectLastCall();
    view.scrollTo(0);
    expectLastCall();

    view.display(Arrays.asList(o1, o2), 0, Arrays.asList(0));
    expectLastCall();
    view.publish(capture(lastEvent));
    expectLastCall();
    view.display(Arrays.asList(o1, o2), 0, Arrays.asList(0));
    expectLastCall();
    replay(view);

    listBox.bindToView(view, 2);
    listBox.addItem(o1);
    listBox.addItem(o2);
    listBox.selectItem(o1);
  }

  @After
  public void after() {
    verify(view);
  }

  @Test
  public void testDeselectItem() {
    reset(view);
    view.display(Arrays.asList(o1, o2), 0, Collections.<Integer>emptyList());
    expectLastCall();
    view.publish(capture(lastEvent));
    expectLastCall();
    replay(view);

    listBox.deselectItem(o1);

    selectionCheck.assertChangedEventSelection(lastEvent.getValue());
    selectionCheck.assertChangedEventSelectionIndices(lastEvent.getValue());
  }

  @Test
  public void testDeselectByIndex() {
    reset(view);
    view.display(Arrays.asList(o1, o2), 0, Collections.<Integer>emptyList());
    expectLastCall();
    view.publish(capture(lastEvent));
    expectLastCall();
    replay(view);

    listBox.deselectItemByIndex(0);

    selectionCheck.assertChangedEventSelection(lastEvent.getValue());
    selectionCheck.assertChangedEventSelectionIndices(lastEvent.getValue());
  }

  @Test
  public void testRemoveSelectedItem() {
    reset(view);
    view.scrollTo(0);
    expectLastCall();
    view.updateTotalCount(1);
    expectLastCall();
    view.display(Arrays.asList(o2), 0, Collections.<Integer>emptyList());
    expectLastCall();
    view.publish(capture(lastEvent));
    expectLastCall();
    replay(view);

    listBox.removeItem(o1);

    selectionCheck.assertChangedEventSelection(lastEvent.getValue());
    selectionCheck.assertChangedEventSelectionIndices(lastEvent.getValue());
  }

  @Test
  public void testRemoveSelectedItemByIndex() {
    reset(view);
    view.scrollTo(0);
    expectLastCall();
    view.updateTotalCount(1);
    expectLastCall();
    view.display(Arrays.asList(o2), 0, Collections.<Integer>emptyList());
    expectLastCall();
    view.publish(capture(lastEvent));
    expectLastCall();
    replay(view);

    listBox.removeItemByIndex(0);

    selectionCheck.assertChangedEventSelection(lastEvent.getValue());
    selectionCheck.assertChangedEventSelectionIndices(lastEvent.getValue());
  }

  @Test
  public void testRemoveSelectionByClear() {
    reset(view);
    view.updateTotalWidth(0);
    expectLastCall();
    view.updateTotalCount(0);
    expectLastCall();
    view.display(Collections.<TestItem>emptyList(), -1, Collections.<Integer>emptyList());
    expectLastCall();
    view.publish(capture(lastEvent));
    expectLastCall();
    replay(view);

    listBox.clear();

    selectionCheck.assertChangedEventSelection(lastEvent.getValue());
    selectionCheck.assertChangedEventSelectionIndices(lastEvent.getValue());
  }
}
