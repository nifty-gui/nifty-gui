package de.lessvoid.nifty.examples.listbox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.LabelBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.PopupBuilder;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.textfield.controller.TextFieldControl;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class ControlsDemoScreenController implements ScreenController {

  private Nifty nifty;
  private Screen screen;

  @Override
  public void bind(final Nifty nifty, final Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  public void addListBoxItem() {
    TextFieldControl addTextField = screen.findControl("addTextField", TextFieldControl.class);
    String newItemContent = addTextField.getText();
    if (newItemContent.length() == 0) {
      showPopup();
      return;
    }
    
    ListBox<JustAnExampleModelClass> listBox = getListBox("listBox");
    listBox.addItem(new JustAnExampleModelClass(newItemContent));

    // make sure that the last item is currently shown
    listBox.showItemByIndex(listBox.itemCount() - 1);
  }

  public void closePopup() {
    nifty.closePopup("test");
  }

  private void showPopup() {
    new PopupBuilder("test") {{
      backgroundColor("#000a");
      childLayoutCenter();
      onStartScreenEffect(new EffectBuilder("fade").length(150).parameter("start", "#0").parameter("end", "#a"));
      onStartScreenEffect(new EffectBuilder("playSound").length(200).startDelay(100).parameter("sound", "popup"));
      onEndScreenEffect(new EffectBuilder("fade").length(150).startDelay(250).parameter("startColor", "#000a").parameter("endColor", "#0000"));
      panel(new PanelBuilder() {{
        childLayoutCenter();
        alignCenter();
        valignCenter();
        style("nifty-panel-red");
        width("45%");
        height("20%");
        onStartScreenEffect(new EffectBuilder("fade").length(100).startDelay(150).parameter("start", "#0").parameter("end", "#f").inherit());
        onStartScreenEffect(new EffectBuilder("imageSize").length(100).startDelay(150).timeType("exp").parameter("factor", "3.5").parameter("startSize", "1.5").parameter("endSize", "1.0").inherit());
        onEndScreenEffect(new EffectBuilder("fade").length(100).startDelay(50).parameter("start", "#f").parameter("end", "#0").inherit());
        onEndScreenEffect(new EffectBuilder("imageSize").length(100).startDelay(50).timeType("exp").parameter("factor", "3.5").parameter("startSize", "1.0").parameter("endSize", "1.5").inherit());
        panel(new PanelBuilder() {{
          childLayoutVertical();
          alignCenter();
          valignCenter();
          onStartScreenEffect(new EffectBuilder("fade").length(50).startDelay(350).parameter("start", "#0").parameter("end", "#f").inherit());
          onEndScreenEffect(new EffectBuilder("fade").length(50).startDelay(0).parameter("start", "#f").parameter("end", "#0").inherit());
          label(new LabelBuilder() {{
            alignCenter();
            text("You'll need to enter some text in the Append field first!");
          }});
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            height("22px");
          }});
          control(new ControlBuilder("button") {{
            alignCenter();
            set("label", "OK, Sorry!");
            interactOnClick("closePopup()");
          }});
        }});
      }});
    }}.build(nifty, screen, null);
    nifty.showPopup(screen, "test", null);
  }

  private ListBox<JustAnExampleModelClass> getListBox(final String id) {
    ListBox<JustAnExampleModelClass> listBox = screen.findNiftyControl(id, ListBox.class);
    return listBox;
  }

  private class JustAnExampleModelClass {
    private String label;

    public JustAnExampleModelClass(final String label) {
      this.label = label;
    }

    public String toString() {
      return label;
    }
  }
}
