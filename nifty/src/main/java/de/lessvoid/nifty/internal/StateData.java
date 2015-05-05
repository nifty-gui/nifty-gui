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
package de.lessvoid.nifty.internal;

import java.util.LinkedHashSet;
import java.util.Set;

import de.lessvoid.nifty.api.NiftyNodeState;
import de.lessvoid.nifty.api.NiftyStateSetter;

/**
 * Helper class to store all the data that is part of a certain NiftyNodeState.
 * @author void
 */
public class StateData {
  private Set<StateDataValue<?, ?>> data = new LinkedHashSet<StateDataValue<?, ?>>();

  @SuppressWarnings({ "unchecked", "rawtypes"})
  <T, V> void set(final T target, final V value, final NiftyStateSetter<T, V> setter) {
    StateDataValue stateDataValue = new StateDataValue(target, value, setter);
    data.remove(stateDataValue);
    data.add(stateDataValue);
  }

  void apply(final NiftyNodeState state) {
    for (StateDataValue<?, ?> stateDataSetter : data) {
      stateDataSetter.setter(state);
    }
  }

  @Override
  public String toString() {
    return data.toString();
  }
}
