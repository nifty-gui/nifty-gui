package de.lessvoid.nifty.controls.dropdown;

import java.util.List;
import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.listbox.ListBoxControl;
import de.lessvoid.nifty.effects.Effect;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.impl.Move;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class DropDownPopup<T> extends AbstractController {
  private Nifty nifty;
  private Screen screen;
  @SuppressWarnings("deprecation")
  private DropDownControl<T> dropDownControl;
  private Element popupInstance;

  @Override
  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    super.bind(element);
    this.nifty = niftyParam;
    this.screen = screenParam;
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @SuppressWarnings("deprecation")
  public void setDropDownElement(final DropDownControl<T> dropDownControl, final Element popupInstance) {
    this.dropDownControl = dropDownControl;
    this.popupInstance = popupInstance;
    linkPopupToDropDownPosition(dropDownControl);
  }

  @Override
  @SuppressWarnings("deprecation")
  public void onStartScreen() {
    @SuppressWarnings("rawtypes")
    final ListBox listBox = getElement().findNiftyControl("#listBox", ListBoxControl.class);
    nifty.subscribe(screen, listBox.getId(), ListBoxSelectionChangedEvent.class,
        new DropDownListBoxSelectionChangedEventSubscriber(nifty, screen, listBox, dropDownControl, popupInstance));
    linkPopupToDropDownPosition(dropDownControl);
    dropDownControl.refresh();
  }

  @SuppressWarnings({"deprecation", "rawtypes"})
  private void linkPopupToDropDownPosition(final DropDownControl<T> dropDownControl) {
    Element panel = getElement().findElementByName("#panel");
    panel.setConstraintX(new SizeValue(dropDownControl.getElement().getX() + "px"));
    panel.setConstraintWidth(new SizeValue(dropDownControl.getWidth() + "px"));
    getElement().layoutElements();

    ListBoxControl listBox = getElement().findNiftyControl("#listBox", ListBoxControl.class);
    listBox.ensureWidthConstraints();

    panel.setConstraintHeight(new SizeValue(listBox.getHeight() + "px"));

    if ((dropDownControl.getElement().getY() + listBox.getHeight()) > nifty.getRenderEngine().getHeight()) {
      panel.setConstraintY(new SizeValue(dropDownControl.getElement().getY() - listBox.getHeight() + "px"));
      updateMoveEffect(listBox, 1);
    } else {
      panel.setConstraintY(new SizeValue(dropDownControl.getElement().getY() + dropDownControl.getHeight() + "px"));
      updateMoveEffect(listBox, -1);
    }
    getElement().layoutElements();
  }

  @SuppressWarnings({"deprecation", "rawtypes"})
  private void updateMoveEffect(final ListBoxControl listBox, final int direction) {
    List<Effect> moveEffects = getElement().findElementByName("#panel").getEffects(EffectEventId.onStartScreen,
        Move.class);
    if ((moveEffects != null) && !moveEffects.isEmpty()) {
      moveEffects.get(0).getParameters().setProperty("offsetY", String.valueOf(direction * listBox.getHeight()));
      moveEffects.get(0).getParameters().setProperty("mode", "fromOffset");
    }
    moveEffects = getElement().findElementByName("#panel").getEffects(EffectEventId.onEndScreen, Move.class);
    if ((moveEffects != null) && !moveEffects.isEmpty()) {
      moveEffects.get(0).getParameters().setProperty("offsetY", String.valueOf(direction * listBox.getHeight()));
      moveEffects.get(0).getParameters().setProperty("mode", "toOffset");
    }
  }

  @SuppressWarnings("deprecation")
  public void close() {
    dropDownControl.close();
  }
}
