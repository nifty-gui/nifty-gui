package org.bushe.swing.event;

/**
 * Subscribers can implement this interface in order to affect the order in which they are called.
 * <p>
 * Subscribers that do not implement this interface are called on a FIFO basis, as are subscribers that implement this
 * interface and return 0.  If the priority returned from this interface is negative, then this subscriber will be
 * called before non-Prioritized subscribers, the more negative, the earlier it is called.  If the priority returned
 * from this interface is positive, then this subscriber will be called after non-Prioritized subscribers, the more
 * positive, the later it is called.
 */
public interface Prioritized {
   int getPriority();
}
