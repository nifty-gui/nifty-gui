package se.fishtank.css.selectors.generic.internal;

import java.util.Set;

import se.fishtank.css.selectors.NodeSelectorException;

/**
 * An abstract base class for <em>checkers</em> that traverses nodes.
 * 
 * @author Christer Sandberg
 * @author Jens Hohmuth
 */
public abstract class GenericNodeTraversalChecker<E> {
    
    /**
     * Check the specified nodes and return a new {@code Set} containing
     * the nodes that passed the check.
     * 
     * @param nodes The nodes to check.
     * @param root The root node.
     * @return A {@link Set} of nodes that passed the check.
     * @throws NodeSelectorException If an error occurred while performing the check.
     */
    public abstract Set<E> check(Set<E> nodes, E root) throws NodeSelectorException;
    
}
