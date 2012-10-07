package de.lessvoid.nifty.controls.listbox;

import org.junit.Ignore;

@Ignore
public class TestItem implements Comparable<TestItem> {
  private String label;

  public TestItem(final String label) {
    this.label = label;
  }

  @Override
  public int compareTo(final TestItem o) {
    return label.compareTo(o.label);
  }

  public String getLabel() {
    return label;
  }

  public String toString() {
    return label;
  }
}
