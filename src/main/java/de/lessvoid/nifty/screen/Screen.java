package de.lessvoid.nifty.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import de.lessvoid.console.Console;
import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * A single screen with elements and input focus.
 * @author void
 */
public class Screen {

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
   * layer elements parked to add.
   */
  private ArrayList < Element > layerElementsToAdd = new ArrayList < Element >();

  /**
   * layer elements parked to remove.
   */
  private ArrayList < Element > layerElementsToRemove = new ArrayList < Element >();

  /**
   * Popup layers are dynamic layers on top of the normal layers.
   * They are treated as "normal" layers and are added to the layerElements variable. In the
   * popupLayer variable we remember the pop ups additionally, so that we can send
   * input events only to these elements when they are present.
   */
  private ArrayList < Element > popupElements = new ArrayList < Element >();
  private ArrayList < Element > popupElementsToAdd = new ArrayList < Element >();
  private ArrayList < Element > popupElementsToRemove = new ArrayList < Element >();

  /**
   * The TimeProvder.
   */
  private TimeProvider timeProvider;

  /**
   * focus handler.
   */
  private FocusHandler focusHandler;

  /**
   * The MouseOverHandler.
   */
  private MouseOverHandler mouseOverHandler;

  /**
   * nifty.
   */
  private Nifty nifty;

  /**
   * InputMappingWithMapping helper.
   * @author void
   */
  public class InputHandlerWithMapping {
    /**
     * Mapping.
     */
    private NiftyInputMapping mapping;

    /**
     * KeyInputHandler.
     */
    private KeyInputHandler handler;

    /**
     * Create InputHandlerWithMapping.
     * @param newMapping NiftyInputMapping
     * @param newHandler KeyInputHandler
     */
    public InputHandlerWithMapping(
        final NiftyInputMapping newMapping,
        final KeyInputHandler newHandler) {
      mapping = newMapping;
      handler = newHandler;
    }

    /**
     * Process Keyboard InputEvent.
     * @param inputEvent KeyboardInputEvent
     * @return event has been processed or not
     */
    public boolean process(final KeyboardInputEvent inputEvent) {
      return handler.keyEvent(mapping.convert(inputEvent));
    }
  }

  /**
   * key input handlers.
   */
  private List < InputHandlerWithMapping > inputHandlers = new ArrayList < InputHandlerWithMapping >();

  /**
   * root element.
   */
  private Element rootElement;

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
    nifty = newNifty;
    screenId = newId;
    screenController = newScreenController;
    if (screenController == null) {
      screenController = new ScreenController() {
        public void bind(final Nifty niftyParam, final Screen screenParam) {
        }
        public void onStartScreen() {
        }
        public void onEndScreen() {
        }
      };
    }
    timeProvider = newTimeProvider;
    focusHandler = new FocusHandler();
    mouseOverHandler = new MouseOverHandler();
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
    layerElementsToAdd.add(layerElement);
  }

  /**
   * remove Layer element.
   * @param layerElement Element to remove
   */
  public void removeLayerElement(final Element layerElement) {
    layerElementsToRemove.add(layerElement);
  }

  /**
   * Remove layer.
   * @param layerId Layer Id to remove
   */
  public void removeLayerElement(final String layerId) {
    for (Element layer : layerElements) {
      if (layer.getId().equals(layerId)) {
        removeLayerElement(layer);
        return;
      }
    }
  }

  /**
   * add a popup layer.
   * @param popup popup
   */
  public void addPopup(final Element popup, final Element defaultFocusElement) {
    resetLayersMouseDown();

    // create the callback
    EndNotify localEndNotify = new EndNotify() {
      public final void perform() {
        for (Element w : layerElements) {
          if (w.isEffectActive(EffectEventId.onStartScreen)) {
            return;
          }
        }
        if (defaultFocusElement != null) {
          defaultFocusElement.setFocus();
        } else {
          setDefaultFocus();
        }
      }
    };

    focusHandler.pushState();

    // attach screenController to the popup element
    popup.attachPopup(screenController);

    // prepare pop up for display
    popup.resetEffects();
    popup.layoutElements();
    popup.startEffect(EffectEventId.onStartScreen, localEndNotify);
    popup.startEffect(EffectEventId.onActive);
    popup.onStartScreen(this);

    // add to layers and add as popup
    addLayerElement(popup);
    addPopupElement(popup);
  }

  /**
   * Add a Popup Element.
   * @param popup Popup Element
   */
  void addPopupElement(final Element popup) {
    popupElementsToAdd.add(popup);
  }

  /**
   * close popup.
   * @param popup popup to close
   */
  public void closePopup(final Element popup) {
    resetLayers();
    removeLayerElement(popup);
    removePopupElement(popup);
    focusHandler.popState();
  }

  /**
   * Popup to remove.
   * @param popup popup
   */
  void removePopupElement(final Element popup) {
    popupElementsToRemove.add(popup);
  }

  public class StartScreenEndNotify implements EndNotify {
    private boolean bind = false;

    public void perform() {
      if (!bind) {
        screenController.bind(nifty, Screen.this);
        bind = true;
      }
      screenController.onStartScreen();
      nifty.getMouseInputEventQueue().reset();
      setDefaultFocus();
    }

    public boolean isBind() {
      return bind;
    }

    public void bindDone() {
      bind = true;
    }
  }

  /**
   * start the screen.
   */
  public final void startScreen() {
    focusHandler.resetFocusElements();
    resetLayers();
    layoutLayers();

    final StartScreenEndNotify endNotify = new StartScreenEndNotify();
    startLayers(EffectEventId.onStartScreen, endNotify);

    activeEffectStart();
    nifty.addControls();

    if (!endNotify.isBind()) {
      screenController.bind(nifty, this);
      endNotify.bindDone();
    }
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

  private void resetLayersMouseDown() {
    for (Element w : layerElements) {
      w.resetMouseDown();
    }
  }

  private class LocalEndNotify implements EndNotify {
    private boolean enabled = false;
    private EffectEventId effectEventId = null;
    private EndNotify endNotify = null;

    public LocalEndNotify(final EffectEventId effectEventIdParam, final EndNotify endNotifyParam) {
      effectEventId = effectEventIdParam;
      endNotify = endNotifyParam;
    }

    public void enable() {
      enabled = true;
    }

    public void perform() {
      if (enabled) {
        for (Element w : layerElements) {
          if (w.isEffectActive(effectEventId)) {
            return;
          }
        }
        if (endNotify != null) {
          endNotify.perform();
        }
      }
    }
  }

  /**
   * send start screen event to all layer elements.
   * @param effectEventId the effect type id
   * @param endNotify stuff to execute when this stuff here ended
   */
  private void startLayers(final EffectEventId effectEventId, final EndNotify endNotify) {

    // create the callback
    LocalEndNotify localEndNotify = new LocalEndNotify(effectEventId, endNotify);

    // start the effect for all layers
    for (Element w : layerElements) {
      w.startEffect(effectEventId, localEndNotify);

      if (effectEventId == EffectEventId.onStartScreen) {
        w.onStartScreen(this);
      }
    }

    // just in case there was no effect activated, we'll check here, if we're already done
    localEndNotify.enable();
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
   * Handle Mouse Events for this screen. Forwards  the event to the layers.
   * @param inputEvent MouseInputEvent
   * @return true when processed and false when not
   */
  public final boolean mouseEvent(final MouseInputEvent inputEvent) {
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
    mouseOverHandler.reset();

    long eventTime = timeProvider.getMsTime();
    for (Element layer : layerList) {
      layer.buildMouseOverElements(inputEvent, eventTime, mouseOverHandler);
      layer.mouseEvent(inputEvent, eventTime);
    }
    mouseOverHandler.processMouseOverEvent(rootElement, inputEvent, eventTime);
    return false;
  }

  /**
   * find an element by name.
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

  public < T extends Controller > T findControl(
      final String elementName,
      final Class < T > requestedControlClass) {
    Element element = findElementByName(elementName);
    if (element == null) {
      return null;
    }
    return element.getControl(requestedControlClass);
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
   * keyboard event.
   * @param inputEvent keyboard event
   */
  public void keyEvent(final KeyboardInputEvent inputEvent) {
    focusHandler.keyEvent(inputEvent);
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
    console.output("mouse over elements");
    console.output(mouseOverHandler.getInfoString());
    console.output(focusHandler.toString());
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
   * Get RootElement.
   * @return root element
   */
  public Element getRootElement() {
    return rootElement;
  }

  /**
   * Set RootElement.
   * @param rootElementParam new root element
   */
  public void setRootElement(final Element rootElementParam) {
    rootElement = rootElementParam;
  }

  /**
   * Set a new ScreenController.
   * @param newScreenController ScreenController
   */
  public void setScreenController(final ScreenController newScreenController) {
    screenController = newScreenController;
  }

  /**
   * Do things when the current frame has ended.
   */
  public void processAddAndRemoveLayerElements() {
    // add/remove layer elements
    layerElements.addAll(layerElementsToAdd);
    layerElements.removeAll(layerElementsToRemove);
    layerElementsToAdd.clear();
    layerElementsToRemove.clear();

    // add/remove popup elements
    popupElements.addAll(popupElementsToAdd);
    popupElements.removeAll(popupElementsToRemove);
    popupElementsToAdd.clear();
    popupElementsToRemove.clear();
  }
}
