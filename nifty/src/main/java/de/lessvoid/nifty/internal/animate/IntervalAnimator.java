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
package de.lessvoid.nifty.internal.animate;

import de.lessvoid.nifty.api.NiftyCallback;
import de.lessvoid.nifty.spi.TimeProvider;

public class IntervalAnimator {
  // we need the time
  private final TimeProvider timeProvider;

  // the delay before the animated request should begin
  private final long delay;

  // the interval between redraws
  private final long interval;

  // the start time (when the animatedRequestRedraw has been enabled for this node)
  private final long startTime;

  // we want to only toggle requestRedraw once when we reach the next interval. To do this we remember the last
  // sign state and only set requestRedraw to true when it changes.
  private boolean lastState;

  // the action we'll call when the interval matches
  private final NiftyCallback<Float> callback;

  public IntervalAnimator(
      final TimeProvider timeProvider,
      final long delay,
      final long interval,
      final NiftyCallback<Float> callback) {
    this.timeProvider = timeProvider;
    this.delay = delay;
    this.interval = interval;
    this.startTime = timeProvider.getMsTime();
    this.lastState = false;
    this.callback = callback;
  }

  public void update() {
    long currentTime = timeProvider.getMsTime();
    long deltaTime = currentTime - startTime;
    if (deltaTime < delay) {
      return;
    }

    // correct deltaTime for the start time so that we start with 0
    boolean isActive = getValue(deltaTime - delay);
    if (isActive != lastState) {
      lastState = isActive;
      callback.execute(deltaTime / 1000.f);
    }
  }

  private boolean getValue(final long delta) {
    double t = Math.PI * delta / (double) interval;
    double sin = Math.sin(t);
    return sin > 0;
  }
}
