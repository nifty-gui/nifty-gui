package de.lessvoid.nifty.examples.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.button.builder.CreateButtonControl;
import de.lessvoid.nifty.controls.checkbox.builder.CreateCheckBoxControl;
import de.lessvoid.nifty.controls.dropdown.builder.CreateDropDownControl;
import de.lessvoid.nifty.controls.dynamic.PanelCreator;
import de.lessvoid.nifty.controls.label.builder.CreateLabelControl;
import de.lessvoid.nifty.controls.listbox.ListBoxControl;
import de.lessvoid.nifty.controls.listbox.builder.CreateListBoxControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class ControlsDemoStartScreen implements ScreenController, NiftyExample {
  private Nifty nifty;
  private Screen screen;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    screen = newScreen;
    nifty = newNifty;

    DropDown dropDown1 = findDropDownControl("dropDown1");
    if (dropDown1 != null) {
      dropDown1.addItem("Nifty GUI");
      dropDown1.addItem("Slick2d");
      dropDown1.addItem("Lwjgl");

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
    for (int i=0; i<5; i++) {
      listBoxStatic.addItem("Listbox Item: " + i);
    }

    // dynamically add another DropDownControl
    Element dynamicParent = screen.findElementByName("dynamic-parent");

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
      CreateLabelControl createLabel = new CreateLabelControl("Dyn. Checkbox:");
      createLabel.setWidth("120px");
      createLabel.setAlign("left");
      createLabel.setTextVAlign("center");
      createLabel.setTextHAlign("left");
      createLabel.create(newNifty, screen, row);

      CreateCheckBoxControl createCheckBoxControl = new CreateCheckBoxControl();
      // this will mess up the tab order to test this =)
      // I bet someone smart will bitch about the out of order tab order tho.
      // IT'S NOT A BUG IT'S A FEATURE DEMONSTRATION! HAH! :P
      createCheckBoxControl.setFocusableInsertBeforeElementId("listBoxStatic");
      createCheckBoxControl.create(nifty, screen, row);

    // create 8px height panel
    createPanel = new PanelCreator();
    createPanel.setChildLayout("horizontal");
    createPanel.setHeight("8px");
    createPanel.create(newNifty, screen, dynamicParent);

    // create new panel for label and drop down
    createPanel = new PanelCreator();
    createPanel.setChildLayout("horizontal");
    row = createPanel.create(newNifty, screen, dynamicParent);

      // create label
      createLabel = new CreateLabelControl("Dynamic:");
      createLabel.setWidth("120px");
      createLabel.setAlign("left");
      createLabel.setTextVAlign("center");
      createLabel.setTextHAlign("left");
      createLabel.create(newNifty, screen, row);

      // create drop down
      CreateDropDownControl dynamicItem = new CreateDropDownControl("dynamicDropDown");
      DropDown dropDown3 = dynamicItem.create(nifty, screen, row);
      if (dropDown3 != null) {
        dropDown3.addItem("dynamic drop down");
        dropDown3.addItem("ftw");
        dropDown3.selectItem("ftw");
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
      createLabel = new CreateLabelControl("Listbox Dyn.:");
      createLabel.setWidth("120px");
      createLabel.setAlign("left");
      createLabel.setTextVAlign("center");
      createLabel.setTextHAlign("left");
      createLabel.create(newNifty, screen, secondRow);

      // create a list box
      CreateListBoxControl dynamicListboxCreate = new CreateListBoxControl("listBoxDynamic");
      dynamicListboxCreate.set("horizontal", "false");
      dynamicListboxCreate.setWidth("*");
      dynamicListboxCreate.setHeight("100%");
      dynamicListboxCreate.set("displayItems", "3");
      dynamicListboxCreate.set("horizontal", "off");
      dynamicListboxCreate.setChildLayout("vertical");

      ListBox dynamicListbox = dynamicListboxCreate.create(nifty, screen, secondRow);
      for (int i=0; i<10; i++) {
        dynamicListbox.addItem("Listbox Item: " + i);
      }

      // select first element
      dynamicListbox.selectItemByIndex(0);

    // dynamically add the backButton for testing purpose
    CreateButtonControl createButton = new CreateButtonControl("backButton");
    createButton.set("label", "Back to Menu");
    createButton.setAlign("right");
    createButton.setInteractOnClick("back()");
    createButton.create(newNifty, screen, screen.findElementByName("buttonPanel"));
  }

  public void onStartScreen() {
    System.out.println(screen.debugOutput());
  }

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

  private DropDown findDropDownControl(final String id) {
    return screen.findNiftyControl(id, DropDown.class);
  }

  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "controls/controls.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty Controls Demonstation";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
