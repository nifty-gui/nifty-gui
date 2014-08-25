package de.lessvoid.nifty.controls.imageselect;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.*;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * ImageSelectControl.
 * <p/>
 * This control allows a simple selection of images.
 *
 * @author void
 * @deprecated Please use {@link de.lessvoid.nifty.controls.ImageSelect} when accessing NiftyControls.
 */
@Deprecated
public class ImageSelectControl extends AbstractController implements ImageSelect {
  @Nonnull
  private static final Logger log = Logger.getLogger(ImageSelectControl.class.getName());
  @Nullable
  private Nifty nifty;
  @Nonnull
  private final List<NiftyImage> images;
  private int currentImageIndex;
  @Nonnull
  private NextPrevHelper nextPrevHelper;
  @Nullable
  private Element backButtonElement;
  @Nullable
  private Element forwardButtonElement;
  @Nullable
  private Element imageElement;
  @Nullable
  private Element imageElement2;
  @Nullable
  private Element backElement;
  @Nullable
  private Element forwardElement;
  private boolean block = false;

  public ImageSelectControl() {
    images = new ArrayList<NiftyImage>();
  }

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters properties) {
    super.bind(element);
    this.nifty = nifty;
    createImages(nifty, screen, properties.get("imageList"));
    currentImageIndex = 0;
    nextPrevHelper = new NextPrevHelper(element, screen.getFocusHandler());

    backButtonElement = element.findElementById("#back-button");
    forwardButtonElement = element.findElementById("#forward-button");
    imageElement = element.findElementById("#image");
    imageElement2 = element.findElementById("#image-2");
    backElement = element.findElementById("#back");
    forwardElement = element.findElementById("#forward");

    // check if binding is okay
    if (backButtonElement == null) {
      log.severe("Failed to locate back-button element. Looked for: #back-button");
    }
    if (forwardButtonElement == null) {
      log.severe("Failed to locate forward-button element. Looked for: #forward-button");
    }
    if (imageElement == null) {
      log.severe("Failed to locate image element. Looked for: #image");
    }
    if (imageElement2 == null) {
      log.severe("Failed to locate image element 2. Looked for: #image-2");
    }
    if (backElement == null) {
      log.severe("Failed to locate back element. Looked for: #back");
    }
    if (forwardElement == null) {
      log.severe("Failed to locate back element. Looked for: #forward");
    }

    String parameterImageWidth = properties.get("imageWidth");
    final int imageWidth;
    if (parameterImageWidth == null) {
      imageWidth = 0;
    } else {
      imageWidth = new SizeValue(parameterImageWidth).getValueAsInt(1.0f);
    }

    if (imageElement != null) {
      List<Effect> moveEffects = imageElement.getEffects(EffectEventId.onCustom, Move.class);
      for (Effect e : moveEffects) {
        if ("back".equals(e.getCustomKey())) {
          e.getParameters().put("offsetX", String.valueOf(-imageWidth - 1));
        } else if ("forward".equals(e.getCustomKey())) {
          e.getParameters().put("offsetX", String.valueOf(imageWidth + 1));
        }
      }
    }

    if (imageElement2 != null) {
      List<Effect> moveEffects = imageElement2.getEffects(EffectEventId.onCustom, Move.class);
      for (Effect e : moveEffects) {
        if ("back".equals(e.getCustomKey())) {
          e.getParameters().put("offsetX", String.valueOf(imageWidth));
        } else if ("forward".equals(e.getCustomKey())) {
          e.getParameters().put("offsetX", String.valueOf(-imageWidth));
        }
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
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    if (NiftyStandardInputEvent.MoveCursorLeft == inputEvent) {
      backClick();
    } else if (NiftyStandardInputEvent.MoveCursorRight == inputEvent) {
      forwardClick();
    } else if (nextPrevHelper.handleNextPrev(inputEvent)) {
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.Activate) {
      Element element = getElement();
      if (element != null) {
        element.onClickAndReleasePrimaryMouseButton();
      }
      return true;
    }
    return false;
  }

  @Override
  public void onFocus(final boolean getFocus) {
    super.onFocus(getFocus);
    if (backButtonElement != null && forwardButtonElement != null) {
      if (getFocus) {
        backButtonElement.startEffect(EffectEventId.onCustom);
        forwardButtonElement.startEffect(EffectEventId.onCustom);
      } else {
        backButtonElement.stopEffect(EffectEventId.onCustom);
        forwardButtonElement.stopEffect(EffectEventId.onCustom);
      }
    }
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

    if (imageElement == null || imageElement2 == null) {
      return;
    }

    ImageRenderer imageRenderer = imageElement.getRenderer(ImageRenderer.class);
    ImageRenderer imageRenderer2 = imageElement2.getRenderer(ImageRenderer.class);

    if (imageRenderer == null || imageRenderer2 == null) {
      return;
    }

    if (currentImageIndex > 0) {
      block = true;

      imageRenderer2.setImage(imageRenderer.getImage());
      imageElement2.setConstraintWidth(SizeValue.px(imageElement.getWidth()));
      imageElement2.setConstraintHeight(SizeValue.px(imageElement.getHeight()));
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

    if (imageElement == null || imageElement2 == null) {
      return;
    }

    ImageRenderer imageRenderer = imageElement.getRenderer(ImageRenderer.class);
    ImageRenderer imageRenderer2 = imageElement2.getRenderer(ImageRenderer.class);

    if (imageRenderer == null || imageRenderer2 == null) {
      return;
    }

    if (currentImageIndex < images.size() - 1) {
      block = true;

      imageRenderer2.setImage(imageRenderer.getImage());
      imageElement2.setConstraintWidth(SizeValue.px(imageElement.getWidth()));
      imageElement2.setConstraintHeight(SizeValue.px(imageElement.getHeight()));
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
   *
   * @param image image
   */
  @Override
  public void addImage(@Nonnull final NiftyImage image) {
    images.add(image);
    updateVisuals();
  }

  /**
   * Get the selected image index.
   *
   * @return selected image index
   */
  @Override
  public int getSelectedImageIndex() {
    return currentImageIndex;
  }

  /**
   * Set the selected image index.
   *
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
   *
   * @param image image
   */
  @Override
  public void removeImage(@Nonnull final NiftyImage image) {
    images.remove(image);
    updateVisuals();
  }

  /**
   * Number of images
   *
   * @return Number of images
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

    if (imageElement != null) {
      ImageRenderer imageRenderer = imageElement.getRenderer(ImageRenderer.class);
      NiftyImage currentImage = images.get(currentImageIndex);
      if (imageRenderer != null) {
        imageRenderer.setImage(currentImage);
      }
      imageElement.setConstraintWidth(SizeValue.px(currentImage.getWidth()));
      imageElement.setConstraintHeight(SizeValue.px(currentImage.getHeight()));
      imageElement.layoutElements();
    }

    if (backElement != null && backButtonElement != null) {
      if (currentImageIndex == 0) {
        backElement.hide();
        backButtonElement.disable();
      } else {
        backElement.show();
        backButtonElement.enable();
      }
    }

    if (forwardElement != null && forwardButtonElement != null) {
      if (currentImageIndex == (images.size() - 1)) {
        forwardElement.hide();
        forwardButtonElement.disable();
      } else {
        forwardElement.show();
        forwardButtonElement.enable();
      }
    }
  }

  /**
   * Fill the images list from the list defined as parameter.
   */
  private void createImages(@Nonnull final Nifty nifty, @Nonnull final Screen screen, @Nullable final String param) {
    if (param == null) {
      return;
    }
    String property = nifty.specialValuesReplace(param);
    NiftyRenderEngine renderEngine = nifty.getRenderEngine();

    images.clear();
    if (property.length() > 0) {
      String[] imageStrings = property.split(",");
      for (String imageString : imageStrings) {
        images.add(renderEngine.createImage(screen, imageString, false));
      }
    }
  }

  private void imageIndexChanged() {
    if (nifty != null) {
      String id = getId();
      if (id != null) {
        nifty.publishEvent(id, new ImageSelectSelectionChangedEvent(this, currentImageIndex));
      }
    }
  }
}
