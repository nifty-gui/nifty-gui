/*
 * Copyright (c) 2015, Nifty GUI Community
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are 
 * met: 
 * 
 *  * Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer. 
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.lessvoid.nifty.renderer.java2d;

import de.lessvoid.nifty.spi.NiftyFont;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;
import de.lessvoid.nifty.types.NiftyCompositeOperation;
import de.lessvoid.nifty.types.NiftyLineCapType;
import de.lessvoid.nifty.types.NiftyLineJoinType;
import de.lessvoid.niftyinternal.NiftyResourceLoader;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;

public class NiftyRenderDeviceJava2D implements NiftyRenderDevice {

  @Override
  public void setResourceLoader(NiftyResourceLoader niftyResourceLoader) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public int getDisplayWidth() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getDisplayHeight() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void clearScreenBeforeRender(boolean clearScreenBeforeRender) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public NiftyTexture createTexture(int width, int height, FilterMode filterMode) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public NiftyTexture createTexture(int width, int height, ByteBuffer data, FilterMode filterMode) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public
      NiftyTexture
      loadTexture(String filename, FilterMode filterMode, PreMultipliedAlphaMode preMultipliedAlphaMode) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void beginRender() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void renderTexturedQuads(NiftyTexture texture, FloatBuffer vertices) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void renderColorQuads(FloatBuffer vertices) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void renderLinearGradientQuads(
      double x0,
      double y0,
      double x1,
      double y1,
      List<ColorStop> colorStops,
      FloatBuffer vertices) {
    // TODO Auto-generated method stub
  }

  @Override
  public void endRender() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void beginRenderToTexture(NiftyTexture texture) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void endRenderToTexture(NiftyTexture texture) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void maskBegin() {

  }

  @Override
  public void maskEnd() {

  }

  @Override
  public void maskClear() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void changeCompositeOperation(NiftyCompositeOperation compositeOperation) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String loadCustomShader(String filename) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void activateCustomShader(String shaderId) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public NiftyFont createFont(final String name) {
    return null;
  }

  @Override
  public void maskRenderLines(FloatBuffer b, float lineWidth, NiftyLineCapType lineCapType, NiftyLineJoinType lineJoinType) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void maskRenderFill(FloatBuffer vertices) {
    // TODO Auto-generated method stub
    
  }

}
