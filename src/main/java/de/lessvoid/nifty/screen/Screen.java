package de.lessvoid.nifty.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import de.lessvoid.console.Console;
import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.MouseFocusHandler;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;
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
   * saves focusElements for popups.
   */
  private ArrayList < Element > focusElementBuffer = new ArrayList < Element >();

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
   * nifty.
   */
  private Nifty nifty;

  /**
   * InputMappingWithMapping helper.
   * @author void
   */
  public class InputHandlerWithMapping {
    public NiftyInputMapping mapping;
    public KeyInputHandler handler;
    public InputHandlerWithMapping(
        final NiftyInputMapping newMapping,
        final KeyInputHandler newHandler) {
      mapping = newMapping;
      handler = newHandler;
    }
    public boolean process(final KeyboardInputEvent inputEvent) {
      return handler.keyEvent(mapping.convert(inputEvent));
    }
  }

  /**
   * key input handlers.
   */
  private List < InputHandlerWithMapping > inputHandlers = new ArrayList < InputHandlerWithMapping >();

  /**
   * create new screen instance.
   * @param newNifty nifty
   * @param newId new id for the new screen instance.
   * @param newScreenController the new screen controller for this screen
   * @param newTimeProvider the TimeProvider we should use
   */
  public Screen(
      final Nifty newNifty,
      final String newId,
      final ScreenController newScreenController,
      final TimeProvider newTimeProvider) {
    this.nifty = newNifty;
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
    // create the callback
    EndNotify localEndNotify = new EndNotify() {
      public final void perform() {
        for (Element w : layerElements) {
          if (w.isEffectActive(EffectEventId.onStartScreen)) {
            return;
          }
        }
        setDefaultFocus();
      }
    };

    if (focusHandler != null) {
      focusHandler.pushState();
    }

    // attach screenController to the popup element
    popup.attachPopup(screenController);

    // prepare pop up for display
    popup.resetEffects();
    popup.layoutElements();
    popup.startEffect(EffectEventId.onStartScreen, timeProvider, localEndNotify);
    popup.onStartScreen(nifty, this);

    // add to layers and add as popup
    layerElements.add(popup);
    popupElements.add(popup);

    focusElementBuffer.add(focusElement);
    focusElement = null;
  }

  /**
   * close popup.
   * @param popup popup to close
   */
  public void closePopup(final Element popup) {
    layerElements.remove(popup);
    popupElements.remove(popup);
    focusElement = focusElementBuffer.get(focusElementBuffer.size() - 1);
    focusElementBuffer.remove(focusElementBuffer.size() - 1);
    if (focusHandler != null) {
      focusHandler.popState();
    }
  }

  /**
   * start the screen.
   */
  public final void startScreen() {
    focusElement = null;
    mouseFocusElement = null;
    nifty.getMouseInputEventQueue().reset();
    resetLayers();
    layoutLayers();
    startLayers(
        EffectEventId.onStartScreen,
        new EndNotify() {
          public final void perform() {
            screenController.onStartScreen();
          }
        });
    activeEffectStart();
    nifty.addControls();
    screenController.bind(nifty, this);
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
        w.onStartScreen(nifty, this);
      }
    }

    // just in case there was no effect activated, we'll check here, if we're already done
    localEndNotify.perform();
  }

  /**
   * set default focus.
   */
  public void setDefaultFocus() {
    Element firstFocus = getFocusHandler().getFirstFocusElement();
    if (firstFocus != null) {
      firstFocus.setFocus();
    }
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
   * @param inputEvent MouseInputEvent
   * @return true when processed and false when not
   */
  public final boolean mouseEvent(final MouseInputEvent inputEvent) {
    // when there are popup elements available this event will only travel to these layers!
    if (!popupElements.isEmpty()) {
      return forwardMouseEventToLayers(popupElements, inputEvent);
    } else {
      return forwardMouseEventToLayers(layerElements, inputEvent);
    }
  }

  /**
   * forward mouse event to the given layer list.
   * @param layerList layer list
   * @param inputEvent TODO
   * @return TODO
   */
  private boolean forwardMouseEventToLayers(final List < Element > layerList, final MouseInputEvent inputEvent) {
    boolean eventProcessed = true;
    for (int i = layerList.size() - 1; i >= 0; i--) {
      Element layer = layerList.get(i);
      eventProcessed = eventProcessed & layer.mouseEvent(inputEvent, timeProvider.getMsTime());
    }
    return eventProcessed;
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
      focusElement.stopEffect(EffectEventId.onHover);
      focusElement.stopEffect(EffectEventId.onFocus);
    }

    focusElement = newFocusElement;
    if (focusElement != null) {
      focusElement.startEffect(EffectEventId.onHover, timeProvider, null);
      focusElement.startEffect(EffectEventId.onFocus, timeProvider, null);
    }
  }

  /**
   * lost focus.
   * @param elementThatLostFocus elementThatLostFocus
   */
  public void lostFocus(final Element elementThatLostFocus) {
    if (mouseFocusElement == elementThatLostFocus) {
      log.info("lostFocus: " + elementThatLostFocus.getId());
      mouseFocusElement = null;
    }
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
    for (InputHandlerWithMapping handler : inputHandlers) {
      if (handler.process(inputEvent)) {
        break;
      }
    }
  }

  /**
   * add a keyboard input handler.
   * @param mapping mapping
   * @param handler new handler to add
   */
  public void addKeyboardInputHandler(final NiftyInputMapping mapping, final KeyInputHandler handler) {
    inputHandlers.add(new InputHandlerWithMapping(mapping, handler));
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

  /**
   * Is the focus on the given element.
   * @param element element to check
   * @return true, when the element has the focus and false when not
   */
  public boolean hasFocus(final Element element) {
    return focusElement == element;
  }
}
