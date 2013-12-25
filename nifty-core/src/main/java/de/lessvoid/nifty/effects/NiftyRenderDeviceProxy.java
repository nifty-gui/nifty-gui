package de.lessvoid.nifty.effects;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStates;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * RenderDeviceProxy.
 *
 * @author void
 */
public class NiftyRenderDeviceProxy implements NiftyRenderEngine {
  @Nonnull
  private final RenderStates renderStates = new RenderStates();

  @Override
  public void beginFrame() {
  }

  @Override
  public void endFrame() {
  }

  @Override
  public void clear() {
  }

  @Nullable
  @Override
  public RenderFont createFont(@Nonnull final String name) {
    return null;
  }

  @Nonnull
  @Override
  public String getFontname(@Nonnull final RenderFont font) {
    throw new UnsupportedOperationException();
  }

  @Nullable
  @Override
  public NiftyImage createImage(@Nonnull final Screen screen, @Nonnull final String name, final boolean filterLinear) {
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
  public void setColor(@Nonnull final Color colorParam) {
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
  public void renderImage(
      @Nonnull final NiftyImage image,
      final int x,
      final int y,
      final int width,
      final int height) {
  }

  @Override
  public void renderQuad(final int x, final int y, final int width, final int height) {
  }

  @Override
  public void renderQuad(
      final int x,
      final int y,
      final int width,
      final int height,
      @Nonnull final Color topLeft,
      @Nonnull final Color topRight,
      @Nonnull final Color bottomRight,
      @Nonnull final Color bottomLeft) {
  }

  @Override
  public void renderText(
      @Nonnull final String text,
      final int x,
      final int y,
      final int selectionStart,
      final int selectionEnd,
      @Nonnull final Color c) {
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

  @Nullable
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
  public void setBlendMode(@Nonnull final BlendMode blendMode) {
    renderStates.addBlendMode();
  }

  @Nonnull
  @Override
  public RenderDevice getRenderDevice() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void disposeImage(@Nonnull final RenderImage image) {
  }

  @Override
  public void displayResolutionChanged() {
  }


  public void reset() {
    renderStates.clear();
  }

  @Nonnull
  public RenderStates getStates() {
    return renderStates;
  }

  @Nonnull
  @Override
  public RenderImage reload(@Nonnull final RenderImage image) {
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
  public void enableAutoScaling(
      final int baseResolutionX,
      final int baseResolutionY,
      final float scaleX,
      final float scaleY) {
  }

  @Override
  public void disableAutoScaling() {
  }

  @Override
  public void screenStarted(@Nonnull Screen screen) {
  }

  @Override
  public void screenEnded(@Nonnull final Screen screen) {
  }

  @Override
  public void screensClear(@Nonnull final Collection<Screen> screens) {
  }

  @Override
  public void screenAdded(@Nonnull final Screen screen) {
  }

  @Override
  public void screenRemoved(@Nonnull final Screen screen) {
  }
}
