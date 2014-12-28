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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import se.fishtank.css.selectors.NodeSelectorException;
import se.fishtank.css.selectors.generic.GenericNodeAdapter;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.internal.InternalNiftyNode;
import de.lessvoid.nifty.internal.common.ListBuilder;

public class NiftyStyleNodeAdapter implements GenericNodeAdapter<InternalNiftyNode> {
  private final static Logger log = Logger.getLogger(NiftyStyleNodeAdapter.class.getName());
  private final Nifty nifty;
  private final NiftyStyleClassInfoCache classInfoCache;

  public NiftyStyleNodeAdapter(final Nifty nifty, final NiftyStyleClassInfoCache classInfoCache) {
    this.nifty = nifty;
    this.classInfoCache = classInfoCache;
  }

  @Override
  public Map<String, String> getAttributes(final InternalNiftyNode node) {
    try {
      NiftyNode internal = node.getNiftyNode();
      Map<String, String> attr = classInfoCache.getNiftyStyleClass(nifty, internal.getClass()).getProperties(internal);
      log.fine("getAttributes(" + node + ") -> " + attr);
      return attr;
    } catch (Exception e) {
      log.log(Level.WARNING, "failed to get properties for {" + node + "}", e);
      return new HashMap<String, String>();
    }
  }

  @Override
  public List<InternalNiftyNode> getChildNodes(final InternalNiftyNode node) {
    log.fine("getChildNodes(" + node + ") -> " + node);
    return node.getChildren();
  }

  @Override
  public boolean isEmptyNode(final InternalNiftyNode node) {
    log.fine("isEmptyNode(" + node + ") -> " + node);
    return false;
  }

  @Override
  public InternalNiftyNode getPreviousSiblingElement(final InternalNiftyNode node) {
    log.fine("getPreviousSiblingElement(" + node + ") -> " + node);
    return null;
  }

  @Override
  public InternalNiftyNode getNextSiblingElement(final InternalNiftyNode node) {
    log.fine("getNextSiblingElement(" + node + ") -> " + node);
    return null;
  }

  @Override
  public String getNodeName(final InternalNiftyNode node) {
    log.fine("getNodeName(" + node + ") -> " + node);
    return null;
  }

  @Override
  public InternalNiftyNode getRootNode(final InternalNiftyNode node) {
    if (node.isRootNode()) {
      log.fine("getRootNode(" + node + ") -> " + node);
      return node;
    }
    InternalNiftyNode rootNode = getRootNode(node.getParent());
    log.fine("getRootNode(" + node + ") -> " + rootNode);
    return rootNode;
  }

  @Override
  public List<InternalNiftyNode> getNodesByTagName(final InternalNiftyNode node, final String tagName) throws NodeSelectorException {
    ArrayList<InternalNiftyNode> result = new ArrayList<InternalNiftyNode>();
    if (isWildcard(tagName) && node.isRootNode()) {
      result.add(node);
    }
    for (int i=0; i<node.getChildren().size(); i++) {
      InternalNiftyNode child = node.getChildren().get(i);
      if (isWildcard(tagName) || isMatch(tagName, child)) {
        result.add(child);
        result.addAll(getNodesByTagName(child, tagName));
      }
    }
    log.fine("getNodesByTagName(" + node + ", " + tagName + ") -> " + ListBuilder.makeString(result));
    return result;
  }

  @Override
  public String getTextContent(final InternalNiftyNode node) {
    log.fine("getTextContent(" + node + ") -> ");
    return "";
  }

  @Override
  public boolean isElementNode(final InternalNiftyNode node) {
    log.fine("isElementNode(" + node + ") -> true");
    return true;
  }

  @Override
  public boolean isCaseSensitive(final InternalNiftyNode node) {
    log.fine("isCaseSensitive(" + node + ") -> true");
    return true;
  }

  private boolean isWildcard(final String tagName) {
    return tagName.equals("*");
  }

  private boolean isMatch(final String tagName, final InternalNiftyNode child) {
    // TODO figure out a way to handle controls later ...
    return child.getNiftyNode().getClass().getSimpleName().equals(tagName);
  }
}
