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

import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.internal.InternalNiftyNode;
import se.fishtank.css.selectors.NodeSelectorException;
import se.fishtank.css.selectors.generic.GenericNodeAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NiftyStyleNodeAdapter implements GenericNodeAdapter<InternalNiftyNode> {
  private final static Logger log = Logger.getLogger(NiftyStyleNodeAdapter.class.getName());
  private final NiftyStyleClassInfoCache classInfoCache;

  public NiftyStyleNodeAdapter(final NiftyStyleClassInfoCache classInfoCache) {
    this.classInfoCache = classInfoCache;
  }

  @Override
  public Map<String, String> getAttributes(final InternalNiftyNode node) {
    try {
      NiftyNode internal = node.getNiftyNode();
      Map<String, String> attr = classInfoCache.getNiftyStyleClass(internal.getClass()).getProperties(internal);
      System.out.println("getAttributes for node [" + node + "]: " + attr);
      return attr;
    } catch (Exception e) {
      log.log(Level.WARNING, "failed to get properties for {" + node + "}", e);
      return new HashMap<String, String>();
    }
  }

  @Override
  public List<InternalNiftyNode> getChildNodes(final InternalNiftyNode node) {
    System.out.println("getChildNodes: " + node);
    return node.getChildren();
  }

  @Override
  public boolean isEmptyNode(InternalNiftyNode node) {
    System.out.println("isEmptyNode: " + node);
    return false;
  }

  @Override
  public InternalNiftyNode getPreviousSiblingElement(InternalNiftyNode node) {
    System.out.println("getPreviousSiblingElement: " + node);
    return null;
  }

  @Override
  public InternalNiftyNode getNextSiblingElement(InternalNiftyNode node) {
    System.out.println("getNextSiblingElement: " + node);
    return null;
  }

  @Override
  public String getNodeName(InternalNiftyNode node) {
    System.out.println("getNodeName: " + node);
    return null;
  }

  @Override
  public InternalNiftyNode getRootNode(InternalNiftyNode node) {
    System.out.println("getRootNode: " + node);
    if (node.isRootNode()) {
      return node;
    }
    return getRootNode(node.getParent());
  }

  @Override
  public List<InternalNiftyNode> getNodesByTagName(InternalNiftyNode node, String tagName) throws NodeSelectorException {
    System.out.println("getNodesByTagName: " + node);
    // FIXME really check tag
    ArrayList<InternalNiftyNode> result = new ArrayList<InternalNiftyNode>();
    if (node.isRootNode() && tagName.equals("*")) {
      result.add(node);
    }
    for (int i=0; i<node.getChildren().size(); i++) {
      InternalNiftyNode child = node.getChildren().get(i);
      result.add(child);
      result.addAll(getNodesByTagName(child, tagName));
    }
    return result;
  }

  @Override
  public String getTextContent(InternalNiftyNode node) {
    System.out.println("getTextContent: " + node);
    return "";
  }

  @Override
  public boolean isElementNode(InternalNiftyNode node) {
    System.out.println("isElementNode: " + node);
    return true;
  }

  @Override
  public boolean isCaseSensitive(InternalNiftyNode node) {
    System.out.println("isCaseSensitive: " + node);
    return true;
  }
}
