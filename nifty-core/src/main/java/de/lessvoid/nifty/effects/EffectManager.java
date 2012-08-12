package de.lessvoid.nifty.effects;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStates;
import de.lessvoid.nifty.spi.time.TimeProvider;

/**
 * manage all effects of an element.
 * @author void
 */
public class EffectManager {
  // define the order of effects as they are rendered later
  private static final EffectEventId[] effectsRenderOrder = new EffectEventId[] {
      EffectEventId.onShow,
      EffectEventId.onHide,
      EffectEventId.onStartScreen,
      EffectEventId.onEndScreen,
      EffectEventId.onCustom,
      EffectEventId.onActive,
      EffectEventId.onHover,
      EffectEventId.onStartHover,
      EffectEventId.onEndHover,
      EffectEventId.onFocus,
      EffectEventId.onLostFocus,
      EffectEventId.onGetFocus,
      EffectEventId.onClick,
      EffectEventId.onEnabled,
      EffectEventId.onDisabled
  };

  // define the order of effects as they are called for hide/show/reset things
  private static final EffectEventId[] effectsHideShowOrder = new EffectEventId[] {
    EffectEventId.onStartScreen,
    EffectEventId.onEndScreen,
    EffectEventId.onShow,
    EffectEventId.onHide,
    EffectEventId.onCustom,
    EffectEventId.onHover,
    EffectEventId.onStartHover,
    EffectEventId.onEndHover,
    // onActive is currently used by the nifty-panel style. when we reset that effect here
    // we would not be able to use the nifty-panel in popups. when a popup is being closed
    // all effects will be reset. which makes sense but probably not for the onActive effect.
    // we need to check later if this uncommenting has any bad influence on other controls.
    //
    //  EffectEventId.onActive
    EffectEventId.onFocus,
    EffectEventId.onLostFocus,
    EffectEventId.onGetFocus,
    EffectEventId.onClick
  };

  private Map<EffectEventId, EffectProcessor> effectProcessor = new EnumMap<EffectEventId, EffectProcessor>(EffectEventId.class);
  private List<EffectProcessor> effectProcessorList = new ArrayList<EffectProcessor>(0);
  private Falloff hoverFalloff;
  private String alternateKey;
  private boolean isEmpty = true;
  private Notify notify;

  // we're not multi-threaded so we can use static in here to save memory allocation when creating lots of elements
  private static NiftyRenderDeviceProxy renderDeviceProxy = new NiftyRenderDeviceProxy();
  private static RenderPhase renderPhasePre = new RenderPhasePre();
  private static RenderPhase renderPhasePost = new RenderPhasePost();
  private static RenderPhase renderPhaseOverlay = new RenderPhaseOverlay();
  private static EffectProcessorNull effectProcessorNull = new EffectProcessorNull();
  private static RenderStates savedRenderStates = new RenderStates();

  /**
   * create a new effectManager with the given listener.
   */
  public EffectManager(final Notify notify) {
    this.alternateKey = null;
    this.notify = notify;
  }

  /**
   * register an effect.
   * @param id the id
   * @param e the effect
   */
  public void registerEffect(final EffectEventId id, final Effect e) {
    EffectProcessor processor = effectProcessor.get(id);
    if (processor == null) {
      processor = id.createEffectProcessor(new NotifyAdapter(id, notify));
      effectProcessor.put(id, processor);
      effectProcessorList.add(processor);
    }
    processor.registerEffect(e);
    isEmpty = false;
  }

  /**
   * start all effects with the given id for the given element.
   * @param id the effect id to start
   * @param w the element
   * @param time TimeProvider
   * @param listener the {@link EndNotify} to use.
   */
  public void startEffect(
      final EffectEventId id,
      final Element w,
      final TimeProvider time,
      final EndNotify listener) {
    getEffectProcessor(id).activate(listener, alternateKey, null);
  }

  public void startEffect(
      final EffectEventId id,
      final Element w,
      final TimeProvider time,
      final EndNotify listener,
      final String customKey) {
    stopEffect(id);
    getEffectProcessor(id).activate(listener, alternateKey, customKey);
  }

  public void stopEffect(final EffectEventId effectId) {
    getEffectProcessor(effectId).setActive(false);
  }

  /**
   * prepare rendering.
   * @param renderDevice RenderDevice
   */
  public void begin(final NiftyRenderEngine renderDevice, final Element element) {
    savedRenderStates.addAll();
    for (int i=0; i<effectProcessorList.size(); i++) {
      effectProcessorList.get(i).getRenderStatesToSave(renderDeviceProxy);
      savedRenderStates.removeAll(renderDeviceProxy.getStates());
    }
    renderDevice.saveState(savedRenderStates);
  }

  /**
   * finish rendering.
   * @param renderDevice RenderDevice
   */
  public void end(final NiftyRenderEngine renderDevice) {
    renderDevice.restoreState();
  }

  public void renderPre(final NiftyRenderEngine renderEngine, final Element element) {
    renderInternal(element, renderEngine, renderPhasePre);
  }

  public void renderPost(final NiftyRenderEngine renderEngine, final Element element) {
    renderInternal(element, renderEngine, renderPhasePost);
  }

  public void renderOverlay(final NiftyRenderEngine renderEngine, final Element element) {
    renderInternal(element, renderEngine, renderPhaseOverlay);
  }

  private void renderInternal(final Element element, final NiftyRenderEngine renderEngine, final RenderPhase phase) {
    for (int i=0; i<effectsRenderOrder.length; i++) {
      phase.render(getEffectProcessor(effectsRenderOrder[i]), renderEngine);
    }
  }

  /**
   * handle mouse hover effects.
   * @param element the current element
   * @param x mouse x position
   * @param y mouse y position
   */
  public void handleHover(final Element element, final int x, final int y) {
    getEffectProcessor(EffectEventId.onHover).processHover(x, y);
  }

  public void handleHoverStartAndEnd(final Element element, final int x, final int y) {
    EffectProcessor processor = getEffectProcessor(EffectEventId.onStartHover);
    processor.processStartHover(x, y);

    processor = getEffectProcessor(EffectEventId.onEndHover);
    processor.processEndHover(x, y);
}

  public void handleHoverDeactivate(final Element element, final int x, final int y) {
    getEffectProcessor(EffectEventId.onHover).processHoverDeactivate(x, y);
  }

  /**
   * checks if a certain effect is active.
   * @param effectEventId the effectEventId to check
   * @return true, if active, false otherwise
   */
  public final boolean isActive(final EffectEventId effectEventId) {
    return getEffectProcessor(effectEventId).isActive();
  }

  public void reset() {
	// onHover should stay active and is not reset
	// onActive should stay active and is not reset
	// onFocus should stay active and is not reset
  // onLostFocus should stay active and is not reset
  // onClick should stay active and is not reset
    getEffectProcessor(EffectEventId.onStartScreen).reset();
    getEffectProcessor(EffectEventId.onEndScreen).reset();
    getEffectProcessor(EffectEventId.onShow).reset();
    getEffectProcessor(EffectEventId.onHide).reset();
  //  effectProcessor.get(EffectEventId.onCustom).reset();
  }

  public void resetAll() {
    for (int i=0; i<effectsHideShowOrder.length; i++) {
      getEffectProcessor(effectsHideShowOrder[i]).reset();
    }
  }

  public void resetForHide() {
    for (int i=0; i<effectsHideShowOrder.length; i++) {
      getEffectProcessor(effectsHideShowOrder[i]).saveActiveNeverStopRenderingEffects();
    }
  }

  public void restoreForShow() {
    for (int i=0; i<effectsHideShowOrder.length; i++) {
      getEffectProcessor(effectsHideShowOrder[i]).restoreNeverStopRenderingEffects();
    }
  }

  public void resetSingleEffect(final EffectEventId effectEventId) {
    getEffectProcessor(effectEventId).reset();
  }
  
  public void resetSingleEffect(final EffectEventId effectEventId, final String customKey) {
	  getEffectProcessor(effectEventId).reset(customKey);
  }

  /**
   * set the alternate key.
   * @param newAlternateKey alternate key
   */
  public void setAlternateKey(final String newAlternateKey) {
    this.alternateKey = newAlternateKey;
  }

  /**
   * get state string.
   * @param offset offset
   * @return String with state information
   */
  public String getStateString(final String offset) {
    StringBuffer data = new StringBuffer();

    int activeProcessors = 0;
    for (EffectEventId eventId : effectProcessor.keySet()) {
      EffectProcessor processor = getEffectProcessor(eventId);
      if (processor.isActive()) {
        activeProcessors++;

        data.append(offset);
        data.append("  {" + eventId.toString() + "} ");
        data.append(processor.getStateString());
      }
    }

    if (activeProcessors == 0) {
      return offset + "{}";
    } else {
      return data.toString();
    }
  }

  public void setFalloff(final Falloff newFalloff) {
    hoverFalloff = newFalloff;
  }

  public Falloff getFalloff() {
    return hoverFalloff;
  }

  public void removeAllEffects() {
    for (int i=0; i<effectProcessorList.size(); i++) {
      effectProcessorList.get(i).removeAllEffects();
    }
    isEmpty = true;
  }

  public boolean isEmpty() {
    return isEmpty;
  }

  public <T extends EffectImpl> List<Effect> getEffects(final EffectEventId effectEventId, final Class<T> requestedClass) {
    return getEffectProcessor(effectEventId).getEffects(requestedClass);
  }

  interface RenderPhase {
    public void render(EffectProcessor effectProcessor,  NiftyRenderEngine renderEngine);
  }

  private final static class RenderPhasePre implements RenderPhase {
    public void render(final EffectProcessor processor, final NiftyRenderEngine renderEngine) {
      processor.renderPre(renderEngine);
    }
  }

  private final static class RenderPhasePost implements RenderPhase {
    public void render(final EffectProcessor processor, final NiftyRenderEngine renderEngine) {
      processor.renderPost(renderEngine);
    }
  }

  private final static class RenderPhaseOverlay implements RenderPhase {
    public void render(final EffectProcessor processor, final NiftyRenderEngine renderEngine) {
      processor.renderOverlay(renderEngine);
    }
  }

  public interface Notify {
    void effectStateChanged(EffectEventId eventId, boolean active);
  }

  private class NotifyAdapter implements EffectProcessorImpl.Notify {
    private Notify notify;
    private EffectEventId eventId;

    public NotifyAdapter(final EffectEventId eventId, final Notify notify) {
      this.eventId = eventId;
      this.notify = notify;
    }

    @Override
    public void effectProcessorStateChanged(final boolean active) {
      notify.effectStateChanged(eventId, active);
    }
  }

  private EffectProcessor getEffectProcessor(final EffectEventId id) {
    EffectProcessor processor = effectProcessor.get(id);
    if (processor == null) {
      return effectProcessorNull;
    }
    return processor;
  }
}
