package de.lessvoid.nifty.controls.dropdown;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.controls.listbox.ListBoxControl;
import de.lessvoid.nifty.effects.Effect;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.impl.Move;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.logging.Logger;

public class DropDownPopup<T> extends AbstractController {
  @Nonnull
  private static final Logger log = Logger.getLogger(DropDownPopup.class.getName());
  @Nullable
  private Nifty nifty;
  @Nullable
  private Screen screen;
  @SuppressWarnings("deprecation")
  private DropDownControl<T> dropDownControl;
  private Element popupInstance;

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    super.bind(element);
    this.nifty = nifty;
    this.screen = screen;

    ListBox listBox = element.findNiftyControl("#listBox", ListBox.class);
    if (listBox == null) {
      log.severe("Drop down popup is corrupted. No reference to list box found. Looked for: #listBox");
    }
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return false;
  }

  @SuppressWarnings("deprecation")
  public void setDropDownElement(@Nonnull final DropDownControl<T> dropDownControl, final Element popupInstance) {
    this.dropDownControl = dropDownControl;
    this.popupInstance = popupInstance;
    linkPopupToDropDownPosition(dropDownControl);
  }

  @Override
  @SuppressWarnings("deprecation")
  public void onStartScreen() {
    if (nifty == null || screen == null) {
      log.severe("Control is not bound yet. Can't start the screen for this element.");
    }
    Element element = getElement();
    if (element != null) {
      ListBox listBox = element.findNiftyControl("#listBox", ListBox.class);
      if (listBox != null) {
        String listBoxId = listBox.getId();
        if (listBoxId == null) {
          log.warning("List box has no ID, can't subscribe to events, functionality limited.");
        } else {
          nifty.subscribe(screen, listBoxId, ListBoxSelectionChangedEvent.class,
              new DropDownListBoxSelectionChangedEventSubscriber(nifty, screen, listBox, dropDownControl,
                  popupInstance));
        }
      }
    }
    linkPopupToDropDownPosition(dropDownControl);
    dropDownControl.refresh();
  }

  @SuppressWarnings("deprecation")
  private void linkPopupToDropDownPosition(@Nonnull final DropDownControl<T> dropDownControl) {
    if (nifty == null) {
      log.severe("Control is not bound yet. Can't start the screen for this element.");
    }
    Element element = getElement();
    if (element == null) {
      return;
    }
    Element panel = element.findElementById("#panel");
    if (panel == null) {
      log.severe("Can't find panel of drop down element, linking the popup location is not possible.");
      return;
    }
    Element dropDownElement = dropDownControl.getElement();
    if (dropDownElement == null) {
      return;
    }
    panel.setConstraintX(SizeValue.px(dropDownControl.getElement().getX()));
    panel.setConstraintWidth(SizeValue.px(dropDownControl.getWidth()));
    element.layoutElements();

    ListBoxControl listBox = element.findNiftyControl("#listBox", ListBoxControl.class);
    if (listBox != null) {
      listBox.ensureWidthConstraints();
      int listHeight = listBox.getHeight();

      panel.setConstraintHeight(SizeValue.px(listHeight));

      if ((dropDownControl.getElement().getY() + listHeight) > nifty.getRenderEngine().getHeight()) {
        panel.setConstraintY(SizeValue.px(dropDownControl.getElement().getY() - listHeight));
        updateMoveEffect(panel, listBox, 1);
      } else {
        panel.setConstraintY(SizeValue.px(dropDownControl.getElement().getY() + dropDownControl.getHeight()));
        updateMoveEffect(panel, listBox, -1);
      }
      getElement().layoutElements();
    }
  }

  @SuppressWarnings("deprecation")
  private void updateMoveEffect(
      @Nonnull final Element panel,
      @Nonnull final ListBoxControl listBox,
      final int direction) {
    List<Effect> moveEffects = panel.getEffects(EffectEventId.onStartScreen, Move.class);
    if (!moveEffects.isEmpty()) {
      moveEffects.get(0).getParameters().setProperty("offsetY", String.valueOf(direction * listBox.getHeight()));
      moveEffects.get(0).getParameters().setProperty("mode", "fromOffset");
    }
    moveEffects = panel.getEffects(EffectEventId.onEndScreen, Move.class);
    if (!moveEffects.isEmpty()) {
      moveEffects.get(0).getParameters().setProperty("offsetY", String.valueOf(direction * listBox.getHeight()));
      moveEffects.get(0).getParameters().setProperty("mode", "toOffset");
    }
  }

  public void close() {
    dropDownControl.close();
  }
}
