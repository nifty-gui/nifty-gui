package de.lessvoid.nifty.loaderv2.types.apply;

import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.loaderv2.types.helper.PaddingAttributeParser;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.xml.xpp3.Attributes;

public class ApplyAttributes {
  private Logger log = Logger.getLogger(ApplyAttributes.class.getName());
  private Convert convert;
  private Attributes attributes;
  private Map < Class < ? extends ElementRenderer >, ApplyRenderer > rendererApplier;

  public ApplyAttributes(final Convert convertParam, final Attributes attributesParam) {
    convert = convertParam;
    attributes = attributesParam;
    setupRendererApply();
  }

  private void setupRendererApply() {
    rendererApplier = new Hashtable < Class < ? extends ElementRenderer>, ApplyRenderer >();
    rendererApplier.put(TextRenderer.class, new ApplyRenderText(convert));
    rendererApplier.put(ImageRenderer.class, new ApplyRendererImage(convert));
    rendererApplier.put(PanelRenderer.class, new ApplyRendererPanel(convert));
  }

  public void perform(final Element element, final NiftyRenderEngine renderEngine) {
    if (attributes == null) {
      return;
    }
    element.setConstraintHeight(convert.sizeValue(attributes.get("height")));
    element.setConstraintWidth(convert.sizeValue(attributes.get("width")));
    element.setConstraintX(convert.sizeValue(attributes.get("x")));
    element.setConstraintY(convert.sizeValue(attributes.get("y")));
    element.setConstraintHorizontalAlign(convert.horizontalAlign(attributes.get("align")));
    element.setConstraintVerticalAlign(convert.verticalAlign(attributes.get("valign")));
    element.setPaddingLeft(convert.paddingSizeValue(attributes.get("paddingLeft")));
    element.setPaddingRight(convert.paddingSizeValue(attributes.get("paddingRight")));
    element.setPaddingTop(convert.paddingSizeValue(attributes.get("paddingTop")));
    element.setPaddingBottom(convert.paddingSizeValue(attributes.get("paddingBottom")));
    processPadding(element, attributes);
    element.setClipChildren(attributes.getAsBoolean("childClip", Convert.DEFAULT_CHILD_CLIP));
    element.setVisible(attributes.getAsBoolean("visible", Convert.DEFAULT_VISIBLE));
    element.setVisibleToMouseEvents(attributes.getAsBoolean("visibleToMouse", Convert.DEFAULT_VISIBLE_TO_MOUSE));
    element.setLayoutManager(convert.layoutManager(attributes.get("childLayout")));
    element.setFocusable(attributes.getAsBoolean("focusable", Convert.DEFAULT_FOCUSABLE));

    applyRenderer(element, renderEngine, attributes);
  }

  private void processPadding(final Element element, final Attributes resolved) {
    if (resolved.isSet("padding")) {
      try {
        PaddingAttributeParser paddingParser = new PaddingAttributeParser(resolved.get("padding"));
        element.setPaddingLeft(convert.paddingSizeValue(paddingParser.getLeft()));
        element.setPaddingRight(convert.paddingSizeValue(paddingParser.getRight()));
        element.setPaddingTop(convert.paddingSizeValue(paddingParser.getTop()));
        element.setPaddingBottom(convert.paddingSizeValue(paddingParser.getBottom()));
      } catch (final Exception e) {
        log.warning(e.getMessage());
      }
    }
  }

  private void applyRenderer(final Element element, final NiftyRenderEngine renderEngine, final Attributes resolved) {
    ElementRenderer[] elementRenderer = element.getElementRenderer();
    if (elementRenderer == null) {
      return;
    }

    for (ElementRenderer renderer : elementRenderer) {
      ApplyRenderer rendererApply = rendererApplier.get(renderer.getClass());
      rendererApply.apply(element, resolved, renderEngine);
    }
  }
}
