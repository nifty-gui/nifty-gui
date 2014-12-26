/**
 * Copyright (c) 2009-2014, Christer Sandberg
 */
package se.fishtank.css.selectors.dom.internal;

import java.util.Set;

import org.w3c.dom.Node;

import se.fishtank.css.selectors.NodeSelectorException;

/**
 * An abstract base class for <em>checkers</em> that traverses nodes.
 * 
 * @author Christer Sandberg
 */
public abstract class NodeTraversalChecker {
    
    /**
     * Check the specified nodes and return a new {@code Set} containing
     * the nodes that passed the check.
     * 
     * @param nodes The nodes to check.
     * @param root The root node.
     * @return A {@link Set} of nodes that passed the check.
     * @throws NodeSelectorException If an error occurred while performing the check.
     */
    public abstract Set<Node> check(Set<Node> nodes, Node root) throws NodeSelectorException;
    
}
