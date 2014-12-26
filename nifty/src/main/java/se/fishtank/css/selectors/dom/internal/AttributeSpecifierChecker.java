/**
 * Copyright (c) 2009-2014, Christer Sandberg
 */
package se.fishtank.css.selectors.dom.internal;

import java.util.LinkedHashSet;
import java.util.Set;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import se.fishtank.css.selectors.NodeSelectorException;
import se.fishtank.css.selectors.specifier.AttributeSpecifier;
import se.fishtank.css.util.Assert;

/**
 * A {@link NodeTraversalChecker} that check if a node's attribute
 * matches the {@linkplain AttributeSpecifier attribute specifier} set.
 * 
 * @author Christer Sandberg
 */
public class AttributeSpecifierChecker extends NodeTraversalChecker {
    
    /** The attribute specifier to check against. */
    private final AttributeSpecifier specifier;
   
    /**
     * Create a new instance.
     * 
     * @param specifier The attribute specifier to check against. 
     */
    public AttributeSpecifierChecker(AttributeSpecifier specifier) {
        Assert.notNull(specifier, "specifier is null!");
        this.specifier = specifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Node> check(Set<Node> nodes, Node root) throws NodeSelectorException {
        Assert.notNull(nodes, "nodes is null!");
        Set<Node> result = new LinkedHashSet<Node>();
        for (Node node : nodes) {
            NamedNodeMap map = node.getAttributes();
            if (map == null) {
                continue;
            }
            
            Attr attr = (Attr) map.getNamedItem(specifier.getName());
            if (attr == null) {
                continue;
            }
            
            // It just have to be present.
            if (specifier.getValue() == null) {
                result.add(node);
                continue;
            }
            
            String value = attr.getNodeValue().trim();
            if (value.length() != 0) {
                String val = specifier.getValue();
                switch (specifier.getMatch()) {
                case EXACT:
                    if (value.equals(val)) {
                        result.add(node);
                    }
                    
                    break;
                case HYPHEN:
                    if (value.equals(val) || value.startsWith(val + '-')) {
                        result.add(node);
                    }
                    
                    break;
                case PREFIX:
                    if (value.startsWith(val)) {
                        result.add(node);
                    }
                    
                    break;
                case SUFFIX:
                    if (value.endsWith(val)) {
                        result.add(node);
                    }
                    
                    break;
                case CONTAINS:
                    if (value.contains(val)) {
                        result.add(node);
                    }
                    
                    break;
                case LIST:
                    for (String v : value.split("\\s+")) {
                        if (v.equals(val)) {
                            result.add(node);
                        }
                    }
                    
                    break;
                }
            }
        }
        
        return result;
    }

}
