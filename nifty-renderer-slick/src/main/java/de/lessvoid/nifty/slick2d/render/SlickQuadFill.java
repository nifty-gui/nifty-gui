package de.lessvoid.nifty.slick2d.render;

import org.newdawn.slick.Color;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * This shape fill implementation is used to render the rectangles of slick that
 * that have a different color on each edge. This implementation is not supposed
 * to be used for any other purpose.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class SlickQuadFill implements ShapeFill {
    /**
     * This is the vector that is returned as offset at all times.
     */
    private static final Vector2f NULL_VECTOR = new Vector2f(0, 0);

    /**
     * The color on the top left corner of the shape.
     */
    private final Color topLeft;

    /**
     * The color on the top right corner of the shape.
     */
    private final Color topRight;

    /**
     * The color on the bottom left corner of the shape.
     */
    private final Color bottomLeft;

    /**
     * The color on the bottom right corner of the shape.
     */
    private final Color bottomRight;

    /**
     * Create a new rectangle filling instance.
     * 
     * @param tlColor the color at the top left
     * @param trColor the color at the top right
     * @param blColor the color at the bottom left
     * @param brColor the color at the bottom right
     */
    public SlickQuadFill(final de.lessvoid.nifty.tools.Color tlColor,
        final de.lessvoid.nifty.tools.Color trColor,
        final de.lessvoid.nifty.tools.Color blColor,
        final de.lessvoid.nifty.tools.Color brColor) {

        topLeft = SlickRenderUtils.convertColorNiftySlick(tlColor);
        topRight = SlickRenderUtils.convertColorNiftySlick(trColor);
        bottomLeft = SlickRenderUtils.convertColorNiftySlick(blColor);
        bottomRight = SlickRenderUtils.convertColorNiftySlick(brColor);
    }

    /**
     * Change all the color values.
     * 
     * @param tlColor the color at the top left
     * @param trColor the color at the top right
     * @param blColor the color at the bottom left
     * @param brColor the color at the bottom right
     */
    public void changeColors(final de.lessvoid.nifty.tools.Color tlColor,
        final de.lessvoid.nifty.tools.Color trColor,
        final de.lessvoid.nifty.tools.Color blColor,
        final de.lessvoid.nifty.tools.Color brColor) {

        SlickRenderUtils.convertColorNiftySlick(tlColor, topLeft);
        SlickRenderUtils.convertColorNiftySlick(trColor, topRight);
        SlickRenderUtils.convertColorNiftySlick(blColor, bottomLeft);
        SlickRenderUtils.convertColorNiftySlick(brColor, bottomRight);
    }

    /**
     * Get the color at one point on the shape.
     */
    @Override
    public Color colorAt(final Shape shape, final float x, final float y) {
        boolean isMaxX = (Math.abs(x - shape.getMaxX()) < 0.001f);
        boolean isMaxY = (Math.abs(y - shape.getMaxY()) < 0.001f);

        if (isMaxX) {
            if (isMaxY) {
                return topRight;
            } else {
                return topLeft;
            }
        } else {
            if (isMaxY) {
                return bottomRight;
            } else {
                return bottomLeft;
            }
        }
    }

    /**
     * Get the offset at a location.
     */
    @Override
    public Vector2f getOffsetAt(final Shape shape, final float x, final float y) {
        return NULL_VECTOR;
    }

}
