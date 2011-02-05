package de.lessvoid.nifty.examples.helloworld;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.TargetElementResolver;

public class CustomHint implements EffectImpl {
  private Nifty nifty;
  private Element targetElement;
  private String hintText;

  public void activate(final Nifty niftyParam, final Element element, final EffectProperties parameter) {
    this.nifty = niftyParam;

    TargetElementResolver resolver = new TargetElementResolver(nifty.getCurrentScreen(), element);
    targetElement = resolver.resolve(parameter.getProperty("targetElement"));

    String text = parameter.getProperty("hintText");
    if (text != null) {
      hintText = text;
    }
  }

  public void execute(final Element element, final float normalizedTime, final Falloff falloff, final NiftyRenderEngine r) {
    if (targetElement != null) {
      if (hintText != null) {
        Element el = targetElement.findElementByName("#content");
        if (el != null) {
          el.getRenderer(TextRenderer.class).setText(hintText);
        }
      }
      targetElement.setConstraintX(new SizeValue(element.getX() + element.getWidth() - 50 + "px"));
      targetElement.setConstraintY(new SizeValue(element.getY() + element.getHeight() - 50 + "px"));
      targetElement.show();
      nifty.getCurrentScreen().layoutLayers();
    }
  }

  public void deactivate() {
    if (targetElement != null) {
      targetElement.hide();
    }
    System.out.println(nifty.getCurrentScreen().debugOutput());
  }
}
