package de.lessvoid.nifty.slick2d.render;

import de.lessvoid.nifty.tools.Color;

/**
 * This utility class implements some utility functions for the render classes.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class SlickRenderUtils {
  /**
   * Convert a Nifty color into a Slick color.
   *
   * @param niftyColor the Nifty color that supplies the color values
   * @return the newly created instance of a Slick color that stores the same color values as the Nifty color that was
   *         set as parameter
   */
  public static org.newdawn.slick.Color convertColorNiftySlick(final Color niftyColor) {
    return new org.newdawn.slick.Color(niftyColor.getRed(), niftyColor.getGreen(), niftyColor.getBlue(),
        niftyColor.getAlpha());
  }

  /**
   * Convert a Nifty color into a Slick color.
   *
   * @param niftyColor the Nifty color that supplies the color values
   * @param slickColor the instance of a Slick color that is supposed to store the color values
   * @return the same instance of the Slick color that was set as parameter
   */
  public static org.newdawn.slick.Color convertColorNiftySlick(
      final Color niftyColor, final org.newdawn.slick.Color slickColor) {
    slickColor.r = niftyColor.getRed();
    slickColor.g = niftyColor.getGreen();
    slickColor.b = niftyColor.getBlue();
    slickColor.a = niftyColor.getAlpha();
    return slickColor;
  }

  /**
   * Convert a Slick color into a Nifty color.
   *
   * @param slickColor the Slick color that supplies the color values
   * @return the newly created Nifty color instance that stores the values of the Slick color set as parameter
   */
  public static Color convertColorSlickNifty(final org.newdawn.slick.Color slickColor) {
    return new Color(slickColor.r, slickColor.g, slickColor.b, slickColor.a);
  }

  /**
   * Convert a Slick color to a Nifty color.
   *
   * @param slickColor the Slick color that supplies the color values
   * @param niftyColor the Nifty color that is supposed to store the values of the Slick color
   * @return the same instances of the Nifty color that is set as parameter
   */
  public static Color convertColorSlickNifty(
      final org.newdawn.slick.Color slickColor, final Color niftyColor) {
    niftyColor.setRed(slickColor.r);
    niftyColor.setGreen(slickColor.g);
    niftyColor.setBlue(slickColor.b);
    niftyColor.setAlpha(slickColor.a);
    return niftyColor;
  }

  /**
   * The private constructor avoids any instances to be created.
   */
  private SlickRenderUtils() {
    // nothing
  }
}
