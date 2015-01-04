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
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyFont;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.controls.Button;

/**
 * Display a simple Button.
 * @author void
 */
public class UseCase_c10_ButtonBasic {
  private final NiftyNode niftyNode;
  private final NiftyFont font;

  public UseCase_c10_ButtonBasic(final Nifty nifty) throws IOException {
    niftyNode = nifty.createRootNodeFullscreen(ChildLayout.Center);
    font = nifty.createFont("fonts/aurulent-sans-16.fnt");

    Button button = niftyNode.newControl(Button.class);
    button.setFont(font);
    button.getNode().setWidthConstraint(UnitValue.px(100));
    button.getNode().setHeightConstraint(UnitValue.px(23));
    button.setText("OK");

    nifty.applyStyle(UseCase_c10_ButtonBasic.class.getResourceAsStream("UseCase_c10_ButtonBasic.css"));
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_c10_ButtonBasic.class, args);
  }
}
