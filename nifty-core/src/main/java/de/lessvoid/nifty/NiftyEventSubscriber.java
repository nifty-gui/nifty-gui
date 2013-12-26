package de.lessvoid.nifty;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NiftyEventSubscriber {
  /**
   * The Nifty Id we want to subscribe too.
   * @return
   */
  @Nonnull String id() default "";

  /**
   * The Pattern for the id we want to subscribe too.
   * @return
   */
  @Nonnull String pattern() default "";
}
