package se.fishtank.css.selectors.generic.internal;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import se.fishtank.css.selectors.NodeSelectorException;
import se.fishtank.css.selectors.Selector;
import se.fishtank.css.selectors.dom.internal.NodeTraversalChecker;
import se.fishtank.css.selectors.generic.GenericNodeAdapter;
import se.fishtank.css.util.Assert;

/**
 * A {@link NodeTraversalChecker} that check if a node
 * matches the {@linkplain Selector#getTagName() tag name} and
 * {@linkplain Selector#getCombinator() combinator} of the {@link Selector} set.
 * 
 * @author Christer Sandberg
 * @author Jens Hohmuth
 */
public class GenericTagChecker<E> extends GenericNodeTraversalChecker<E> {

    /** The GenericNodeAdapter to use */
    private GenericNodeAdapter<E> nodeAdapter;

    /** The selector to check against. */
    private final Selector selector;
    
    /** The set of nodes to check. */
    private Set<E> nodes;
    
    /** The result of the checks. */
    private Set<E> result;

    /** Whether the underlying DOM is case sensitive. */
    private boolean caseSensitive;
    
    /**
     * Create a new instance.
     * 
     * @param selector The selector to check against.
     */
    public GenericTagChecker(GenericNodeAdapter<E> nodeAdapter, Selector selector) {
        Assert.notNull(nodeAdapter, "nodeAdapter is null!");
        Assert.notNull(selector, "selector is null!");

        this.nodeAdapter = nodeAdapter;
        this.selector = selector;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<E> check(Set<E> nodes, E root) throws NodeSelectorException {
        Assert.notNull(nodes, "nodes is null!");
        this.nodes = nodes;
        this.caseSensitive = nodeAdapter.isCaseSensitive(root);

        result = new LinkedHashSet<E>();
        switch (selector.getCombinator()) {
        case DESCENDANT:
            addDescendantElements();
            break;
        case CHILD:
            addChildElements();
            break;
        case ADJACENT_SIBLING:
            addAdjacentSiblingElements();
            break;
        case GENERAL_SIBLING:
            addGeneralSiblingElements();
            break;
        }
        
        return result;
    }
    
    /**
     * Add descendant elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#descendant-combinators">Descendant combinator</a>
     * 
     * @throws NodeSelectorException If one of the nodes have an illegal type.
     */
    private void addDescendantElements() throws NodeSelectorException {
        for (E node : nodes) {
            result.addAll(nodeAdapter.getNodesByTagName(node, selector.getTagName()));
        }
    }
    
    /**
     * Add child elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#child-combinators">Child combinators</a>
     */
    private void addChildElements() {
        for (E node : nodes) {
            List<E> nl = nodeAdapter.getChildNodes(node);
            for (int i = 0; i < nl.size(); i++) {
                node = nl.get(i);
                if (!nodeAdapter.isElementNode(node)) {
                    continue;
                }
                
                String tag = selector.getTagName();
                if (tagEquals(tag, nodeAdapter.getNodeName(node)) || tag.equals(Selector.UNIVERSAL_TAG)) {
                    result.add(node);
                }
            }
        }
    }
    
    /**
     * Add adjacent sibling elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#adjacent-sibling-combinators">Adjacent sibling combinator</a>
     */
    private void addAdjacentSiblingElements() {
        for (E node : nodes) {
            E n = nodeAdapter.getNextSiblingElement(node);
            if (n != null) {
                String tag = selector.getTagName();
                if (tagEquals(tag, nodeAdapter.getNodeName(n)) || tag.equals(Selector.UNIVERSAL_TAG)) {
                    result.add(n);
                }
            }
        }
    }
    
    /**
     * Add general sibling elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#general-sibling-combinators">General sibling combinator</a>
     */
    private void addGeneralSiblingElements() {
        for (E node : nodes) {
            E n = nodeAdapter.getNextSiblingElement(node);
            while (n != null) {
                String tag = selector.getTagName();
                if (tagEquals(tag, nodeAdapter.getNodeName(n)) || tag.equals(Selector.UNIVERSAL_TAG)) {
                    result.add(n);
                }
                
                n = nodeAdapter.getNextSiblingElement(n);
            }
        }
    }

    /**
     * Determine if the two specified tag names are equal.
     *
     * @param tag1 A tag name.
     * @param tag2 A tag name.
     * @return <code>true</code> if the tag names are equal, <code>false</code> otherwise.
     */
    private boolean tagEquals(String tag1, String tag2) {
        if (caseSensitive) {
            return tag1.equals(tag2);
        }

        return tag1.equalsIgnoreCase(tag2);
    }

}
