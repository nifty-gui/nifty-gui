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
package de.lessvoid.nifty.internal.style;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import se.fishtank.css.selectors.NodeSelector;
import se.fishtank.css.selectors.generic.GenericNodeSelector;
import self.philbrown.cssparser.CSSHandler;
import self.philbrown.cssparser.CSSParser;
import self.philbrown.cssparser.Declaration;
import self.philbrown.cssparser.FontFace;
import self.philbrown.cssparser.KeyFrame;
import self.philbrown.cssparser.RuleSet;
import self.philbrown.cssparser.TokenSequence;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.internal.InternalNiftyNode;
import de.lessvoid.nifty.internal.accessor.NiftyNodeAccessor;
import de.lessvoid.nifty.internal.common.ListBuilder;

public class NiftyStyle {
  private final static Logger log = Logger.getLogger(NiftyStyle.class.getName());
  private NiftyStyleClassInfoCache classInfoCache = new NiftyStyleClassInfoCache();

  public void applyStyle(final Nifty nifty, final InputStream source, final List<NiftyNode> rootNodes) throws IOException {
    CSSParser parser = new CSSParser(source, new NiftyNodeCSSHandler(nifty, rootNodes, classInfoCache));
    parser.parse();
  }

  private static class NiftyNodeCSSHandler implements CSSHandler {
    private final Nifty nifty;
    private final NiftyStyleClassInfoCache classInfoCache;
    private final List<InternalNiftyNode> rootNodes = new ArrayList<InternalNiftyNode>();
    private final NiftyStyleNodeAdapter nodeAdapter;

    NiftyNodeCSSHandler(final Nifty nifty, final List<NiftyNode> rootNodes, final NiftyStyleClassInfoCache classInfoCache) {
      this.nifty = nifty;
      this.classInfoCache = classInfoCache;
      this.nodeAdapter = new NiftyStyleNodeAdapter(nifty, classInfoCache);

      for (int i=0; i<rootNodes.size(); i++) {
        this.rootNodes.add(NiftyNodeAccessor.getDefault().getInternalNiftyNode(rootNodes.get(i)));
      }
    }

    @Override
    public void handleError(String error, Throwable t) {
      // TODO Auto-generated method stub

    }

    @Override
    public InputStream handleImport(TokenSequence importSequence) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public void handleNewCharset(String charset) {
      // TODO Auto-generated method stub

    }

    @Override
    public void handleNamespace(String namespace) {
      // TODO Auto-generated method stub

    }

    @Override
    public boolean supports(TokenSequence logic) {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public void handleKeyframes(String identifier, List<KeyFrame> keyframes) {
      // TODO Auto-generated method stub

    }

    @Override
    public void handleFontFace(FontFace font) {
      // TODO Auto-generated method stub

    }

    @Override
    public boolean queryMedia(TokenSequence[] types) {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public void handlePage(TokenSequence pseudoClass, List<Declaration> properties) {
      // TODO Auto-generated method stub

    }

    @Override
    public boolean queryDocument(TokenSequence[] functions) {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public void handleRuleSet(final RuleSet ruleSet) {
      for (int i=0; i<rootNodes.size(); i++) {
        NodeSelector<InternalNiftyNode> selector = new GenericNodeSelector<InternalNiftyNode>(nodeAdapter, rootNodes.get(i));
        try {
          Set<InternalNiftyNode> result = selector.querySelectorAll(ruleSet.getSelector().toString());
          log.fine("found: " + result.size() + " " + ListBuilder.makeString(new ArrayList<InternalNiftyNode>(result)));
          for (InternalNiftyNode r : result) {
            applyRuleSet(r, ruleSet);
          }
        } catch (Exception e) {
          log.log(Level.WARNING, "Exception querying with selector", e);
        }
      }
    }

    private void applyRuleSet(final InternalNiftyNode node, final RuleSet ruleSet) throws Exception {
      applyRuleSetInternal(ruleSet, node.getControl());
      applyRuleSetInternal(ruleSet, node.getNiftyNode());
    }

    private void applyRuleSetInternal(final RuleSet ruleSet, Object object) throws Exception {
      if (object == null) {
        return;
      }
      NiftyStyleClassInfo classInfo = classInfoCache.getNiftyStyleClass(nifty, object.getClass());
      for (int i=0; i<ruleSet.getDeclarationBlock().size(); i++) {
        Declaration decleration = ruleSet.getDeclarationBlock().get(i);

        TokenSequence tokenSequence = decleration.getValue();

        String prop = decleration.getProperty().toString();
        String value = decleration.getValue().toString();
        if (classInfo.writeValue(object, prop, value)) {
          log.fine("Applying rule to (" + object + "): {" + prop + "} -> {" + value + "}");  
        }
      }
    }
  }
}
