package de.lessvoid.nifty.controls.button;

import java.util.Properties;

import org.bushe.swing.event.EventTopicSubscriber;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * Implementation of the TextButton Control.
 * @author void
 * @deprecated Please use {@link de.lessvoid.nifty.controls.Button} when accessing NiftyControls.
 */
@Deprecated
public class ButtonControl extends AbstractController implements Button {
  private Nifty nifty;
  private Screen screen;
  private FocusHandler focusHandler;
  private Element buttonTextElement;
  private TextRenderer buttonTextRenderer;

  @Override
  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element newElement,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    super.bind(newElement);
    nifty = niftyParam;
    screen = screenParam;
    buttonTextElement = getElement().findElementByName("#text");
    buttonTextRenderer = buttonTextElement.getRenderer(TextRenderer.class);
    focusHandler = screen.getFocusHandler();
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    EventTopicSubscriber<NiftyMousePrimaryClickedEvent> mouseClickedSubscriber = new EventTopicSubscriber<NiftyMousePrimaryClickedEvent>() {
      @Override
      public void onEvent(final String topic, final NiftyMousePrimaryClickedEvent data) {
        nifty.publishEvent(topic, new ButtonClickedEvent(ButtonControl.this));
      }
    };
    nifty.subscribe(screen, getElement().getId(), NiftyMousePrimaryClickedEvent.class, mouseClickedSubscriber);
    super.init(parameter, controlDefinitionAttributes);
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onFocus(final boolean getFocus) {
    super.onFocus(getFocus);
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    Element buttonElement = getElement();
    if (inputEvent == NiftyStandardInputEvent.NextInputElement) {
      if (focusHandler != null) {
        focusHandler.getNext(buttonElement).setFocus();
      }
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.PrevInputElement) {
      if (focusHandler != null) {
        focusHandler.getPrev(buttonElement).setFocus();
      }
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.Activate) {
      buttonClick();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.MoveCursorDown) {
      if (focusHandler != null) {
        Element nextElement = focusHandler.getNext(buttonElement);
        if (nextElement.getParent().equals(buttonElement.getParent())) {
          nextElement.setFocus();
          return true;
        }
      }
    } else if (inputEvent == NiftyStandardInputEvent.MoveCursorUp) {
      if (focusHandler != null) {
        Element prevElement = focusHandler.getPrev(buttonElement);
        if (prevElement.getParent().equals(buttonElement.getParent())) {
          prevElement.setFocus();
          return true;
        }
      }
    }
    return false;
  }

  private void buttonClick() {
    getElement().onClick();
  }

  // Button Implementation

  @Override
  public void activate() {
    buttonClick();
  }

  @Override
  public String getText() {
    return buttonTextRenderer.getOriginalText();
  }

  @Override
  public void setText(final String text) {
    buttonTextRenderer.setText(text);
    if (!buttonTextRenderer.isLineWrapping()) {
      buttonTextElement.setConstraintWidth(new SizeValue(buttonTextRenderer.getTextWidth() + "px"));
    }
  }

  @Override
  public int getTextWidth() {
    return buttonTextRenderer.getTextWidth();
  }

  @Override
  public int getTextHeight() {
    return buttonTextRenderer.getTextHeight();
  }

  @Override
  public RenderFont getFont() {
    return buttonTextRenderer.getFont();
  }

  @Override
  public void setFont(final RenderFont fontParam) {
    buttonTextRenderer.setFont(fontParam);
  }

  @Override
  public VerticalAlign getTextVAlign() {
    return buttonTextRenderer.getTextVAlign();
  }

  @Override
  public void setTextVAlign(final VerticalAlign newTextVAlign) {
    buttonTextRenderer.setTextVAlign(newTextVAlign);
  }

  @Override
  public HorizontalAlign getTextHAlign() {
    return buttonTextRenderer.getTextHAlign();
  }

  @Override
  public void setTextHAlign(final HorizontalAlign newTextHAlign) {
    buttonTextRenderer.setTextHAlign(newTextHAlign);
  }

  @Override
  public Color getTextColor() {
    return buttonTextRenderer.getColor();
  }

  @Override
  public void setTextColor(final Color newColor) {
    buttonTextRenderer.setColor(newColor);
  }
}
