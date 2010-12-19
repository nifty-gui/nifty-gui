package de.lessvoid.nifty.controls.listbox;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ListBoxViewDefaultTest {
  private ListBoxImpl<String> listBox = new ListBoxImpl<String>();
  private ListBoxView<String> view;

  @SuppressWarnings("unchecked")
  @Before
  public void before() {
    view = createMock(ListBoxView.class);
    listBox.bindToView(view, 2);
  }

  @After
  public void after() {
    verify(view);
  }

  @Test
  public void testUpdateViewWithEmptyList() {
    expect(view.getWidth("a")).andReturn(100);
    view.updateTotalWidth(100);
    view.updateTotalCount(1);
    view.display(buildValues("a", null), 0, buildValuesSelection());
    expect(view.getWidth("b")).andReturn(100);
    view.updateTotalCount(2);
    view.display(buildValues("a", "b"), 0, buildValuesSelection());
    view.display(buildValues("a", "b"), 0, buildValuesSelection());
    replay(view);

    listBox.addItem("a");
    listBox.addItem("b");
    listBox.updateView(0);
  }

  private List<String> buildValues(final String ... values) {
    List<String> result = new ArrayList<String>();
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
