package de.lessvoid.nifty.controls.listbox.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

import javax.annotation.Nonnull;

public class ListBoxBuilder extends ControlBuilder {
  public ListBoxBuilder() {
    super("listBox");
  }

  public ListBoxBuilder(@Nonnull final String id) {
    super(id, "listBox");
  }

  protected ListBoxBuilder(@Nonnull final String id, @Nonnull final String name) {
    super(id, name);
  }

  public ListBoxBuilder displayItems(final int displayItems) {
    set("displayItems", String.valueOf(displayItems));
    return this;
  }

  public ListBoxBuilder selectionModeSingle() {
    set("selectionMode", "Single");
    return this;
  }

  public ListBoxBuilder selectionModeMutliple() {
    set("selectionMode", "Multiple");
    return this;
  }

  public ListBoxBuilder selectionModeDisabled() {
    set("selectionMode", "Disabled");
    return this;
  }

  public ListBoxBuilder showVerticalScrollbar() {
    set("vertical", "on");
    return this;
  }

  public ListBoxBuilder showHorizontalScrollbar() {
    set("horizontal", "on");
    return this;
  }

  public ListBoxBuilder hideVerticalScrollbar() {
    set("vertical", "off");
    return this;
  }

  public ListBoxBuilder hideHorizontalScrollbar() {
    set("horizontal", "off");
    return this;
  }

  public ListBoxBuilder optionalVerticalScrollbar() {
    set("vertical", "optional");
    return this;
  }

  public ListBoxBuilder optionalHorizontalScrollbar() {
    set("horizontal", "optional");
    return this;
  }

  public ListBoxBuilder viewConverterClass(@Nonnull final Class<?> clazz) {
    set("viewConverterClass", clazz.getName());
    return this;
  }
}
