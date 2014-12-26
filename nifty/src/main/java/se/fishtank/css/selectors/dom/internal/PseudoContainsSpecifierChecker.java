/**
 * Copyright (c) 2014, John Heintz and Christer Sandberg
 */
package se.fishtank.css.selectors.dom.internal;

import java.util.LinkedHashSet;
import java.util.Set;

import org.w3c.dom.Node;

import se.fishtank.css.selectors.NodeSelectorException;
import se.fishtank.css.selectors.specifier.PseudoContainsSpecifier;
import se.fishtank.css.util.Assert;

/**
 * A {@link NodeTraversalChecker} that check if a node matches
 * the {@linkplain PseudoContainsSpecifier pseudo-class specifier} set.
 * 
 * Checks for {@code a:contains('some text')} selector matches.
 * 
 * @author John Heintz
 * @author Christer Sandberg
 */
public class PseudoContainsSpecifierChecker extends NodeTraversalChecker {
    
    /** The pseudo-class specifier to check against. */
    private final PseudoContainsSpecifier specifier;
    
    /**
     * Create a new instance.
     * 
     * @param specifier The pseudo-class specifier to check against.
     */
    public PseudoContainsSpecifierChecker(PseudoContainsSpecifier specifier) {
        Assert.notNull(specifier, "specifier is null!");
        this.specifier = specifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Node> check(Set<Node> nodes, Node root) throws NodeSelectorException {
        Assert.notNull(nodes, "nodes is null!");
        Assert.notNull(root, "root is null!");
        LinkedHashSet<Node> result = new LinkedHashSet<Node>();
        String value = specifier.getValue();
        for (Node node : nodes) {
            String content = node.getTextContent();
            if (content != null && content.contains(value)) {
        		result.add(node);
        	}
        }
        
        return result;
    }
}
