package de.lessvoid.nifty.examples.controls.common;

/**
 * This is just an example. This should really be your own model. The ListBox does call toString() on this
 * to get the label it should display. But you can use your own ListBoxViewConverter if you want to use more
 * sophisticated mechanism.
 * @author void
 */
public class JustAnExampleModelClass {
  private String label;

  public JustAnExampleModelClass(final String label) {
    this.label = label;
  }

  public String toString() {
    return label;
  }
}