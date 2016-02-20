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

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyCompositeOperation;
import de.lessvoid.nifty.NiftyFont;
import de.lessvoid.nifty.NiftyImage;
import de.lessvoid.nifty.types.NiftyLineCapType;
import de.lessvoid.nifty.types.NiftyLineJoinType;
import de.lessvoid.nifty.types.NiftyLinearGradient;

public class InternalNiftyCanvas {
  private final List<Command> commands = new ArrayList<>();
  private boolean changed = true;

  public InternalNiftyCanvas() {
  }

  public void setFillColor(final NiftyColor color) {
    commands.add(new CommandFillColor(color));
    changed = true;
  }

  public void setFillLinearGradient(final NiftyLinearGradient gradient) {
    commands.add(new CommandFillLinearGradient(gradient));
    changed = true;
  }

  public void setStrokeColor(final NiftyColor color) {
    commands.add(new CommandStrokeStyle(color));
    changed = true;
  }

  public void setTextColor(final NiftyColor color) {
    commands.add(new CommandTextColor(color));
    changed = true;
  }

  public void setTextSize(final double textSize) {
    commands.add(new CommandTextSize(textSize));
    changed = true;
  }

  public void setLineWidth(final double lineWidth) {
    commands.add(new CommandLineWidth(lineWidth));
    changed = true;
  }

  public void setLineCap(final NiftyLineCapType lineCapType) {
    commands.add(new CommandLineCapType(lineCapType));
    changed = true;
  }

  public void setLineJoin(final NiftyLineJoinType lineJoinType) {
    commands.add(new CommandLineJoinType(lineJoinType));
    changed = true;
  }

  public void filledRect(final double x, final double y, final double width, final double height) {
    commands.add(new CommandFilledRect(x, y, width, height));
    changed = true;
  }

  public void text(final NiftyFont niftyFont, final int x, final int y, final String text) {
    commands.add(new CommandText(niftyFont, x, y, text));
    changed = true;
  }

  public void image(final int x, final int y, final NiftyImage image) {
    commands.add(new CommandImage(x, y, image));
    changed = true;
  }

  public void scale(final double scaleWidth, final double scaleHeight) {
    commands.add(new CommandScale(scaleWidth, scaleHeight));
    changed = true;
  }

  public void rotate(final double angleDegrees) {
    commands.add(new CommandRotate(angleDegrees));
    changed = true;
  }

  public void translate(final double x, final double y) {
    commands.add(new CommandTranslate(x, y));
    changed = true;
  }

  public void resetTransform() {
    commands.add(new CommandResetTransform());
    changed = true;
  }

  public void customerShader(final String shaderId) {
    commands.add(new CommandCustomShader(shaderId));
  }

  public void clear() {
  }

  public void beginPath() {
    commands.add(new CommandBeginPath());
    changed = true;
  }

  public void closePath() {
    commands.add(new CommandClosePath());
    changed = true;
  }

  public void moveTo(final double x, final double y) {
    commands.add(new CommandMoveTo(x, y));
    changed = true;
  }

  public void lineTo(final double x, final double y) {
    commands.add(new CommandLineTo(x, y));
    changed = true;
  }

  public void stroke() {
    commands.add(new CommandStroke());
    changed = true;
  }

  public void fillPath() {
    commands.add(new CommandFillPath());
    changed = true;
  }

  public void arc(final double x, final double y, final double r, final double startAngle, final double endAngle) {
    commands.add(new CommandArc(x, y, r, startAngle, endAngle));
    changed = true;
  }

  public void arcTo(final double x1, final double y1, final double x2, final double y2, final double r) {
    commands.add(new CommandArcTo(x1, y1, x2, y2, r));
    changed = true;
  }

  public boolean isChanged() {
    return changed;
  }

  public List<Command> getCommands() {
    changed = false;
    return new ArrayList<>(commands);
  }

  public void bezierCurveTo(final double cp1x, final double cp1y, final double cp2x, final double cp2y, final double x, final double y) {
    commands.add(new CommandBezierCurveTo(cp1x, cp1y, cp2x, cp2y, x, y));
    changed = true;
  }

  public void setGlobalCompositeOperation(final NiftyCompositeOperation compositeOperation) {
    commands.add(new CommandGlobalCompositeOperation(compositeOperation));
    changed = true;    
  }

  public void reset() {
    commands.clear();
    changed = true;
  }
}
