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
  private List<EffectProcessor> effectProcessorList;
  private Falloff hoverFalloff;
  private NiftyRenderDeviceProxy renderDeviceProxy = new NiftyRenderDeviceProxy();
  private String alternateKey;
  private boolean isEmpty = true;
  private RenderPhase renderPhasePre = new RenderPhasePre();
  private RenderPhase renderPhasePost = new RenderPhasePost();
  private RenderPhase renderPhaseOverlay = new RenderPhaseOverlay();
  private RenderStates savedRenderStates = new RenderStates();

  /**
   * create a new effectManager with the given listener.
   */
  public EffectManager(final Notify notify) {
    this.alternateKey = null;

    for (EffectEventId eventId : EffectEventId.values()) {
      effectProcessor.put(eventId, eventId.createEffectProcessor(new NotifyAdapter(eventId, notify)));
    }

    // we'll need to iterate over all effectProcessors later and so we keep them here in an ArrayList
    effectProcessorList = new ArrayList<EffectProcessor>(effectProcessor.values());
  }

  /**
   * register an effect.
   * @param id the id
   * @param e the effect
   */
  public void registerEffect(final EffectEventId id, final Effect e) {
    effectProcessor.get(id).registerEffect(e);
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
    effectProcessor.get(id).activate(listener, alternateKey, null);
  }

  public void startEffect(
      final EffectEventId id,
      final Element w,
      final TimeProvider time,
      final EndNotify listener,
      final String customKey) {
    stopEffect(id);
    effectProcessor.get(id).activate(listener, alternateKey, customKey);
  }

  public void stopEffect(final EffectEventId effectId) {
    effectProcessor.get(effectId).setActive(false);
  }

  /**
   * prepare rendering.
   * @param renderDevice RenderDevice
   */
  public void begin(final NiftyRenderEngine renderDevice, final Element element) {
    savedRenderStates.addAll();
    for (int i=0; i<effectProcessorList.size(); i++) {
      EffectProcessor processor = effectProcessorList.get(i);
      processor.getRenderStatesToSave(renderDeviceProxy);
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

  private void render(final Element element, final NiftyRenderEngine renderEngine, final RenderPhase phase) {
    for (int i=0; i<effectsRenderOrder.length; i++) {
      phase.render(effectProcessor.get(effectsRenderOrder[i]), renderEngine);
    }
  }

  public void renderPre(final NiftyRenderEngine renderEngine, final Element element) {
    render(element, renderEngine, renderPhasePre);
  }

  public void renderPost(final NiftyRenderEngine renderEngine, final Element element) {
    render(element, renderEngine, renderPhasePost);
  }

  public void renderOverlay(final NiftyRenderEngine renderEngine, final Element element) {
    render(element, renderEngine, renderPhaseOverlay);
  }

  /**
   * handle mouse hover effects.
   * @param element the current element
   * @param x mouse x position
   * @param y mouse y position
   */
  public void handleHover(final Element element, final int x, final int y) {
    EffectProcessor processor = effectProcessor.get(EffectEventId.onHover);
    processor.processHover(x, y);
  }

  public void handleHoverStartAndEnd(final Element element, final int x, final int y) {
    EffectProcessor processor = effectProcessor.get(EffectEventId.onStartHover);
    processor.processStartHover(x, y);

    processor = effectProcessor.get(EffectEventId.onEndHover);
    processor.processEndHover(x, y);
}

  public void handleHoverDeactivate(final Element element, final int x, final int y) {
    EffectProcessor processor = effectProcessor.get(EffectEventId.onHover);
    processor.processHoverDeactivate(x, y);
  }

  /**
   * checks if a certain effect is active.
   * @param effectEventId the effectEventId to check
   * @return true, if active, false otherwise
   */
  public final boolean isActive(final EffectEventId effectEventId) {
    return effectProcessor.get(effectEventId).isActive();
  }

  public void reset() {
	// onHover should stay active and is not reset
	// onActive should stay active and is not reset
	// onFocus should stay active and is not reset
  // onLostFocus should stay active and is not reset
  // onClick should stay active and is not reset
    effectProcessor.get(EffectEventId.onStartScreen).reset();
    effectProcessor.get(EffectEventId.onEndScreen).reset();
    effectProcessor.get(EffectEventId.onShow).reset();
    effectProcessor.get(EffectEventId.onHide).reset();
  //  effectProcessor.get(EffectEventId.onCustom).reset();
  }

  public void resetAll() {
    for (int i=0; i<effectsHideShowOrder.length; i++) {
      effectProcessor.get(effectsHideShowOrder[i]).reset();
    }
  }

  public void resetForHide() {
    for (int i=0; i<effectsHideShowOrder.length; i++) {
      effectProcessor.get(effectsHideShowOrder[i]).saveActiveNeverStopRenderingEffects();
    }
  }

  public void restoreForShow() {
    for (int i=0; i<effectsHideShowOrder.length; i++) {
      effectProcessor.get(effectsHideShowOrder[i]).restoreNeverStopRenderingEffects();
    }
  }

  public void resetSingleEffect(final EffectEventId effectEventId) {
    effectProcessor.get(effectEventId).reset();
  }
  
  public void resetSingleEffect(final EffectEventId effectEventId, final String customKey) {
	  effectProcessor.get(effectEventId).reset(customKey);
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
      EffectProcessor processor = effectProcessor.get(eventId);
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
      EffectProcessor processor = effectProcessorList.get(i);
      processor.removeAllEffects();
    }
    isEmpty = true;
  }

  public boolean isEmpty() {
    return isEmpty;
  }

  public <T extends EffectImpl> List<Effect> getEffects(final EffectEventId effectEventId, final Class<T> requestedClass) {
    return effectProcessor.get(effectEventId).getEffects(requestedClass);
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

  private class NotifyAdapter implements EffectProcessor.Notify {
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
}
