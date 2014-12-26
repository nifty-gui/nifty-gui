/**
 * Copyright (c) 2009-2014, Christer Sandberg
 */
package se.fishtank.css.selectors.dom.internal;

import java.util.LinkedHashSet;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import se.fishtank.css.selectors.NodeSelectorException;
import se.fishtank.css.selectors.Selector;
import se.fishtank.css.selectors.dom.DOMHelper;
import se.fishtank.css.util.Assert;

/**
 * A {@link NodeTraversalChecker} that check if a node
 * matches the {@linkplain Selector#getTagName() tag name} and
 * {@linkplain Selector#getCombinator() combinator} of the {@link Selector} set.
 * 
 * @author Christer Sandberg
 */
public class TagChecker extends NodeTraversalChecker {
   
    /** The selector to check against. */
    private final Selector selector;
    
    /** The set of nodes to check. */
    private Set<Node> nodes;
    
    /** The result of the checks. */
    private Set<Node> result;

    /** Whether the underlying DOM is case sensitive. */
    private boolean caseSensitive;
    
    /**
     * Create a new instance.
     * 
     * @param selector The selector to check against.
     */
    public TagChecker(Selector selector) {
        Assert.notNull(selector, "selector is null!");
        this.selector = selector;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Node> check(Set<Node> nodes, Node root) throws NodeSelectorException {
        Assert.notNull(nodes, "nodes is null!");
        this.nodes = nodes;

        Document doc = (root instanceof Document) ? (Document) root : root.getOwnerDocument();
        caseSensitive = !doc.createElement("a").isEqualNode(doc.createElement("A"));

        result = new LinkedHashSet<Node>();
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
        for (Node node : nodes) {
            NodeList nl;
            if (node.getNodeType() == Node.DOCUMENT_NODE) {
                nl = ((Document) node).getElementsByTagName(selector.getTagName());
            } else if (node.getNodeType() == Node.ELEMENT_NODE) {
                nl = ((Element) node).getElementsByTagName(selector.getTagName());
            } else {
                throw new NodeSelectorException("Only document and element nodes allowed!");
            }
            
            for (int i = 0; i < nl.getLength(); i++) {
                result.add(nl.item(i));
            }
        }
    }
    
    /**
     * Add child elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#child-combinators">Child combinators</a>
     */
    private void addChildElements() {
        for (Node node : nodes) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                node = nl.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                
                String tag = selector.getTagName();
                if (tagEquals(tag, node.getNodeName()) || tag.equals(Selector.UNIVERSAL_TAG)) {
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
        for (Node node : nodes) {
            Node n = DOMHelper.getNextSiblingElement(node);
            if (n != null) {
                String tag = selector.getTagName();
                if (tagEquals(tag, n.getNodeName()) || tag.equals(Selector.UNIVERSAL_TAG)) {
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
        for (Node node : nodes) {
            Node n = DOMHelper.getNextSiblingElement(node);
            while (n != null) {
                String tag = selector.getTagName();
                if (tagEquals(tag, n.getNodeName()) || tag.equals(Selector.UNIVERSAL_TAG)) {
                    result.add(n);
                }
                
                n = DOMHelper.getNextSiblingElement(n);
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
