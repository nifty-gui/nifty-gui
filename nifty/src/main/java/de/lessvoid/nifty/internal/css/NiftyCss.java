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
package de.lessvoid.nifty.internal.css;

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
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.internal.InternalNiftyNode;
import de.lessvoid.nifty.internal.accessor.NiftyNodeAccessor;

public class NiftyCss {
  private final static Logger log = Logger.getLogger(NiftyCss.class.getName());
  private NiftyCssClassInfoCache classInfoCache = new NiftyCssClassInfoCache();

  public void applyCss(final InputStream source, final List<NiftyNode> rootNodes) throws IOException {
    CSSParser parser = new CSSParser(source, new NiftyNodeCSSHandler(rootNodes, classInfoCache));
    parser.parse();
  }

  private static class NiftyNodeCSSHandler implements CSSHandler {
    private final NiftyCssClassInfoCache classInfoCache;
    private final List<InternalNiftyNode> rootNodes = new ArrayList<InternalNiftyNode>();
    private final NiftyCssNodeAdapter nodeAdapter;

    NiftyNodeCSSHandler(final List<NiftyNode> rootNodes, final NiftyCssClassInfoCache classInfoCache) {
      this.classInfoCache = classInfoCache;
      this.nodeAdapter = new NiftyCssNodeAdapter(classInfoCache);

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
          log.fine("found: " + result.size());
          for (InternalNiftyNode r : result) {
            applyRuleSet(r, ruleSet);
          }
        } catch (Exception e) {
          log.log(Level.WARNING, "Exception querying with selector", e);
        }
      }
    }

    private void applyRuleSet(final InternalNiftyNode node, final RuleSet ruleSet) throws Exception {
      NiftyNode internal = node.getNiftyNode();
      NiftyCssClassInfo classInfo = classInfoCache.getNiftyCssClass(internal.getClass());
      for (int i=0; i<ruleSet.getDeclarationBlock().size(); i++) {
        Declaration decleration = ruleSet.getDeclarationBlock().get(i);
        String prop = decleration.getProperty().toString();
        String value = decleration.getValue().toString();
        log.fine("Applying rule: {" + prop + "} -> {" + value + "}");
        classInfo.writeValue(internal, prop, value);
      }
    }
  }
}
