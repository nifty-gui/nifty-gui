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
package de.lessvoid.niftyinternal.style.specialparser;

import java.util.*;

import self.philbrown.cssparser.Token;
import self.philbrown.cssparser.TokenSequence;
import de.lessvoid.nifty.types.NiftyColor;

/**
 * from: http://dev.w3.org/csswg/css-images-3/#linear-gradients
 *
 * 3.1.1. linear-gradient() syntax
 * 
 * The linear gradient syntax is:
 *
 * linear-gradient() = linear-gradient([ <angle> | to <side-or-corner> ]? , <color-stop-list>)
 * <side-or-corner> = [left | right] || [top | bottom]
 *
 * The first argument to the function specifies the gradient line, which gives the gradient a direction and determines
 * how color-stops are positioned. It may be omitted; if so, it defaults to to bottom.
 *
 * The gradient line's direction may be specified in two ways:
 *
 * using angles
 * For the purpose of this argument, 0deg points upward, and positive angles represent clockwise rotation, so
 * 90deg point toward the right.
 *
 * using keywords
 * If the argument is to top, to right, to bottom, or to left, the angle of the gradient line is 0deg, 90deg,
 * 180deg, or 270deg, respectively.
 *
 * If the argument instead specifies a corner of the box such as to top left, the gradient line must be angled
 * such that it points into the same quadrant as the specified corner, and is perpendicular to a line intersecting
 * the two neighboring corners of the gradient box. This causes a color-stop at 50% to intersect the two
 * neighboring corners (see example).
 *
 * Starting from the center of the gradient box, extend a line at the specified angle in both directions. The ending
 * point is the point on the gradient line where a line drawn perpendicular to the gradient line would intersect the
 * corner of the gradient box in the specified direction. The starting point is determined identically, but in the
 * opposite direction.
 *
 * 3.4. Gradient Color-Stops
 *
 * <color-stop-list> = <color-stop>{2,}
 * <color-stop> = <color> [ <percentage> | <length> ]?
 *
 * NOTE: Not everything is supported yet ...
 *
 * @author void
 */
public class LinearGradientParser {
  private static final String LEFT = "left";
  private static final String LEFT_TOP = "left;top";
  private static final String LEFT_BOTTOM = "left;bottom";

  private static final String RIGHT = "right";
  private static final String RIGHT_TOP = "right;top";
  private static final String RIGHT_BOTTOM = "right;bottom";

  private static final String TOP = "top";
  private static final String TOP_LEFT = "top;left";
  private static final String TOP_RIGHT = "top;right";

  private static final String BOTTOM = "bottom";
  private static final String BOTTOM_LEFT = "bottom;left";
  private static final String BOTTOM_RIGHT = "bottom;right";

  private static final String UNIT_DEG = "deg";
  private static final String UNIT_GRAD = "grad";
  private static final String UNIT_RAD = "rad";
  private static final String UNIT_TURN = "turn";

  private static final Map<String, String> toSideOrCornerMap = new HashMap<>();

  static {
    toSideOrCornerMap.put(TOP,          "0");
    toSideOrCornerMap.put(TOP_RIGHT,    "45");
    toSideOrCornerMap.put(RIGHT_TOP,    "45");
    toSideOrCornerMap.put(RIGHT,        "90");
    toSideOrCornerMap.put(RIGHT_BOTTOM, "135");
    toSideOrCornerMap.put(BOTTOM_RIGHT, "135");
    toSideOrCornerMap.put(BOTTOM,       "180");
    toSideOrCornerMap.put(LEFT_BOTTOM,  "225");
    toSideOrCornerMap.put(BOTTOM_LEFT,  "225");
    toSideOrCornerMap.put(LEFT,         "270");
    toSideOrCornerMap.put(LEFT_TOP,     "315");
    toSideOrCornerMap.put(TOP_LEFT,     "315");
  }

  public static class ColorStop {
    private String color;
    private Double pos;

    public ColorStop(final String color) {
      this.color = color;
      this.pos = null;
    }

    public ColorStop(final String color, final Double pos) {
      this.color = color;
      this.pos = pos;
    }

    public String getColor() {
      return color;
    }

    public Double getPos() {
      return pos;
    }

    public String toString() {
      return color + ", " + pos;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((color == null) ? 0 : color.hashCode());
      result = prime * result + ((pos == null) ? 0 : pos.hashCode());
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
      ColorStop other = (ColorStop) obj;
      if (color == null) {
        if (other.color != null)
          return false;
      } else if (!color.equals(other.color))
        return false;
      if (pos == null) {
        if (other.pos != null)
          return false;
      } else if (!pos.equals(other.pos))
        return false;
      return true;
    }
  }

  public static class Result {
    private double angleInRadiants;
    private List<ColorStop> colorStops = new ArrayList<>();

    public Result(final double angle) {
      this.angleInRadiants = angle;
    }

    public double getAngleInRadiants() {
      return angleInRadiants;
    }

    public void addStop(final ColorStop colorStop) {
      colorStops.add(colorStop);
    }

    public List<ColorStop> getStops() {
      return Collections.unmodifiableList(colorStops);
    }
  }

  public Result parse(final String value) throws Exception {
    Queue<Token> tokenSeq = new LinkedList<>(TokenSequence.parse(value).getTokens());
    assertIdentifier(tokenSeq, "linear-gradient");
    assertLeftParenthesis(tokenSeq);

    Result result;

    // angle mode
    if (peekNumber(tokenSeq)) {
      String number = readNumber(tokenSeq);
      String angleUnit = assertAngleUnit(tokenSeq);
      assertComma(tokenSeq);
      result = makeNiftyLinearGradientFromAngle(number, angleUnit);
    } else if (peekIdentifier(tokenSeq, "to")) {
      tokenSeq.poll(); // skip 'to' identifier
      String sideOrCorner = assertSideOrCorner(tokenSeq);
      assertComma(tokenSeq);
      result = makeNiftyLinearGradientFromAngle(gradientAngleFromTo(sideOrCorner), "deg");
    } else {
      result = makeNiftyLinearGradientFromAngle(gradientAngleFromTo(BOTTOM), "deg");
    }
    result.addStop(assertColorWithOptionalStop(tokenSeq));

    while (tokenSeq.peek() != null && tokenSeq.peek().tokenCode != Token.RIGHTPAREN) {
      assertComma(tokenSeq);
      result.addStop(assertColorWithOptionalStop(tokenSeq));
    }

    assertRightParenthesis(tokenSeq);
    assertSemicolon(tokenSeq);
    return result;
  }

  private String readNumber(final Queue<Token> tokenSeq) throws Exception {
    StringBuilder number = new StringBuilder();
    while (peekNumber(tokenSeq)) {
      Token next = tokenSeq.poll();
      if (next.tokenCode == Token.IDENTIFIER) {
        number.append(next.attribute);
      } else if (next.tokenCode == Token.DOT) {
        number.append('.');
      } else {
        number.append(next.attribute);
      }
    }
    return number.toString();
  }

  private String readColor(final Queue<Token> tokenSeq) throws Exception {
    StringBuilder number = new StringBuilder();
    while (peekColorPart(tokenSeq)) {
      Token next = tokenSeq.poll();
      number.append(next.attribute);
      if (number.length() == 3 || number.length() == 6) {
        return number.toString();
      }
    }
    return number.toString();
  }

  private String assertSideOrCorner(final Queue<Token> tokenSeq) throws Exception {
    Token first = assertNext(tokenSeq);
    if (first.tokenCode != Token.IDENTIFIER) {
      throw new Exception("expected identifier token after 'to' but was (" + first.toDebugString() + ")");
    }
    Set<String> supportedSidesH = new TreeSet<>();
    supportedSidesH.add(LEFT);
    supportedSidesH.add(RIGHT);

    Set<String> supportedSidesV = new TreeSet<>();
    supportedSidesV.add(TOP);
    supportedSidesV.add(BOTTOM);

    if (supportedSidesH.contains(first.attribute.toLowerCase())) {
      if (peekIdentifier(tokenSeq)) {
        Token second = assertNext(tokenSeq);
        if (supportedSidesV.contains(second.attribute.toLowerCase())) {
          return first.attribute.toLowerCase() + ";" + second.attribute.toLowerCase();
        }
        throw new Exception("expecting (" + supportedSidesV + ") after (" + first.attribute + ")");
      }
      return first.attribute.toLowerCase();
    }

    if (supportedSidesV.contains(first.attribute.toLowerCase())) {
      if (peekIdentifier(tokenSeq)) {
        Token second = assertNext(tokenSeq);
        if (supportedSidesH.contains(second.attribute.toLowerCase())) {
          return first.attribute.toLowerCase() + ";" + second.attribute.toLowerCase();
        }
        throw new Exception("expecting (" + supportedSidesH + ") after (" + first.attribute + ")");
      }
      return first.attribute.toLowerCase();
    }

    throw new Exception("expecting (" + supportedSidesH + ") or (" + supportedSidesV + ")");
}

  private boolean peekIdentifier(final Queue<Token> tokenSeq, final String expectedIdentifier) {
    Token next = tokenSeq.peek();
    if (next == null) {
      return false;
    }
    if (next.tokenCode != Token.IDENTIFIER) {
      return false;
    }
    return (next.attribute.equals(expectedIdentifier));
  }

  private boolean peekIdentifier(final Queue<Token> tokenSeq) {
    Token next = tokenSeq.peek();
    if (next == null) {
      return false;
    }
    return (next.tokenCode == Token.IDENTIFIER);
  }

  private String assertAngleUnit(final Queue<Token> tokenSeq) throws Exception {
    Token next = assertNext(tokenSeq);
    if (next.tokenCode != Token.IDENTIFIER) {
      throw new Exception("expected angle unit identifier here but was (" + next.toDebugString() + ")");
    }
    Set<String> supportedUnits = new HashSet<>();
    supportedUnits.add(UNIT_DEG);
    supportedUnits.add(UNIT_GRAD);
    supportedUnits.add(UNIT_RAD);
    supportedUnits.add(UNIT_TURN);
    if (!supportedUnits.contains(next.attribute.toLowerCase())) {
      throw new Exception("unsupported angle unit (" + next.attribute + ")");
    }
    return next.attribute.toLowerCase();
  }

  private void assertIdentifier(final Queue<Token> tokenSeq, final String expected) throws Exception {
    Token nextElement = assertNext(tokenSeq);
    assertTokenCode(nextElement, Token.IDENTIFIER);
    assertTokenAttribute(nextElement, expected);
  }

  private void assertLeftParenthesis(final Queue<Token> tokenSeq) throws Exception {
    assertTokenCode(assertNext(tokenSeq), Token.LEFTPAREN);
  }

  private void assertRightParenthesis(final Queue<Token> tokenSeq) throws Exception {
    assertTokenCode(assertNext(tokenSeq), Token.RIGHTPAREN);
  }

  private void assertSemicolon(final Queue<Token> tokenSeq) throws Exception {
    assertTokenCode(assertNext(tokenSeq), Token.SEMICOLON);
  }

  private ColorStop assertColorWithOptionalStop(final Queue<Token> tokenSeq) throws Exception {
    String color = assertColor(tokenSeq);
    Double pos = assertOptionalStep(tokenSeq);
    return new ColorStop(color, pos);
  }

  private String assertColor(final Queue<Token> tokenSeq) throws Exception {
    Token nextElement = assertNext(tokenSeq);
    if (nextElement.tokenCode == Token.IDENTIFIER) {
      assertTokenCode(nextElement, Token.IDENTIFIER);
      return assertTokenAttributeColor(nextElement);
    } else if (nextElement.tokenCode == Token.HASH) {
      String colorValue = readColor(tokenSeq);
      if (!NiftyColor.isColor("#" + colorValue)) {
        throw new Exception("expected color value but was (" + "#" + colorValue + ")");
      }
      return "#" + colorValue;
    } else {
      throw new Exception("expected color token here but was (" + nextElement.toDebugString() + ")");
    }
  }

  private Double assertOptionalStep(final Queue<Token> tokenSeq) throws Exception {
    if (!peekNumber(tokenSeq)) {
      return null;
    }
    String number = readNumber(tokenSeq);
    assertPercent(tokenSeq);
    return Double.valueOf(number) / 100.;
  }

  private boolean peekNumber(final Queue<Token> tokenSeq) throws Exception {
    Token nextElement = tokenSeq.peek();
    if (nextElement == null) {
      return false;
    }
    return (
        nextElement.tokenCode == Token.NUMBER ||
        nextElement.tokenCode == Token.DOT ||
          (
            nextElement.tokenCode == Token.IDENTIFIER &&
            nextElement.attribute != null &&
            nextElement.attribute.startsWith("-")
          )
        );
  }

  private boolean peekColorPart(final Queue<Token> tokenSeq) throws Exception {
    Token nextElement = tokenSeq.peek();
    if (nextElement == null) {
      return false;
    }
    return (
        nextElement.tokenCode == Token.NUMBER ||
        nextElement.tokenCode == Token.IDENTIFIER);
  }

  private void assertPercent(final Queue<Token> tokenSeq) throws Exception {
    assertTokenCode(assertNext(tokenSeq), Token.PERCENT);
  }

  private void assertComma(final Queue<Token> tokenSeq) throws Exception {
    assertTokenCode(assertNext(tokenSeq), Token.COMMA);
  }

  private String assertTokenAttributeColor(final Token token) throws Exception {
    if (!NiftyColor.isColor(token.attribute)) {
      throw new Exception("expected color value but was (" + token.attribute + ")");
    }
    return token.attribute;
  }

  private Token assertNext(final Queue<Token> tokenSeq) throws Exception {
    if (tokenSeq.peek() == null) {
      throw new Exception("no more tokens");
    }
    return tokenSeq.poll();
  }

  private void assertTokenCode(final Token token, final int ... identifier) throws Exception {
    for (int x : identifier) {
      if (token.tokenCode == x) {
        return;
      }
    }
    throw new Exception("unexpected token (" + token.toDebugString() + ") in token stream");
  }

  private void assertTokenAttribute(final Token token, final String expectedAttribute) throws Exception {
    if (!expectedAttribute.equals(token.attribute)) {
      throw new Exception("expected token attribute (" + expectedAttribute + ") but got (" + token.toDebugString() + ")");
    }
  }

  private Result makeNiftyLinearGradientFromAngle(final String angleValue, final String unit) {
    return new Result(convertToRadiants(Float.parseFloat(angleValue), unit));
  }

  private String gradientAngleFromTo(final String sideOrCorner) {
    return toSideOrCornerMap.get(sideOrCorner);
  }

  private float convertToRadiants(final float value, final String unit) {
    if (UNIT_DEG.equals(unit)) {
      return (float) Math.PI * 2 * value / 360.f;
    }
    if (UNIT_GRAD.equals(unit)) {
      return (float) Math.PI * 2 * value / 400.f;
    }
    if (UNIT_TURN.equals(unit)) {
      return (float) Math.PI * 2 * value;
    }
    return value;
  }
}
