package de.lessvoid.nifty.tools;

import javax.annotation.Nonnull;

/**
 * This enumerator stores the different types of size values.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public enum SizeValueType {
  /**
   * The default size type. This size type resolves to the minimal needed size to contain all child elements in case
   * all the children have a size that is not depending on the size of the parent.
   */
  Default("d", true, ValueRequirement.CalculatedOnly),

  /**
   * The wildcard size type. This size type will be handled by the layout managers to contain the maximal available
   * size.
   */
  Wildcard("*", false, ValueRequirement.CalculatedOnly),

  /**
   * The sum size type.
   * <p/>
   * When set, this size value will be the sum of the sizes of the content/children of the element it is used on.
   * Only children with absolute size values will be considered, or children that also have a sum/max/min size value.
   * <p/>
   * Builders or XML should use the value "s" or "sum". A pixel value can also be given in the form of "100s",
   * but this is for internal use (it represents the calculated size).
   */
  Sum("s", true, ValueRequirement.CalculatedOnly),

  /**
   * The maximum size type.
   * <p/>
   * When set, this size value will be the highest of the sizes of the content/children of the element it is used on.
   * Only children with absolute size values will be considered, or children that also have a sum/max/min size value.
   * <p/>
   * Builders or XML should use the value "m" or "max". A pixel value can also be given in the form of "100m",
   * but this is for internal use (it represents the calculated size).
   */
  Maximum("m", true, ValueRequirement.CalculatedOnly),

  /**
   * The minimum size type.
   * <p/>
   * When set, this size value will be the lowest of the sizes of the content/children of the element it is used on.
   * Only children with absolute size values will be considered, or children that also have a sum/max/min size value.
   * <p/>
   * Builders or XML should use the value "min". A pixel value can also be given in the form of "100min",
   * but this is for internal use (it represents the calculated size).
   */
  Minimum("min", true, ValueRequirement.CalculatedOnly),

  /**
   * The percent value type. This will resolve to a percentage of the parents size.
   */
  Percent("%", false, ValueRequirement.Required),

  /**
   * The percent of the height type. This will resolve to a percentage of the height value of the parent. This type is
   * only valid  when used as width.
   */
  PercentHeight("%h", false, ValueRequirement.Required),

  /**
   * The percent of the width type. This will resolve to a percentage of the width value of the parent. This type is
   * only valid  when used as height.
   */
  PercentWidth("%w", false, ValueRequirement.Required),

  /**
   * The pixel value type identifies a size value that contains a fixed size in pixel.
   */
  Pixel("px", true, ValueRequirement.Required);

  /**
   * The extension to the value that identifies the type.
   */
  @Nonnull
  private final String extension;

  /**
   * The independent flag.
   */
  private final boolean independent;

  /**
   * The value requirement flag of this type.
   */
  @Nonnull
  private final ValueRequirement valueRequirement;

  /**
   * Constructor for the type that allows setting the properties of the type.
   *
   * @param extension        the extension string that is used to identify a size value of this type
   * @param independent      the independent flag that marks if the size of the element depends on the parent element
   *                         or not
   * @param valueRequirement the value requirement flag
   */
  private SizeValueType(
      @Nonnull final String extension,
      final boolean independent,
      @Nonnull final ValueRequirement valueRequirement) {
    this.extension = extension;
    this.independent = independent;
    this.valueRequirement = valueRequirement;
  }

  /**
   * Get the extension string of this size value.
   *
   * @return the extension string
   */
  @Nonnull
  public String getExtension() {
    return extension;
  }

  /**
   * Check if a value of this type depends on the parents size or not.
   *
   * @return {@code true} in case the size is not depending on the parent size
   */
  public boolean isIndependent() {
    return independent;
  }

  /**
   * Check the value requirement flag.
   *
   * @return the value requirement flag
   */
  @Nonnull
  public ValueRequirement getValueRequirement() {
    return valueRequirement;
  }

  /**
   * This enum contains the possible values to define if the size type requires a value or not.
   */
  public enum ValueRequirement {
    /**
     * A value is required, the size type does not work without.
     */
    Required,

    /**
     * A value is optional. The type works just fine with and without.
     */
    Optional,

    /**
     * A value is in general forbidden. But the layout process is allowed to set a calculated size.
     */
    CalculatedOnly,

    /**
     * A value is forbidden. The type does never contain a value, neither a calculated nor a set one.
     */
    Forbidden
  }
}
