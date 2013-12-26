package de.lessvoid.nifty.examples.defaultcontrols.dragndrop;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.controls.dragndrop.builder.DraggableBuilder;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The DragAndDropDialogController.
 * @author void
 */
public class DragAndDropDialogController implements Controller {
  private Nifty nifty;
  private Screen screen;
  @Nullable
  private Button resetButton;
  @Nullable
  private Element chestOpenElement;
  @Nullable
  private Label dragAndDropDescription;

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    this.nifty = nifty;
    this.screen = screen;
    this.resetButton = screen.findNiftyControl("resetButton", Button.class);
    this.chestOpenElement = screen.findElementById("chest-open");
    this.dragAndDropDescription = screen.findNiftyControl("dragAndDropDescription", Label.class);
    resetButton.disable();
  }

  @Override
  public void init(@Nonnull final Parameters parameter) {
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return false;
  }

  @NiftyEventSubscriber(id="chest")
  public void onDrop(final String id, @Nonnull final DroppableDroppedEvent event) {
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
        filename("defaultcontrols/dragndrop/Key.png");
      }});
    }}.build(nifty, screen, screen.findElementById("key-initial#droppableContent"));
  }
}
