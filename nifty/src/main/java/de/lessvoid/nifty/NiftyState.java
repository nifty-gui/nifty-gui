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
package de.lessvoid.nifty;

import de.lessvoid.niftyinternal.accessor.NiftyStateAccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * Created by void on 16.08.15.
 */
public class NiftyState {
  private final Map<State, Object> stateMap = new HashMap<>();

  interface State {
    String name();
  }

  NiftyState() {
  }

  NiftyState(final NiftyState niftyState) {
    stateMap.putAll(niftyState.stateMap);
  }

  public enum NiftyStandardState implements State {
    NiftyStateBackgroundColor,
    NiftyStateTransformation,
    NiftyStateTransformationChanged,
    NiftyStateTransformationLayoutRect
  }

  public <T> void setState(final State state, final T value) {
    stateMap.put(state, value);
  }

  public <T> T getState(final State state) {
    return (T) stateMap.get(state);
  }

  public <T> T getState(final State state, final T defaultValue) {
    T value = (T) stateMap.get(state);
    if (value != null) {
      return value;
    }
    return defaultValue;
  }

  @Override
  public String toString() {
    Map<State, Object> map = new TreeMap<>(stateMap);
    StringBuilder b = new StringBuilder();
    for (Map.Entry<State, Object> e : map.entrySet()) {
      b.append("  ").append(e.getKey()).append(" [");

      if (e.getValue() == null) {
        b.append("    ").append(e.getValue());
      } else {
        boolean first = true;
        for (String s : e.getValue().toString().split("\n")) {
          if (!first) {
            b.append(" ");
          }
          if (first) {
            first = false;
          }
          b.append(s);
        }
      }
      b.append("]\n");
    }
    return b.toString();
  }

  // Internal methods

  static {
    NiftyStateAccessor.DEFAULT = new InternalNiftyStateAccessorImpl();
  }

}
