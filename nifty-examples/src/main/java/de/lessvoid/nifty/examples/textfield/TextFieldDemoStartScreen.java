package de.lessvoid.nifty.examples.textfield;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.dynamic.PanelCreator;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.textfield.TextFieldControl;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;

/**
 * TextFieldDemoStartScreen.
 *
 * @author void
 */
public class TextFieldDemoStartScreen implements ScreenController, NiftyExample {
  private Nifty nifty;
  private Screen screen;

  @Override
  public void bind(@Nonnull final Nifty newNifty, @Nonnull final Screen newScreen) {
    screen = newScreen;
    nifty = newNifty;

    // dynamically add another Textfield
    Element dynamicParent = screen.findElementById("dynamic-parent");

    PanelCreator panelCreator = new PanelCreator();
    panelCreator.setChildLayout("horizontal");
    panelCreator.setHeight("8px");
    panelCreator.create(newNifty, screen, dynamicParent);

    panelCreator = new PanelCreator();
    panelCreator.setId("bla");
    panelCreator.setChildLayout("horizontal");
    Element row = panelCreator.create(newNifty, screen, dynamicParent);

    final LabelBuilder labelBuilder = new LabelBuilder();
    labelBuilder.text("Dynamic:");
    labelBuilder.width("150px");
    labelBuilder.align(ElementBuilder.Align.Left);
    labelBuilder.textVAlign(ElementBuilder.VAlign.Center);
    labelBuilder.textHAlign(ElementBuilder.Align.Left);
    labelBuilder.build(newNifty, screen, row);

    final TextFieldBuilder textFieldBuilder = new TextFieldBuilder();
    textFieldBuilder.text("Dynamically created TextField");
    textFieldBuilder.build(nifty, screen, row);

    Element element = screen.findElementById("labelName");
    if (element != null) {
      element.getRenderer(TextRenderer.class).setText("Name:");
    }
  }

  @Override
  public void onStartScreen() {
    screen.findNiftyControl("maxLengthTest", TextField.class).setMaxLength(5);
    screen.findElementById("name").setFocus();
    screen.findNiftyControl("name", TextField.class).setCursorPosition(3);
    System.out.println(screen.getFocusHandler().toString());
  }

  @Override
  public void onEndScreen() {
    System.out.println("ip: " + screen.findControl("ip", TextFieldControl.class).getText());
  }

  public void back() {
    System.out.println(screen.findElementById("password").getControl(TextFieldControl.class).getText());
    nifty.fromXml("all/intro.xml", "menu");
  }

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "textfield/textfield.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Textfield Demonstation";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
