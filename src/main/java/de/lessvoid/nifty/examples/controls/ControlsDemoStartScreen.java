package de.lessvoid.nifty.examples.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.button.CreateButtonControl;
import de.lessvoid.nifty.controls.dropdown.CreateDropDownControl;
import de.lessvoid.nifty.controls.dropdown.controller.DropDownControl;
import de.lessvoid.nifty.controls.dropdown.controller.DropDownControlNotify;
import de.lessvoid.nifty.controls.dynamic.LabelCreator;
import de.lessvoid.nifty.controls.dynamic.PanelCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectAttributes;
import de.lessvoid.nifty.controls.listbox.ListBoxControl;
import de.lessvoid.nifty.effects.EffectType;
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

    PanelCreator createPanel = new PanelCreator();
    createPanel.setChildLayout("horizontal");
    createPanel.setHeight("8px");
    createPanel.create(newNifty, screen, dynamicParent);

    createPanel = new PanelCreator();
    createPanel.setChildLayout("horizontal");
    Element row = createPanel.create(newNifty, screen, dynamicParent);

    LabelCreator createLabel = new LabelCreator("Dynamic:");
    createLabel.setWidth("120px");
    createLabel.setAlign("left");
    createLabel.setTextVAlign("center");
    createLabel.setTextHAlign("left");
    createLabel.create(newNifty, screen, row);

    CreateDropDownControl dynamicItem = new CreateDropDownControl("dynamicDropDown");
    DropDownControl dropDown3 = dynamicItem.create(nifty, screen, row);
    if (dropDown3 != null) {
      dropDown3.addItem("dynamic drop down");
      dropDown3.addItem("ftw");
      dropDown3.setSelectedItem("ftw");
    }

    // dynamically add the backButton for testing purpose
    CreateButtonControl createButton = new CreateButtonControl("backButton");
    createButton.set("label", "Back to Menu");
    createButton.setAlign("right");
    createButton.setInteractOnClick("back()");
    createButton.create(newNifty, screen, screen.findElementByName("buttonPanel"));

    // dynamically populate the Listbox with labels
    Element listBoxDataParent = screen.findElementByName("listBoxDataParent");
    for (int i=0; i<10; i++) {
      createLabel = new LabelCreator("Listbox Item: " + i);
      createLabel.setWidth("100%");
      createLabel.setAlign("left");
      createLabel.setTextVAlign("center");
      createLabel.setTextHAlign("left");
      createLabel.setColor("#000f");

      ControlEffectAttributes effectParam = new ControlEffectAttributes();
      effectParam.setName("imageOverlay");
      effectParam.setAttribute("post", "false");
      effectParam.setAttribute("filename", "textfield/field-selected.png");
      effectParam.setAttribute("imageMode", "resize:1,30,1,1,1,30,1,30,1,30,1,1");
      effectParam.setAttribute("neverStopRendering", "true");
      createLabel.addEffectsOnCustom(effectParam);

      effectParam = new ControlEffectAttributes();
      effectParam.setName("textColor");
      effectParam.setAttribute("color", "#ffff");
      effectParam.setAttribute("post", "false");
      effectParam.setAttribute("neverStopRendering", "true");
      createLabel.addEffectsOnCustom(effectParam);

      effectParam = new ControlEffectAttributes();
      effectParam.setName("updateScrollpanelPositionToDisplayElement");
      effectParam.setAttribute("target", "listBoxPanel");
      effectParam.setAttribute("oneShot", "true");
      createLabel.addEffectsOnCustom(effectParam);

      createLabel.setController("de.lessvoid.nifty.controls.listbox.ListBoxItemController");
      createLabel.setInputMapping("de.lessvoid.nifty.input.mapping.MenuInputMapping");
      createLabel.setVisibleToMouse("true");
      createLabel.create(newNifty, screen, listBoxDataParent);
    }
    ListBoxControl listBox = screen.findControl("listBoxPanel", ListBoxControl.class);
    listBox.changeSelection(0);
  }

  private DropDownControl findDropDownControl(final String id) {
    DropDownControl dropDown1 = screen.findControl(id, DropDownControl.class);
    return dropDown1;
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  public void back() {
    DropDownControl dropDown1 = findDropDownControl("dropDown1");
    System.out.println(dropDown1.getSelectedItemIdx() + ":" + dropDown1.getSelectedItem());
    DropDownControl dropDown2 = findDropDownControl("dropDown2");
    System.out.println(dropDown2.getSelectedItemIdx() + ":" + dropDown2.getSelectedItem());
    nifty.fromXml("all/intro.xml", "menu");
  }

  public void dropDownSelectionChanged(final DropDownControl dropDownControl) {
    System.out.println(
        "changed selection on [" + dropDownControl.toString() + "]"
        + " to [" + dropDownControl.getSelectedItemIdx() + "]"
        + " = [" + dropDownControl.getSelectedItem() + "]");
  }
}
