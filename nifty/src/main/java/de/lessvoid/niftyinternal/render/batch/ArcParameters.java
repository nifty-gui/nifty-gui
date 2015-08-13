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
package de.lessvoid.niftyinternal.render.batch;

import de.lessvoid.niftyinternal.canvas.LineParameters;

/**
 * We keep all parameters that might create a new ArcBatch in this class. If any of these change
 * a new ArcBatch is being created.  
 * @author void
 */
public class ArcParameters {
  private final LineParameters lineParameters;
  private final float startAngle;
  private final float endAngle;
  private final float r;

  public ArcParameters(final LineParameters lineParameters, final float startAngle, final float endAngle, final float r) {
    this.lineParameters = new LineParameters(lineParameters);
    this.startAngle = startAngle;
    this.endAngle = endAngle;
    this.r = r;
  }

  public ArcParameters(final ArcParameters arcParameters) {
    this.lineParameters = new LineParameters(arcParameters.lineParameters);
    this.startAngle = arcParameters.startAngle;
    this.endAngle = arcParameters.endAngle;
    this.r = arcParameters.r;
  }

  public LineParameters getLineParameters() {
    return lineParameters;
  }

  public float getStartAngle() {
    return startAngle;
  }

  public float getEndAngle() {
    return endAngle;
  }

  public float getRadius() {
    return r;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Float.floatToIntBits(endAngle);
    result = prime * result + ((lineParameters == null) ? 0 : lineParameters.hashCode());
    result = prime * result + Float.floatToIntBits(r);
    result = prime * result + Float.floatToIntBits(startAngle);
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
    ArcParameters other = (ArcParameters) obj;
    if (Float.floatToIntBits(endAngle) != Float.floatToIntBits(other.endAngle))
      return false;
    if (lineParameters == null) {
      if (other.lineParameters != null)
        return false;
    } else if (!lineParameters.equals(other.lineParameters))
      return false;
    if (Float.floatToIntBits(r) != Float.floatToIntBits(other.r))
      return false;
    if (Float.floatToIntBits(startAngle) != Float.floatToIntBits(other.startAngle))
      return false;
    return true;
  }
}