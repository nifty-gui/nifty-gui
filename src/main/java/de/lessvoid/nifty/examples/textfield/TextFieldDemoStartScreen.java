package de.lessvoid.nifty.examples.textfield;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.CreateLabel;
import de.lessvoid.nifty.controls.dynamic.CreatePanel;
import de.lessvoid.nifty.controls.textfield.CreateTextFieldControl;
import de.lessvoid.nifty.controls.textfield.controller.TextFieldControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * TextFieldDemoStartScreen.
 * @author void
 */
public class TextFieldDemoStartScreen implements ScreenController {
  private Nifty nifty;
  private Screen screen;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    screen = newScreen;
    nifty = newNifty;

    // dynamically add another Textfield
    Element dynamicParent = screen.findElementByName("dynamic-parent");

    CreatePanel createPanel = new CreatePanel();
    createPanel.setChildLayout("horizontal");
    createPanel.setHeight("8px");
    createPanel.create(newNifty, screen, dynamicParent);

    createPanel = new CreatePanel();
    createPanel.setChildLayout("horizontal");
    Element row = createPanel.create(newNifty, screen, dynamicParent);

    CreateLabel createLabel = new CreateLabel("Dynamic:");
    createLabel.setWidth("150px");
    createLabel.setAlign("left");
    createLabel.setTextVAlign("center");
    createLabel.setTextHAlign("left");
    createLabel.create(newNifty, screen, row);

    CreateTextFieldControl dynamicItem = new CreateTextFieldControl();
    TextFieldControl textFieldControl = dynamicItem.create(nifty, screen, row);
    textFieldControl.setText("Dynamically created TextField");
  }

  public void onStartScreen() {
//    screen.findElementByName("maxLengthTest").getControl(TextFieldControl.class).setMaxLength(5);
//    screen.findElementByName("name").setFocus();
//    screen.findControl("name", TextFieldControl.class).setCursorPosition(3);
  }

  public void onEndScreen() {
  }

  public void back() {
    System.out.println(screen.findElementByName("password").getControl(TextFieldControl.class).getText());
    nifty.fromXml("all/intro.xml", "menu");
  }
}
