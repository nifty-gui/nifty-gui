package de.lessvoid.nifty.controls.console;

import java.util.Properties;

import org.bushe.swing.event.EventTopicSubscriber;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Console;
import de.lessvoid.nifty.controls.ConsoleExecuteCommandEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * A Nifty Control that represents a input console.
 * @author void
 * @deprecated Please use {@link de.lessvoid.nifty.controls.Console} when accessing NiftyControls.
 */
@Deprecated
public class ConsoleControl extends AbstractController implements Console, EventTopicSubscriber<NiftyInputEvent> {
  private Nifty nifty;
  private Screen screen;
  private Element element;
  private ListBox<String> listBox;
  private TextField textfield;

  @SuppressWarnings("unchecked")
  @Override
  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element newElement,
      final Properties properties,
      final Attributes controlDefinitionAttributes) {
    super.bind(newElement);
    this.nifty = niftyParam;
    this.screen = screenParam;
    this.element = newElement;
    this.listBox = element.findNiftyControl("#listBox", ListBox.class);
    this.textfield = element.findNiftyControl("#textInput", TextField.class);
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    nifty.subscribe(screen, textfield.getId(), NiftyInputEvent.class, this);
    initialFill();
  }

  @Override
  public void onStartScreen() {
    element.findElementByName("#textInput").setFocus();
  }

  @Override
  public TextField getTextField() {
    return textfield;
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @Override
  public void onEvent(final String topic, final NiftyInputEvent data) {
    if (data.equals(NiftyInputEvent.SubmitText)) {
      String text = textfield.getText();
      listBox.addItem(text);
      listBox.showItemByIndex(listBox.itemCount() - 1);

      textfield.setText("");
      nifty.publishEvent(element.getId(), new ConsoleExecuteCommandEvent(text));
    }
  }

  @Override
  public void output(final String value) {
    String[] lines = value.split("\n");
    for (String line : lines) {
      listBox.addItem(line);
      listBox.showItemByIndex(listBox.itemCount() - 1);
    }
  }

  @Override
  public String[] getConsoleContent() {
    return listBox.getItems().toArray(new String[0]);
  }

  @Override
  public void clear() {
    listBox.clear();
    initialFill();
  }

  private void initialFill() {
    for (int i=0; i<listBox.getDisplayItemCount(); i++) {
      listBox.addItem("");
    }
  }
}
