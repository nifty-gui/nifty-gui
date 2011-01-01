package de.lessvoid.nifty.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.NiftyControl;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * A single screen with elements and input focus.
 * @author void
 */
public class Screen {
  private Logger log = Logger.getLogger(Screen.class.getName());

  private String screenId;
  private ScreenController screenController = new NullScreenController();

  private ArrayList < Element > layerElements = new ArrayList < Element >();
  private ArrayList < Element > layerElementsToAdd = new ArrayList < Element >();
  private ArrayList < Element > layerElementsToRemove = new ArrayList < Element >();

  /**
   * Popup layers are dynamic layers on top of the normal layers.
   * They are treated as "normal" layers and are added to the layerElements variable. In the
   * popupLayer variable we remember the pop ups additionally, so that we can send
   * input events only to these elements when they are present.
   */
  private ArrayList < Element > popupElements = new ArrayList < Element >();
  private ArrayList < Element > popupElementsToAdd = new ArrayList < Element >();
  private ArrayList < ElementWithEndNotify > popupElementsToRemove = new ArrayList < ElementWithEndNotify >();

  private TimeProvider timeProvider;
  private FocusHandler focusHandler;
  private MouseOverHandler mouseOverHandler;
  private Nifty nifty;
  private List < InputHandlerWithMapping > inputHandlers = new ArrayList < InputHandlerWithMapping >();
  private Element rootElement;
  private String defaultFocusElementId;
  private boolean running = false;

  public Screen(
      final Nifty newNifty,
      final String newId,
      final ScreenController newScreenController,
      final TimeProvider newTimeProvider) {
    nifty = newNifty;
    screenId = newId;
    screenController = newScreenController;
    if (screenController == null) {
      log.info("Missing ScreenController for screen [" + newId + "] using DefaultScreenController() instead but this might not be what you want.");
      screenController = new DefaultScreenController();
    }
    timeProvider = newTimeProvider;
    focusHandler = new FocusHandler();
    mouseOverHandler = new MouseOverHandler();
  }

  public final String getScreenId() {
    return screenId;
  }

  public final List < Element > getLayerElements() {
    return layerElements;
  }

  public void addLayerElement(final Element layerElement) {
    layerElementsToAdd.add(layerElement);
  }

  public void removeLayerElement(final Element layerElement) {
    layerElementsToRemove.add(layerElement);
  }

  public void removeLayerElement(final String layerId) {
    for (Element layer : layerElements) {
      if (layer.getId().equals(layerId)) {
        removeLayerElement(layer);
        return;
      }
    }
  }

  public void addPopup(final Element popup, final Element defaultFocusElement) {
    nifty.resetEvents();

    // create the callback
    EndNotify localEndNotify = new EndNotify() {
      public final void perform() {
        for (Element w : layerElements) {
          if (w.isEffectActive(EffectEventId.onStartScreen)) {
            return;
          }
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
    if (defaultFocusElement != null) {
      defaultFocusElement.setFocus();
    } else {
      setDefaultFocus();
    }

    // add to layers and add as popup
    addLayerElement(popup);
    addPopupElement(popup);
  }

  void addPopupElement(final Element popup) {
    popupElementsToAdd.add(popup);
  }

  public void closePopup(final Element popup, final EndNotify closeNotify) {
    resetLayers();
    removeLayerElement(popup);
    schedulePopupElementRemoval(new ElementWithEndNotify(popup, closeNotify));
  }

  private void schedulePopupElementRemoval(final ElementWithEndNotify elementWithEndNotify) {
    popupElementsToRemove.add(elementWithEndNotify);
  }

  public void startScreen() {
    startScreen(null);
  }

  public void startScreen(final EndNotify startScreenEndNotify) {
    running = false;

    focusHandler.resetFocusElements();
    resetLayers();
    layoutLayers();

    // bind happens right BEFORE the onStartScreen
    screenController.bind(nifty, Screen.this);

    // activate the onActive event right now
    activeEffectStart();

    // onStartScreen
    final StartScreenEndNotify endNotify = createScreenStartEndNotify(startScreenEndNotify);
    startLayers(EffectEventId.onStartScreen, endNotify);

    // default focus attribute has been set in onStartScreen
    // event of the elements. so we have to set the default focus
    // here after the onStartScreen is started.
    setDefaultFocus();
  }

  public void endScreen(final EndNotify callback) {
    resetLayers();
    final EndScreenEndNotify endNotify = createScreenEndNotify(callback);
    startLayers(EffectEventId.onEndScreen, endNotify);
  }

  public void layoutLayers() {
    for (Element w : layerElements) {
      w.layoutElements();
    }
  }

  private void resetLayers() {
    nifty.resetEvents();

    for (Element w : layerElements) {
      w.resetEffects();
    }
  }

  private void resetLayersMouseDown() {
    for (Element w : layerElements) {
      w.resetMouseDown();
    }
  }

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

  public void setDefaultFocus() {
    if (focusHandler.getKeyboardFocusElement() != null) {
      return;
    }

    if (defaultFocusElementId != null) {
      Element defaultFocus = getFocusHandler().findElement(defaultFocusElementId);
      if (defaultFocus != null) {
        defaultFocus.setFocus();
        return;
      }
    }

    // fall back to first element
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
      w.startEffect(EffectEventId.onActive, null);

      // in case this element is disabled we will start the disabled effect right here.
      if (!w.isEnabled()) {
        w.startEffect(EffectEventId.onDisabled, null);
      }
    }
  }

  /**
   * render all layers.
   * @param renderDevice the renderDevice to use
   */
  public final void renderLayers(final NiftyRenderEngine renderDevice) {
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
    }

    if (log.isLoggable(Level.FINE)) {
      log.fine(mouseOverHandler.getInfoString());
    }

    mouseOverHandler.processMouseOverEvent(rootElement, inputEvent, eventTime);
    mouseOverHandler.processMouseEvent(inputEvent, eventTime);

    return mouseOverHandler.hitsElement();
  }

  /**
   * find an element by name.
   * @param name the id to find
   * @return the element or null
   */
  public Element findElementByName(final String name) {
    for (Element layer : layerElements) {
      Element found = layer.findElementByName(name);
      if (found != null) {
        return found;
      }
    }
    return null;
  }

  public < T extends Controller > T findControl(final String elementName, final Class < T > requestedControlClass) {
    Element element = findElementByName(elementName);
    if (element == null) {
      return null;
    }
    return element.getControl(requestedControlClass);
  }

  public < T extends NiftyControl > T findNiftyControl(final String elementName, final Class < T > requestedControlClass) {
    Element element = findElementByName(elementName);
    if (element == null) {
      return null;
    }
    return element.getNiftyControl(requestedControlClass);
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
  public boolean keyEvent(final KeyboardInputEvent inputEvent) {
    if (focusHandler.keyEvent(inputEvent)) {
      return true;
    }
    for (InputHandlerWithMapping handler : inputHandlers) {
      if (handler.process(inputEvent)) {
        return true;
      }
    }
    return false;
  }

  /**
   * add a keyboard input handler.
   * @param mapping mapping
   * @param handler new handler to add
   */
  public void addKeyboardInputHandler(final NiftyInputMapping mapping, final KeyInputHandler handler) {
    inputHandlers.add(new InputHandlerWithMapping(mapping, handler));
  }

  public String debugOutput() {
    return debugOutput(".*", ".*");
  }

  public String debugOutput(final String regexpElement, final String regexpAttribute) {
    StringBuffer result = new StringBuffer();
    for (Element layer : getLayerElements()) {
      String layerType = " +";
      if (!layer.isVisible()) {
        layerType = " -";
      }
      result.append(
          "\n" + layerType + getIdText(layer) +
          "\n" + StringHelper.whitespace(layerType.length()) + layer.getElementStateString(StringHelper.whitespace(layerType.length()), regexpAttribute));
      result.append(outputElement(layer, "   ", regexpElement, regexpAttribute));
    }
    result.append(focusHandler.toString());
    return result.toString();
  }

  public String outputElement(final Element w, final String offset, final String regexpElement, final String regexpAttribute) {
    StringBuffer result = new StringBuffer();
    for (Element ww : w.getElements()) {
      String elementId = getIdText(ww);
      if (elementId.matches(regexpElement)) {
        result.append("\n" + offset + elementId + " " + ww.getElementType().getClass().getSimpleName() + " childLayout [" + ww.getElementType().getAttributes().get("childLayout") + "]");  
        result.append("\n" + StringHelper.whitespace(offset.length()) + ww.getElementStateString(StringHelper.whitespace(offset.length()), regexpAttribute));
        result.append(outputElement(ww, offset + " ", ".*", regexpAttribute));
      } else {
        result.append(outputElement(ww, offset + " ", regexpElement, regexpAttribute));
      }
    }
    return result.toString();
  }

  private String getIdText(final Element ww) {
    String id = ww.getId();
    if (id == null) {
      return "[---]";
    } else {
      return "[" + id + "]";
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
    popupElementsToAdd.clear();

    for (ElementWithEndNotify e : popupElementsToRemove) {
      e.remove();
    }
    popupElementsToRemove.clear();
  }

  public boolean hasDynamicElements() {
    if (!layerElementsToAdd.isEmpty() || !layerElementsToRemove.isEmpty() || !popupElementsToAdd.isEmpty() || !popupElementsToRemove.isEmpty()) {
      return true;
    }
    return false;
  }

  public void setDefaultFocusElement(final String defaultFocusElementIdParam) {
    defaultFocusElementId = defaultFocusElementIdParam;
  }

  public String getDefaultFocusElementId() {
    return defaultFocusElementId;
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

  StartScreenEndNotify createScreenStartEndNotify(final EndNotify startScreenEndNotify) {
    return new StartScreenEndNotify(startScreenEndNotify);
  }

  class StartScreenEndNotify implements EndNotify {
    private EndNotify additionalEndNotify;

    public StartScreenEndNotify(final EndNotify additionalEndNotify) {
      this.additionalEndNotify = additionalEndNotify;
    }

    public void perform() {
      log.info("onStartScreen has ended");

      if (additionalEndNotify != null) {
        additionalEndNotify.perform();
      }

      onStartScreenHasEnded();
    }
  }

  EndScreenEndNotify createScreenEndNotify(final EndNotify endScreenEndNotify) {
    return new EndScreenEndNotify(endScreenEndNotify);
  }

  class EndScreenEndNotify implements EndNotify {
    private EndNotify additionalEndNotify;

    public EndScreenEndNotify(final EndNotify additionalEndNotify) {
      this.additionalEndNotify = additionalEndNotify;
    }

    public void perform() {
      Logger.getAnonymousLogger().info("onEndScreen has ended");

      if (additionalEndNotify != null) {
        additionalEndNotify.perform();
      }

      onEndScreenHasEnded();
    }
  }

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

  public boolean isRunning() {
    return running;
  }

  public boolean isNull() {
    return false;
  }

  void onStartScreenHasEnded() {
    // onStartScreen has ENDED so call the event.
    screenController.onStartScreen();

    // add dynamic controls
    nifty.addControls();
    running = true;
  }

  void onEndScreenHasEnded() {
    // onEndScreen has ENDED so call the event.
    screenController.onEndScreen();
  }

  public boolean isEffectActive(final EffectEventId effectEventId) {
    if (!popupElements.isEmpty()) {
      return isEffectActive(popupElements, effectEventId);
    } else {
      return isEffectActive(layerElements, effectEventId);
    }
  }

  private boolean isEffectActive(final List < Element > elements, final EffectEventId effectEventId) {
    for (Element element : elements) {
      if (element.isEffectActive(effectEventId)) {
        return true;
      }
    }
    return false;
  }

  public Element getTopMostPopup() {
    if (popupElements.isEmpty()) {
      return null;
    }
    return popupElements.get(popupElements.size() - 1);
  }

  public class ElementWithEndNotify {
    private Element element;
    private EndNotify closeNotify;

    public ElementWithEndNotify(final Element element, final EndNotify closeNotify) {
      this.element = element;
      this.closeNotify = closeNotify;
    }

    public void remove() {
      popupElements.remove(element);
      focusHandler.popState();
      if (closeNotify != null) {
        closeNotify.perform();
      }
    }
  }
}
