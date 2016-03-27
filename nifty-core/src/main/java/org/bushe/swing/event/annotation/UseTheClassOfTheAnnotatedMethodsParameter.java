package org.bushe.swing.event.annotation;

/**
 * This is a dummy class to get around a limitation with annotations.
 * <p/>
 * It's nice to use an @EventSubscriber annotation without any parameters.  For example:
 * <pre>
 * <code>@EventSubscriber public void onEvent(FooEvent event) { //do something } </code></pre> In this case, the method should
 * obviously be subscribed to the FooEvent class. Since the eventClass is not required, annotations require a default to
 * be supplied. A default of null is not allowed by the compiler since it is not a class literal. A default of
 * Object.class cannot be used, since it is legal to subscribe to Object. hence, this class was created which documents
 * the issue and provides decent feedback when using an IDE's parameter insight.
 */
public final class UseTheClassOfTheAnnotatedMethodsParameter {
}
