package de.lessvoid.nifty.examples.controls.sliderandscrollbar;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.Scrollbar;
import de.lessvoid.nifty.controls.ScrollbarChangedEvent;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The SliderAndScrollbarDialogController to show off the new Slider and Scrollbar Controls and a couple of more new Nifty 1.3 things.
 * @author void
 */
public class SliderAndScrollbarDialogController implements Controller {
  private Screen screen;
  private Element color;
  private float red;
  private float green;
  private float blue;
  private float alpha;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    this.screen = screen;
    this.color = screen.findElementByName("color");
    this.red = 0.f;
    this.green = 0.f;
    this.blue = 0.f;
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
  }

  @Override
  public void onStartScreen() {
    getSlider("sliderR").setup(0.f, 255.f,   0.f, 1.f, 10.f);
    getSlider("sliderG").setup(0.f, 255.f,   0.f, 1.f, 10.f);
    getSlider("sliderB").setup(0.f, 255.f,   0.f, 1.f, 10.f);
    getSlider("sliderA").setup(0.f, 255.f, 255.f, 1.f, 10.f);

    getScrollbar("scrollbarH").setWorldMax(1000.f);
    getScrollbar("scrollbarH").setWorldPageSize(10.f);

    getTextfield("scrollbarH_WorldMax_Textfield").setText(String.valueOf((int)getScrollbar("scrollbarH").getWorldMax()));
    getTextfield("scrollbarH_CurrentValue_Textfield").setText(String.valueOf((int)getScrollbar("scrollbarH").getValue()));
    getTextfield("scrollbarH_ViewMax_Textfield").setText(String.valueOf((int)getScrollbar("scrollbarH").getWorldPageSize()));
    getTextfield("scrollbarH_ButtonStepSize_Textfield").setText(String.valueOf((int)getScrollbar("scrollbarH").getButtonStepSize()));
    getTextfield("scrollbarH_PageStepSize_Textfield").setText(String.valueOf((int)getScrollbar("scrollbarH").getPageStepSize()));
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @NiftyEventSubscriber(id="sliderR")
  public void onRedSliderChange(final String id, final SliderChangedEvent event) {
    red = event.getValue();
    changeColor();
  }

  @NiftyEventSubscriber(id="sliderG")
  public void onGreenSliderChange(final String id, final SliderChangedEvent event) {
    green = event.getValue();
    changeColor();
  }

  @NiftyEventSubscriber(id="sliderB")
  public void onBlueSliderChange(final String id, final SliderChangedEvent event) {
    blue = event.getValue();
    changeColor();
  }

  @NiftyEventSubscriber(id="sliderA")
  public void onAlphaSliderChange(final String id, final SliderChangedEvent event) {
    alpha = event.getValue();
    changeColor();
  }

  @NiftyEventSubscriber(id="scrollbarH")
  public void onScrollbarHChanged(final String id, final ScrollbarChangedEvent event) {
    getTextfield("scrollbarH_CurrentValue_Textfield").setText(String.valueOf(getScrollbar("scrollbarH").getValue()));
  }

  @NiftyEventSubscriber(id="scrollbarH_WorldMax_Textfield")
  public void onMaxValueChanged(final String id, final TextFieldChangedEvent event) {
    try {
      float f = Float.valueOf(event.getText());
      Scrollbar scrollbar = getScrollbar("scrollbarH");
      scrollbar.setWorldMax(f);
    } catch (NumberFormatException e) {
    }
  }

  @NiftyEventSubscriber(id="scrollbarH_ViewMax_Textfield")
  public void onViewMaxValueChanged(final String id, final TextFieldChangedEvent event) {
    try {
      float f = Float.valueOf(event.getText());
      Scrollbar scrollbar = getScrollbar("scrollbarH");
      scrollbar.setWorldPageSize(f);
    } catch (NumberFormatException e) {
    }
  }

  @NiftyEventSubscriber(id="scrollbarH_CurrentValue_Textfield")
  public void onCurrentValueChanged(final String id, final TextFieldChangedEvent event) {
    try {
      float f = Float.valueOf(event.getText());
      Scrollbar scrollbar = getScrollbar("scrollbarH");
      scrollbar.setValue(f);
    } catch (NumberFormatException e) {
    }
  }

  @NiftyEventSubscriber(id="scrollbarH_ButtonStepSize_Textfield")
  public void onButtonStepSizeChanged(final String id, final TextFieldChangedEvent event) {
    try {
      float f = Float.valueOf(event.getText());
      Scrollbar scrollbar = getScrollbar("scrollbarH");
      scrollbar.setButtonStepSize(f);
    } catch (NumberFormatException e) {
    }
  }

  @NiftyEventSubscriber(id="scrollbarH_PageStepSize_Textfield")
  public void onPageStepSizeChanged(final String id, final TextFieldChangedEvent event) {
    try {
      float f = Float.valueOf(event.getText());
      Scrollbar scrollbar = getScrollbar("scrollbarH");
      scrollbar.setPageStepSize(f);
    } catch (NumberFormatException e) {
    }
  }

  private void changeColor() {
    color.getRenderer(PanelRenderer.class).setBackgroundColor(new Color(red / 255.f, green / 255.f, blue / 255.f, alpha / 255.f));
    getLabel("redLabel").setText(String.valueOf((int)red));
    getLabel("greenLabel").setText(String.valueOf((int)green));
    getLabel("blueLabel").setText(String.valueOf((int)blue));
    getLabel("alphaLabel").setText(String.valueOf((int)alpha));
  }

  private Slider getSlider(final String id) {
    return (Slider) screen.findNiftyControl(id, Slider.class);
  }

  private Scrollbar getScrollbar(final String id) {
    return (Scrollbar) screen.findNiftyControl(id, Scrollbar.class);
  }

  private TextField getTextfield(final String id) {
    return (TextField) screen.findNiftyControl(id, TextField.class);
  }

  private Label getLabel(final String id) {
    return (Label) screen.findNiftyControl(id, Label.class);
  }
}
