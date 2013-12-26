package de.lessvoid.nifty.effects;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStates;
import de.lessvoid.nifty.spi.time.TimeProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * manage all effects of an element.
 *
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

  @Nonnull
  private final Map<EffectEventId, EffectProcessor> effectProcessor = new EnumMap<EffectEventId,
      EffectProcessor>(EffectEventId.class);
  @Nonnull
  private final List<EffectProcessor> effectProcessorList = new ArrayList<EffectProcessor>(0);
  @Nullable
  private Falloff hoverFalloff;
  @Nullable
  private String alternateKey;
  private boolean isEmpty = true;
  @Nonnull
  private final Notify notify;

  // we're not multi-threaded so we can use static in here to save memory allocation when creating lots of elements
  @Nonnull
  private static final NiftyRenderDeviceProxy renderDeviceProxy = new NiftyRenderDeviceProxy();
  @Nonnull
  private static final RenderPhase renderPhasePre = new RenderPhasePre();
  @Nonnull
  private static final RenderPhase renderPhasePost = new RenderPhasePost();
  @Nonnull
  private static final RenderPhase renderPhaseOverlay = new RenderPhaseOverlay();
  @Nonnull
  private static final RenderStates savedRenderStates = new RenderStates();

  /**
   * create a new effectManager with the given listener.
   */
  public EffectManager(@Nonnull final Notify notify) {
    this.alternateKey = null;
    this.notify = notify;
  }

  /**
   * register an effect.
   *
   * @param id the id
   * @param e  the effect
   */
  public void registerEffect(@Nonnull final EffectEventId id, @Nonnull final Effect e) {
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
   *
   * @param id       the effect id to start
   * @param w        the element
   * @param time     TimeProvider
   * @param listener the {@link EndNotify} to use.
   */
  public void startEffect(
      @Nonnull final EffectEventId id,
      @Nonnull final Element w,
      @Nonnull final TimeProvider time,
      @Nullable final EndNotify listener) {
    startEffect(id, w, time, listener, null);
  }

  public void startEffect(
      @Nonnull final EffectEventId id,
      @Nonnull final Element w,
      @Nonnull final TimeProvider time,
      @Nullable final EndNotify listener,
      @Nullable final String customKey) {
    stopEffect(id);
    EffectProcessor processor = getEffectProcessor(id);
    if (processor != null) {
      processor.activate(listener, alternateKey, customKey);
    }
  }

  public void stopEffect(@Nonnull final EffectEventId effectId) {
    EffectProcessor processor = getEffectProcessor(effectId);
    if (processor != null) {
      processor.setActive(false);
    }
  }

  /**
   * prepare rendering.
   *
   * @param renderDevice RenderDevice
   */
  public void begin(@Nonnull final NiftyRenderEngine renderDevice, @Nonnull final Element element) {
    savedRenderStates.addAll();
    for (int i = 0; i < effectProcessorList.size(); i++) {
      effectProcessorList.get(i).getRenderStatesToSave(renderDeviceProxy);
      savedRenderStates.removeAll(renderDeviceProxy.getStates());
    }
    renderDevice.saveState(savedRenderStates);
  }

  /**
   * finish rendering.
   *
   * @param renderDevice RenderDevice
   */
  public void end(@Nonnull final NiftyRenderEngine renderDevice) {
    renderDevice.restoreState();
  }

  public void renderPre(@Nonnull final NiftyRenderEngine renderEngine, final Element element) {
    renderInternal(renderEngine, renderPhasePre);
  }

  public void renderPost(@Nonnull final NiftyRenderEngine renderEngine, final Element element) {
    renderInternal(renderEngine, renderPhasePost);
  }

  public void renderOverlay(@Nonnull final NiftyRenderEngine renderEngine, final Element element) {
    renderInternal(renderEngine, renderPhaseOverlay);
  }

  private void renderInternal(
      @Nonnull final NiftyRenderEngine renderEngine,
      @Nonnull final RenderPhase phase) {
    for (int i = 0; i < effectsRenderOrder.length; i++) {
      EffectProcessor processor = getEffectProcessor(effectsRenderOrder[i]);
      if (processor != null) {
        phase.render(processor, renderEngine);
      }
    }
  }

  /**
   * handle mouse hover effects.
   *
   * @param element the current element
   * @param x       mouse x position
   * @param y       mouse y position
   */
  public void handleHover(final Element element, final int x, final int y) {
    EffectProcessor processor = getEffectProcessor(EffectEventId.onHover);
    if (processor != null) {
      processor.processHover(x, y);
    }
  }

  public void handleHoverStartAndEnd(final Element element, final int x, final int y) {
    EffectProcessor processor = getEffectProcessor(EffectEventId.onStartHover);
    if (processor != null) {
      processor.processStartHover(x, y);
    }

    processor = getEffectProcessor(EffectEventId.onEndHover);
    if (processor != null) {
      processor.processEndHover(x, y);
    }
  }

  public void handleHoverDeactivate(final Element element, final int x, final int y) {
    EffectProcessor processor = getEffectProcessor(EffectEventId.onHover);
    if (processor != null) {
      processor.processHoverDeactivate(x, y);
    }
  }

  /**
   * checks if a certain effect is active.
   *
   * @param effectEventId the effectEventId to check
   * @return true, if active, false otherwise
   */
  public final boolean isActive(@Nonnull final EffectEventId effectEventId) {
    EffectProcessor processor = getEffectProcessor(effectEventId);
    if (processor == null) {
      return false;
    }
    return processor.isActive();
  }

  public void reset() {
    // onHover should stay active and is not reset
    // onActive should stay active and is not reset
    // onFocus should stay active and is not reset
    // onLostFocus should stay active and is not reset
    // onClick should stay active and is not reset
    resetSingleEffect(EffectEventId.onStartScreen);
    resetSingleEffect(EffectEventId.onEndScreen);
    resetSingleEffect(EffectEventId.onShow);
    resetSingleEffect(EffectEventId.onHide);
    //  effectProcessor.get(EffectEventId.onCustom).reset();
  }

  public void resetAll() {
    for (int i = 0; i < effectsHideShowOrder.length; i++) {
      resetSingleEffect(effectsHideShowOrder[i]);
    }
  }

  public void resetForHide() {
    for (int i = 0; i < effectsHideShowOrder.length; i++) {
      EffectProcessor processor = getEffectProcessor(effectsHideShowOrder[i]);
      if (processor != null) {
        processor.saveActiveNeverStopRenderingEffects();
      }
    }
  }

  public void restoreForShow() {
    for (int i = 0; i < effectsHideShowOrder.length; i++) {
      EffectProcessor processor = getEffectProcessor(effectsHideShowOrder[i]);
      if (processor != null) {
        processor.restoreNeverStopRenderingEffects();
      }
    }
  }

  public void resetSingleEffect(@Nonnull final EffectEventId effectEventId) {
    EffectProcessor processor = getEffectProcessor(effectEventId);
    if (processor != null) {
      processor.reset();
    }
  }

  public void resetSingleEffect(@Nonnull final EffectEventId effectEventId, @Nonnull final String customKey) {
    EffectProcessor processor = getEffectProcessor(effectEventId);
    if (processor != null) {
      processor.reset(customKey);
    }
  }

  /**
   * set the alternate key.
   *
   * @param newAlternateKey alternate key
   */
  public void setAlternateKey(@Nullable final String newAlternateKey) {
    this.alternateKey = newAlternateKey;
  }

  /**
   * get state string.
   *
   * @param offset offset
   * @return String with state information
   */
  @Nonnull
  public String getStateString(final String offset) {
    StringBuilder data = new StringBuilder();

    int activeProcessors = 0;
    for (EffectEventId eventId : effectProcessor.keySet()) {
      EffectProcessor processor = getEffectProcessor(eventId);
      if (processor != null && processor.isActive()) {
        activeProcessors++;

        data.append(offset);
        data.append("  {").append(eventId.toString()).append("} ");
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

  @Nullable
  public Falloff getFalloff() {
    return hoverFalloff;
  }

  public void removeAllEffects() {
    for (int i = 0; i < effectProcessorList.size(); i++) {
      effectProcessorList.get(i).removeAllEffects();
    }
    isEmpty = true;
  }

  public boolean isEmpty() {
    return isEmpty;
  }

  @Nonnull
  public <T extends EffectImpl> List<Effect> getEffects(
      @Nonnull final EffectEventId effectEventId,
      @Nonnull final Class<T> requestedClass) {
    EffectProcessor processor = getEffectProcessor(effectEventId);
    if (processor == null) {
      return Collections.emptyList();
    }
    return processor.getEffects(requestedClass);
  }

  interface RenderPhase {
    public void render(@Nonnull EffectProcessor effectProcessor, @Nonnull NiftyRenderEngine renderEngine);
  }

  private final static class RenderPhasePre implements RenderPhase {
    @Override
    public void render(@Nonnull final EffectProcessor processor, @Nonnull final NiftyRenderEngine renderEngine) {
      processor.renderPre(renderEngine);
    }
  }

  private final static class RenderPhasePost implements RenderPhase {
    @Override
    public void render(@Nonnull final EffectProcessor processor, @Nonnull final NiftyRenderEngine renderEngine) {
      processor.renderPost(renderEngine);
    }
  }

  private final static class RenderPhaseOverlay implements RenderPhase {
    @Override
    public void render(@Nonnull final EffectProcessor processor, @Nonnull final NiftyRenderEngine renderEngine) {
      processor.renderOverlay(renderEngine);
    }
  }

  public interface Notify {
    void effectStateChanged(@Nonnull EffectEventId eventId, boolean active);
  }

  private static class NotifyAdapter implements EffectProcessorImpl.Notify {
    @Nonnull
    private final Notify notify;
    @Nonnull
    private final EffectEventId eventId;

    public NotifyAdapter(@Nonnull final EffectEventId eventId, @Nonnull final Notify notify) {
      this.eventId = eventId;
      this.notify = notify;
    }

    @Override
    public void effectProcessorStateChanged(final boolean active) {
      notify.effectStateChanged(eventId, active);
    }
  }

  @Nullable
  private EffectProcessor getEffectProcessor(@Nonnull final EffectEventId id) {
    EffectProcessor processor = effectProcessor.get(id);
    if (processor == null) {
      return null;
    }
    return processor;
  }
}
