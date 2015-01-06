package de.lessvoid.nifty.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.lessvoid.nifty.api.converter.NiftyStyleStringConverter;
import de.lessvoid.nifty.api.converter.NiftyStyleStringConverterDefault;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NiftyStyleProperty {
  /**
   * The name that this attribute will be referred to in the stylesheet.
   * @return the name of this attribute
   */
  String name();

  Class<? extends NiftyStyleStringConverter<?>> converter() default NiftyStyleStringConverterDefault.class;
}
