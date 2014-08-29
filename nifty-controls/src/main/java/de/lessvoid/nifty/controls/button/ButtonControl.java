package de.lessvoid.nifty.controls.button;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

/**
 * Implementation of the TextButton Control.
 *
 * @author void
 * @deprecated Please use {@link de.lessvoid.nifty.controls.Button} when accessing NiftyControls.
 */
@Deprecated
public class ButtonControl extends AbstractController implements Button {
  @Nonnull
  private static final Logger log = Logger.getLogger(ButtonControl.class.getName());
  @Nullable
  private Nifty nifty;
  @Nullable
  private FocusHandler focusHandler;
  @Nullable
  private Element buttonTextElement;
  @Nullable
  private TextRenderer buttonTextRenderer;

  @Override
  public void bind(
      @Nonnull final Nifty niftyParam,
      @Nonnull final Screen screenParam,
      @Nonnull final Element newElement,
      @Nonnull final Parameters parameter) {
    super.bind(newElement);
    nifty = niftyParam;

    final Element text = newElement.findElementById("#text");
    if (text == null) {
      log.severe("Button element misses the text content element.");
      return;
    }
    buttonTextElement = text;

    if (newElement.getId() == null) {
      log.warning("Button element has no ID and can't publish any events properly.");
    }

    final TextRenderer renderer = buttonTextElement.getRenderer(TextRenderer.class);
    if (renderer == null) {
      throw new RuntimeException("ButtonControl is corrupted, #text element found, but missing TextRenderer");
    }
    buttonTextRenderer = renderer;
    focusHandler = screenParam.getFocusHandler();
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onFocus(final boolean getFocus) {
    super.onFocus(getFocus);
  }

  public boolean onClick() {
    if (nifty != null) {
      String id = getId();
      if (id != null) {
        nifty.publishEvent(id, new ButtonClickedEvent(this));
      }
    }
    return true;
  }

  public boolean onRelease() {
    if (nifty != null) {
      String id = getId();
      if (id != null) {
        nifty.publishEvent(id, new ButtonReleasedEvent(this));
      }
    }
    return true;
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.Activate) {
      activate();
      return true;
    }

    Element buttonElement = getElement();
    if (buttonElement == null || focusHandler == null) {
      return false;
    }
    if (inputEvent == NiftyStandardInputEvent.NextInputElement) {
      focusHandler.getNext(buttonElement).setFocus();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.PrevInputElement) {
      focusHandler.getPrev(buttonElement).setFocus();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.MoveCursorDown) {
      focusHandler.getNext(buttonElement).setFocus();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.MoveCursorUp) {
      focusHandler.getPrev(buttonElement).setFocus();
      return true;
    }
    return false;
  }

  // Button Implementation

  @Override
  public void activate() {
    Element element = getElement();

    if (element == null) {
      return;
    }

    getElement().onClickAndReleasePrimaryMouseButton();
  }

  @Nonnull
  @Override
  public String getText() {
    if (buttonTextRenderer == null) {
      return "";
    } else {
      return buttonTextRenderer.getOriginalText();
    }
  }

  @Override
  public void setText(@Nonnull final String text) {
    if (buttonTextRenderer != null && buttonTextElement != null) {
      buttonTextRenderer.setText(text);
      if (!buttonTextRenderer.isLineWrapping()) {
        buttonTextElement.setConstraintWidth(SizeValue.px(getTextWidth()));
      }
    } else {
      if (!isBound()) {
        throw new IllegalStateException("Setting the text is not possible before the binding is done.");
      }
      log.warning("Failed to apply the text because the required references are not set. Maybe the element is not " +
          "bound yet?");
    }
  }

  @Override
  public int getTextWidth() {
    return buttonTextRenderer != null ? buttonTextRenderer.getTextWidth() : 0;
  }

  @Override
  public int getTextHeight() {
    return buttonTextRenderer != null ? buttonTextRenderer.getTextHeight() : 0;
  }

  @Nullable
  @Override
  public RenderFont getFont() {
    return buttonTextRenderer != null ? buttonTextRenderer.getFont() : null;
  }

  @Override
  public void setFont(@Nullable final RenderFont fontParam) {
    if (buttonTextRenderer != null) {
      buttonTextRenderer.setFont(fontParam);
    } else {
      if (!isBound()) {
        throw new IllegalStateException("Setting the font is not possible before the binding is done.");
      }
      log.warning("Failed to set the font of the renderer. Maybe the element is not bound yet?");
    }
  }

  @Nonnull
  @Override
  public VerticalAlign getTextVAlign() {
    return buttonTextRenderer != null ? buttonTextRenderer.getTextVAlign() : VerticalAlign.center;
  }

  @Override
  public void setTextVAlign(@Nonnull final VerticalAlign newTextVAlign) {
    if (buttonTextRenderer != null) {
      buttonTextRenderer.setTextVAlign(newTextVAlign);
    } else {
      log.warning("Failed to set the vertical text align. Maybe the element is not bound yet?");
    }
  }

  @Nonnull
  @Override
  public HorizontalAlign getTextHAlign() {
    return buttonTextRenderer != null ? buttonTextRenderer.getTextHAlign() : HorizontalAlign.center;
  }

  @Override
  public void setTextHAlign(@Nonnull final HorizontalAlign newTextHAlign) {
    if (buttonTextRenderer != null) {
      buttonTextRenderer.setTextHAlign(newTextHAlign);
    } else {
      log.warning("Failed to set the horizontal text align. Maybe the element is not bound yet?");
    }
  }

  @Nonnull
  @Override
  public Color getTextColor() {
    return buttonTextRenderer != null ? buttonTextRenderer.getColor() : TextRenderer.DEFAULT_COLOR;
  }

  @Override
  public void setTextColor(@Nonnull final Color newColor) {
    if (buttonTextRenderer != null) {
      buttonTextRenderer.setColor(newColor);
    } else {
      log.warning("Failed to set the text color. Maybe the element is not bound yet?");
    }
  }
}
