package de.lessvoid.nifty.examples.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.checkbox.builder.CheckboxBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.dynamic.PanelCreator;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.listbox.ListBoxControl;
import de.lessvoid.nifty.controls.listbox.builder.ListBoxBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ControlsDemoStartScreen implements ScreenController, NiftyExample {
  private Nifty nifty;
  private Screen screen;

  @Override
  public void bind(@Nonnull final Nifty newNifty, @Nonnull final Screen newScreen) {
    screen = newScreen;
    nifty = newNifty;

    DropDown dropDown1 = findDropDownControl("dropDown1");
    if (dropDown1 != null) {
      dropDown1.addItem("Nifty GUI");
      dropDown1.addItem("Slick2D");
      dropDown1.addItem("Lwjgl");
      dropDown1.addItem("LibGDX");

      dropDown1.selectItemByIndex(0);
    }

    DropDown dropDown2 = findDropDownControl("dropDown2");
    if (dropDown2 != null) {
      dropDown2.addItem("rocks!");
      dropDown2.addItem("rules!");
      dropDown2.addItem("kicks ass!");
      dropDown2.addItem("is awesome!");
      dropDown2.addItem("shizzles :D");

      dropDown2.selectItem("rocks!");
    }

    ListBox listBoxStatic = screen.findNiftyControl("listBoxStatic", ListBox.class);
    for (int i = 0; i < 5; i++) {
      listBoxStatic.addItem("Listbox Item: " + i);
    }

    // dynamically add another DropDownControl
    Element dynamicParent = screen.findElementById("dynamic-parent");

    // create 8px height panel
    PanelCreator createPanel = new PanelCreator();
    createPanel.setChildLayout("horizontal");
    createPanel.setHeight("8px");
    createPanel.create(newNifty, screen, dynamicParent);

    // create new panel for label and checkbox
    createPanel = new PanelCreator();
    createPanel.setChildLayout("horizontal");
    Element row = createPanel.create(newNifty, screen, dynamicParent);

    // create label
    final LabelBuilder labelBuilder = new LabelBuilder();
    labelBuilder.text("Dyn. Checkbox:");
    labelBuilder.width("120px");
    labelBuilder.align(ElementBuilder.Align.Left);
    labelBuilder.textVAlign(ElementBuilder.VAlign.Center);
    labelBuilder.textHAlign(ElementBuilder.Align.Left);
    labelBuilder.build(newNifty, screen, row);

    final CheckboxBuilder checkboxBuilder = new CheckboxBuilder();
    // this will mess up the tab order to test this =)
    // I bet someone smart will bitch about the out of order tab order tho.
    // IT'S NOT A BUG IT'S A FEATURE DEMONSTRATION! HAH! :P
    checkboxBuilder.focusableInsertBeforeElementId("listBoxStatic");
    checkboxBuilder.build(nifty, screen, row);

    // create 8px height panel
    createPanel = new PanelCreator();
    createPanel.setChildLayout("horizontal");
    createPanel.setHeight("8px");
    createPanel.create(newNifty, screen, dynamicParent);

    // create new panel for label and drop down
    createPanel = new PanelCreator();
    createPanel.setChildLayout("horizontal");
    row = createPanel.create(newNifty, screen, dynamicParent);

    // create label (reusing builder)
    labelBuilder.text("Dynamic:");
    labelBuilder.build(newNifty, screen, row);

    // create drop down
    DropDownBuilder dropDownBuilder = new DropDownBuilder();
    final Element dropDown3Element = dropDownBuilder.build(nifty, screen, row);
    //noinspection unchecked
    DropDown<String> dropDown3 = (DropDown<String>) dropDown3Element.getNiftyControl(DropDown.class);
    if (dropDown3 != null) {
      dropDown3.addItem("dynamic drop down");
      dropDown3.addItem("ftw");
      dropDown3.selectItem("ftw");
    } else {
      throw new IllegalStateException("DropDown was not properly created.");
    }

    // create 8px height panel
    createPanel = new PanelCreator();
    createPanel.setChildLayout("horizontal");
    createPanel.setHeight("8px");
    createPanel.create(newNifty, screen, dynamicParent);

    // create new panel for label and drop down
    createPanel = new PanelCreator();
    createPanel.setChildLayout("horizontal");
    Element secondRow = createPanel.create(newNifty, screen, dynamicParent);

    // create label
    labelBuilder.text("Listbox Dyn.:");
    labelBuilder.build(newNifty, screen, secondRow);

    final ListBoxBuilder listBoxBuilder = new ListBoxBuilder();
    listBoxBuilder.hideHorizontalScrollbar();
    listBoxBuilder.displayItems(3);
    listBoxBuilder.width(SizeValue.wildcard());
    listBoxBuilder.height(SizeValue.percent(100));
    listBoxBuilder.childLayout(ElementBuilder.ChildLayoutType.Vertical);
    final Element dynamicListBoxElement = listBoxBuilder.build(nifty, screen, secondRow);
    ListBox dynamicListBox = dynamicListBoxElement.getNiftyControl(ListBox.class);
    if (dynamicListBox != null) {
      for (int i = 0; i < 10; i++) {
        dynamicListBox.addItem("Listbox Item: " + i);
      }
      dynamicListBox.selectItemByIndex(0);
    } else {
      throw new IllegalStateException("ListBox was not properly created.");
    }

    // dynamically add the backButton for testing purpose
    final ButtonBuilder buttonBuilder = new ButtonBuilder("backButton", "Back to Menu");
    buttonBuilder.align(ElementBuilder.Align.Right);
    buttonBuilder.interactOnClick("back()");
    final Element buttonPanel = screen.findElementById("buttonPanel");
    if (buttonPanel != null) {
      buttonBuilder.build(newNifty, screen, buttonPanel);
    } else {
      throw new IllegalStateException("Failed to locate button panel.");
    }
  }

  @Override
  public void onStartScreen() {
    System.out.println(screen.debugOutput());
  }

  @Override
  public void onEndScreen() {
  }

  public void back() {
    // this demonstrates how to access selected items
    DropDown dropDown1 = findDropDownControl("dropDown1");
    System.out.println(dropDown1.getSelection());

    DropDown dropDown2 = findDropDownControl("dropDown2");
    System.out.println(dropDown2.getSelection());

    ListBox listBoxStatic = screen.findNiftyControl("listBoxStatic", ListBoxControl.class);
    System.out.println("listBoxStatic selectedItemIndex: " + listBoxStatic.getSelection());

    CheckBox checkBoxControl = screen.findNiftyControl("checkbox", CheckBox.class);
    System.out.println("checkbox: " + checkBoxControl.isChecked());

    // go back to another page
    nifty.fromXml("all/intro.xml", "menu");
  }

  @Nullable
  private DropDown findDropDownControl(final String id) {
    return screen.findNiftyControl(id, DropDown.class);
  }

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "controls/controls.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Controls Demonstation";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
