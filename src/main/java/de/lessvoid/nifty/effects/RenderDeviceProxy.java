package de.lessvoid.nifty.effects;

import java.util.HashSet;
import java.util.Set;

import de.lessvoid.nifty.render.RenderDevice;
import de.lessvoid.nifty.render.RenderFont;
import de.lessvoid.nifty.render.RenderImage;
import de.lessvoid.nifty.render.RenderState;

public class RenderDeviceProxy implements RenderDevice {

  private Set < RenderState > renderStates;

  public RenderDeviceProxy() {
    this.renderStates = new HashSet < RenderState > ();
  }

  public void clear() {
  }

  public RenderFont createFont(String name) {
    return null;
  }

  public RenderImage createImage(String name, boolean filter) {
    return null;
  }

  public void disableTexture() {
  }

  public void enableBlend() {
  }

  public int getHeight() {
    return 0;
  }

  public float getMoveToX() {
    return 0;
  }

  public float getMoveToY() {
    return 0;
  }

  public int getWidth() {
    return 0;
  }

  public void moveTo(float x, float y) {
    renderStates.add(RenderState.position);
  }

  public void renderImage(RenderImage background, int x, int y, int width, int height) {
  }

  public void renderImagePart(RenderImage image, int x, int y, int w, int h, int srcX, int srcY, int srcW, int srcH) {
  }

  public void renderQuad(int x, int y, int width, int height) {
  }

  public void renderText(RenderFont font, String text, int x, int y) {
  }

  public void restoreState() {
  }

  public void saveState(Set<RenderState> stateToSave) {
  }

  public void setColor(float r, float g, float b, float a) {
    renderStates.add(RenderState.color);
  }

  public void setFontColor(float r, float g, float b, float a) {
    renderStates.add(RenderState.fontColor);
  }

  public void setGlobalPosition(float x, float y) {
  }

  public void setImageScale(float f) {
  }

  public void setRenderTextSize(float size) {
  }

  public void reset() {
    renderStates.clear();
  }

  public Set < RenderState > getStates() {
    return renderStates;
  }

  public void disableClip() {
  }

  public void enableClip(int x0, int y0, int x1, int y1) {
  }

  public boolean isColorChanged() {
    return false;
  }
}
