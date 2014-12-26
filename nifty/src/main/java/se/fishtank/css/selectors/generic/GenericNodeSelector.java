package se.fishtank.css.selectors.generic;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import se.fishtank.css.selectors.NodeSelector;
import se.fishtank.css.selectors.NodeSelectorException;
import se.fishtank.css.selectors.Selector;
import se.fishtank.css.selectors.Selectors;
import se.fishtank.css.selectors.Specifier;
import se.fishtank.css.selectors.generic.internal.GenericAttributeSpecifierChecker;
import se.fishtank.css.selectors.generic.internal.GenericNodeTraversalChecker;
import se.fishtank.css.selectors.generic.internal.GenericPseudoClassSpecifierChecker;
import se.fishtank.css.selectors.generic.internal.GenericPseudoContainsSpecifierChecker;
import se.fishtank.css.selectors.generic.internal.GenericPseudoNthSpecifierChecker;
import se.fishtank.css.selectors.generic.internal.GenericTagChecker;
import se.fishtank.css.selectors.specifier.AttributeSpecifier;
import se.fishtank.css.selectors.specifier.NegationSpecifier;
import se.fishtank.css.selectors.specifier.PseudoClassSpecifier;
import se.fishtank.css.selectors.specifier.PseudoContainsSpecifier;
import se.fishtank.css.selectors.specifier.PseudoNthSpecifier;
import se.fishtank.css.util.Assert;

/**
 * A GenericNodeSelector for some unspecified E.
 *
 * @param <E> the generic node this class works with
 */
public class GenericNodeSelector<E> implements NodeSelector<E> {

    /** The GenericNodeAdapter to use */
    private GenericNodeAdapter<E> nodeAdapter;

    /** The root node */
    private final E root;

    /**
     * Create a new instance.
     * 
     * @param nodeAdapter The GenericNodeAdapter to do all the processing / translating to your real Node.
     * @param root The root node. Must be a NiftyNode.
     */
    public GenericNodeSelector(final GenericNodeAdapter<E> nodeAdapter, final E root) {
        Assert.notNull(nodeAdapter, "nodeAdapter is null!");
        Assert.notNull(root, "root is null!");

        this.nodeAdapter = nodeAdapter;
        this.root = root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E querySelector(String selectors) throws NodeSelectorException {
        return querySelector(Selectors.fromString(selectors));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E querySelector(Selectors selectors) throws NodeSelectorException {
        Set<E> result = querySelectorAll(selectors);
        if (result.isEmpty()) {
            return null;
        }

        return result.iterator().next();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<E> querySelectorAll(String selectors) throws NodeSelectorException {
        return querySelectorAll(Selectors.fromString(selectors));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<E> querySelectorAll(Selectors selectors) throws NodeSelectorException {
        Assert.notNull(selectors, "selectors is null!");
        Set<E> results = new LinkedHashSet<E>();
        for (List<Selector> parts : selectors.getGroups()) {
            Set<E> result = check(parts);
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
    private Set<E> check(final List<Selector> parts) throws NodeSelectorException {
        Set<E> result = new LinkedHashSet<E>();
        result.add(root);
        for (Selector selector : parts) {
            GenericNodeTraversalChecker<E> checker = new GenericTagChecker<E>(nodeAdapter, selector);
            result = checker.check(result, root);
            if (selector.hasSpecifiers()) {
                for (Specifier specifier : selector.getSpecifiers()) {
                    switch (specifier.getType()) {
                    case ATTRIBUTE:
                        checker = new GenericAttributeSpecifierChecker<E>(nodeAdapter, (AttributeSpecifier) specifier);
                        break;
                    case PSEUDO:
                        if (specifier instanceof PseudoClassSpecifier) {
                            checker = new GenericPseudoClassSpecifierChecker<E>(nodeAdapter, (PseudoClassSpecifier) specifier);
                        } else if (specifier instanceof PseudoNthSpecifier) {
                            checker = new GenericPseudoNthSpecifierChecker<E>(nodeAdapter, (PseudoNthSpecifier) specifier);
                        } else if (specifier instanceof PseudoContainsSpecifier) {
                          checker = new GenericPseudoContainsSpecifierChecker<E>(nodeAdapter, (PseudoContainsSpecifier) specifier);
                        }
                        
                        break;
                    case NEGATION:
                        final Set<E> negationNodes = checkNegationSpecifier((NegationSpecifier) specifier);
                        checker = new GenericNodeTraversalChecker<E>() {
                            @Override
                            public Set<E> check(Set<E> nodes, E root) throws NodeSelectorException {
                                Set<E> set = new LinkedHashSet<E>(nodes);
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
    private Set<E> checkNegationSpecifier(final NegationSpecifier specifier) throws NodeSelectorException {
        List<Selector> parts = new ArrayList<Selector>(1);
        parts.add(specifier.getSelector());
        return check(parts);
    }
    
}

