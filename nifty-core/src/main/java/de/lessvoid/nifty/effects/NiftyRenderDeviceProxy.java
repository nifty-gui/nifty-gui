package de.lessvoid.nifty.effects;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStates;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

/**
 * RenderDeviceProxy.
 * @author void
 */
public class NiftyRenderDeviceProxy implements NiftyRenderEngine {
  private RenderStates renderStates = new RenderStates();

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
  public String getFontname(final RenderFont font) {
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
    renderStates.addClip();
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
  public int getNativeWidth() {
    return 0;
  }

  @Override
  public int getNativeHeight() {
    return 0;
  }

  @Override
  public void setColor(final Color colorParam) {
    renderStates.addColor();
    renderStates.addAlpha();
  }

  @Override
  public void setColorAlpha(final float newColorAlpha) {
    renderStates.addAlpha();
  }

  @Override
  public void setColorIgnoreAlpha(final Color color) {
    renderStates.addColor();
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
    renderStates.addPosition();
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
  public void saveState(final RenderStates statesToSave) {
  }

  @Override
  public void setFont(final RenderFont font) {
    renderStates.addFont();
  }

  @Override
  public RenderFont getFont() {
    return null;
  }

  @Override
  public void setGlobalPosition(final float pos, final float pos2) {
    renderStates.addPosition();
  }

  @Override
  public void setImageScale(final float scale) {
    renderStates.addImageScale();
  }

  @Override
  public void setRenderTextSize(final float size) {
    renderStates.addTextSize();
  }

  @Override
  public void setBlendMode(final BlendMode blendMode) {
    renderStates.addBlendMode();
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

  public RenderStates getStates() {
    return renderStates;
  }

  @Override
  public RenderImage reload(final RenderImage image) {
    return image;
  }

  @Override
  public int convertToNativeX(final int x) {
    return 0;
  }

  @Override
  public int convertToNativeY(final int y) {
    return 0;
  }

  @Override
  public int convertToNativeWidth(final int x) {
    return 0;
  }

  @Override
  public int convertToNativeHeight(final int y) {
    return 0;
  }

  @Override
  public int convertFromNativeX(final int x) {
    return 0;
  }

  @Override
  public int convertFromNativeY(final int y) {
    return 0;
  }

  @Override
  public float convertToNativeTextSizeX(final float size) {
    return 0.f;
  }

  @Override
  public float convertToNativeTextSizeY(final float size) {
    return 0.f;
  }

  @Override
  public void enableAutoScaling(final int baseResolutionX, final int baseResolutionY) {
  }

  @Override
  public void enableAutoScaling(final int baseResolutionX, final int baseResolutionY, final float scaleX, final float scaleY) {
  }

  @Override
  public void disableAutoScaling() {
  }
}
