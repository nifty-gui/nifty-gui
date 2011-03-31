/*
 * Created on 12.02.2005
 *  
 */
package de.lessvoid.nifty.examples.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.StyleBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.examples.controls.common.CommonBuilders;
import de.lessvoid.nifty.examples.controls.common.LwjglInitHelper;
import de.lessvoid.nifty.examples.controls.custom.MenuButtonBuilder;
import de.lessvoid.nifty.examples.controls.dropdown.DropDownDialogBuilder;
import de.lessvoid.nifty.examples.controls.listbox.ListBoxDialogBuilder;
import de.lessvoid.nifty.examples.controls.scrollpanel.ScrollPanelDialogBuilder;
import de.lessvoid.nifty.examples.controls.sliderandscrollbar.SliderAndScrollbarDialogBuilder;
import de.lessvoid.nifty.examples.controls.textfield.TextFieldDialogBuilder;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.TimeProvider;

public class ControlsDemo {
  private static CommonBuilders builders = new CommonBuilders();

  public static void main(final String[] args) {
    // init lwjgl
    if (!LwjglInitHelper.initSubSystems("Nifty Controls Demonstation")) {
      System.exit(0);
    }

    // create Nifty and load default styles and controls
    Nifty nifty = new Nifty(new LwjglRenderDevice(), new OpenALSoundDevice(), LwjglInitHelper.getInputSystem(), new TimeProvider());
    nifty.loadStyleFile("nifty-default-styles.xml");
    nifty.loadControlFile("nifty-default-controls.xml");
    nifty.registerSound("popup", "sound/744__elmomo__MutantJewsArp.wav");
    nifty.registerSound("intro", "sound/19546__tobi123__Gong_mf2.wav");
    nifty.registerMouseCursor("hand", "mouse-cursor-hand.png", 5, 4);
    registerMenuButtonHintStyle(nifty);

    // register the dialogs
    new ListBoxDialogBuilder(nifty);
    new DropDownDialogBuilder(nifty);
    new TextFieldDialogBuilder(nifty);
    new SliderAndScrollbarDialogBuilder(nifty);
    new ScrollPanelDialogBuilder(nifty);

    nifty.addScreen("start", createIntroScreen(nifty));
    nifty.addScreen("demo", createDemoScreen(nifty));
    nifty.gotoScreen("start");

    // start the render loop
    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }

  private static Screen createIntroScreen(final Nifty nifty) {
    Screen screen = new ScreenBuilder("start") {{
      controller(new DefaultScreenController() {
        @Override
        public void onStartScreen() {
          nifty.gotoScreen("demo");
        }
      });
      layer(new LayerBuilder("layer") {{
        childLayoutCenter();
        onStartScreenEffect(new EffectBuilder("fade") {{
          length(3000);
          effectParameter("start", "#0");
          effectParameter("end", "#f");
        }});
        onStartScreenEffect(new EffectBuilder("playSound") {{
          startDelay(1400);
          effectParameter("sound", "intro");
        }});
        onActiveEffect(new EffectBuilder("gradient") {{
          effectValue("offset", "0%", "color", "#66666fff");
          effectValue("offset", "85%", "color", "#000f");
          effectValue("offset", "100%", "color", "#44444fff");
        }});
        panel(new PanelBuilder() {{
          alignCenter();
          valignCenter();
          childLayoutHorizontal();
          width("856px");
          panel(new PanelBuilder() {{
            width("300px");
            height("256px");
            childLayoutCenter();
            text(new TextBuilder() {{
              text("Nifty 1.3 Core");
              style("base-font");
              alignCenter();
              valignCenter();
              onStartScreenEffect(new EffectBuilder("fade") {{
                length(500);
                startDelay(1700);
                effectParameter("start", "#0");
                effectParameter("end", "#f");
                neverStopRendering(true);
                post(false);
              }});
            }});
          }});
          panel(new PanelBuilder() {{
            alignCenter();
            valignCenter();
            childLayoutOverlay();
            width("256px");
            height("256px");
            onStartScreenEffect(new EffectBuilder("shake") {{
              length(250);
              startDelay(1300);
              inherit();
              effectParameter("global", "false");
              effectParameter("distance", "10.");
            }});
            onStartScreenEffect(new EffectBuilder("imageSize") {{
              length(600);
              startDelay(3000);
              effectParameter("startSize", "1.0");
              effectParameter("endSize", "2.0");
              inherit();
              neverStopRendering(true);
            }});
            onStartScreenEffect(new EffectBuilder("fade") {{
              length(600);
              startDelay(3000);
              effectParameter("start", "#f");
              effectParameter("end", "#0");
              inherit();
              neverStopRendering(true);
            }});
            image(new ImageBuilder() {{
              filename("yin.png");
              onStartScreenEffect(new EffectBuilder("move") {{
                length(1000);
                startDelay(300);
                timeType("exp");
                effectParameter("factor", "6.f");
                effectParameter("mode", "in");
                effectParameter("direction", "left");
              }});
            }});
            image(new ImageBuilder() {{
              filename("yang.png");
              onStartScreenEffect(new EffectBuilder("move") {{
                length(1000);
                startDelay(300);
                timeType("exp");
                effectParameter("factor", "6.f");
                effectParameter("mode", "in");
                effectParameter("direction", "right");
              }});
            }});
          }});
          panel(new PanelBuilder() {{
            width("300px");
            height("256px");
            childLayoutCenter();
            text(new TextBuilder() {{
              text("Nifty 1.3 Standard Controls");
              style("base-font");
              alignCenter();
              valignCenter();
              onStartScreenEffect(new EffectBuilder("fade") {{
                length(500);
                startDelay(1700);
                effectParameter("start", "#0");
                effectParameter("end", "#f");
                neverStopRendering(true);
                post(false);
              }});
            }});
          }});
        }});
      }});
      layer(new LayerBuilder() {{
        backgroundColor("#ddff");
        onStartScreenEffect(new EffectBuilder("fade") {{
          length(1000);
          startDelay(3000);
          effectParameter("start", "#0");
          effectParameter("end", "#f");
        }});
      }});
    }}.build(nifty);
    return screen;
  }

  private static Screen createDemoScreen(final Nifty nifty) {
    final MenuButtonBuilder menuButtonBuilder = new MenuButtonBuilder(nifty);
    Screen screen = new ScreenBuilder("demo") {{
      controller(new ControlsDemoScreenController(
          "menuButtonListBox", "dialogListBox",
          "menuButtonDropDown", "dialogDropDown",
          "menuButtonTextField", "dialogTextField",
          "menuButtonSlider", "dialogSliderAndScrollbar",
          "menuButtonScrollPanel", "dialogScrollPanel"
      ));
      layer(new LayerBuilder("layer") {{
        backgroundImage("background-new.png");
        childLayoutVertical();
        panel(new PanelBuilder("navigation") {{
          width("100%");
          height("63px");
          backgroundColor("#5588");
          childLayoutHorizontal();
          padding("20px");
          control(menuButtonBuilder.getControlBuilder("menuButtonListBox", "ListBox", "This demonstrates the ListBox control.\n\nThis example shows adding and removing items from a ListBox\nas well as the different selection modes that are available."));
          panel(builders.hspacer("10px"));
          control(menuButtonBuilder.getControlBuilder("menuButtonDropDown", "DropDown", "The DropDown demonstration.\n\nThis shows how to dynamically add items to the\nDropDown control as well as the change event."));
          panel(builders.hspacer("10px"));
          control(menuButtonBuilder.getControlBuilder("menuButtonTextField", "TextField", "The TextField demonstration.\n\nThis example demonstrates the Textfield example using the password\nmode and the input length restriction. It also demonstrates\nall of the new events the Textfield publishes on the Eventbus."));
          panel(builders.hspacer("10px"));
          control(menuButtonBuilder.getControlBuilder("menuButtonSlider", "Slider & Scrollbars", "Sliders and Scrollbars example.\n\nThis creates sliders to change a RGBA value and it\ndisplays a scrollbar that can be customized."));
          panel(builders.hspacer("10px"));
          control(menuButtonBuilder.getControlBuilder("menuButtonScrollPanel", "ScrollPanel", "ScrollPanel demonstration.\n\nThis simply shows an image and uses the ScrollPanel\nto scroll around its area. You can directly input\nthe x/y position you want the ScrollPanel to scroll to."));
        }});
        panel(new PanelBuilder("dialogParent") {{
          childLayoutOverlay();
          width("100%");
          alignCenter();
          valignCenter();
          control(ListBoxDialogBuilder.getControlBuilder("dialogListBox"));
          control(TextFieldDialogBuilder.getControlBuilder("dialogTextField"));
          control(SliderAndScrollbarDialogBuilder.getControlBuilder("dialogSliderAndScrollbar"));
          control(DropDownDialogBuilder.getControlBuilder("dialogDropDown"));
          control(ScrollPanelDialogBuilder.getControlBuilder("dialogScrollPanel"));
        }});
      }});
    }}.build(nifty);
    return screen;
  }

  private static void registerMenuButtonHintStyle(final Nifty nifty) {
    new StyleBuilder() {{
      id("special-hint");
      base("nifty-panel-bright");
      childLayoutCenter();
      onShowEffect(new EffectBuilder("fade") {{
        length(150);
        effectParameter("start", "#0");
        effectParameter("end", "#d");
        inherit();
        neverStopRendering(true);
      }});
      onShowEffect(new EffectBuilder("move") {{
        length(150);
        inherit();
        neverStopRendering(true);
        effectParameter("mode", "fromOffset");
        effectParameter("offsetY", "-15");
      }});
      onCustomEffect(new EffectBuilder("fade") {{
        length(150);
        effectParameter("start", "#d");
        effectParameter("end", "#0");
        inherit();
        neverStopRendering(true);
      }});
      onCustomEffect(new EffectBuilder("move") {{
        length(150);
        inherit();
        neverStopRendering(true);
        effectParameter("mode", "toOffset");
        effectParameter("offsetY", "-15");
      }});
    }}.build(nifty);

    new StyleBuilder() {{
      id("special-hint#hint-text");
      base("base-font");
      alignLeft();
      valignCenter();
      textHAlignLeft();
      color(new Color("#000f"));
    }}.build(nifty);
  }
}
