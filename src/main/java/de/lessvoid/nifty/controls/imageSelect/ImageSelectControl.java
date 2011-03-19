package de.lessvoid.nifty.controls.imageSelect;

import java.util.ArrayList;
import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * ImageSelectControl.
 * 
 * @author void
 */
@Deprecated
public class ImageSelectControl extends AbstractController {
    private Nifty nifty;
    private Element element;
    private ArrayList<NiftyImage> images;
    private int currentImageIndex;

    public void bind(final Nifty niftyParam, final Screen screenParam, final Element newElement,
            final Properties properties,
            final Attributes controlDefinitionAttributes) {
        nifty = niftyParam;
        element = newElement;

        images = createImages(nifty.getRenderEngine(), properties.getProperty("imageList"));
        currentImageIndex = 0;
        updateVisuals();
    }

    @Override
    public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    }

    /**
     * update visuals.
     */
    private void updateVisuals() {
        if (images.isEmpty()) {
            return;
        }

        NiftyImage currentImage = images.get(currentImageIndex);
        element.findElementByName("image").getRenderer(ImageRenderer.class).setImage(currentImage);
        element.findElementByName("image").setConstraintWidth(
                new SizeValue(currentImage.getWidth() + "px"));
        element.findElementByName("image").setConstraintHeight(
                new SizeValue(currentImage.getHeight() + "px"));
        element.findElementByName("image").layoutElements();

        if (currentImageIndex == 0) {
            element.findElementByName("back").hide();
        } else {
            element.findElementByName("back").show();
        }

        if (currentImageIndex == (images.size() - 1)) {
            element.findElementByName("forward").hide();
        } else {
            element.findElementByName("forward").show();
        }
    }

    /**
     * create NiftyImage list from given property.
     * 
     * @param renderDevice renderDevice
     * @param property property
     * @return NiftyImage list.
     */
    private ArrayList<NiftyImage> createImages(final NiftyRenderEngine renderDevice,
            final String property) {
        ArrayList<NiftyImage> imageList = new ArrayList<NiftyImage>();
        if (property != null && property.length() > 0) {
            String[] imageStrings = property.split(",");
            for (String imageString : imageStrings) {
                imageList.add(renderDevice.createImage(imageString, false));
            }
        }
        return imageList;
    }

    public void onStartScreen() {
    }

    /**
     * This controller gets the focus.
     * 
     * @param getFocus get focus (true) or loose focus (false)
     */
    @Override
    public void onFocus(final boolean getFocus) {
        super.onFocus(getFocus);
    }

    /**
     * input event.
     * 
     * @param inputEvent the NiftyInputEvent to process
     */
    public boolean inputEvent(final NiftyInputEvent inputEvent) {
        return false;
    }

    /**
     * back click.
     */
    public void backClick() {
        if (currentImageIndex > 0) {
            currentImageIndex--;
            updateVisuals();
        }
    }

    /**
     * forward click.
     */
    public void forwardClick() {
        if (currentImageIndex < images.size() - 1) {
            currentImageIndex++;
            updateVisuals();
        }
    }

    public void addImage(final NiftyImage image) {
        images.add(image);
        updateVisuals();
    }
}
