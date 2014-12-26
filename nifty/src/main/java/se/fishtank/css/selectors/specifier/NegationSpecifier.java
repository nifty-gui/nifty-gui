/**
 * Copyright (c) 2009-2014, Christer Sandberg
 */
package se.fishtank.css.selectors.specifier;

import se.fishtank.css.selectors.Selector;
import se.fishtank.css.selectors.Specifier;
import se.fishtank.css.util.Assert;

/**
 * An implementation of {@link Specifier} for the negation pseudo-class.
 * 
 * @see <a href="http://www.w3.org/TR/css3-selectors/#negation">The negation pseudo-class</a>
 * 
 * @author Christer Sandberg
 */
public class NegationSpecifier implements Specifier {
    
    /** The negation {@linkplain Selector selector}. */
    private final Selector selector;
    
    /**
     * Create a new negation specifier with the specified negation selector.
     * 
     * @param selector The negation {@linkplain Selector selector}.
     */
    public NegationSpecifier(Selector selector) {
        Assert.notNull(selector, "selector is null!");
        this.selector = selector;
    }
    
    /**
     * Get the negation selector.
     * 
     * @return The negation selector.
     */
    public Selector getSelector() {
        return selector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType() {
        return Type.NEGATION;
    }

}
