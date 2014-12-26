package se.fishtank.css.selectors.generic.internal;

import java.util.LinkedHashSet;
import java.util.Set;

import se.fishtank.css.selectors.NodeSelectorException;
import se.fishtank.css.selectors.dom.internal.NodeTraversalChecker;
import se.fishtank.css.selectors.generic.GenericNodeAdapter;
import se.fishtank.css.selectors.specifier.PseudoNthSpecifier;
import se.fishtank.css.util.Assert;

/**
 * A {@link NodeTraversalChecker} that check if a node matches
 * the {@code nth-*} {@linkplain PseudoNthSpecifier pseudo-class specifier} set.
 * 
 * @author Christer Sandberg
 * @author Jens Hohmuth
 */
public class GenericPseudoNthSpecifierChecker<E> extends GenericNodeTraversalChecker<E> {
    
    /** The generic node adapter */
    private final GenericNodeAdapter<E> nodeAdapter;

    /** The {@code nth-*} pseudo-class specifier to check against. */
    private final PseudoNthSpecifier specifier;
    
    /** The set of nodes to check. */
    private Set<E> nodes;
    
    /** The result of the checks. */
    private Set<E> result;
    
    /**
     * Create a new instance.
     * 
     * @param specifier The {@code nth-*} pseudo-class specifier to check against.
     */
    public GenericPseudoNthSpecifierChecker(GenericNodeAdapter<E> nodeAdapter, PseudoNthSpecifier specifier) {
        Assert.notNull(nodeAdapter, "nodeAdapter is null!");
        Assert.notNull(specifier, "specifier is null!");

        this.nodeAdapter = nodeAdapter;
        this.specifier = specifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<E> check(Set<E> nodes, E root) throws NodeSelectorException {
        Assert.notNull(nodes, "nodes is null!");
        this.nodes = nodes;
        result = new LinkedHashSet<E>();
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
        for (E node : nodes) {
            int count = 1;
            E n = nodeAdapter.getPreviousSiblingElement(node);
            while (n != null) {
                count++;
                n = nodeAdapter.getPreviousSiblingElement(n);
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
        for (E node : nodes) {
            int count = 1;
            E n = nodeAdapter.getNextSiblingElement(node);
            while (n != null) {
                count++;
                n = nodeAdapter.getNextSiblingElement(n);
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
        for (E node : nodes) {
            int count = 1;
            E n = nodeAdapter.getPreviousSiblingElement(node);
            while (n != null) {
                String nodeName = nodeAdapter.getNodeName(n);
                if (nodeName != null && nodeName.equals(nodeAdapter.getNodeName(node))) {
                    count++;
                }
                
                n = nodeAdapter.getPreviousSiblingElement(n);
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
        for (E node : nodes) {
            int count = 1;
            E n = nodeAdapter.getNextSiblingElement(node);
            while (n != null) {
                String nodeName = nodeAdapter.getNodeName(n);
                if (nodeName != null && nodeName.equals(nodeAdapter.getNodeName(node))) {
                    count++;
                }
                
                n = nodeAdapter.getNextSiblingElement(n);
            }
            
            if (specifier.isMatch(count)) {
                result.add(node);
            }
        }
    }
    
}