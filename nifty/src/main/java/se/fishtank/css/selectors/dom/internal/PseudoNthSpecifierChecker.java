/**
 * Copyright (c) 2009-2014, Christer Sandberg
 */
package se.fishtank.css.selectors.dom.internal;

import java.util.LinkedHashSet;
import java.util.Set;

import org.w3c.dom.Node;

import se.fishtank.css.selectors.NodeSelectorException;
import se.fishtank.css.selectors.dom.DOMHelper;
import se.fishtank.css.selectors.specifier.PseudoNthSpecifier;
import se.fishtank.css.util.Assert;

/**
 * A {@link NodeTraversalChecker} that check if a node matches
 * the {@code nth-*} {@linkplain PseudoNthSpecifier pseudo-class specifier} set.
 * 
 * @author Christer Sandberg
 */
public class PseudoNthSpecifierChecker extends NodeTraversalChecker {
    
    /** The {@code nth-*} pseudo-class specifier to check against. */
    private final PseudoNthSpecifier specifier;
    
    /** The set of nodes to check. */
    private Set<Node> nodes;
    
    /** The result of the checks. */
    private Set<Node> result;
    
    /**
     * Create a new instance.
     * 
     * @param specifier The {@code nth-*} pseudo-class specifier to check against.
     */
    public PseudoNthSpecifierChecker(PseudoNthSpecifier specifier) {
        Assert.notNull(specifier, "specifier is null!");
        this.specifier = specifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Node> check(Set<Node> nodes, Node root) throws NodeSelectorException {
        Assert.notNull(nodes, "nodes is null!");
        this.nodes = nodes;
        result = new LinkedHashSet<Node>();
        String value = specifier.getValue();
        if ("nth-child".equals(value)) {
            addNthChild();
        } else if ("nth-last-child".equals(value)) {
            addNthLastChild();
        } else if ("nth-of-type".equals(value)) {
            addNthOfType();
        } else if ("nth-last-of-type".equals(value)) {
            addNthLastOfType();
        } else {
            throw new NodeSelectorException("Unknown pseudo nth class: " + value);
        }
        
        return result;
    }
    
    /**
     * Add the {@code :nth-child} elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#nth-child-pseudo"><code>:nth-child</code> pseudo-class</a>
     */
    private void addNthChild() {
        for (Node node : nodes) {
            int count = 1;
            Node n = DOMHelper.getPreviousSiblingElement(node);
            while (n != null) {
                count++;
                n = DOMHelper.getPreviousSiblingElement(n);
            }
            
            if (specifier.isMatch(count)) {
                result.add(node);
            }
        }
    }
    
    /**
     * Add {@code :nth-last-child} elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#nth-last-child-pseudo"><code>:nth-last-child</code> pseudo-class</a>
     */
    private void addNthLastChild() {
        for (Node node : nodes) {
            int count = 1;
            Node n = DOMHelper.getNextSiblingElement(node);
            while (n != null) {
                count++;
                n = DOMHelper.getNextSiblingElement(n);
            }
            
            if (specifier.isMatch(count)) {
                result.add(node);
            }
        }
    }
    
    /**
     * Add {@code :nth-of-type} elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#nth-of-type-pseudo"><code>:nth-of-type</code> pseudo-class</a>
     */
    private void addNthOfType() {
        for (Node node : nodes) {
            int count = 1;
            Node n = DOMHelper.getPreviousSiblingElement(node);
            while (n != null) {
                if (n.getNodeName().equals(node.getNodeName())) {
                    count++;
                }
                
                n = DOMHelper.getPreviousSiblingElement(n);
            }
            
            if (specifier.isMatch(count)) {
                result.add(node);
            }
        }
    }
    
    /**
     * Add {@code nth-last-of-type} elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#nth-last-of-type-pseudo"><code>:nth-last-of-type</code> pseudo-class</a>
     */
    private void addNthLastOfType() {
        for (Node node : nodes) {
            int count = 1;
            Node n = DOMHelper.getNextSiblingElement(node);
            while (n != null) {
                if (n.getNodeName().equals(node.getNodeName())) {
                    count++;
                }
                
                n = DOMHelper.getNextSiblingElement(n);
            }
            
            if (specifier.isMatch(count)) {
                result.add(node);
            }
        }
    }
    
}
