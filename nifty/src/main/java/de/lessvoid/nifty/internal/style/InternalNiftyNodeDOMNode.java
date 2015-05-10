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
package de.lessvoid.nifty.internal.style;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.internal.InternalNiftyNode;
import se.fishtank.css.selectors.dom.DOMNode;

/**
 * {@linkplain se.fishtank.css.selectors.dom.DOMNode} implementation for a
 * {@linkplain de.lessvoid.nifty.internal.InternalNiftyNode}
 */
public class InternalNiftyNodeDOMNode implements DOMNode<InternalNiftyNodeDOMNode, InternalNiftyNode> {
  private final static Logger log = Logger.getLogger(InternalNiftyNodeDOMNode.class.getName());
  private final NiftyStyleClassInfoCache classInfoCache;
  private final Nifty nifty;
  private final InternalNiftyNode node;

  /**
   * Create a new node.
   *
   * @param node
   *          The underlying node.
   */
  public InternalNiftyNodeDOMNode(final InternalNiftyNode node, final Nifty nifty, final NiftyStyleClassInfoCache classInfoCache) {
    this.node = node;
    this.nifty = nifty;
    this.classInfoCache = classInfoCache;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InternalNiftyNode getUnderlying() {
    return node;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Type getType() {
    log.fine("getType(" + this + ") -> " + Type.ELEMENT);
    return Type.ELEMENT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getData() {
    log.fine("getData(" + this + ") -> " + null);
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> getAttributes() {
    try {
      NiftyNode internal = node.getNiftyNode();
      Map<String, String> attr = removeNullValues(classInfoCache.getNiftyStyleClass(nifty, internal.getClass()).getProperties(internal));
      log.fine("getAttributes(" + node + ") -> " + attr);
      return attr;
    } catch (Exception e) {
      log.log(Level.WARNING, "failed to get properties for {" + node + "}", e);
      log.fine("getAttributes(" + this + ") -> failed");
      return new HashMap<String, String>();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InternalNiftyNodeDOMNode getFirstChild() {
    if (node.getChildren().size() == 0) {
      log.fine("getFirstChild(" + this + ") -> null");
      return null;
    }
    InternalNiftyNodeDOMNode result = wrap(node.getChildren().get(0));
    log.fine("getFirstChild(" + this + ") -> " + result);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InternalNiftyNodeDOMNode getPreviousSibling() {
    InternalNiftyNode parent = node.getParent();
    if (parent == null) {
      log.fine("getPreviousSibling(" + this + ") -> null");
      return null;
    }
    int indexInParent = parent.getChildren().indexOf(node);
    if (indexInParent <= 0) {
      log.fine("getPreviousSibling(" + this + ") -> null");
      return null;
    }
    InternalNiftyNodeDOMNode result = wrap(parent.getChildren().get(indexInParent - 1));
    log.fine("getPreviousSibling(" + this + ") -> " + result);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InternalNiftyNodeDOMNode getNextSibling() {
    InternalNiftyNode parent = node.getParent();
    if (parent == null) {
      log.fine("getNextSibling(" + this + ") -> null");
      return null;
    }
    int indexInParent = parent.getChildren().indexOf(node);
    if (indexInParent >= parent.getChildren().size() - 1) {
      log.fine("getNextSibling(" + this + ") -> null");
      return null;
    }
    InternalNiftyNodeDOMNode result = wrap(parent.getChildren().get(indexInParent + 1));
    log.fine("getNextSibling(" + this + ") -> " + result);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InternalNiftyNodeDOMNode getParentNode() {
    InternalNiftyNodeDOMNode result = wrap(node.getParent());
    log.fine("getParentNode(" + this + ") -> " + result);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other) {
      return true;
    }

    if (other == null || getClass() != other.getClass()) {
      return false;
    }

    InternalNiftyNodeDOMNode that = (InternalNiftyNodeDOMNode) other;
    return Objects.equals(node, that.node);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(node);
  }

  @Override
  public String toString() {
    return super.toString() + " (" + node + ")";
  }

  private InternalNiftyNodeDOMNode wrap(final InternalNiftyNode n) {
    return n == null ? null : new InternalNiftyNodeDOMNode(n, nifty, classInfoCache);
  }

  private Map<String, String> removeNullValues(final Map<String, String> properties) {
    Map<String, String> result = new HashMap<String, String>();
    for (Map.Entry<String, String> entry : properties.entrySet()) {
      if (entry.getValue() != null) {
        result.put(entry.getKey(), entry.getValue());
      }
    }
    return result;
  }
}
