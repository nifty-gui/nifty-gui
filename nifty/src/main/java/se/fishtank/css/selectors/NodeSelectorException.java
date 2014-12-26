/**
 * Copyright (c) 2009-2014, Christer Sandberg
 */
package se.fishtank.css.selectors;

/**
 * An exception thrown on errors related to the methods in {@link NodeSelector}.
 * 
 * @author Christer Sandberg
 */
public class NodeSelectorException extends Exception {

    /** Serial version UID. */
    private static final long serialVersionUID = -3786197030095043071L;

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause The cause.
     */
    public NodeSelectorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message.
     */
    public NodeSelectorException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     *
     * @param cause The cause.
     */
    public NodeSelectorException(Throwable cause) {
        super(cause);
    }
    
}
