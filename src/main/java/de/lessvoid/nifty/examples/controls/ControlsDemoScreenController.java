package de.lessvoid.nifty.examples.controls;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.effects.Effect;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.impl.Move;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseClickedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class ControlsDemoScreenController implements ScreenController {
  private static Logger logger = Logger.getLogger(ControlsDemoScreenController.class.getName());
  private Screen screen;

  // This simply maps the IDs of the MenuButton elements to the corresponding Dialog elements we'd
  // like to show with the given MenuButton. This map will make our life a little bit easier when
  // switching between the menus.
  private Map<String, String> buttonToDialogMap = new Hashtable<String, String>();

  // We keep all the button IDs in this list so that we can later decide if an index is before or
  // after the current button.
  private List<String> buttonIdList = new ArrayList<String>();

  // This keeps the current menu button
  private String currentMenuButtonId;

  public ControlsDemoScreenController(final String ... mapping) {
    if (mapping == null || mapping.length == 0 || mapping.length % 2 != 0) {
      logger.warning("expecting pairs of values that map menuButton IDs to dialog IDs");
    } else {
      for (int i=0; i<mapping.length/2; i++) {
        String menuButtonId = mapping[i*2+0];
        String dialogId = mapping[i*2+1];
        buttonToDialogMap.put(menuButtonId, dialogId);
        buttonIdList.add(menuButtonId);
        if (i == 0) {
          currentMenuButtonId = menuButtonId;
        }
      }
    }
  }

  @Override
  public void bind(final Nifty nifty, final Screen screen) {
    this.screen = screen;
  }

  @Override
  public void onStartScreen() {
    screen.findElementByName(buttonToDialogMap.get(currentMenuButtonId)).show();
    screen.findElementByName(currentMenuButtonId).startEffect(EffectEventId.onCustom, null, "selected");
  }

  @Override
  public void onEndScreen() {
  }

  @NiftyEventSubscriber(pattern="menuButton.*")
  public void onMenuButtonListBoxClick(final String id, final NiftyMouseClickedEvent clickedEvent) {
    if (!id.equals(currentMenuButtonId)) {
      int currentIndex = buttonIdList.indexOf(currentMenuButtonId);
      int nextIndex = buttonIdList.indexOf(id);

      Element nextElement = screen.findElementByName(buttonToDialogMap.get(id));
      modifyMoveEffect(EffectEventId.onShow, nextElement, currentIndex < nextIndex ? "right" : "left");
      nextElement.show();

      Element currentElement = screen.findElementByName(buttonToDialogMap.get(currentMenuButtonId));
      modifyMoveEffect(EffectEventId.onHide, currentElement, currentIndex < nextIndex ? "left" : "right");
      currentElement.hide();

      screen.findElementByName(currentMenuButtonId).stopEffect(EffectEventId.onCustom);
      screen.findElementByName(id).startEffect(EffectEventId.onCustom, null, "selected");
      currentMenuButtonId = id;
    }
  }

  private void modifyMoveEffect(final EffectEventId effectEventId, final Element element, final String direction) {
    List<Effect> moveEffects = element.findElementByName("#effectPanel").getEffects(effectEventId, Move.class);
    if (!moveEffects.isEmpty()) {
      moveEffects.get(0).getParameters().put("direction", direction);
    }
  }
}
