package de.lessvoid.nifty.examples.textfield;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.dynamic.PanelCreator;
import de.lessvoid.nifty.controls.label.builder.CreateLabelControl;
import de.lessvoid.nifty.controls.textfield.TextFieldControl;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * TextFieldDemoStartScreen.
 * @author void
 */
public class TextFieldDemoStartScreen implements ScreenController, NiftyExample {
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

    CreateLabelControl labelCreator = new CreateLabelControl("Dynamic:");
    labelCreator.setWidth("150px");
    labelCreator.setAlign("left");
    labelCreator.setTextVAlign("center");
    labelCreator.setTextHAlign("left");
    labelCreator.create(newNifty, screen, row);

    TextFieldCreator textFieldCreator = new TextFieldCreator();
    TextField textField = textFieldCreator.create(nifty, screen, row);
    textField.setText("Dynamically created TextField");

    Element element = screen.findElementByName("labelName");
    element.getRenderer(TextRenderer.class).setText("Name:");
  }

  public void onStartScreen() {
    screen.findNiftyControl("maxLengthTest", TextField.class).setMaxLength(5);
    screen.findElementByName("name").setFocus();
    screen.findNiftyControl("name", TextField.class).setCursorPosition(3);
    System.out.println(screen.getFocusHandler().toString());
  }

  public void onEndScreen() {
    System.out.println("ip: " + screen.findControl("ip", TextFieldControl.class).getText());
  }

  public void back() {
    System.out.println(screen.findElementByName("password").getControl(TextFieldControl.class).getText());
    nifty.fromXml("all/intro.xml", "menu");
  }

  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "textfield/textfield.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty Textfield Demonstation";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
