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
import de.lessvoid.nifty.loaderv2.types.resolver.parameter.ParameterResolver;
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

  public void perform(
      final Element element,
      final NiftyRenderEngine renderEngine,
      final ParameterResolver parameterResolver) {
    if (attributes == null) {
      return;
    }
    Attributes resolved = parameterResolver.resolve(attributes);
    element.setConstraintHeight(convert.sizeValue(resolved.get("height")));
    element.setConstraintWidth(convert.sizeValue(resolved.get("width")));
    element.setConstraintX(convert.sizeValue(resolved.get("x")));
    element.setConstraintY(convert.sizeValue(resolved.get("y")));
    element.setConstraintHorizontalAlign(convert.horizontalAlign(resolved.get("align")));
    element.setConstraintVerticalAlign(convert.verticalAlign(resolved.get("valign")));
    element.setPaddingLeft(convert.paddingSizeValue(resolved.get("paddingLeft")));
    element.setPaddingRight(convert.paddingSizeValue(resolved.get("paddingRight")));
    element.setPaddingTop(convert.paddingSizeValue(resolved.get("paddingTop")));
    element.setPaddingBottom(convert.paddingSizeValue(resolved.get("paddingBottom")));
    processPadding(element, resolved);
    element.setClipChildren(resolved.getAsBoolean("childClip", Convert.DEFAULT_CHILD_CLIP));
    element.setVisible(resolved.getAsBoolean("visible", Convert.DEFAULT_VISIBLE));
    element.setVisibleToMouseEvents(resolved.getAsBoolean("visibleToMouse", Convert.DEFAULT_VISIBLE_TO_MOUSE));
    element.setLayoutManager(convert.layoutManager(resolved.get("childLayout")));
    element.setFocusable(resolved.getAsBoolean("focusable", Convert.DEFAULT_FOCUSABLE));

    applyRenderer(element, renderEngine, resolved);
  }

  private void processPadding(final Element element, final Attributes resolved) {
    if (resolved.isSet("padding")) {
      try {
        PaddingAttributeParser paddingParser = new PaddingAttributeParser(resolved.get("padding"));
        element.setPaddingLeft(convert.paddingSizeValue(paddingParser.getPaddingLeft()));
        element.setPaddingRight(convert.paddingSizeValue(paddingParser.getPaddingRight()));
        element.setPaddingTop(convert.paddingSizeValue(paddingParser.getPaddingTop()));
        element.setPaddingBottom(convert.paddingSizeValue(paddingParser.getPaddingBottom()));
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
