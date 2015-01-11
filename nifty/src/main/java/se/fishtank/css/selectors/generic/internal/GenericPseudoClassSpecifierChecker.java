package se.fishtank.css.selectors.generic.internal;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import se.fishtank.css.selectors.NodeSelectorException;
import se.fishtank.css.selectors.dom.internal.NodeTraversalChecker;
import se.fishtank.css.selectors.generic.GenericNodeAdapter;
import se.fishtank.css.selectors.specifier.PseudoClassSpecifier;
import se.fishtank.css.util.Assert;

/**
 * A {@link NodeTraversalChecker} that check if a node matches
 * the {@linkplain PseudoClassSpecifier pseudo-class specifier} set.
 * 
 * @author Christer Sandberg
 * @author Jens Hohmuth
 */
public class GenericPseudoClassSpecifierChecker<E> extends GenericNodeTraversalChecker<E> {
    
    /** The generic node adapter */
    private final GenericNodeAdapter<E> nodeAdapter;

    /** The pseudo-class specifier to check against. */
    private final PseudoClassSpecifier specifier;
    
    /** The set of nodes to check. */
    private Set<E> nodes;

    /** The root node. */
    private E root;
    
    /** The result of the checks. */
    private Set<E> result;
    
    /**
     * Create a new instance.
     * 
     * @param specifier The pseudo-class specifier to check against.
     */
    public GenericPseudoClassSpecifierChecker(GenericNodeAdapter<E> nodeAdapter, PseudoClassSpecifier specifier) {
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
        Assert.notNull(root, "root is null!");
        this.nodes = nodes;
        this.root = root;
        result = new LinkedHashSet<E>();
        String value = specifier.getValue();
        if ("empty".equals(value)) {
            addEmptyElements();
        } else if ("first-child".equals(value)) {
            addFirstChildElements();
        } else if ("first-of-type".equals(value)) {
            addFirstOfType();
        } else if ("last-child".equals(value)) {
            addLastChildElements();
        } else if ("last-of-type".equals(value)) {
            addLastOfType();
        } else if ("only-child".equals(value)) {
            addOnlyChildElements();
        } else if ("only-of-type".equals(value)) {
            addOnlyOfTypeElements();
        } else if ("root".equals(value)) {
            addRootElement();
        } else {
            throw new NodeSelectorException("Unknown pseudo class: " + value);
        }
        
        return result;
    }

    /**
     * Add {@code :empty} elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#empty-pseudo"><code>:empty</code> pseudo-class</a>
     */
    private void addEmptyElements() {
        for (E node : nodes) {
            boolean empty = true;
            List<E> nl = nodeAdapter.getChildNodes(node);
            for (int i = 0; i < nl.size(); i++) {
                E n = nl.get(i);
                if (nodeAdapter.isEmptyNode(n)) {
                    empty = false;
                    break;
                }
            }
            
            if (empty) {
                result.add(node);
            }
        }
    }
    
    /**
     * Add {@code :first-child} elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#first-child-pseudo"><code>:first-child</code> pseudo-class</a>
     */
    private void addFirstChildElements() {
        for (E node : nodes) {
            if (nodeAdapter.getPreviousSiblingElement(node) == null) {
                result.add(node);
            }
        }
    }
    
    /**
     * Add {@code :first-of-type} elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#first-of-type-pseudo"><code>:first-of-type</code> pseudo-class</a>
     */
    private void addFirstOfType() {
        for (E node : nodes) {
            E n = nodeAdapter.getPreviousSiblingElement(node);
            while (n != null) {
                String nodeName = nodeAdapter.getNodeName(n);
                if (nodeName != null && nodeName.equals(nodeAdapter.getNodeName(node))) {
                    break;
                }
                
                n = nodeAdapter.getPreviousSiblingElement(n);
            }
            
            if (n == null) {
                result.add(node);
            }
        }
    }

    /**
     * Add {@code :last-child} elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#last-child-pseudo"><code>:last-child</code> pseudo-class</a>
     */
    private void addLastChildElements() {
        for (E node : nodes) {
            if (nodeAdapter.getNextSiblingElement(node) == null) {
                result.add(node);
            }
        }
    }
    
    /**
     * Add {@code :last-of-type} elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#last-of-type-pseudo"><code>:last-of-type</code> pseudo-class</a>
     */
    private void addLastOfType() {
        for (E node : nodes) {
            E n = nodeAdapter.getNextSiblingElement(node);
            while (n != null) {
              String nodeName = nodeAdapter.getNodeName(n);
                if (nodeName != null && nodeName.equals(nodeAdapter.getNodeName(node))) {
                    break;
                }
                
                n = nodeAdapter.getNextSiblingElement(n);
            }
            
            if (n == null) {
                result.add(node);
            }
        }
    }
    
    /**
     * Add {@code :only-child} elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#only-child-pseudo"><code>:only-child</code> pseudo-class</a>
     */
    private void addOnlyChildElements() {
        for (E node : nodes) {
            if (nodeAdapter.getPreviousSiblingElement(node) == null &&
                nodeAdapter.getNextSiblingElement(node) == null) {
                result.add(node);
            }
        }
    }
    
    /**
     * Add {@code :only-of-type} elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#only-of-type-pseudo"><code>:only-of-type</code> pseudo-class</a>
     */
    private void addOnlyOfTypeElements() {
        for (E node : nodes) {
            E n = nodeAdapter.getPreviousSiblingElement(node);
            while (n != null) {
                String nodeName = nodeAdapter.getNodeName(n);
                if (nodeName != null && nodeName.equals(nodeAdapter.getNodeName(node))) {
                    break;
                }
                
                n = nodeAdapter.getPreviousSiblingElement(n);
            }
            
            if (n == null) {
                n = nodeAdapter.getNextSiblingElement(node);
                while (n != null) {
                    String nodeName = nodeAdapter.getNodeName(n);
                    if (nodeName != null && nodeName.equals(nodeAdapter.getNodeName(node))) {
                        break;
                    }
                    
                    n = nodeAdapter.getNextSiblingElement(n);
                }
                
                if (n == null) {
                    result.add(node);
                }
            }
        }
    }

    /**
     * Add the {@code :root} element.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#root-pseudo"><code>:root</code> pseudo-class</a>
     */
    private void addRootElement() {
        E rootNode = nodeAdapter.getRootNode(root);
        if (rootNode != null) {
          result.add(rootNode);
        }
    }
    
}
