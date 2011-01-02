package de.lessvoid.nifty.examples.textfield;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.LabelCreator;
import de.lessvoid.nifty.controls.dynamic.PanelCreator;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldCreator;
import de.lessvoid.nifty.controls.textfield.controller.TextFieldControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
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

    PanelCreator panelCreator = new PanelCreator();
    panelCreator.setChildLayout("horizontal");
    panelCreator.setHeight("8px");
    panelCreator.create(newNifty, screen, dynamicParent);

    panelCreator = new PanelCreator();
    panelCreator.setId("bla");
    panelCreator.setChildLayout("horizontal");
    Element row = panelCreator.create(newNifty, screen, dynamicParent);

    LabelCreator labelCreator = new LabelCreator("Dynamic:");
    labelCreator.setWidth("150px");
    labelCreator.setAlign("left");
    labelCreator.setTextVAlign("center");
    labelCreator.setTextHAlign("left");
    labelCreator.create(newNifty, screen, row);

    TextFieldCreator textFieldCreator = new TextFieldCreator();
    TextFieldControl textFieldControl = textFieldCreator.create(nifty, screen, row);
    textFieldControl.setText("Dynamically created TextField");

    // dynamically change a label name
    Element element = screen.findElementByName("labelName");
    element.getRenderer(TextRenderer.class).setText("Name:");
  }

  public void onStartScreen() {
//    screen.findElementByName("maxLengthTest").getControl(TextFieldControl.class).setMaxLength(5);
//    screen.findElementByName("name").setFocus();
//    screen.findControl("name", TextFieldControl.class).setCursorPosition(3);
  }

  public void onEndScreen() {
    System.out.println("ip: " + screen.findControl("ip", TextFieldControl.class).getText());
  }

  public void back() {
    System.out.println(screen.findElementByName("password").getControl(TextFieldControl.class).getText());
    nifty.fromXml("all/intro.xml", "menu");
  }
}
