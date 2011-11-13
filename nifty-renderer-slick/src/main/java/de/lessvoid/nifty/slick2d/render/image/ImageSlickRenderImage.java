package de.lessvoid.nifty.slick2d.render.image;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import de.lessvoid.nifty.slick2d.render.SlickRenderUtils;
import de.lessvoid.nifty.tools.Color;

/**
 * This slick render image implementation uses a Slick image to draw on the
 * screen.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class ImageSlickRenderImage implements SlickRenderImage {
    /**
     * The image that is used for the rendering operation.
     */
    private final Image image;

    /**
     * This instance of a slick color is used to avoid the need to create a new
     * color instance every time this image is rendered.
     */
    private final org.newdawn.slick.Color slickColor;

    /**
     * Create this render image that is supposed to render a specified Slick
     * image.
     * 
     * @param image the image to draw
     */
    public ImageSlickRenderImage(final Image image) {
        this.image = image;
        slickColor = new org.newdawn.slick.Color(0.f, 0.f, 0.f, 0.f);
    }

    /**
     * Get the width of the image.
     */
    @Override
    public int getWidth() {
        return image.getWidth();
    }

    /**
     * Get the height of the image.
     */
    @Override
    public int getHeight() {
        return image.getHeight();
    }

    /**
     * Dispose this image. After calling this method its not possible anymore to
     * render this image. Also all functions of this image are likely to yield
     * invalid results.
     */
    @Override
    public void dispose() {
        try {
            image.destroy();
        } catch (final SlickException e) {
            // Destorying failed... does not matter
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderImage(final Graphics g, final int x, final int y,
        final int width, final int height, final Color color, final float scale) {

        final int centerX = x + (width >> 1);
        final int centerY = y + (height >> 1);

        renderImage(g, x, y, width, height, 0, 0, getWidth(), getHeight(),
            color, scale, centerX, centerY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderImage(Graphics g, int x, int y, int w, int h, int srcX,
        int srcY, int srcW, int srcH, Color color, float scale, int centerX,
        int centerY) {

        g.pushTransform();
        g.translate(centerX, centerY);
        g.scale(scale, scale);
        g.translate(-centerX, -centerY);

        g.drawImage(image, x, y, x + w, y + h, srcX, srcY, srcX + srcW, srcY
            + srcH, SlickRenderUtils.convertColorNiftySlick(color, slickColor));
        

        final int glError = GL11.glGetError();
        if (glError != GL11.GL_NO_ERROR) {
            System.err.println("OpenGL Error: " + Integer.toString(glError));
        }

        g.popTransform();
    }
}
