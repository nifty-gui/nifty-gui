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
package de.lessvoid.niftyinternal.canvas;

import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyLineCapType;
import de.lessvoid.nifty.types.NiftyLineJoinType;


/**
 * This collects all available parameters for rendering lines. This is part of the NiftyRenderDevice interface
 * because it is only part of the SPI and not really a part of the public Nifty API in the sense that this class
 * is only used to communicate settings between Nifty and the NiftyRenderDevice. This class is not meant to be used
 * by actual user code.
 */
public class LineParameters {
  private NiftyLineCapType lineCapType = NiftyLineCapType.Round;
  private NiftyLineJoinType lineJoinType = NiftyLineJoinType.Miter;
  private float lineWidth = 1.f;
  private NiftyColor lineColor = NiftyColor.white();

  public LineParameters() {
  }

  public LineParameters(final LineParameters lineParameters) {
    lineCapType = lineParameters.getLineCapType();
    lineJoinType = lineParameters.getLineJoinType();
    lineWidth = lineParameters.getLineWidth();
    lineColor = lineParameters.getColor();
  }

  public NiftyLineCapType getLineCapType() {
    return lineCapType;
  }

  public void setLineCapType(final NiftyLineCapType lineCapType) {
    if (lineCapType == null) {
      throw new IllegalArgumentException("LineCapType can't be null");
    }
    this.lineCapType = lineCapType;
  }

  public NiftyLineJoinType getLineJoinType() {
    return lineJoinType;
  }

  public void setLineJoinType(final NiftyLineJoinType lineJoinType) {
    if (lineJoinType == null) {
      throw new IllegalArgumentException("LineJoinType can't be null");
    }
    this.lineJoinType = lineJoinType;
  }

  public float getLineWidth() {
    return lineWidth;
  }

  public void setLineWidth(float lineWidth) {
    this.lineWidth = lineWidth;
  }

  public void setColor(final NiftyColor color) {
    this.lineColor = color;
  }

  public NiftyColor getColor() {
    return lineColor;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((lineCapType == null) ? 0 : lineCapType.hashCode());
    result = prime * result + ((lineColor == null) ? 0 : lineColor.hashCode());
    result = prime * result + ((lineJoinType == null) ? 0 : lineJoinType.hashCode());
    result = prime * result + Float.floatToIntBits(lineWidth);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    LineParameters other = (LineParameters) obj;
    if (lineCapType != other.lineCapType)
      return false;
    if (lineColor == null) {
      if (other.lineColor != null)
        return false;
    } else if (!lineColor.equals(other.lineColor))
      return false;
    if (lineJoinType != other.lineJoinType)
      return false;
    if (Float.floatToIntBits(lineWidth) != Float.floatToIntBits(other.lineWidth))
      return false;
    return true;
  }
}