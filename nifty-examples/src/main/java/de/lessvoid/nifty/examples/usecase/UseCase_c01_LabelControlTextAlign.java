/*
 * Copyright (c) 2014, Jens Hohmuth 
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
package de.lessvoid.nifty.examples.usecase;

import java.io.IOException;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.HAlign;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyFont;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.VAlign;
import de.lessvoid.nifty.api.controls.Label;

/**
 * Display a simple text.
 * @author void
 */
public class UseCase_c01_LabelControlTextAlign {
  private final NiftyNode niftyNode;
  private final NiftyFont font;

  public UseCase_c01_LabelControlTextAlign(final Nifty nifty) throws IOException {
    niftyNode = nifty.createRootNodeFullscreen(ChildLayout.Vertical);
    niftyNode.setBackgroundColor(NiftyColor.fromString("#003f"));

    font = nifty.createFont("fonts/aurulent-sans-16.fnt");
    addLabel("TextHAlign: center, TextVAlign: top", HAlign.center, VAlign.top, NiftyColor.fromString("#f60f"));
    addLabel("TextHAlign: center, TextVAlign: center", HAlign.center, VAlign.center, NiftyColor.fromString("#f80f"));
    addLabel("TextHAlign: center, TextVAlign: bottom", HAlign.center, VAlign.bottom, NiftyColor.fromString("#fa0f"));
    addLabel("TextHAlign: left, TextVAlign: center", HAlign.left, VAlign.center, NiftyColor.fromString("#fc0f"));
    addLabel("TextHAlign: center, TextVAlign: center", HAlign.center, VAlign.center, NiftyColor.fromString("#fe0f"));
    addLabel("TextHAlign: right, TextVAlign: center", HAlign.right, VAlign.center, NiftyColor.fromString("#ff2f"));
  }

  private void addLabel(final String text, final HAlign halign, final VAlign valign, final NiftyColor backgroundColor) {
    Label label = niftyNode.newControl(Label.class);
    label.setFont(font);
    label.setText(text);
    label.setTextColor(NiftyColor.black());
    label.setTextHAlign(halign);
    label.setTextVAlign(valign);
    label.getNode().setBackgroundColor(backgroundColor);
    label.getNode().setWidthConstraint(UnitValue.percent(75));
    label.getNode().setHeightConstraint(UnitValue.percent(15));
    label.getNode().setHAlign(HAlign.center);
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_c01_LabelControlTextAlign.class, args);
  }
}
