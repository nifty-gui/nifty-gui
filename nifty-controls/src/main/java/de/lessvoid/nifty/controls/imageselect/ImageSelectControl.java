package de.lessvoid.nifty.controls.imageselect;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.ImageSelectSelectionChangedEvent;
import de.lessvoid.nifty.controls.NextPrevHelper;
import de.lessvoid.nifty.effects.Effect;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.impl.Move;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * ImageSelectControl.
 *
 * This control allows a simple selection of images.
 *
 * @author void
 * @deprecated Please use {@link de.lessvoid.nifty.controls.ImageSelect} when accessing NiftyControls.
 */
@Deprecated
public class ImageSelectControl extends AbstractController implements de.lessvoid.nifty.controls.ImageSelect {
    private Nifty nifty;
    private ArrayList<NiftyImage> images;
    private int currentImageIndex;
    private int imageWidth;
    private NextPrevHelper nextPrevHelper;
    private Element backButtonElement;
    private Element forwardButtonElement;
    private Element imageElement;
    private Element imageElement2;
    private Element backElement;
    private Element forwardElement;
    private boolean block = false;

    @Override
    public void bind(
        final Nifty niftyParam,
        final Screen screenParam,
        final Element newElement,
        final Properties properties,
        final Attributes controlDefinitionAttributes) {
        super.bind(newElement);
        nifty = niftyParam;
        images = createImages(nifty.getRenderEngine(), properties.getProperty("imageList"));
        currentImageIndex = 0;
        imageWidth = new SizeValue(properties.getProperty("imageWidth", "0px")).getValueAsInt(1.0f);
        nextPrevHelper = new NextPrevHelper(getElement(), screenParam.getFocusHandler());
        backButtonElement = getElement().findElementByName("#back-button");
        forwardButtonElement = getElement().findElementByName("#forward-button");
        imageElement = getElement().findElementByName("#image");
        imageElement2 = getElement().findElementByName("#image-2");
        backElement = getElement().findElementByName("#back");
        forwardElement = getElement().findElementByName("#forward");

        List<Effect> moveEffects = imageElement.getEffects(EffectEventId.onCustom, Move.class);
        for (Effect e : moveEffects) {
          if ("back".equals(e.getCustomKey())) {
            e.getParameters().put("offsetX", String.valueOf(-imageWidth - 1));
          } else if ("forward".equals(e.getCustomKey())) {
            e.getParameters().put("offsetX", String.valueOf(imageWidth + 1));
          }
        }
        moveEffects = imageElement2.getEffects(EffectEventId.onCustom, Move.class);
        for (Effect e : moveEffects) {
          if ("back".equals(e.getCustomKey())) {
            e.getParameters().put("offsetX", String.valueOf(imageWidth));
          } else if ("forward".equals(e.getCustomKey())) {
            e.getParameters().put("offsetX", String.valueOf(-imageWidth));
          }
        }

        updateVisuals();
    }

    @Override
    public void onStartScreen() {
    }

    /**
     * input event.
     * 
     * @param inputEvent the NiftyInputEvent to process
     */
    @Override
    public boolean inputEvent(final NiftyInputEvent inputEvent) {
      if (NiftyStandardInputEvent.MoveCursorLeft == inputEvent) {
        backClick();
      } else if (NiftyStandardInputEvent.MoveCursorRight == inputEvent) {
        forwardClick();
      } else if (nextPrevHelper.handleNextPrev(inputEvent)) {
        return true;
      } else if (inputEvent == NiftyStandardInputEvent.Activate) {
        getElement().onClick();
        return true;
      }
      return false;
    }

    @Override
    public void onFocus(final boolean getFocus) {
      if (getFocus) {
        backButtonElement.startEffect(EffectEventId.onCustom);
        forwardButtonElement.startEffect(EffectEventId.onCustom);
      } else {
        backButtonElement.stopEffect(EffectEventId.onCustom);
        forwardButtonElement.stopEffect(EffectEventId.onCustom);
      }

      super.onFocus(getFocus);
    }

    // ImageSelect implementation

    /**
     * back click.
     */
    @Override
    public void backClick() {
      if (block) {
        return;
      }

      if (currentImageIndex > 0) {
        block = true;

        imageElement2.getRenderer(ImageRenderer.class).setImage(imageElement.getRenderer(ImageRenderer.class).getImage());
        imageElement2.setConstraintWidth(new SizeValue(imageElement.getWidth() + "px"));
        imageElement2.setConstraintHeight(new SizeValue(imageElement.getHeight() + "px"));
        imageElement2.layoutElements();
        imageElement2.show();

        imageElement.hide();
        currentImageIndex--;
        imageIndexChanged();
        updateVisuals();
        imageElement.stopEffect(EffectEventId.onCustom);
        imageElement.startEffect(EffectEventId.onCustom, new EndNotify() {
          @Override
          public void perform() {
            block = false;
          }
        }, "back");
        imageElement.show();

        imageElement2.stopEffect(EffectEventId.onCustom);
        imageElement2.startEffect(EffectEventId.onCustom, new EndNotify() {
          @Override
          public void perform() {
            imageElement2.hide();
          }
        }, "back");
      }
    }

    /**
     * forward click.
     */
    @Override
    public void forwardClick() {
      if (block) {
        return;
      }
      if (currentImageIndex < images.size() - 1) {
        block = true;

        imageElement2.getRenderer(ImageRenderer.class).setImage(imageElement.getRenderer(ImageRenderer.class).getImage());
        imageElement2.setConstraintWidth(new SizeValue(imageElement.getWidth() + "px"));
        imageElement2.setConstraintHeight(new SizeValue(imageElement.getHeight() + "px"));
        imageElement2.layoutElements();
        imageElement2.show();

        imageElement.hide();
        currentImageIndex++;
        imageIndexChanged();
        updateVisuals();
        imageElement.stopEffect(EffectEventId.onCustom);
        imageElement.startEffect(EffectEventId.onCustom, new EndNotify() {
          @Override
          public void perform() {
            block = false;
          }
        }, "forward");
        imageElement.show();

        imageElement2.stopEffect(EffectEventId.onCustom);
        imageElement2.startEffect(EffectEventId.onCustom, new EndNotify() {
          @Override
          public void perform() {
            imageElement2.hide();
          }
        }, "forward");
      }
    }

    /**
     * Add Image.
     * @param image image
     */
    @Override
    public void addImage(final NiftyImage image) {
        images.add(image);
        updateVisuals();
    }

    /**
     * Get the selected image index.
     * @return selected image index
     */
    @Override
    public int getSelectedImageIndex() {
      return currentImageIndex;
    }

    /**
     * Set the selected image index.
     * @param imageIndex the new image index
     */
    @Override
    public void setSelectedImageIndex(final int imageIndex) {
      if (imageIndex < 0 || imageIndex > images.size()) {
        return;
      }
      currentImageIndex = imageIndex;
      updateVisuals();
      imageIndexChanged();
    }

    /**
     * Remove Image.
     * @param image image
     */
    @Override
    public void removeImage(final NiftyImage image) {
      images.remove(image);
      updateVisuals();
    }

    /**
     * Number of images
     * @Return Number of images
     */
    @Override
    public int getImageCount() {
      return images.size();
    }

    // private stuff

    /**
     * update visuals.
     */
    private void updateVisuals() {
        if (images.isEmpty()) {
            return;
        }

        NiftyImage currentImage = images.get(currentImageIndex);
        imageElement.getRenderer(ImageRenderer.class).setImage(currentImage);
        imageElement.setConstraintWidth(new SizeValue(currentImage.getWidth() + "px"));
        imageElement.setConstraintHeight(new SizeValue(currentImage.getHeight() + "px"));
        imageElement.layoutElements();

        if (currentImageIndex == 0) {
            backElement.hide();
            backButtonElement.disable();
        } else {
            backElement.show();
            backButtonElement.enable();
        }

        if (currentImageIndex == (images.size() - 1)) {
            forwardElement.hide();
            forwardButtonElement.disable();
        } else {
            forwardElement.show();
            forwardButtonElement.enable();
        }
    }

    /**
     * create NiftyImage list from given property.
     * 
     * @param renderDevice renderDevice
     * @param property property
     * @return NiftyImage list.
     */
    private ArrayList<NiftyImage> createImages(final NiftyRenderEngine renderDevice, final String param) {
      String property = nifty.specialValuesReplace(param);

        ArrayList<NiftyImage> imageList = new ArrayList<NiftyImage>();
        if (property != null && property.length() > 0) {
            String[] imageStrings = property.split(",");
            for (String imageString : imageStrings) {
                imageList.add(renderDevice.createImage(imageString, false));
            }
        }
        return imageList;
    }

    private void imageIndexChanged() {
      nifty.publishEvent(getElement().getId(), new ImageSelectSelectionChangedEvent(this, currentImageIndex));
    }
}
