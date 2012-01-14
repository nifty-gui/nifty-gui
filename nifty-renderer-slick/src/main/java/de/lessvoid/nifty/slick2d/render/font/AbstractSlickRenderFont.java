package de.lessvoid.nifty.slick2d.render.font;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import de.lessvoid.nifty.slick2d.render.SlickRenderUtils;
import de.lessvoid.nifty.tools.Color;

/**
 * This abstract slick render font implements the functions all the other font
 * implementations will share.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public abstract class AbstractSlickRenderFont implements SlickRenderFont {
  /**
   * This pattern contains the regular expression to detect color changes within
   * the text.
   */
  private final Pattern colorChangePattern;

  /**
   * This instance of the slick color is used to convert the color of Nifty to
   * slick without creating new objects over and over again.
   */
  private final org.newdawn.slick.Color convertColor;

  /**
   * The font that is used.
   */
  private final Font internalFont;

  /**
   * Create this render font and define the internal used font.
   * 
   * @param font
   *          the font that supplies the render font with informations
   */
  protected AbstractSlickRenderFont(final Font font) throws SlickLoadFontException {
    if (font == null) {
      throw new SlickLoadFontException();
    }
    internalFont = font;
    convertColor = new org.newdawn.slick.Color(0.f, 0.f, 0.f, 0.f);
    colorChangePattern = Pattern.compile("\\\\(#\\p{XDigit}{3,8})#");
  }

  /**
   * {@inheritDoc}
   * 
   * Overwrite this method in case you need to dispose the font data.
   */
  @Override
  public void dispose() {
    // usually no data needs to be disposed
  }

  /**
   * {@inheritDoc}
   * 
   * This implementation is very crappy. In case a better way is found for any
   * special font type, this method should be overwritten.
   */
  @Override
  public int getCharacterAdvance(final char currentCharacter, final char nextCharacter, final float size) {
    // this is a ugly implementation, but I failed to come up with anything
    // better...
    final int firstLength = internalFont.getWidth(Character.toString(currentCharacter));
    final int secondLength = internalFont.getWidth(Character.toString(currentCharacter)
        + Character.toString(nextCharacter));
    return (int) ((secondLength - firstLength) * size);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final int getHeight() {
    return internalFont.getLineHeight();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final int getWidth(final String text) {
    return internalFont.getWidth(getCleanedString(text));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final int getWidth(final String text, final float size) {
    return (int) (getWidth(text) * size);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void renderText(
      final Graphics g,
      final String text,
      final int locX,
      final int locY,
      final Color color,
      final float sizeX,
      final float sizeY) {

    final Matcher patternMatcher = colorChangePattern.matcher(text);

    if (!patternMatcher.find()) {
      // The color change thingy was not found. So we can just write the entire
      // text and be done with it.
      renderTextImpl(g, text, locX, locY, color, sizeX, sizeY);
      return;
    }
    
    final int maximumLineHeight = internalFont.getHeight(getCleanedString(text));

    Color currentColor = color;
    int nextTextIndex = 0;
    int currentX = locX;

    do {
      final int lastIndex = patternMatcher.start() - 1;
      
      String textPart;
      if (lastIndex > -1) {
        textPart = text.substring(nextTextIndex, lastIndex);
        final int currentY = locY + (maximumLineHeight - internalFont.getHeight(textPart));
        renderTextImpl(g, textPart, currentX, currentY, currentColor, sizeX, sizeY);
      } else {
        textPart = "";
      }

      nextTextIndex = patternMatcher.end();
      // get the new color
      final String colorText = patternMatcher.group(1);
      if (Color.check(colorText)) {
        currentColor = new Color(colorText);
      } else {
        currentColor = color;
      }
      // get the new location

      if (lastIndex > -1) {
        currentX += getWidth(textPart, sizeX) + getCharacterAdvance(text.charAt(lastIndex), text.charAt(nextTextIndex), sizeX);
      }
    } while (patternMatcher.find());

    if (text.length() >= nextTextIndex) {
      final String textPart = text.substring(nextTextIndex);
      final int currentY = locY + (maximumLineHeight - internalFont.getHeight(textPart));
      renderTextImpl(g, textPart, currentX, currentY, currentColor, sizeX, sizeY);
    }
  }
  
  /**
   * Clean all color change markers from the text.
   * 
   * @param orgString the original text
   * @return the cleaned text
   */
  private String getCleanedString(final String orgString) {
    return colorChangePattern.matcher(orgString).replaceAll("");
  }

  /**
   * This function contains the actual text rendering implementation.
   * 
   * @param g the graphics object used to drawn
   * @param text the text to draw
   * @param locX the x coordinate of the screen location to drawn on
   * @param locY the y coordinate of the screen location to drawn on
   * @param color the color of the text
   * @param sizeX the scaling factor along the x axis
   * @param sizeY the scaling factor along the y axis
   */
  private void renderTextImpl(
      final Graphics g,
      final String text,
      final int locX,
      final int locY,
      final Color color,
      final float sizeX,
      final float sizeY) {

    g.setFont(internalFont);
    g.pushTransform();
    g.translate(locX, locY);
    g.scale(sizeX, sizeY);

    if (color != null) {
      g.setColor(SlickRenderUtils.convertColorNiftySlick(color, convertColor));
    }
    g.drawString(text, 0, 0);
    g.popTransform();
  }
}
