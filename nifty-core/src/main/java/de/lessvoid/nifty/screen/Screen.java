package de.lessvoid.nifty.screen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyStopwatch;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.NiftyControl;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.tools.NullObjectFactory;
import de.lessvoid.nifty.tools.StringHelper;

/**
 * A single screen with elements and input focus.
 * @author void
 */
public class Screen {
  public int layoutLayersCallCount = 0;
  private static Logger log = Logger.getLogger(Screen.class.getName());

  private String screenId;
  private ScreenController screenController = new NullScreenController();
  private boolean screenControllerBound = false;

  private ArrayList < Element > layerElements = new ArrayList < Element >();
  private LinkedList < Element > layerElementsToAdd = new LinkedList < Element >();
  private LinkedList < Element > layerElementsToRemove = new LinkedList < Element >();

  /**
   * Popup layers are dynamic layers on top of the normal layers.
   * They are treated as "normal" layers and are added to the layerElements variable. In the
   * popupLayer variable we remember the pop ups additionally, so that we can send
   * input events only to these elements when they are present.
   */
  private ArrayList < Element > popupElements = new ArrayList < Element >();
  private LinkedList < Element > popupElementsToAdd = new LinkedList < Element >();
  private LinkedList<ElementWithEndNotify> popupElementsToRemove = new LinkedList < ElementWithEndNotify >();

  private TimeProvider timeProvider;
  private FocusHandler focusHandler;
  private MouseOverHandler mouseOverHandler;
  private Nifty nifty;
  private List < InputHandlerWithMapping > postInputHandlers = new ArrayList < InputHandlerWithMapping >();
  private List < InputHandlerWithMapping > preInputHandlers = new ArrayList < InputHandlerWithMapping >();
  private Element rootElement;
  private String defaultFocusElementId;
  private boolean running = false;
  private Set<String> registeredIds = new HashSet<String>();

  private boolean bound;

  public Screen(
      final Nifty newNifty,
      final String newId,
      final ScreenController newScreenController,
      final TimeProvider newTimeProvider) {
    nifty = newNifty;
    screenId = newId;
    screenController = newScreenController;
    if (screenController == null) {
      log.warning("Missing ScreenController for screen [" + newId + "] using DefaultScreenController() instead but this might not be what you want.");
      screenController = new DefaultScreenController();
    }
    timeProvider = newTimeProvider;
    focusHandler = new FocusHandler();
    mouseOverHandler = new MouseOverHandler();
  }

  public void registerElementId(final String id) {
    if (id != null && registeredIds.contains(id)) {
      log.warning("Possible conflicting id [" + id + "] detected. Consider making all Ids unique or use #id in control-definitions.");
    } else {
      registeredIds.add(id);
    }
  }

  public void unregisterElementId(final String id) {
    registeredIds.remove(id);
  }

  public String getScreenId() {
    return screenId;
  }

  public List<Element> getLayerElements() {
    return layerElements;
  }

  public void addLayerElement(final Element layerElement) {
    layerElementsToAdd.add(layerElement);
  }

  public void removeLayerElement(final Element layerElement) {
    layerElementsToRemove.add(layerElement);
  }

  public void removeLayerElement(final String layerId) {
    for (int i=0; i<layerElements.size(); i++) {
      Element layer = layerElements.get(i);
      if (layer.getId().equals(layerId)) {
        removeLayerElement(layer);
        return;
      }
    }
  }

  public void addPopup(final Element popup, final Element defaultFocusElement) {

    // This enforced all mouse buttons to the released state when a new popup
    // is about to be created. But I can't remember what that was for :)
    //
    // It made problems in the drop down example where the window controls have
    // been used. Since drop down controls are temporarily moved to a popup layer
    // so that they can be moved around above all other stuff this resetEvents()
    // call had one odd side effect: If you would click on a window title bar (to
    // move it around) - in this moment the control is being moved to the popup layer -
    // and then you would release the button this release would never been detected
    // by the window title bar (because the button has already been released by
    // this call to resetEvents() in here). In this case the window got stuck in the
    // popup until you'd press and release the mouse button again.
    //
    // Not calling resetEvents() in here fixes this issue but might break something
    // else although I'm currently not sure what that might be :)
    //
    // nifty.resetEvents();

    // create the callback
    EndNotify localEndNotify = new EndNotify() {
      public final void perform() {
        for (int i=0; i<layerElements.size(); i++) {
          Element w = layerElements.get(i);
          if (w.isEffectActive(EffectEventId.onStartScreen)) {
            return;
          }
        }
      }
    };

    Element mouseFocusElement = focusHandler.getMouseFocusElement();
    if (mouseFocusElement != null) {
      mouseFocusElement.stopEffect(EffectEventId.onHover);
    }

    focusHandler.pushState();

    // prepare pop up for display
    popup.resetEffects();
    popup.layoutElements();
    popup.initControls(true);
    popup.startEffect(EffectEventId.onStartScreen, localEndNotify);
    popup.startEffect(EffectEventId.onActive);
    popup.onStartScreen();
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
    popup.onEndScreen(this);
    nifty.resetMouseInputEvents();
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
    NiftyStopwatch.start();
    running = false;

    focusHandler.resetFocusElements();
    resetLayers();
    layoutLayers();
    bindControls();

    // bind happens right BEFORE the onStartScreen
    if (!screenControllerBound) {
      screenController.bind(nifty, Screen.this);
      screenControllerBound = true;
    }

    // activate the onActive event right now
    activeEffectStart();

    // onStartScreen
    final StartScreenEndNotify endNotify = createScreenStartEndNotify(startScreenEndNotify);
    startLayers(EffectEventId.onStartScreen, endNotify);

    // default focus attribute has been set in onStartScreen
    // event of the elements. so we have to set the default focus
    // here after the onStartScreen is started.
    setDefaultFocus();
    NiftyStopwatch.stop("Screen.startScreen(" + layoutLayersCallCount + ")");
  }

  public void endScreen(final EndNotify callback) {
    resetLayers();
    final EndScreenEndNotify endNotify = createScreenEndNotify(callback);
    startLayers(EffectEventId.onEndScreen, endNotify);
  }

  public void layoutLayers() {
    NiftyStopwatch.start();
    layoutLayersCallCount++;

    for (int i=0; i<layerElements.size(); i++) {
      Element w = layerElements.get(i);
      w.layoutElements();
    }
    NiftyStopwatch.stop("Screen.layoutLayers()");
  }

  private void resetLayers() {
    nifty.resetMouseInputEvents();

    for (int i=0; i<layerElements.size(); i++) {
      Element w = layerElements.get(i);
      w.resetEffects();
      w.reactivate();
    }
  }

  private void startLayers(final EffectEventId effectEventId, final EndNotify endNotify) {
    // create the callback
    LocalEndNotify localEndNotify = new LocalEndNotify(effectEventId, endNotify);

    // start the effect for all layers
    for (int i=0; i<layerElements.size(); i++) {
      Element w = layerElements.get(i);
      w.startEffect(effectEventId, localEndNotify);

      if (effectEventId == EffectEventId.onStartScreen) {
        w.onStartScreen();
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
    for (int i=0; i<layerElements.size(); i++) {
      Element w = layerElements.get(i);
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
    for (int i=0; i<layerElements.size(); i++) {
      Element layer = layerElements.get(i);
      layer.render(renderDevice);
    }
  }

  public void resetLayout() {
    for (int i=0; i<layerElements.size(); i++) {
      Element layer = layerElements.get(i);
      layer.resetLayout();
    }
  }

  /**
   * Handle Mouse Events for this screen. Forwards  the event to the layers.
   * @param inputEvent MouseInputEvent
   * @return true when processed and false when not
   */
  public boolean mouseEvent(final NiftyMouseInputEvent inputEvent) {
    if (log.isLoggable(Level.FINE)) {
      log.fine("screen mouseEvent: " + inputEvent.toString());
    }
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
  private boolean forwardMouseEventToLayers(final List < Element > layerList, final NiftyMouseInputEvent inputEvent) {
    mouseOverHandler.reset();

    long eventTime = timeProvider.getMsTime();

    if (focusHandler.hasAnyElementTheMouseFocus()) {
      Element e = focusHandler.getMouseFocusElement();
      mouseOverHandler.addMouseOverElement(e);
    } else {
      for (int i=0; i<layerList.size(); i++) {
        Element layer = layerList.get(i);
        layer.buildMouseOverElements(inputEvent, eventTime, mouseOverHandler);
      }
    }

    if (log.isLoggable(Level.FINER)) {
      log.fine(mouseOverHandler.getInfoString());
    }

    mouseOverHandler.processMouseOverEvent(rootElement, inputEvent, eventTime);
    mouseOverHandler.processMouseEvent(inputEvent, eventTime);

    return mouseOverHandler.hitsElement();
  }

  /**
   * find an element by name.
   * this method is deprecated, use findElementById() instead
   *
   * @param name the id to find
   * @return the element or null
   *
   * @see Screen#findElementById(java.lang.String)
   */
  @Deprecated
  public Element findElementByName(final String name) {
      return findElementById(name);
  }

  /**
   * find an element by id.
   * @param findId the id to find
   * @return the element or null
   */
  public Element findElementById(final String findId) {
    for (int i=0; i<layerElements.size(); i++) {
      Element layer = layerElements.get(i);
      Element found = layer.findElementById(findId);
      if (found != null) {
        return found;
      }
    }
    return null;
  }

  public < T extends Controller > T findControl(final String elementName, final Class < T > requestedControlClass) {
    Element element = findElementById(elementName);
    if (element == null) {
      return null;
    }
    return element.getControl(requestedControlClass);
  }

  /**
   * Finds the Nifty Control matching the given id and control class on this screen
   * and returns it. If no matching control is found then a Null version of the control
   * will be returned to prevent NullPointerExceptions if you use it directly like:
   * screen.findNiftyControl(...).doStuff(). There is no need to check the return value
   * or catch exceptions. While testing your GUI please take a look at the console log
   * so that you see the error message that is written in case a control can't be found
   * (see also http://en.wikipedia.org/wiki/Null_Object_pattern for details).
   *
   * @param <T> The type of the control class being looked for
   * @param elementName The id of the control
   * @param requestedControlClass The class being looked for
   * @return Either a NiftyControl or a Null version of a NiftyControl for the matching control class.
   */
  public < T extends NiftyControl > T findNiftyControl(final String elementName, final Class < T > requestedControlClass) {
    Element element = findElementById(elementName);
    if (element == null) {
      log.warning("missing element/control with id [" + elementName + "] for requested control class [" + requestedControlClass.getName() + "]");
      return NullObjectFactory.createNull(requestedControlClass);
    }
    return element.getNiftyControl(requestedControlClass);
  }

  /**
   * set alternate key.
   * @param alternateKey alternate key to set
   */
  public void setAlternateKey(final String alternateKey) {
    for (int i=0; i<layerElements.size(); i++) {
      Element layer = layerElements.get(i);
      layer.setAlternateKey(alternateKey);
    }
  }

  /**
   * keyboard event.
   * @param inputEvent keyboard event
   */
  public boolean keyEvent(final KeyboardInputEvent inputEvent) {
    for (int i=0; i<preInputHandlers.size(); i++) {
      InputHandlerWithMapping handler = preInputHandlers.get(i);
      if (handler.process(inputEvent)) {
        return true;
      }
    }
    if (focusHandler.keyEvent(inputEvent)) {
      return true;
    }
    for (int i=0; i<postInputHandlers.size(); i++) {
      InputHandlerWithMapping handler = postInputHandlers.get(i);
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
    postInputHandlers.add(new InputHandlerWithMapping(mapping, handler));
  }

  public void removeKeyboardInputHandler(final KeyInputHandler handler) {
    for (int i=0; i<postInputHandlers.size(); i++) {
      if (postInputHandlers.get(i).getKeyInputHandler().equals(handler)) {
        postInputHandlers.remove(i);
        return;
      }
    }
  }

  /**
   * add a keyboard input handler.
   * @param mapping mapping
   * @param handler new handler to add
   */
  public void addPreKeyboardInputHandler(final NiftyInputMapping mapping, final KeyInputHandler handler) {
    preInputHandlers.add(new InputHandlerWithMapping(mapping, handler));
  }

  public void removePreKeyboardInputHandler(final KeyInputHandler handler) {
    for (int i=0; i<preInputHandlers.size(); i++) {
      if (preInputHandlers.get(i).getKeyInputHandler().equals(handler)) {
        preInputHandlers.remove(i);
        return;
      }
    }
  }

  public String debugOutput() {
    return debugOutput(".*", ".*");
  }

  public String debugOutputFocusElements() {
    StringBuffer result = new StringBuffer();
    result.append(focusHandler.toString());
    return result.toString();
  }

  public String debugOutput(final String regexpElement, final String regexpAttribute) {
    StringBuffer result = new StringBuffer();
    debugOutputLayerElements(regexpElement, regexpAttribute, result, layerElements);
    result.append("\n\n### popupElements: " + popupElements.size());
    debugOutputLayerElements(regexpElement, regexpAttribute, result, popupElements);
    result.append(focusHandler.toString());
    return result.toString();
  }

  private void debugOutputLayerElements(final String regexpElement, final String regexpAttribute, StringBuffer result, ArrayList<Element> layers) {
    for (int i=0; i<layers.size(); i++) {
      Element layer = layers.get(i);
      String layerType = " +";
      if (!layer.isVisible()) {
        layerType = " -";
      }
      result.append(
          "\n" + layerType + getIdText(layer) +
          "\n" + StringHelper.whitespace(layerType.length()) + layer.getElementStateString(StringHelper.whitespace(layerType.length()), regexpAttribute));
      result.append(outputElement(layer, "   ", regexpElement, regexpAttribute));
    }
  }

  public String outputElement(final Element w, final String offset, final String regexpElement, final String regexpAttribute) {
    StringBuffer result = new StringBuffer();
    List<Element> wwElements = w.getElements();
    for (int i=0; i<wwElements.size(); i++) {
      Element ww = wwElements.get(i);
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

    while (!popupElementsToRemove.isEmpty()) {
      ElementWithEndNotify e = popupElementsToRemove.remove(0);
      e.remove();
    }
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
        for (int i=0; i<layerElements.size(); i++) {
          Element w = layerElements.get(i);
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
      log.fine("onStartScreen has ended");

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
      onEndScreenHasEnded();
      if (additionalEndNotify != null) {
        additionalEndNotify.perform();
      }
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

    public KeyInputHandler getKeyInputHandler() {
      return handler;
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
    nifty.subscribeAnnotations(screenController);

    // onStartScreen has ENDED so call the event.
    screenController.onStartScreen();

    // add dynamic controls
    nifty.addControls();
    running = true;
  }

  void onEndScreenHasEnded() {
    nifty.unsubscribeAnnotations(screenController);
    nifty.unsubscribeScreen(this);

    // onEndScreen has ENDED so call the event.
    screenController.onEndScreen();

    for (int i=0; i<layerElements.size(); i++) {
      layerElements.get(i).onEndScreen(this);
    }
  }

  public boolean isEffectActive(final EffectEventId effectEventId) {
    if (!popupElements.isEmpty()) {
      return isEffectActive(popupElements, effectEventId);
    } else {
      return isEffectActive(layerElements, effectEventId);
    }
  }

  private boolean isEffectActive(final List < Element > elements, final EffectEventId effectEventId) {
    for (int i=0; i<elements.size(); i++) {
      Element element = elements.get(i);
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

  public boolean isActivePopup(final String id) {
    for (int i=0; i<popupElements.size(); i++) {
      if (id.equals(popupElements.get(i).getId())) {
        return true;
      }
    }
    return false;
  }

  public boolean isActivePopup(final Element element) {
    return popupElements.contains(element);
  }

  /**
   * Checks if the mouse currently hovers any element that is able to handle mouse events.
   *
   * @return true if the mouse hovers an element that is visibleToMouse and
   *         false if the mouse would hit the background and not any element at all
   */
  public boolean isMouseOverElement() {
    return mouseOverHandler.hitsElement();
  }

  /**
   * This returns an informational String containing all elements that Nifty is aware of that
   * could handle mouse events with the ones currently hovering the mouse sorted from top to
   * bottom.
   * @return String for debug output purpose
   */
  public String getMouseOverInfoString() {
    return mouseOverHandler.getInfoString();
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

  private void bindControls() {
    bound = true;
    for (int i=0; i<layerElements.size(); i++) {
      layerElements.get(i).bindControls(this);
    }
    for (int i=0; i<layerElements.size(); i++) {
      layerElements.get(i).initControls(false);
    }
  }

  public boolean isBound() {
    return bound;
  }

  public void resetMouseDown() {
    for (Element layer : layerElements) {
      layer.resetMouseDown();
    }

  }
}
