package de.lessvoid.nifty.effects.impl;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * Hint - show a hint, a nifty hint!
 * <p/>
 * This is the new, even more nifty version of the hint. It maintains only a single layer and does not delete this
 * layer once its created. All hints are stored on this single layer. Thanks to the render order system of Nifty the
 * hints are still always shown at the very top of the layer stack.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class Hint implements EffectImpl {
  @Nonnull
  private static final Logger log = Logger.getLogger(Hint.class.getName());
  @Nonnull
  private static final String HINT_LAYER_ID = "niftyHintLayer";
  @Nullable
  private Nifty nifty;
  @Nullable
  private Screen screen;
  @Nullable
  private Element hintLayer;
  @Nullable
  private Element hintPanel;
  private int hintDelay;
  @Nullable
  private String offsetX;
  @Nullable
  private String offsetY;

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    this.nifty = nifty;
    screen = nifty.getCurrentScreen();

    if (screen == null) {
      log.severe("Can't activate hint effect without a screen that is currently active.");
      return;
    }

    final String hintControl = parameter.getProperty("hintControl", "nifty-default-hint");
    final String hintStyle = parameter.getProperty("hintStyle", null);
    final String hintText = parameter.getProperty("hintText", "hint: add a 'hintText' attribute to the hint effect :)");
    hintDelay = Integer.valueOf(parameter.getProperty("hintDelay", "0"));
    offsetX = parameter.getProperty("offsetX", "0");
    offsetY = parameter.getProperty("offsetY", "0");

    Element hintLayer = getHintLayer();
    hintLayer.setVisible(false);

    ControlBuilder builder = new ControlBuilder(NiftyIdCreator.generate(), hintControl);
    builder.parameter("hintText", hintText);
    if (hintStyle != null) {
      builder.style(hintStyle);
    }
    hintPanel = builder.build(nifty, screen, hintLayer);
  }

  @Nonnull
  private Element getHintLayer() {
    if (hintLayer != null) {
      return hintLayer;
    }
    if (screen == null || nifty == null) {
      throw new IllegalStateException("Can't create or retrieve hint layer while there is no active screen.");
    }
    hintLayer = screen.findElementById(HINT_LAYER_ID);
    if (hintLayer != null) {
      return hintLayer;
    }
    LayerBuilder builder = new LayerBuilder(HINT_LAYER_ID);
    builder.childLayout(ElementBuilder.ChildLayoutType.Absolute);
    builder.visible(false);
    hintLayer = builder.build(nifty, screen, screen.getRootElement());
    hintLayer.setRenderOrder(Integer.MAX_VALUE);
    return hintLayer;
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (nifty == null || screen == null || hintLayer == null || hintPanel == null) {
      log.severe("Can't execute effect. Activation not done or failed.");
      return;
    }
    if (normalizedTime > 0.0) {
      if (!hintLayer.isVisible()) {
        // decide if we can already show the hint
        if (nifty.getNiftyMouse().getNoMouseMovementTime() > hintDelay) {
          hintPanel.setConstraintX(SizeValue.px(getPosX(element, hintPanel, r.getWidth())));
          hintPanel.setConstraintY(SizeValue.px(getPosY(element, hintPanel, r.getHeight())));

          // hard remove all old hints, else they only mess with the new ones
          List<Element> hints = hintLayer.getChildren();
          for (int i = 0; i < hints.size() - 1; i++) {
            nifty.removeElement(screen, hints.get(i));
          }

          hintLayer.layoutElements();
          hintLayer.show();
        }
      }
    }
  }

  @Override
  public void deactivate() {
    if (hintPanel != null) {
      if (hintPanel.isVisible()) {
        hintPanel.startEffect(EffectEventId.onCustom, new EndNotify() {
          @Override
          public void perform() {
            removePanel();
          }
        });
      } else {
        removePanel();
      }
    }
  }

  private void removePanel() {
    if (hintPanel != null && hintLayer != null) {
      hintPanel.markForRemoval(new EndNotify() {
        @Override
        public void perform() {
          if (hintLayer.getChildrenCount() <= 1) {
            hintLayer.hide();
          }
        }
      });
    }
  }

  private int getPosX(@Nonnull final Element element, @Nonnull final Element hintPanel, final int screenWidth) {
    int pos;
    if ("center".equals(offsetX)) {
      pos = element.getX() + element.getWidth() / 2 - hintPanel.getWidth() / 2;
    } else if ("left".equals(offsetX)) {
      pos = element.getX();
    } else if ("right".equals(offsetX)) {
      pos = element.getX() + element.getWidth() - hintPanel.getWidth();
    } else {
      pos = Integer.valueOf(offsetX);
    }
    if (pos < 0) {
      pos = 0;
    }
    if (pos + hintPanel.getWidth() > screenWidth) {
      pos = screenWidth - hintPanel.getWidth();
    }
    return pos;
  }

  private int getPosY(@Nonnull final Element element, @Nonnull final Element hintPanel, final int screenHeight) {
    int pos;
    if ("center".equals(offsetY)) {
      pos = element.getY() + element.getHeight() / 2 - hintPanel.getHeight() / 2;
    } else if ("top".equals(offsetY)) {
      pos = element.getY();
    } else if ("bottom".equals(offsetY)) {
      pos = element.getY() + element.getHeight() - hintPanel.getHeight();
    } else {
      pos = Integer.valueOf(offsetY);
    }
    if (pos < 0) {
      pos = 0;
    }
    if (pos + hintPanel.getHeight() > screenHeight) {
      pos = screenHeight - hintPanel.getHeight();
    }
    return pos;
  }

}
