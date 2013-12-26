package de.lessvoid.nifty.loaderv2.types.apply;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.image.ImageMode;
import de.lessvoid.nifty.render.image.ImageModeHelper;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

public class ApplyRendererImage implements ApplyRenderer {
  private static final Logger log = Logger.getLogger(ApplyRendererImage.class.getName());
  @Nonnull
  private final Convert convert;

  public ApplyRendererImage(@Nonnull final Convert convertParam) {
    convert = convertParam;
  }

  @Override
  public void apply(
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Attributes attributes,
      @Nonnull final NiftyRenderEngine renderEngine) {
    PanelRenderer panelRenderer = element.getRenderer(PanelRenderer.class);
    if (panelRenderer != null) {
      // panel renderer set means this is a panel and therefore the
      // image attributes here do not apply
      return;
    }
    ImageRenderer imageRenderer = element.getRenderer(ImageRenderer.class);
    if (imageRenderer == null) {
      return;
    }

    String filename = attributes.get("filename");
    if (filename == null) {
      log.severe("Filename missing for image.");
      return;
    }

    NiftyImage image = renderEngine.createImage(
        screen,
        filename,
        attributes.getAsBoolean("filter", Convert.DEFAULT_IMAGE_FILTER));
    if (image == null) {
      return;
    }

    image.setColor(convert.color(attributes.get("color")));

    String areaProviderProperty = ImageModeHelper.getAreaProviderProperty(attributes.getAttributes());
    String renderStrategyProperty = ImageModeHelper.getRenderStrategyProperty(attributes.getAttributes());
    ImageMode imageMode = convert.imageMode(areaProviderProperty, renderStrategyProperty);

    image.setImageMode(imageMode);
    imageRenderer.setImage(image);

    imageRenderer.setInset(convert.insetSizeValue(attributes.get("inset"), image.getHeight()));

    Size imageDimension = imageMode.getImageNativeSize(image);

    if (element.getConstraintWidth().hasDefault()) {
      element.setConstraintWidth(SizeValue.def(imageDimension.getWidth()));
    }
    if (element.getConstraintHeight().hasDefault()) {
      element.setConstraintHeight(SizeValue.def(imageDimension.getHeight()));
    }
  }
}
