package de.lessvoid.nifty.effects;

import java.util.HashSet;
import java.util.Set;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStateType;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

/**
 * RenderDeviceProxy.
 * @author void
 */
public class NiftyRenderDeviceProxy implements NiftyRenderEngine {

  private Set < RenderStateType > renderStates = new HashSet < RenderStateType >();

  @Override
  public void beginFrame() {
  }

  @Override
  public void endFrame() {
  }

  @Override
  public void clear() {
  }

  @Override
  public RenderFont createFont(final String name) {
    return null;
  }

  @Override
  public NiftyImage createImage(final String name, final boolean filterLinear) {
    return null;
  }

  @Override
  public void disableClip() {
  }

  @Override
  public void enableClip(final int x0, final int y0, final int x1, final int y1) {
    renderStates.add(RenderStateType.clip);
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public void setColor(final Color colorParam) {
    renderStates.add(RenderStateType.color);
    renderStates.add(RenderStateType.alpha);
  }

  @Override
  public void setColorAlpha(final float newColorAlpha) {
    renderStates.add(RenderStateType.alpha);
  }

  @Override
  public void setColorIgnoreAlpha(final Color color) {
    renderStates.add(RenderStateType.color);
  }

  @Override
  public boolean isColorChanged() {
    return false;
  }

  @Override
  public boolean isColorAlphaChanged() {
    return false;
  }

  @Override
  public void moveTo(final float param, final float param2) {
    renderStates.add(RenderStateType.position);
  }

  @Override
  public void renderImage(final NiftyImage image, final int x, final int y, final int width, final int height) {
  }

  @Override
  public void renderQuad(final int x, final int y, final int width, final int height) {
  }

  @Override
  public void renderQuad(final int x, final int y, final int width, final int height, final Color topLeft, final Color topRight, final Color bottomRight, final Color bottomLeft) {
  }

  @Override
  public void renderText(
      final String text,
      final int x,
      final int y,
      final int selectionStart,
      final int selectionEnd,
      final Color c) {
  }

  @Override
  public void restoreState() {
  }

  @Override
  public void saveState(final Set < RenderStateType > statesToSave) {
  }

  @Override
  public void setFont(final RenderFont font) {
    renderStates.add(RenderStateType.font);
  }

  @Override
  public RenderFont getFont() {
    return null;
  }

  @Override
  public void setGlobalPosition(final float pos, final float pos2) {
    renderStates.add(RenderStateType.position);
  }

  @Override
  public void setImageScale(final float scale) {
    renderStates.add(RenderStateType.imageScale);
  }

  @Override
  public void setRenderTextSize(final float size) {
    renderStates.add(RenderStateType.textSize);
  }

  @Override
  public void setBlendMode(final BlendMode blendMode) {
    renderStates.add(RenderStateType.blendMode);
  }

  @Override
  public RenderDevice getRenderDevice() {
    return null;
  }

  @Override
  public void disposeImage(final RenderImage image) {
  }

  @Override
  public void displayResolutionChanged() {
  }

  
  public void reset() {
    renderStates.clear();
  }

  public Set < RenderStateType > getStates() {
    return renderStates;
  }

  @Override
  public RenderImage reload(final RenderImage image) {
    return image;
  }
}
