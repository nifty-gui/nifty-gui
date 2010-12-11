package de.lessvoid.nifty.controls.listbox;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ListBoxViewTest {
  private ListBoxImpl<TestItem> listBox = new ListBoxImpl<TestItem>();
  private TestItem o1 = new TestItem("o1");
  private TestItem o2 = new TestItem("o2");
  private TestItem o3 = new TestItem("o3");
  private ListBoxView<TestItem> view;

  @SuppressWarnings("unchecked")
  @Before
  public void before() {
    view = createMock(ListBoxView.class);
    assertEquals(0, listBox.bindToView(view, 2));
  }

  @After
  public void after() {
    verify(view);
  }

  @Test
  public void testUpdateViewWithEmptyList() {
    view.display(buildValues(null, null), -1, buildValuesSelection());
    replay(view);

    listBox.updateView(0);
  }

  @Test
  public void testSingleEntry() {
    view.updateTotalCount(1);
    view.display(buildValues(o1, null), 0, buildValuesSelection());
    replay(view);

    listBox.addItem(o1);
  }

  @Test
  public void testTwoEntries() {
    view.updateTotalCount(1);
    view.display(buildValues(o1, null), 0, buildValuesSelection());
    view.updateTotalCount(2);
    view.display(buildValues(o1, o2), 0, buildValuesSelection());
    replay(view);

    listBox.addItem(o1);
    listBox.addItem(o2);
  }

  @Test
  public void testThreeEntries() {
    view.updateTotalCount(1);
    view.display(buildValues(o1, null), 0, buildValuesSelection());
    view.updateTotalCount(2);
    view.display(buildValues(o1, o2), 0, buildValuesSelection());
    view.updateTotalCount(3);
    view.display(buildValues(o1, o2), 0, buildValuesSelection());
    replay(view);

    listBox.addItem(o1);
    listBox.addItem(o2);
    listBox.addItem(o3);
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
