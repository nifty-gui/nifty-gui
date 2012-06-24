package de.lessvoid.nifty.loaderv2.types.apply;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.image.ImageMode;
import de.lessvoid.nifty.render.image.ImageModeHelper;
import de.lessvoid.xml.xpp3.Attributes;

public class ApplyRendererImage implements ApplyRenderer {
  private Convert convert;

  public ApplyRendererImage(final Convert convertParam) {
    convert = convertParam;
  }

  public void apply(
      final Element element,
      final Attributes attributes,
      final NiftyRenderEngine renderEngine) {
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

    NiftyImage image =
      renderEngine.createImage(
          attributes.get("filename"),
          attributes.getAsBoolean("filter", Convert.DEFAULT_IMAGE_FILTER));
    if (image == null) {
      return;
    }

	String areaProviderProperty = ImageModeHelper.getAreaProviderProperty(attributes.getAttributes());
	String renderStrategyProperty = ImageModeHelper.getRenderStrategyProperty(attributes.getAttributes());
    ImageMode imageMode = convert.imageMode(areaProviderProperty, renderStrategyProperty);

    image.setImageMode(imageMode);
    imageRenderer.setImage(image);
    
    imageRenderer.setInset(convert.insetSizeValue(attributes.get("inset"), image.getHeight()));

    Size imageDimension = imageMode.getImageNativeSize(image);
    if (element.getConstraintWidth() == null) {
    	element.setConstraintWidth(convert.sizeValue(imageDimension.getWidth() + "px"));
    }
    if (element.getConstraintHeight() == null) {
    	element.setConstraintHeight(convert.sizeValue(imageDimension.getHeight() + "px"));
    }
  }
}
