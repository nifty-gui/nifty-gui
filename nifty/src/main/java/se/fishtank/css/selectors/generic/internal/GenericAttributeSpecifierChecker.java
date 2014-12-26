package se.fishtank.css.selectors.generic.internal;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import se.fishtank.css.selectors.NodeSelectorException;
import se.fishtank.css.selectors.dom.internal.NodeTraversalChecker;
import se.fishtank.css.selectors.generic.GenericNodeAdapter;
import se.fishtank.css.selectors.specifier.AttributeSpecifier;
import se.fishtank.css.util.Assert;

/**
 * A {@link NodeTraversalChecker} that check if a node's attribute
 * matches the {@linkplain AttributeSpecifier attribute specifier} set.
 * 
 * @author Christer Sandberg
 * @author Jens Hohmuth
 */
public class GenericAttributeSpecifierChecker<E> extends GenericNodeTraversalChecker<E> {

    /** The generic node adapter */
    private final GenericNodeAdapter<E> nodeAdapter;

    /** The attribute specifier to check against. */
    private final AttributeSpecifier specifier;
   
    /**
     * Create a new instance.
     * 
     * @param specifier The attribute specifier to check against. 
     */
    public GenericAttributeSpecifierChecker(final GenericNodeAdapter<E> nodeAdapter, final AttributeSpecifier specifier) {
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
        Set<E> result = new LinkedHashSet<E>();
        for (E node : nodes) {
            Map<String, String> map = nodeAdapter.getAttributes(node);
            if (map == null) {
                continue;
            }
            
            String attr = map.get(specifier.getName());
            if (attr == null) {
                continue;
            }
            
            // It just have to be present.
            if (specifier.getValue() == null) {
                result.add(node);
                continue;
            }
            
            String value = attr.trim();
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
