package de.lessvoid.nifty.effects;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStateType;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * manage all effects of an element.
 * @author void
 */
public class EffectManager {

  interface RenderPhase {
    public void render(EffectProcessor effectProcessor);
  }

  /**
   * all the effects.
   */
  private Map < EffectEventId, EffectProcessor > effectProcessor = new Hashtable < EffectEventId, EffectProcessor >();

  /**
   * alternateKey we should use.
   */
  private String alternateKey;

  /**
   * create a new effectManager with the given listener.
   */
  public EffectManager() {
    this.alternateKey = null;

    effectProcessor.put(EffectEventId.onStartScreen, new EffectProcessor(false));
    effectProcessor.put(EffectEventId.onEndScreen, new EffectProcessor(true));
    effectProcessor.put(EffectEventId.onFocus, new EffectProcessor(true));
    effectProcessor.put(EffectEventId.onGetFocus, new EffectProcessor(false));
    effectProcessor.put(EffectEventId.onLostFocus, new EffectProcessor(false));
    effectProcessor.put(EffectEventId.onClick, new EffectProcessor(false));
    effectProcessor.put(EffectEventId.onHover, new EffectProcessor(true));
    effectProcessor.put(EffectEventId.onActive, new EffectProcessor(true));
    effectProcessor.put(EffectEventId.onCustom, new EffectProcessor(false));
    effectProcessor.put(EffectEventId.onShow, new EffectProcessor(false));
    effectProcessor.put(EffectEventId.onHide, new EffectProcessor(true));

    effectProcessor.get(EffectEventId.onHover).setHoverEffect();
  }

  /**
   * register an effect.
   * @param id the id
   * @param e the effect
   */
  public final void registerEffect(final EffectEventId id, final Effect e) {
    effectProcessor.get(id).registerEffect(e);
  }

  /**
   * start all effects with the given id for the given element.
   * @param id the effect id to start
   * @param w the element
   * @param time TimeProvider
   * @param listener the {@link EndNotify} to use.
   */
  public final void startEffect(
      final EffectEventId id,
      final Element w,
      final TimeProvider time,
      final EndNotify listener) {
    effectProcessor.get(id).activate(listener, alternateKey);
  }

  /**
   * Stop effects with the given id.
   * @param effectId effect id to stop
   */
  public void stopEffect(final EffectEventId effectId) {
    effectProcessor.get(effectId).setActive(false);
  }

  /**
   * prepare rendering.
   * @param renderDevice RenderDevice
   */
  public void begin(final NiftyRenderEngine renderDevice, final Element element) {
    Set < RenderStateType > renderStates = RenderStateType.allStates();

    for (EffectProcessor processor : effectProcessor.values()) {
      renderStates.removeAll(processor.getRenderStatesToSave());
    }

    renderDevice.saveState(renderStates);
  }

  /**
   * finish rendering.
   * @param renderDevice RenderDevice
   */
  public void end(final NiftyRenderEngine renderDevice) {
    renderDevice.restoreState();
  }

  private void render(final Element element, final RenderPhase phase) {
    phase.render(effectProcessor.get(EffectEventId.onShow));
    phase.render(effectProcessor.get(EffectEventId.onHide));
    phase.render(effectProcessor.get(EffectEventId.onStartScreen));
    phase.render(effectProcessor.get(EffectEventId.onEndScreen));
    phase.render(effectProcessor.get(EffectEventId.onActive));
    phase.render(effectProcessor.get(EffectEventId.onHover));
    phase.render(effectProcessor.get(EffectEventId.onFocus));
    phase.render(effectProcessor.get(EffectEventId.onLostFocus));
    phase.render(effectProcessor.get(EffectEventId.onGetFocus));
    phase.render(effectProcessor.get(EffectEventId.onClick));
    phase.render(effectProcessor.get(EffectEventId.onCustom));
  }

  public void renderPre(final NiftyRenderEngine renderEngine, final Element element) {
    render(element, new RenderPhase() {
      public void render(final EffectProcessor processor) {
        processor.renderPre(renderEngine);
      }
    });
  }

  public void renderPost(final NiftyRenderEngine renderEngine, final Element element) {
    render(element, new RenderPhase() {
      public void render(final EffectProcessor processor) {
        processor.renderPost(renderEngine);
      }
    });
  }

  public void renderOverlay(final NiftyRenderEngine renderEngine, final Element element) {
    render(element, new RenderPhase() {
      public void render(final EffectProcessor processor) {
        processor.renderOverlay(renderEngine);
      }
    });
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
      effectProcessor.get(EffectEventId.onStartScreen).reset();
      effectProcessor.get(EffectEventId.onEndScreen).reset();
      effectProcessor.get(EffectEventId.onShow).reset();
      effectProcessor.get(EffectEventId.onHide).reset();
      effectProcessor.get(EffectEventId.onCustom).reset();
      effectProcessor.get(EffectEventId.onHover).reset();
      effectProcessor.get(EffectEventId.onActive).reset();
      effectProcessor.get(EffectEventId.onFocus).reset();
      effectProcessor.get(EffectEventId.onLostFocus).reset();
      effectProcessor.get(EffectEventId.onGetFocus).reset();
      effectProcessor.get(EffectEventId.onClick).reset();
    }

  public void resetSingleEffect(final EffectEventId effectEventId) {
    effectProcessor.get(effectEventId).reset();
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

        data.append("\n" + offset);
        data.append("  {" + eventId.toString() + "} ");
        data.append(processor.getStateString());
      }
    }

    if (activeProcessors == 0) {
      return "";
    } else {
      return data.toString();
    }
  }

  public void setFalloff(final Falloff newFalloff) {
    effectProcessor.get(EffectEventId.onHover).setFalloff(newFalloff);
  }

  public Falloff getFalloff() {
    return effectProcessor.get(EffectEventId.onHover).getFalloff();
  }

  public void removeAllEffects() {
    for (EffectProcessor processor : effectProcessor.values()) {
      processor.removeAllEffects();
    }
  }
}
