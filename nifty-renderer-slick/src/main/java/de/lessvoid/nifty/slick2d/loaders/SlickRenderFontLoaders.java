package de.lessvoid.nifty.slick2d.loaders;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.slick2d.render.font.LoadFontException;
import de.lessvoid.nifty.slick2d.render.font.SlickRenderFont;
import de.lessvoid.nifty.slick2d.render.font.loader.AngelCodeSlickRenderFontLoader;
import de.lessvoid.nifty.slick2d.render.font.loader.SlickRenderFontLoader;
import de.lessvoid.nifty.slick2d.render.font.loader.TrueTypeSlickRenderFontLoader;
import de.lessvoid.nifty.slick2d.render.font.loader.UnicodeSlickRenderFontLoader;

/**
 * This class is used to trigger the actual font loading. It will query all the
 * font loaders and try to load a font with the specified name.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class SlickRenderFontLoaders {
    /**
     * The singleton instance of this class.
     */
    private static final SlickRenderFontLoaders INSTANCE =
        new SlickRenderFontLoaders();

    /**
     * The list of font loaders that are expected to be queried.
     */
    private final List<SlickRenderFontLoader> loaders;

    /**
     * Private constructor so no instances but the singleton instance are
     * created.
     */
    private SlickRenderFontLoaders() {
        loaders = new ArrayList<SlickRenderFontLoader>();
    }

    /**
     * Get the singleton instance of this class.
     * 
     * @return the singleton instance
     */
    public static SlickRenderFontLoaders getInstance() {
        return INSTANCE;
    }

    /**
     * Load the font with the defined name.
     * 
     * @param filename name of the file that contains the font
     * @return the font loaded
     * @throws IllegalArgumentException in case all loaders fail to load the
     *             font
     */
    public SlickRenderFont loadFont(final String filename) {
        if (loaders.isEmpty()) {
            loadDefaultLoaders(SlickAddLoaderLocation.first);
        }

        for (final SlickRenderFontLoader currentLoader : loaders) {
            try {
                return currentLoader.loadFont(filename);
            } catch (final LoadFontException e) {
                // this loader failed... lets try the next one
            }
        }

        throw new IllegalArgumentException("No way known to load font \""
            + filename + "\"");
    }

    /**
     * Add the default implemented loaders to the loader list. This is done
     * automatically in case fonts are requested but no loaders got registered.
     * In general using this function should be avoided. Its better to load only
     * the loaders that are actually needed for your resources.
     * 
     * @param order the place where the default loaders are added to the list
     */
    public void loadDefaultLoaders(final SlickAddLoaderLocation order) {
        switch (order) {
            case first:
                addLoader(new AngelCodeSlickRenderFontLoader(), order);
                addLoader(new UnicodeSlickRenderFontLoader(), order);
                addLoader(new TrueTypeSlickRenderFontLoader(), order);
                break;
            case last:
            case dontCare:
                addLoader(new TrueTypeSlickRenderFontLoader(), order);
                addLoader(new UnicodeSlickRenderFontLoader(), order);
                addLoader(new AngelCodeSlickRenderFontLoader(), order);
                break;
        }
    }

    /**
     * Add a loader to the list of loaders that get queried when loading a new
     * font.
     * 
     * @param newLoader the new font loader
     * @param order the loader where the place the new loader on the list
     */
    public void addLoader(final SlickRenderFontLoader newLoader,
        final SlickAddLoaderLocation order) {
        if (checkAlreadyLoaded(newLoader)) {
            return;
        }

        switch (order) {
            case first:
                loaders.add(0, newLoader);
                break;
            case last:
            case dontCare:
                loaders.add(newLoader);
                break;
        }
    }

    /**
     * Check if the font loader that is about to be added already a part of the
     * loaders list. This is done be comparing the classes of the loaders.
     * 
     * @param newLoader the loader that is to be added
     * @return <code>true</code> in case the loader is already added to the
     *         loaders list
     */
    private boolean checkAlreadyLoaded(final SlickRenderFontLoader newLoader) {
        final Class<?> newLoaderClass = newLoader.getClass();
        Class<?> currentLoaderClass;
        for (final SlickRenderFontLoader currentLoader : loaders) {
            currentLoaderClass = currentLoader.getClass();
            if (newLoaderClass.equals(currentLoaderClass)) {
                return true;
            }
        }
        return false;
    }
}
