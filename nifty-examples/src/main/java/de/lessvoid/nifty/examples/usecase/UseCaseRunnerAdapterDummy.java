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
package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.dummy.DummyInputDevice;
import de.lessvoid.nifty.examples.dummy.DummyRenderDevice;
import de.lessvoid.nifty.time.AccurateTimeProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.lessvoid.nifty.Nifty.createNifty;

/**
 * UseCaseRunnerAdapter for dummy devices. These devices do not render anything or generate any input.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class UseCaseRunnerAdapterDummy implements UseCaseRunnerAdapter {
  private static Logger log = LoggerFactory.getLogger(UseCaseRunnerAdapterDummy.class.getName());

  private boolean run = true;

  @Override
  public void run(final Class<?> useCaseClass, final String[] args) throws Exception {
    // create nifty instance
    final Nifty nifty = createNifty(
        new DummyRenderDevice(1024, 786, 60),
        new DummyInputDevice(),
        new AccurateTimeProvider());

    useCaseClass.getConstructor(Nifty.class).newInstance(nifty);

    nifty.update();
    nifty.render();

    logScene(nifty);

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        run = false;
      }
    });

    while (run) {
      nifty.update();
      nifty.render();
    }
  }

  private void logScene(final Nifty nifty) {
    log.info(nifty.getSceneInfoLog());
  }
}
