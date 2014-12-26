/**
 * Copyright (c) 2014, Christer Sandberg
 */
package se.fishtank.css.selectors;

import java.util.List;

import se.fishtank.css.selectors.scanner.Scanner;
import se.fishtank.css.selectors.scanner.ScannerException;
import se.fishtank.css.util.Assert;

/**
 * Represents a list of selector groups.
 *
 * @author Christer Sandberg
 */
public class Selectors {

    /** The list of selector groups. */
    private final List<List<Selector>> groups;

    /**
     * Create a new instance.
     *
     * @param groups A list of selector groups.
     */
    public Selectors(List<List<Selector>> groups) {
        Assert.notNull(groups, "groups is null!");
        this.groups = groups;
    }

    /**
     * Returns the list of selector groups.
     *
     * @return The selector groups.
     */
    public List<List<Selector>> getGroups() {
        return groups;
    }

    /**
     * Create an instance by parsing the given selectors string.
     *
     * @see se.fishtank.css.selectors.scanner.Scanner
     *
     * @param selectors The selectors string to parse.
     * @return A new instance.
     * @throws java.lang.IllegalArgumentException If the selectors string is invalid.
     */
    public static Selectors fromString(String selectors) {
        Assert.notNull(selectors, "selectors is null!");
        try {
            Scanner scanner = new Scanner(selectors);
            return new Selectors(scanner.scan());
        } catch (ScannerException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
