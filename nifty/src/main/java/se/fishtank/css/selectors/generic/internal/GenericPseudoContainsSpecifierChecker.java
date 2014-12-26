package se.fishtank.css.selectors.generic.internal;

import java.util.LinkedHashSet;
import java.util.Set;

import se.fishtank.css.selectors.NodeSelectorException;
import se.fishtank.css.selectors.dom.internal.NodeTraversalChecker;
import se.fishtank.css.selectors.generic.GenericNodeAdapter;
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
 * @author Jens Hohmuth
 */
public class GenericPseudoContainsSpecifierChecker<E> extends GenericNodeTraversalChecker<E> {
    
    /** The generic node adapter */
    private final GenericNodeAdapter<E> nodeAdapter;

    /** The pseudo-class specifier to check against. */
    private final PseudoContainsSpecifier specifier;
    
    /**
     * Create a new instance.
     * 
     * @param specifier The pseudo-class specifier to check against.
     */
    public GenericPseudoContainsSpecifierChecker(GenericNodeAdapter<E> nodeAdapter, PseudoContainsSpecifier specifier) {
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
        LinkedHashSet<E> result = new LinkedHashSet<E>();
        String value = specifier.getValue();
        for (E node : nodes) {
        	String textContent = nodeAdapter.getTextContent(node);
          if (textContent != null && textContent.contains(value)) {
        		result.add(node);
        	}
        }
        
        return result;
    }
}
