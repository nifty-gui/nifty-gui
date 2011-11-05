package de.lessvoid.nifty.slick2d.render.font;

/**
 * This exception is expected to be thrown be the constructor of this class
 * in case loading the specified font failed.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class LoadFontException extends Exception {
    /**
     * The serialization UID.
     */
    private static final long serialVersionUID = 4068721034678365719L;

    /**
     * Create the exception without an attached message or parent Throwable.
     */
    public LoadFontException() {
        super();
    };

    /**
     * Create the exception with an attached message and without parent
     * Throwable.
     */
    public LoadFontException(final String msg) {
        super(msg);
    };

    /**
     * Create the exception without an attached message and with parent
     * Throwable.
     */
    public LoadFontException(final Throwable e) {
        super(e);
    };

    /**
     * Create the exception with an attached message and parent Throwable.
     */
    public LoadFontException(final String msg, final Throwable e) {
        super(msg, e);
    };
}