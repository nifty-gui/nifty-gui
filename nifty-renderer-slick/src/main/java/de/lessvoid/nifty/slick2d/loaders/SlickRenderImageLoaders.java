package de.lessvoid.nifty.slick2d.loaders;

import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import de.lessvoid.nifty.slick2d.render.image.SlickLoadImageException;
import de.lessvoid.nifty.slick2d.render.image.SlickRenderImage;
import de.lessvoid.nifty.slick2d.render.image.loader.ImageSlickRenderImageLoader;
import de.lessvoid.nifty.slick2d.render.image.loader.SlickRenderImageLoader;

/**
 * This class is used to trigger the actual image loading. It will query all
 * known image loaders in order to load the render image.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class SlickRenderImageLoaders extends
    AbstractSlickLoaders<SlickRenderImageLoader> {
    /**
     * The singleton instance of this class.
     */
    private static final SlickRenderImageLoaders INSTANCE =
        new SlickRenderImageLoaders();

    /**
     * Private constructor so no instances but the singleton instance are
     * created.
     */
    private SlickRenderImageLoaders() {
        super();
    }

    /**
     * Get the singleton instance of this class.
     * 
     * @return the singleton instance
     */
    public static SlickRenderImageLoaders getInstance() {
        return INSTANCE;
    }

    /**
     * Load the default implementation of the render image loader.
     */
    @Override
    public void loadDefaultLoaders(final SlickAddLoaderLocation order) {
        addLoader(new ImageSlickRenderImageLoader(), order);
    }

    /**
     * Load the image with the defined name.
     * 
     * @param filename name of the file that contains the image
     * @param filterLinear <code>true</code> in case a linear filter should be
     *            applied when resizing this image
     * @return the image loaded
     * @throws IllegalArgumentException in case all loaders fail to load the
     *             image
     */
    public SlickRenderImage loadImage(final String filename,
        final boolean filterLinear) {
        final Iterator<SlickRenderImageLoader> itr = getLoaderIterator();

        while (itr.hasNext()) {
            try {
                final SlickRenderImage resultImage;
                resultImage = itr.next().loadImage(filename, filterLinear);
                
                return resultImage;
            } catch (final SlickLoadImageException e) {
                // this loader failed... does not matter
            }
        }

        throw new IllegalArgumentException("Failed to load image \""
            + filename + "\".");
    }
}
