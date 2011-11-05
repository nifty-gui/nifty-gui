package de.lessvoid.nifty.slick2d.render.image.loader;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import de.lessvoid.nifty.slick2d.render.image.ImageSlickRenderImage;
import de.lessvoid.nifty.slick2d.render.image.SlickLoadImageException;
import de.lessvoid.nifty.slick2d.render.image.SlickRenderImage;

/**
 * This image loader takes care for loading Slick image based RenderImages.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class ImageSlickRenderImageLoader implements
    SlickRenderImageLoader {
    /**
     * Load the image.
     */
    @Override
    public SlickRenderImage loadImage(final String filename, final boolean filterLinear)
        throws SlickLoadImageException {
        try {
            Image image = null;
            if (filterLinear) {
                image = new Image(filename, false, Image.FILTER_LINEAR);
            } else {
                image = new Image(filename, false, Image.FILTER_NEAREST);
            }
            return new ImageSlickRenderImage(image);
        } catch (SlickException e) {
            throw new SlickLoadImageException("Loadint the image \"" + filename
                + "\" failed.", e);
        }
    }
}
