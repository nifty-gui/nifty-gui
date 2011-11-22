package de.lessvoid.nifty.slick2d.render.font;

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
     * The font that is used.
     */
    private final Font internalFont;
    
    /**
     * This instance of the slick color is used to convert the color of
     * Nifty to slick without creating new objects over and over again.
     */
    private final org.newdawn.slick.Color convertColor;

    /**
     * Create this render font and define the internal used font.
     * 
     * @param font the font that supplies the render font with informations
     */
    protected AbstractSlickRenderFont(final Font font)
        throws SlickLoadFontException {
        if (font == null) {
            throw new SlickLoadFontException();
        }
        internalFont = font;
        convertColor = new org.newdawn.slick.Color(0.f, 0.f, 0.f, 0.f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void renderText(final Graphics g, final String text,
        final int locX, final int locY, final Color color, final float sizeX,
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

    /**
     * {@inheritDoc}
     * 
     * This implementation is very crappy. In case a better way is found for
     * any special font type, this method should be overwritten.
     */
    @Override
    public int getCharacterAdvance(final char currentCharacter,
        final char nextCharacter, final float size) {
        // this is a ugly implementation, but I failed to come up with anything
        // better...
        final int firstLength =
            internalFont.getWidth(Character.toString(currentCharacter));
        final int secondLength =
            internalFont.getWidth(Character.toString(currentCharacter)
                + Character.toString(nextCharacter));
        return (int) ((secondLength - firstLength) * size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getWidth(final String text) {
        return internalFont.getWidth(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getWidth(final String text, final float size) {
        return (int) (internalFont.getWidth(text) * size);
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
     * 
     * Overwrite this method in case you need to dispose the font data.
     */
    @Override
    public void dispose() {
    }
}
