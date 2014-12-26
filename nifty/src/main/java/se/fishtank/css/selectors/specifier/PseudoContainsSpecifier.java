/**
 * Copyright (c) 2014, John Heintz and Christer Sandberg
 */
package se.fishtank.css.selectors.specifier;

import se.fishtank.css.selectors.Specifier;
import se.fishtank.css.util.Assert;

/**
 * An implementation of {@link Specifier} for the non standard pseudo-class @{code :contains}.
 *
 * @author John Heintz
 * @author Christer Sandberg
 */
public class PseudoContainsSpecifier implements Specifier {
    
    /** The pseudo-class value. */
    private final String value;
    
    /**
     * Create a new pseudo-class specifier with the specified value.
     * 
     * @param value The pseudo-class value.
     */
    public PseudoContainsSpecifier(String value) {
        Assert.notNull(value, "value is null!");
        this.value = value.substring(1, value.length()-1); // take off outer single or double quote marks
    }
    
    /**
     * Get the pseudo-class value.
     * 
     * @return The pseudo-class value.
     */
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType() {
        return Type.PSEUDO;
    }

}
