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
package de.lessvoid.nifty.api.converter;

import de.lessvoid.nifty.api.UnitValue;

public class NiftyStyleStringConverterUnitValueArray implements NiftyStyleStringConverter<UnitValue[]> {

  @Override
  public UnitValue[] fromString(final String value) throws Exception {
    if (value == null || value.length() == 0) {
      return null;
    }
    String[] split = value.split(" ");
    UnitValue[] result = new UnitValue[split.length];
    for (int i=0; i<split.length; i++) {
      result[i] = new UnitValue(split[i].trim());
    }
    return result;
  }

  @Override
  public String toString(final UnitValue[] values) throws Exception {
    if (values == null || values.length == 0) {
      return "";
    }
    StringBuilder result = new StringBuilder();
    for (int i=0; i<values.length; i++) {
      result.append(values[i].toString());
      if (i < values.length - 1) {
        result.append(" ");
      }
    }
    return result.toString();
  }
}
