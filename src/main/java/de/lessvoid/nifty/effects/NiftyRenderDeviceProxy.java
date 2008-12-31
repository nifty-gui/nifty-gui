package de.lessvoid.nifty.effects;

import java.util.HashSet;
import java.util.Set;

import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStateType;
import de.lessvoid.nifty.render.spi.BlendMode;
import de.lessvoid.nifty.render.spi.RenderFont;
import de.lessvoid.nifty.tools.Color;

/**
 * RenderDeviceProxy.
 * @author void
 */
public class NiftyRenderDeviceProxy implements NiftyRenderEngine {

  private Set < RenderStateType > renderStates = new HashSet < RenderStateType >();

  public void clear() {
  }

  public RenderFont createFont(final String name) {
    return null;
  }

  public NiftyImage createImage(final String name, final boolean filterLinear) {
    return null;
  }

  public void disableClip() {
  }

  public void enableClip(final int x0, final int y0, final int x1, final int y1) {
    renderStates.add(RenderStateType.clip);
  }

  public int getHeight() {
    return 0;
  }

  public int getWidth() {
    return 0;
  }

  public void setColor(final Color colorParam) {
    renderStates.add(RenderStateType.color);
    renderStates.add(RenderStateType.alpha);
  }

  public void setColorAlpha(final float newColorAlpha) {
    renderStates.add(RenderStateType.alpha);
  }

  public void setColorIgnoreAlpha(final Color color) {
    renderStates.add(RenderStateType.color);
  }

  public boolean isColorChanged() {
    return false;
  }

  public boolean isColorAlphaChanged() {
    return false;
  }

  public float getColorAlpha() {
    return 0.0f;
  }

  public void moveTo(final float param, final float param2) {
    renderStates.add(RenderStateType.position);
  }

  public void renderImage(final NiftyImage image, final int x, final int y, final int width, final int height) {
  }

  public void renderQuad(final int x, final int y, final int width, final int height) {
  }

  public void renderText(
      final String text,
      final int x,
      final int y,
      final int selectionStart,
      final int selectionEnd,
      final Color c) {
  }

  public void restoreState() {
  }

  public void saveState(final Set < RenderStateType > statesToSave) {
  }

  public void setFont(final RenderFont font) {
    renderStates.add(RenderStateType.font);
  }

  public RenderFont getFont() {
    return null;
  }

  public void setGlobalPosition(final float pos, final float pos2) {
  }

  public void setImageScale(final float scale) {
  }

  public void setRenderTextSize(final float size) {
  }

  public void reset() {
    renderStates.clear();
  }

  public Set < RenderStateType > getStates() {
    return renderStates;
  }

  public void setBlendMode(final BlendMode blendMode) {
    renderStates.add(RenderStateType.blendMode);
  }
}
