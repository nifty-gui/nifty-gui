package de.lessvoid.nifty.loaderv2.types.apply;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.xml.xpp3.Attributes;

public class ApplyRendererPanel implements ApplyRenderer {
  private Convert convert;

  public ApplyRendererPanel(final Convert convertParam) {
    convert = convertParam;
  }

  public void apply(
      final Element element,
      final Attributes attributes,
      final NiftyRenderEngine renderEngine) {
    PanelRenderer panelRenderer = element.getRenderer(PanelRenderer.class);
    if (panelRenderer == null) {
      return;
    }
    panelRenderer.setBackgroundColor(convert.color(attributes.get("backgroundColor")));

    ImageRenderer imageRenderer = element.getRenderer(ImageRenderer.class);
    if (imageRenderer == null) {
      return;
    }

    NiftyImage image =
      renderEngine.createImage(
          attributes.get("backgroundImage"),
          attributes.getAsBoolean("filter", Convert.DEFAULT_IMAGE_FILTER));
    if (image == null) {
      return;
    }
    image.setImageMode(convert.imageMode(attributes.get("imageMode")));
    imageRenderer.setImage(image);
  }
}
