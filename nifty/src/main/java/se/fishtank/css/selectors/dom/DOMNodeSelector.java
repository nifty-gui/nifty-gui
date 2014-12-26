/**
 * Copyright (c) 2009-2014, Christer Sandberg
 */
package se.fishtank.css.selectors.dom;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Node;
import se.fishtank.css.selectors.*;
import se.fishtank.css.selectors.dom.internal.*;
import se.fishtank.css.selectors.specifier.*;
import se.fishtank.css.util.Assert;

/**
 * An implementation of a DOM based {@link NodeSelector}.
 * <p/>
 * <strong>Possible enhancements:</strong>
 * <br/>
 * When searching for an element by its {@code id} we traverse the whole
 * tree. An attribute with the name <strong>id</strong> is not of the type
 * id unless it's been defined that way by a DTD etc, and we can't assume that
 * this is the case. If it's possible to make this work somehow we could speed
 * this up a bit. Maybe!?
 */
public class DOMNodeSelector implements NodeSelector<Node> {

    /** The root node (document or element). */
    private final Node root;

    /**
     * Create a new instance.
     * 
     * @param root The root node. Must be a document or element node.
     */
    public DOMNodeSelector(Node root) {
        Assert.notNull(root, "root is null!");
        short nodeType = root.getNodeType();
        Assert.isTrue(nodeType == Node.DOCUMENT_NODE ||
                nodeType == Node.ELEMENT_NODE, "root must be a document or element node!");
        this.root = root;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Node querySelector(String selectors) throws NodeSelectorException {
        return querySelector(Selectors.fromString(selectors));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node querySelector(Selectors selectors) throws NodeSelectorException {
        Set<Node> result = querySelectorAll(selectors);
        if (result.isEmpty()) {
            return null;
        }

        return result.iterator().next();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Node> querySelectorAll(String selectors) throws NodeSelectorException {
        return querySelectorAll(Selectors.fromString(selectors));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Node> querySelectorAll(Selectors selectors) throws NodeSelectorException {
        Assert.notNull(selectors, "selectors is null!");
        Set<Node> results = new LinkedHashSet<Node>();
        for (List<Selector> parts : selectors.getGroups()) {
            Set<Node> result = check(parts);
            if (!result.isEmpty()) {
                results.addAll(result);
            }
        }

        return results;
    }

    /**
     * Check the list of selector <em>parts</em> and return a set of nodes with the result.
     * 
     * @param parts A list of selector <em>parts</em>.
     * @return A set of nodes.
     * @throws NodeSelectorException In case of an error.
     */
    private Set<Node> check(List<Selector> parts) throws NodeSelectorException {
        Set<Node> result = new LinkedHashSet<Node>();
        result.add(root);
        for (Selector selector : parts) {
            NodeTraversalChecker checker = new TagChecker(selector);
            result = checker.check(result, root);
            if (selector.hasSpecifiers()) {
                for (Specifier specifier : selector.getSpecifiers()) {
                    switch (specifier.getType()) {
                    case ATTRIBUTE:
                        checker = new AttributeSpecifierChecker((AttributeSpecifier) specifier);
                        break;
                    case PSEUDO:
                        if (specifier instanceof PseudoClassSpecifier) {
                            checker = new PseudoClassSpecifierChecker((PseudoClassSpecifier) specifier);
                        } else if (specifier instanceof PseudoNthSpecifier) {
                            checker = new PseudoNthSpecifierChecker((PseudoNthSpecifier) specifier);
                        } else if (specifier instanceof PseudoContainsSpecifier) {
                        	checker = new PseudoContainsSpecifierChecker((PseudoContainsSpecifier) specifier);
                        }
                        
                        break;
                    case NEGATION:
                        final Set<Node> negationNodes = checkNegationSpecifier((NegationSpecifier) specifier);
                        checker = new NodeTraversalChecker() {
                            @Override
                            public Set<Node> check(Set<Node> nodes, Node root) throws NodeSelectorException {
                                Set<Node> set = new LinkedHashSet<Node>(nodes);
                                set.removeAll(negationNodes);
                                return set;
                            }
                        };
                        
                        break;
                    }
                    
                    result = checker.check(result, root);
                    if (result.isEmpty()) {
                        // Bail out early.
                        return result;
                    }
                }
            }
        }
        
        return result;
    }
    
    /**
     * Check the {@link NegationSpecifier}.
     * <p/>
     * This method will add the {@link Selector} from the specifier in
     * a list and invoke {@link #check(List)} with that list as the argument.
     *  
     * @param specifier The negation specifier.
     * @return A set of nodes after invoking {@link #check(List)}.
     * @throws NodeSelectorException In case of an error.
     */
    private Set<Node> checkNegationSpecifier(NegationSpecifier specifier) throws NodeSelectorException {
        List<Selector> parts = new ArrayList<Selector>(1);
        parts.add(specifier.getSelector());
        return check(parts);
    }
    
}
