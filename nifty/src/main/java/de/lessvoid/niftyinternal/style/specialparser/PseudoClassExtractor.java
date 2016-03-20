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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import self.philbrown.cssparser.Token;

public class PseudoClassExtractor {
  private final static Set<Integer> PSEUDO_CLASSES = new HashSet<>();
  {
    PSEUDO_CLASSES.add(Token.HOVER);
    PSEUDO_CLASSES.add(Token.ACTIVE);
    PSEUDO_CLASSES.add(Token.FOCUS);
    PSEUDO_CLASSES.add(Token.ENABLED);
    PSEUDO_CLASSES.add(Token.DISABLED);
    PSEUDO_CLASSES.add(Token.CHECKED);
  }

  public List<String> parse(final List<Token> tokenList) {
    List<String> result = new ArrayList<>();

    Queue<Token> tokenSeq = new LinkedList<>(tokenList);
    while (tokenSeq.peek() != null) {
      Token colon = scanToColon(tokenSeq);
      if (colon != null) {
        Token identifier = identifier(tokenSeq);
        if (identifier != null) {
          result.add(identifier.attribute);
        }
      }
    }

    return result;
  }

  public String extractSelector(final List<Token> tokenList) {
    StringBuilder result = new StringBuilder();
    Queue<Token> tokenSeq = new LinkedList<>(tokenList);
    while (tokenSeq.peek() != null) {
      Token next = tokenSeq.poll();
      while (next != null && next.tokenCode != Token.COLON) {
        result.append(next.toString());
        next = tokenSeq.poll();
      }
      if (next == null) {
        break;
      }
      Token colon = next;
      if (tokenSeq.peek() == null) {
        result.append(colon.toString());
        break;
      }
      Token identifier = tokenSeq.poll();
      if (PSEUDO_CLASSES.contains(identifier.tokenCode)) {
        break;
      }
      result.append(colon.toString());
      result.append(identifier.toString());
    }
    return result.toString();
  }

  private Token scanToColon(final Queue<Token> tokenSeq) {
    Token next = tokenSeq.poll();
    while (next != null && next.tokenCode != Token.COLON) {
      next = tokenSeq.poll();
    }
    return next;
  }

  private Token identifier(final Queue<Token> tokenSeq) {
    Token next = tokenSeq.poll();
    if (PSEUDO_CLASSES.contains(next.tokenCode)) {
      return next;
    }
    return null;
  }
}
