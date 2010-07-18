package de.lessvoid.nifty.controls.button.controller;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * ButtonControl.
 * @author void
 */
public class ButtonControl implements Controller {
  private Element element;
  private FocusHandler focusHandler;
  private Screen screen;

  public void bind(
      final Nifty nifty,
      final Screen screenParam,
      final Element newElement,
      final Properties parameter,
      final ControllerEventListener listener,
      final Attributes controlDefinitionAttributes) {
    element = newElement;
    screen = screenParam;
  }

  public void onStartScreen() {
    focusHandler = screen.getFocusHandler();
  }

  public void onFocus(final boolean getFocus) {
  }

  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.NextInputElement) {
      focusHandler.getNext(element).setFocus();
      return true;
    } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
      focusHandler.getPrev(element).setFocus();
      return true;
    } else if (inputEvent == NiftyInputEvent.Activate) {
      element.onClick();
      return true;
    } else if (inputEvent == NiftyInputEvent.MoveCursorDown) {
      if (focusHandler != null) {
        Element nextElement = focusHandler.getNext(element);
        if (nextElement.getParent().equals(element.getParent())) {
          nextElement.setFocus();
          return true;
        }
      }
    } else if (inputEvent == NiftyInputEvent.MoveCursorUp) {
      if (focusHandler != null) {
        Element prevElement = focusHandler.getPrev(element);
        if (prevElement.getParent().equals(element.getParent())) {
          prevElement.setFocus();
          return true;
        }
      }
    }
    return false;
  }

  public void setText(final String text) {
    TextRenderer textRenderer = getButtonTextRenderer();
    textRenderer.setText(text);
    if (!textRenderer.isLineWrapping()) {
      buttonTextElement().setConstraintWidth(new SizeValue(textRenderer.getTextWidth() + "px"));
    }
  }

  public int getTextWidth() {
    return getButtonTextRenderer().getTextWidth();
  }

  public int getTextHeight() {
    return getButtonTextRenderer().getTextHeight();
  }

  public RenderFont getFont() {
    return getButtonTextRenderer().getFont();
  }

  public void setFont(final RenderFont fontParam) {
    getButtonTextRenderer().setFont(fontParam);
  }

  public void setTextVAlign(final VerticalAlign newTextVAlign) {
    getButtonTextRenderer().setTextVAlign(newTextVAlign);
  }

  public void setTextHAlign(final HorizontalAlign newTextHAlign) {
    getButtonTextRenderer().setTextHAlign(newTextHAlign);
  }

  public void setColor(final Color newColor) {
    getButtonTextRenderer().setColor(newColor);
  }

  private TextRenderer getButtonTextRenderer() {
    return buttonTextElement().getRenderer(TextRenderer.class);
  }

  private Element buttonTextElement() {
    return element.findElementByName("button-text");
  }
}
