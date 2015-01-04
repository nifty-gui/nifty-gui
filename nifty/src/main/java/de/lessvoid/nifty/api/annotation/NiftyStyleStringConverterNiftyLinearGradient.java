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
package de.lessvoid.nifty.api.annotation;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyColorStop;
import de.lessvoid.nifty.api.NiftyLinearGradient;
import de.lessvoid.nifty.internal.style.specialparser.LinearGradientParser;
import de.lessvoid.nifty.internal.style.specialparser.LinearGradientParser.ColorStop;
import de.lessvoid.nifty.internal.style.specialparser.LinearGradientParser.Result;

public class NiftyStyleStringConverterNiftyLinearGradient implements NiftyStyleStringConverter<NiftyLinearGradient> {
  private LinearGradientParser parser = new LinearGradientParser();

  @Override
  public NiftyLinearGradient fromString(final String value) throws Exception {
    return createNiftyLinearGradient(parser.parse(value + ";"));
  }

  @Override
  public String toString(final NiftyLinearGradient value) throws Exception {
    return null;
  }

  private NiftyLinearGradient createNiftyLinearGradient(final Result parserResult) {
    return NiftyLinearGradient.createFromAngleInRad(parserResult.getAngleInRadiants()).addColorSteps(processStops(parserResult.getStops()));
  }

  private List<NiftyColorStop> processStops(final List<ColorStop> stops) {
    return convert(secondPass(firstPass(stops)));
  }

  private List<ColorStop> firstPass(final List<ColorStop> stops) {
    List<ColorStop> result = new ArrayList<ColorStop>();
    double maxPos = Double.NEGATIVE_INFINITY;
    for (int i=0; i<stops.size(); i++) {
      ColorStop colorStop = stops.get(i);
      Double pos = colorStop.getPos();

      // 1a. If the first color stop does not have a position, set its position to 0%.
      if (i == 0) {
        if (pos == null) {
          pos = 0.;
        }
      }

      // 1b. If the last color stop does not have a position, set its position to 100%.
      if (i == stops.size() - 1) {
        if (pos == null) {
          pos = 1.;
        }
      }

      if (pos != null && pos > maxPos) {
        maxPos = pos;
      }

      // 2. If a color stop has a position that is less than the specified position of any color stop before it in the
      //    list, set its position to be equal to the largest specified position of any color stop before it.
      if (pos != null && pos < maxPos) {
        pos = maxPos;
      }
      result.add(new ColorStop(colorStop.getColor(), pos));
    }
    return result;
  }

  // 3. If any color stop still does not have a position, then, for each run of adjacent color stops without
  //    positions, set their positions so that they are evenly spaced between the preceding and following color
  //    stops with positions.
  private List<ColorStop> secondPass(List<ColorStop> firstPassResult) {
    List<ColorStop> secondPassResult = new ArrayList<ColorStop>();
    ColorStop lastWithPos = firstPassResult.get(0);
    for (int i=0; i<firstPassResult.size(); i++) {
      ColorStop current = firstPassResult.get(i);
      if (current.getPos() != null) {
        lastWithPos = current;
        secondPassResult.add(current);
      } else {
        List<ColorStop> nullStops = new ArrayList<ColorStop>();

        // look for the next stop that actually has a position (we know that the last one has because of the firstPass)
        ColorStop next = current;
        while (next.getPos() == null && i < firstPassResult.size()) {
          nullStops.add(next);
          i++;
          next = firstPassResult.get(i);
        }

        // now we have a ColorStop with pos in lastWithPos and in next as well as a list of ColorStops without pos
        // in the list nullStops
        double start = lastWithPos.getPos();
        double end = next.getPos();
        double add = (end - start) / (nullStops.size() + 1);
        for (int j=0; j<nullStops.size(); j++) {
          start += add;
          secondPassResult.add(new ColorStop(nullStops.get(j).getColor(), start));
        }
        secondPassResult.add(next);
      }
    }
    return secondPassResult;
  }

  private List<NiftyColorStop> convert(final List<ColorStop> source) {
    List<NiftyColorStop> result = new ArrayList<NiftyColorStop>();
    for (int i=0; i<source.size(); i++) {
      ColorStop color = source.get(i);
      result.add(new NiftyColorStop(color.getPos(), NiftyColor.fromString(color.getColor())));
    }
    return result;
  }
}
