/**
 * Copyright (c) 2009-2014, Christer Sandberg
 */
package se.fishtank.css.selectors;

import java.util.Set;

/**
 * A representation of the {@code NodeSelector} specified in the
 * <a href="http://www.w3.org/TR/selectors-api/">W3C Selectors API</a>.
 * <p/>
 * This interface uses a generic type as the return type for the methods
 * {@link #querySelector(String)} and {@link #querySelectorAll(String)}.
 * The {@code NodeList} result type for {@link #querySelectorAll(String)}
 * has been replaced with a {@link Set}. Those changes will make it easier
 * to implement this interface for other XML technologies etc.
 * <p/>
 * According to the spec the result of {@link #querySelectorAll(String)} must be
 * static. This restriction is not enforced by the implementations of this interface.
 * 
 * @author Christer Sandberg
 *
 * @param <E> The return type.
 */
public interface NodeSelector<E> {

    /**
     * Returns the first matching {@code E} node within the node's subtree.
     * 
     * @param selectors A group of selectors to query.
     * @return The first matching {@code E} node or {@code null}.
     * @throws NodeSelectorException On errors.
     */
    public E querySelector(String selectors) throws NodeSelectorException;

    /**
     * Returns the first matching {@code E} node within the node's subtree.
     *
     * @param selectors A group of selectors to query.
     * @return The first matching {@code E} node or {@code null}.
     * @throws NodeSelectorException On errors.
     */
    public E querySelector(Selectors selectors) throws NodeSelectorException;

    /**
     * Returns a {@link Set} of all the matching {@code E} nodes within the node's subtree.
     * 
     * @param selectors A group of selectors to query.
     * @return All the matching {@code E} nodes or an empty {@link Set}.
     * @throws NodeSelectorException On errors.
     */
    public Set<E> querySelectorAll(String selectors) throws NodeSelectorException;

    /**
     * Returns a {@link Set} of all the matching {@code E} nodes within the node's subtree.
     *
     * @param selectors A group of selectors to query.
     * @return All the matching {@code E} nodes or an empty {@link Set}.
     * @throws NodeSelectorException On errors.
     */
    public Set<E> querySelectorAll(Selectors selectors) throws NodeSelectorException;

}
