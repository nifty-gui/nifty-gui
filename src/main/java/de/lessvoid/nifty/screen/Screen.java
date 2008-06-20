package de.lessvoid.nifty.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import de.lessvoid.console.Console;
import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.MouseFocusHandler;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * A single screen with elements and input focus.
 * @author void
 */
public class Screen implements MouseFocusHandler {

  /**
   * the logger.
   */
  private static Logger log = Logger.getLogger(Screen.class.getName());

  /**
   * screen id of this screen.
   */
  private String screenId;

  /**
   * screen controller that controls this screen.
   */
  private ScreenController screenController = new NullScreenController();

  /**
   * layer elements the root elements of all elements in this screen.
   */
  private ArrayList < Element > layerElements = new ArrayList < Element >();

  /**
   * Popup layers are dynamic layers on top of the normal layers.
   * They are treated as "normal" layers and are added to the layerElements variable. In the
   * popupLayer variable we remember the pop ups additionally, so that we can send
   * input events only to these elements when they are present.
   */
  private ArrayList < Element > popupElements = new ArrayList < Element >();

  /**
   * the current element that has exclusive access to the mouse or null.
   */
  private Element mouseFocusElement;

  /**
   * element that has the focus.
   */
  private Element focusElement;

  /**
   * The TimeProvder.
   */
  private TimeProvider timeProvider;

  /**
   * focus handler.
   */
  private FocusHandler focusHandler;

  /**
   * create new screen instance.
   * @param newId new id for the new screen instance.
   * @param newScreenController the new screen controller for this screen
   * @param newTimeProvider the TimeProvider we should use
   */
  public Screen(final String newId, final ScreenController newScreenController, final TimeProvider newTimeProvider) {
    this.screenId = newId;
    this.screenController = newScreenController;
    this.mouseFocusElement = null;
    this.focusElement = null;
    this.timeProvider = newTimeProvider;
    this.focusHandler = new FocusHandler();
  }

  /**
   * get the screen id.
   * @return the screen id
   */
  public final String getScreenId() {
    return screenId;
  }

  /**
   * get layer elements.
   * @return the list of elements
   */
  public final List < Element > getLayerElements() {
    return layerElements;
  }

  /**
   * add a layer element to this screen.
   * @param layerElement the layer element to add
   */
  public void addLayerElement(final Element layerElement) {
    layerElements.add(layerElement);
  }

  /**
   * add a popup layer.
   * @param popup popup
   */
  public void addPopup(final Element popup) {
    // attach screenController to the popup element
    popup.attachPopup(screenController);

    // prepare popup for display
    popup.resetEffects();
    popup.layoutElements();
    popup.startEffect(EffectEventId.onStartScreen, timeProvider, null);

    // add to layers and add as popup
    layerElements.add(popup);
    popupElements.add(popup);
  }

  /**
   * close popup.
   * @param popup popup to close
   */
  public void closePopup(final Element popup) {
    layerElements.remove(popup);
    popupElements.remove(popup);
  }

  /**
   * start the screen.
   */
  public final void startScreen() {
    log.info("screen [" + getScreenId() + "] start");
    screenController.onStartScreen();

    resetLayers();
    layoutLayers();
    startLayers(
        EffectEventId.onStartScreen,
        new EndNotify() {
          public final void perform() {
            screenController.onStartInteractive();
          }
        });
    activeEffectStart();
  }

  /**
   * end the screen.
   * @param callback to call when the end screen effect finished.
   */
  public final void endScreen(final EndNotify callback) {
    resetLayers();
    startLayers(EffectEventId.onEndScreen, callback);
  }

  /**
   * layout all the layer elements.
   */
  public void layoutLayers() {
    for (Element w : layerElements) {
      w.layoutElements();
    }
  }

  /**
   * reset all the layer elements.
   */
  private void resetLayers() {
    for (Element w : layerElements) {
      w.resetEffects();
    }
  }

  /**
   * send start screen event to all layer elements.
   * @param effectEventId the effect type id
   * @param endNotify stuff to execute when this stuff here ended
   */
  private void startLayers(
      final EffectEventId effectEventId,
      final EndNotify endNotify) {

    // create the callback
    EndNotify localEndNotify = new EndNotify() {
      public final void perform() {
        for (Element w : layerElements) {
          if (w.isEffectActive(effectEventId)) {
            return;
          }
        }
        if (endNotify != null) {
          endNotify.perform();
        }
      }
    };

    // start the effect for all layers
    for (Element w : layerElements) {
      w.startEffect(
          effectEventId,
          timeProvider,
          localEndNotify);

      if (effectEventId == EffectEventId.onStartScreen) {
        w.onStartScreen();
      }
    }

    // just in case there was no effect activated, we'll check here, if we're already done
    localEndNotify.perform();
  }

  /**
   * Start the onActive effect.
   */
  private void activeEffectStart() {
    for (Element w : layerElements) {
      w.startEffect(
        EffectEventId.onActive,
        timeProvider,
        null);
    }
  }

  /**
   * render all layers.
   * @param renderDevice the renderDevice to use
   */
  public final void renderLayers(final NiftyRenderEngine renderDevice) {
    // render all layers
    for (Element layer : layerElements) {
      layer.render(renderDevice);
    }
  }

  /**
   * mouse event for this screen. forwards to the layers.
   * @param x x position of mouse
   * @param y y position of mouse
   * @param leftMouseDown mouse button down
   */
  public final void mouseEvent(final int x, final int y, final boolean leftMouseDown) {
    // when there are popup elements available this event will only travel to these layers!
    if (!popupElements.isEmpty()) {
      forwardMouseEventToLayers(popupElements, x, y, leftMouseDown);
    } else {
      forwardMouseEventToLayers(layerElements, x, y, leftMouseDown);
    }
  }

  /**
   * forward mouse event to the given layer list.
   * @param layerList layer list
   * @param x x position of mouse
   * @param y y position of mouse
   * @param leftMouseDown mouse button down
   */
  private void forwardMouseEventToLayers(
      final List < Element > layerList,
      final int x,
      final int y,
      final boolean leftMouseDown) {
    for (int i = layerList.size() - 1; i >= 0; i--) {
      Element layer = layerList.get(i);
      layer.mouseEvent(x, y, leftMouseDown, timeProvider.getMsTime());
    }
  }

  /**
   * find an element by name.
   *
   * @param name the id to find
   * @return the element or null
   */
  public final Element findElementByName(final String name) {
    for (Element layer : layerElements) {
      Element found = layer.findElementByName(name);
      if (found != null) {
        return found;
      }
    }
    return null;
  }

  /**
   * set alternate key.
   * @param alternateKey alternate key to set
   */
  public void setAlternateKey(final String alternateKey) {
    for (Element layer : layerElements) {
      layer.setAlternateKey(alternateKey);
    }
  }

  /**
   * request exclusive focus.
   * @param newFocusElement element that request focus
   */
  public void requestExclusiveFocus(final Element newFocusElement) {
    log.info("requestExclusiveFocus: " + newFocusElement.getId());

    setFocus(newFocusElement);
    mouseFocusElement = newFocusElement;
  }

  /**
   * set the focus to the given element.
   * @param newFocusElement new focus element
   */
  public void setFocus(final Element newFocusElement) {
    if (focusElement == newFocusElement) {
      return;
    }

    if (focusElement != null) {
      focusElement.stopEffect(EffectEventId.onFocus);
    }

    focusElement = newFocusElement;
    if (focusElement != null) {
      focusElement.startEffect(EffectEventId.onFocus, timeProvider, null);
    }
  }

  /**
   * lost focus.
   * @param elementThatLostFocus elementThatLostFocus
   */
  public void lostFocus(final Element elementThatLostFocus) {
    log.info("lostFocus: " + elementThatLostFocus.getId());
    mouseFocusElement = null;
  }

  /**
   * checks to see if access to mouse event is granted for the given element.
   * @param element element
   * @return true or false
   */
  public boolean canProcessMouseEvents(final Element element) {
    if (mouseFocusElement == null) {
      return true;
    }

    return mouseFocusElement == element;
  }

  /**
   * keyboard event.
   * @param inputEvent keyboard event
   */
  public void keyEvent(final KeyboardInputEvent inputEvent) {
    if (focusElement != null) {
      focusElement.keyEvent(inputEvent);
    }
  }

  /**
   * Debug output to the console.
   * @param console console
   */
  public void debug(final Console console) {
    if (focusElement != null) {
      console.output("focus element: " + focusElement.getId());
    }
  }

  /**
   * get current attached screen controller.
   * @return ScreenController
   */
  public ScreenController getScreenController() {
    return screenController;
  }

  /**
   * get the screens focus handler.
   * @return focus handler
   */
  public FocusHandler getFocusHandler() {
    return focusHandler;
  }
}
