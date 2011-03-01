package de.lessvoid.nifty.effects.impl;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
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
  private String hintTextId;

  public void activate(final Nifty niftyParam, final Element element, final EffectProperties parameter) {
    this.nifty = niftyParam;

    // we'll create a new layer on the fly for the hint and because we could really
    // have lots of hints we'll generate a unique id for this new layer
    this.hintLayerId = NiftyIdCreator.generate();
    this.hintPanelId = hintLayerId + "#hint-panel";
    this.hintTextId = hintLayerId + "#hint-text";

    final String text = parameter.getProperty("hintText", "");

    new LayerBuilder(hintLayerId) {{
      childLayoutAbsolute();
      visible(false);
      panel(new PanelBuilder(hintPanelId) {{
        style("nifty-panel-hint");
        text(new TextBuilder(hintTextId) {{
          text(text);
        }});
      }});
    }}.build(niftyParam, niftyParam.getCurrentScreen(), niftyParam.getCurrentScreen().getRootElement());
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    if (normalizedTime > 0.0) {
      Element hintLayer = nifty.getCurrentScreen().findElementByName(hintLayerId);
      if (!hintLayer.isVisible()) {
        Element hintPanel = hintLayer.findElementByName(hintPanelId);
        if (hintPanel != null) {
          hintPanel.setConstraintX(new SizeValue(element.getX() + "px"));
          hintPanel.setConstraintY(new SizeValue(element.getY() + 40 + "px"));

          hintLayer.layoutElements();
          hintLayer.show();
        }
      }
    }
  }

  public void deactivate() {
    final Element hintLayer = nifty.getCurrentScreen().findElementByName(hintLayerId);
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
