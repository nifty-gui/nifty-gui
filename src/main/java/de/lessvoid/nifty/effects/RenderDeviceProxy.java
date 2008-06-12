package de.lessvoid.nifty.effects;

import java.util.HashSet;
import java.util.Set;

import de.lessvoid.nifty.render.RenderEngine;
import de.lessvoid.nifty.render.RenderFont;
import de.lessvoid.nifty.render.RenderImage;
import de.lessvoid.nifty.render.RenderState;
import de.lessvoid.nifty.tools.Color;

/**
 * RenderDeviceProxy.
 * @author void
 */
public class RenderDeviceProxy implements RenderEngine {

  private Set < RenderState > renderStates = new HashSet < RenderState > ();

  public void clear() {
  }

  public RenderFont createFont(String name) {
    return null;
  }

  public RenderImage createImage(String name, boolean filterLinear) {
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

  public void moveTo(float param, float param2) {
    renderStates.add(RenderState.position);
  }

  public void renderImage(RenderImage image, int x, int y, int width, int height) {
  }

  public void renderQuad(int x, int y, int width, int height) {
  }

  public void renderText(RenderFont font, String text, int x, int y, int selectionStart, int selectionEnd, Color c) {
  }

  public void restoreState() {
  }

  public void saveState(Set<RenderState> statesToSave) {
  }

  public void setColor(Color colorParam) {
    renderStates.add(RenderState.color);
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

  public Set < RenderState > getStates() {
    return renderStates;
  }
}
