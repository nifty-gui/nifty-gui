package de.lessvoid.nifty.effects;

import java.util.List;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.render.NiftyRenderEngine;

public class EffectProcessorNull implements EffectProcessor {
  @Override
  public void registerEffect(Effect e) {
  }

  @Override
  public void getRenderStatesToSave(NiftyRenderDeviceProxy renderDeviceProxy) {
  }

  @Override
  public void renderPre(NiftyRenderEngine renderDevice) {
  }

  @Override
  public void renderPost(NiftyRenderEngine renderDevice) {
  }

  @Override
  public void renderOverlay(NiftyRenderEngine renderDevice) {
  }

  @Override
  public boolean isActive() {
    return false;
  }

  @Override
  public void saveActiveNeverStopRenderingEffects() {
  }

  @Override
  public void restoreNeverStopRenderingEffects() {
  }

  @Override
  public void reset() {
  }

  @Override
  public void reset(String customKey) {
  }

  @Override
  public void activate(EndNotify newListener, String alternate, String customKey) {
  }

  @Override
  public String getStateString() {
    return null;
  }

  @Override
  public void setActive(boolean newActive) {
  }

  @Override
  public void processHover(int x, int y) {
  }

  @Override
  public void processStartHover(int x, int y) {
  }

  @Override
  public void processEndHover(int x, int y) {
  }

  @Override
  public void processHoverDeactivate(int x, int y) {
  }

  @Override
  public void removeAllEffects() {
  }

  @Override
  public <T extends EffectImpl> List<Effect> getEffects(Class<T> requestedClass) {
    return null;
  }
}
