package de.lessvoid.nifty.controls.console;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import org.bushe.swing.event.EventTopicSubscriber;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * A Nifty Control that represents a input console.
 *
 * @author void
 * @deprecated Please use {@link de.lessvoid.nifty.controls.Console} when accessing NiftyControls.
 */
@Deprecated
public class ConsoleControl extends AbstractController implements Console, EventTopicSubscriber<NiftyInputEvent> {
  @Nonnull
  private static final Logger log = Logger.getLogger(ConsoleControl.class.getName());
  @Nullable
  private Nifty nifty;
  @Nullable
  private Screen screen;
  @Nullable
  private ListBox<String> listBox;
  @Nullable
  private TextField textfield;
  @Nullable
  private Color standardColor = null;
  @Nonnull
  private Color errorColor = new Color("#f00a");

  @SuppressWarnings("unchecked")
  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters properties) {
    super.bind(element);
    this.nifty = nifty;
    this.screen = screen;
    listBox = element.findNiftyControl("#listBox", ListBox.class);
    textfield = element.findNiftyControl("#textInput", TextField.class);

    if (listBox == null) {
      log.severe("Failed to locate the list to show the console log. Console functionality severely limited. Looked " +
          "for: #listBox");
    }
    if (textfield == null) {
      log.severe("Failed to locate the input area of the console. No console input possible. Looked for: #textInput");
    }

    initialFill();
  }

  @Override
  public void init(@Nonnull final Parameters parameter) {
    super.init(parameter);
    if (textfield == null || nifty == null || screen == null) {
      log.severe("The element is not bound or the binding failed.");
    } else {
      String id = textfield.getId();
      if (id == null) {
        log.warning("The text field does not seem to have a proper ID. Event subscribing is not possible. Console " +
            "functions limited.");
      } else {
        nifty.subscribe(screen, id, NiftyInputEvent.class, this);
      }

      Element element = getElement();
      if (element != null) {
        element.getParent().layoutElements();
      }
    }
  }

  @Override
  public void onStartScreen() {
    if (textfield != null) {
      textfield.setFocus();
    }
  }

  @Nullable
  @Override
  public TextField getTextField() {
    return textfield;
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return false;
  }

  @Override
  public void onEvent(final String topic, @Nonnull final NiftyInputEvent data) {
    if (data == NiftyStandardInputEvent.SubmitText) {
      if (textfield != null && listBox != null) {
        String text = textfield.getText();
        listBox.addItem(text);
        listBox.showItemByIndex(listBox.itemCount() - 1);

        textfield.setText("");
        String id = getId();
        if (id != null && nifty != null) {
          nifty.publishEvent(id, new ConsoleExecuteCommandEvent(this, text));
        }
      }
    }
  }

  @Override
  public void output(@Nonnull final String value) {
    out(value, standardColor);
  }

  @Override
  public void output(@Nonnull final String value, @Nullable final Color color) {
    out(value, color);
  }

  @Override
  public void outputError(@Nonnull final String value) {
    out(value, errorColor);
  }

  @Nonnull
  @Override
  public String[] getConsoleContent() {
    if (listBox == null) {
      return new String[0];
    }
    List<String> var = listBox.getItems();
    return var.toArray(new String[var.size()]);
  }

  @Override
  public void clear() {
    if (listBox != null) {
      listBox.clear();
    }
    initialFill();
  }

  @Override
  public void changeColors(@Nullable final Color standardColor, @Nonnull final Color errorColor) {
    this.standardColor = standardColor;
    this.errorColor = errorColor;
  }

  private void initialFill() {
    if (listBox != null) {
      for (int i = 0; i < listBox.getDisplayItemCount(); i++) {
        listBox.addItem("");
      }
    }
  }

  private void out(@Nonnull final String param, @Nullable final Color color) {
    if (listBox == null) {
      return;
    }
    final String value;
    if (nifty == null) {
      value = param;
    } else {
      value = nifty.specialValuesReplace(param);
    }

    String[] lines = value.split("\n");
    List<String> list = new ArrayList<String>(lines.length);
    for (String line : lines) {
      if (color != null) {
        list.add("\\" + color.getColorString() + "#" + line);
      } else {
        list.add(line);
      }
    }
    listBox.addAllItems(list);
    listBox.showItemByIndex(listBox.itemCount() - 1);
  }
}
