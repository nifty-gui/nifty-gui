package de.lessvoid.nifty.examples.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.button.builder.CreateButtonControl;
import de.lessvoid.nifty.controls.checkbox.CheckboxControl;
import de.lessvoid.nifty.controls.checkbox.builder.CreateCheckBoxControl;
import de.lessvoid.nifty.controls.dropdown.CreateDropDownControl;
import de.lessvoid.nifty.controls.dropdown.controller.DropDownControl;
import de.lessvoid.nifty.controls.dropdown.controller.DropDownControlNotify;
import de.lessvoid.nifty.controls.dynamic.LabelCreator;
import de.lessvoid.nifty.controls.dynamic.PanelCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectAttributes;
import de.lessvoid.nifty.controls.listbox.ListBoxControl;
import de.lessvoid.nifty.controls.listbox.builder.CreateListBoxControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class ControlsDemoStartScreen implements ScreenController, DropDownControlNotify {
  private Nifty nifty;
  private Screen screen;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    screen = newScreen;
    nifty = newNifty;
    DropDownControl dropDown1 = findDropDownControl("dropDown1");
    if (dropDown1 != null) {
      dropDown1.addNotify(this);
      dropDown1.addItem("Nifty GUI");
      dropDown1.addItem("Slick2d");
      dropDown1.addItem("Lwjgl");
      dropDown1.setSelectedItemIdx(0);
    }

    DropDownControl dropDown2 = findDropDownControl("dropDown2");
    if (dropDown2 != null) {
      dropDown2.addNotify(this);
      dropDown2.addItem("rocks!");
      dropDown2.addItem("rules!");
      dropDown2.addItem("kicks ass!");
      dropDown2.addItem("is awesome!");
      dropDown2.addItem("shizzles :D");
      dropDown2.setSelectedItem("rocks!");
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
      LabelCreator createLabel = new LabelCreator("Dyn. Checkbox:");
      createLabel.setWidth("120px");
      createLabel.setAlign("left");
      createLabel.setTextVAlign("center");
      createLabel.setTextHAlign("left");
      createLabel.create(newNifty, screen, row);

      CreateCheckBoxControl createCheckBoxControl = new CreateCheckBoxControl();
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
      createLabel = new LabelCreator("Dynamic:");
      createLabel.setWidth("120px");
      createLabel.setAlign("left");
      createLabel.setTextVAlign("center");
      createLabel.setTextHAlign("left");
      createLabel.create(newNifty, screen, row);

      // create drop down
      CreateDropDownControl dynamicItem = new CreateDropDownControl("dynamicDropDown");
      DropDownControl dropDown3 = dynamicItem.create(nifty, screen, row);
      if (dropDown3 != null) {
        dropDown3.addItem("dynamic drop down");
        dropDown3.addItem("ftw");
        dropDown3.setSelectedItem("ftw");
      }

    // create 8px height panel
    createPanel = new PanelCreator();
    createPanel.setChildLayout("horizontal");
    createPanel.setHeight("8px");
    createPanel.create(newNifty, screen, dynamicParent);

    // create new panel for label and drop down
    createPanel = new PanelCreator();
    createPanel.setChildLayout("horizontal");
    createPanel.setHeight("80px");
    Element secondRow = createPanel.create(newNifty, screen, dynamicParent);

      // create label
      createLabel = new LabelCreator("Listbox Dyn.:");
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
      dynamicListboxCreate.setChildLayout("vertical");
      ListBoxControl dynamicListbox = dynamicListboxCreate.create(nifty, screen, secondRow);
      for (int i=0; i<10; i++) {
        dynamicListbox.addItem("Listbox Item: " + i);
      }
      // you can add elements too :)
      createLabel = new LabelCreator("show off element add");
      createLabel.setStyle("nifty-listbox-item");

      ControlEffectAttributes effectParam = new ControlEffectAttributes();
      effectParam.setName("updateScrollpanelPositionToDisplayElement");
      effectParam.setAttribute("target", "listBoxDynamic");
      effectParam.setAttribute("oneShot", "true");
      createLabel.addEffectsOnCustom(effectParam);

//      Element listBoxDataParent = screen.findElementByName("listBoxDynamicData");
//      dynamicListbox.addElement(createLabel.create(nifty, screen, listBoxDataParent));
//
//      // select first element
//      dynamicListbox.changeSelection(0);

    // dynamically add the backButton for testing purpose
    CreateButtonControl createButton = new CreateButtonControl("backButton");
    createButton.set("label", "Back to Menu");
    createButton.setAlign("right");
    createButton.setInteractOnClick("back()");
    createButton.create(newNifty, screen, screen.findElementByName("buttonPanel"));

    // select first item on the static listbox too
//    ListBoxControl listBoxStatic = screen.findControl("listBoxStatic", ListBoxControl.class);
//    listBoxStatic.changeSelection(0);

    // check the checkbox
//    CheckboxControl checkBoxControl = screen.findControl("checkbox", CheckboxControl.class);
//    checkBoxControl.uncheck();
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  public void back() {
    // this demonstrates how to access selected items
    DropDownControl dropDown1 = findDropDownControl("dropDown1");
    System.out.println(dropDown1.getSelectedItemIdx() + ":" + dropDown1.getSelectedItem());

    DropDownControl dropDown2 = findDropDownControl("dropDown2");
    System.out.println(dropDown2.getSelectedItemIdx() + ":" + dropDown2.getSelectedItem());

//    ListBoxControl listBoxStatic = screen.findControl("listBoxStatic", ListBoxControl.class);
//    System.out.println("listBoxStatic selectedItemIndex: " + listBoxStatic.getSelectedItemIndex() + ", selectedItem: " + listBoxStatic.getSelectedElement());
 
    CheckboxControl checkBoxControl = screen.findControl("checkbox", CheckboxControl.class);
    System.out.println("checkbox: " + checkBoxControl.isChecked());
    
    // go back to another page
    nifty.fromXml("all/intro.xml", "menu");
  }

  public void dropDownSelectionChanged(final DropDownControl dropDownControl) {
    System.out.println(
        "changed selection on [" + dropDownControl.toString() + "]"
        + " to [" + dropDownControl.getSelectedItemIdx() + "]"
        + " = [" + dropDownControl.getSelectedItem() + "]");
  }

  private DropDownControl findDropDownControl(final String id) {
    DropDownControl dropDown1 = screen.findControl(id, DropDownControl.class);
    return dropDown1;
  }
}
