package de.lessvoid.nifty.effects.impl;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * Hint - show a hint, a nifty hint!
 * @author void
 */
public class Hint implements EffectImpl {
  private Nifty nifty;
  private String hintLayerId;
  private String hintPanelId;
  private int hintDelay;

  public void activate(final Nifty niftyParam, final Element element, final EffectProperties parameter) {
    this.nifty = niftyParam;

    final String hintControl = parameter.getProperty("hintControl", "nifty-default-hint");
    final String hintStyle = parameter.getProperty("hintStyle", null);
    final String hintText = parameter.getProperty("hintText", "hint: add a 'hintText' attribute to the hint effect :)");
    hintDelay = Integer.valueOf(parameter.getProperty("hintDelay", "0"));

    // we'll create a new layer on the fly for the hint and because we could really
    // have lots of hints we'll generate a unique id for this new layer
    this.hintLayerId = NiftyIdCreator.generate();
    this.hintPanelId = hintLayerId + "-hint-panel";

    new LayerBuilder(hintLayerId) {{
      childLayoutAbsolute();
      visible(false);
      control(new ControlBuilder(hintPanelId, hintControl) {{
        parameter("hintText", hintText);
        if (hintStyle != null) {
          style(hintStyle);
        }
      }});
    }}.build(niftyParam, niftyParam.getCurrentScreen(), niftyParam.getCurrentScreen().getRootElement());
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    if (normalizedTime > 0.0) {
      final Element hintLayer = nifty.getCurrentScreen().findElementByName(hintLayerId);
      if (!hintLayer.isVisible()) {
        // decide if we can already show the hint
        if (nifty.getNiftyMouse().getNoMouseMovementTime() > hintDelay) {
          Element hintPanel = hintLayer.findElementByName(hintPanelId);
          if (hintPanel != null) {
            hintPanel.setConstraintX(new SizeValue(element.getX() + 20 + "px"));
            hintPanel.setConstraintY(new SizeValue(element.getY() + 20 + "px"));

            hintLayer.layoutElements();
            hintLayer.show();
          }
        }
      }
    }
  }

  public void deactivate() {
    final Element hintLayer = nifty.getCurrentScreen().findElementByName(hintLayerId);
    if (hintLayer == null) {
      return;
    }
    if (hintLayer.isVisible()) {
      hintLayer.startEffect(EffectEventId.onCustom, new EndNotify() {
        @Override
        public void perform() {
          hintLayer.markForRemoval();
        }
      });
    } else {
      hintLayer.markForRemoval();
    }
  }
}
