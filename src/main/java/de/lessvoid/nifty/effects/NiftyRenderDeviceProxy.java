package de.lessvoid.nifty.effects;

import java.util.HashSet;
import java.util.Set;

import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStateType;
import de.lessvoid.nifty.render.spi.RenderFont;
import de.lessvoid.nifty.tools.Color;

/**
 * RenderDeviceProxy.
 * @author void
 */
public class NiftyRenderDeviceProxy implements NiftyRenderEngine {

  private Set < RenderStateType > renderStates = new HashSet < RenderStateType > ();

  public void clear() {
  }

  public RenderFont createFont(String name) {
    return null;
  }

  public NiftyImage createImage(String name, boolean filterLinear) {
    return null;
  }

  public void disableClip() {
  }

  public void enableClip(int x0, int y0, int x1, int y1) {
  }

  public int getHeight() {
    return 0;
  }

  public int getWidth() {
    return 0;
  }

  public boolean isColorChanged() {
    return false;
  }

  public float getColorAlpha() {
    return 0.0f;
  }

  public void moveTo(float param, float param2) {
    renderStates.add(RenderStateType.position);
  }

  public void renderImage(NiftyImage image, int x, int y, int width, int height) {
  }

  public void renderQuad(int x, int y, int width, int height) {
  }

  public void renderText(String text, int x, int y, int selectionStart, int selectionEnd, Color c) {
  }

  public void restoreState() {
  }

  public void saveState(Set<RenderStateType> statesToSave) {
  }

  public void setFont(RenderFont font) {
    renderStates.add(RenderStateType.font); 
  }

  public RenderFont getFont() {
    return null;
  }
  public void setColor(Color colorParam) {
    renderStates.add(RenderStateType.color);
  }

  public void setGlobalPosition(float pos, float pos2) {
  }

  public void setImageScale(float scale) {
  }

  public void setRenderTextSize(float size) {
  }

  public void reset() {
    renderStates.clear();
  }

  public Set < RenderStateType > getStates() {
    return renderStates;
  }
}
