package de.lessvoid.nifty.examples.controls.dragndrop;

import java.util.Properties;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.DroppableDroppedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.dragndrop.builder.DraggableBuilder;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The DragAndDropDialogController.
 * @author void
 */
public class DragAndDropDialogController implements Controller {
  private Nifty nifty;
  private Screen screen;
  private Button resetButton;
  private Element chestOpenElement;
  private Label dragAndDropDescription;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    this.nifty = nifty;
    this.screen = screen;
    this.resetButton = screen.findNiftyControl("resetButton", Button.class);
    this.chestOpenElement = screen.findElementByName("chest-open");
    this.dragAndDropDescription = screen.findNiftyControl("dragAndDropDescription", Label.class);
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    resetButton.disable();
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @NiftyEventSubscriber(id="chest")
  public void onDrop(final String id, final DroppableDroppedEvent event) {
    if ("key".equals(event.getDraggable().getId())) {
      event.getDraggable().getElement().markForRemoval();
      chestOpenElement.startEffect(EffectEventId.onCustom, new EndNotify() {
        @Override
        public void perform() {
          chestOpenElement.hide();
          dragAndDropDescription.setText("Well Done!");
          resetButton.enable();
        }
      }, "switchOpen");
    }
  }

  @NiftyEventSubscriber(id="resetButton")
  public void onResetButtonClicked(final String id, final ButtonClickedEvent event) {
    resetButton.disable();
    screen.findNiftyControl("dragAndDropDescription", Label.class).setText("Well Done!");
    dragAndDropDescription.setText("Drop the Key on the Chest to open it.");
    chestOpenElement.show();
    chestOpenElement.stopEffect(EffectEventId.onCustom);
    new DraggableBuilder("key") {{
      childLayoutCenter();
      image(new ImageBuilder() {{
        filename("dragndrop/Key.png");
      }});
    }}.build(nifty, screen, screen.findElementByName("key-initial#droppableContent"));
  }
}
