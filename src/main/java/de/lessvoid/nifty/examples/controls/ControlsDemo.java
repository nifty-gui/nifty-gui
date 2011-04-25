/*
 * Created on 12.02.2005
 *  
 */
package de.lessvoid.nifty.examples.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.PopupBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.StyleBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.console.builder.ConsoleBuilder;
import de.lessvoid.nifty.examples.controls.chatcontrol.ChatControlDialogDefinition;
import de.lessvoid.nifty.examples.controls.common.CommonBuilders;
import de.lessvoid.nifty.examples.controls.common.DialogPanelControlDefinition;
import de.lessvoid.nifty.examples.controls.common.MenuButtonControlDefinition;
import de.lessvoid.nifty.examples.controls.dragndrop.DragAndDropDialogDefinition;
import de.lessvoid.nifty.examples.controls.dropdown.DropDownDialogControlDefinition;
import de.lessvoid.nifty.examples.controls.listbox.ListBoxDialogControlDefinition;
import de.lessvoid.nifty.examples.controls.scrollpanel.ScrollPanelDialogControlDefinition;
import de.lessvoid.nifty.examples.controls.sliderandscrollbar.SliderAndScrollbarDialogControlDefinition;
import de.lessvoid.nifty.examples.controls.textfield.TextFieldDialogControlDefinition;
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
    registerTextLinkStyle(nifty);
    registerConsolePopup(nifty);

    // register some helper controls
    MenuButtonControlDefinition.register(nifty);
    DialogPanelControlDefinition.register(nifty);

    // register the dialog controls
    ListBoxDialogControlDefinition.register(nifty);
    DropDownDialogControlDefinition.register(nifty);
    ScrollPanelDialogControlDefinition.register(nifty);
    ChatControlDialogDefinition.register(nifty);
    TextFieldDialogControlDefinition.register(nifty);
    SliderAndScrollbarDialogControlDefinition.register(nifty);
    DragAndDropDialogDefinition.register(nifty);
    
    nifty.addScreen("start", createIntroScreen(nifty));
    nifty.addScreen("demo", createDemoScreen(nifty));
    nifty.gotoScreen("demo");

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
    Screen screen = new ScreenBuilder("demo") {{
      controller(new ControlsDemoScreenController(
          "menuButtonListBox", "dialogListBox",
          "menuButtonDropDown", "dialogDropDown",
          "menuButtonTextField", "dialogTextField",
          "menuButtonSlider", "dialogSliderAndScrollbar",
          "menuButtonScrollPanel", "dialogScrollPanel",
          "menuButtonChatControl", "dialogChatControl",
          "menuButtonDragAndDrop", "dialogDragAndDrop"
      ));
      inputMapping("de.lessvoid.nifty.input.mapping.DefaultInputMapping"); // this will enable Keyboard events for the screen controller
      layer(new LayerBuilder("layer") {{
        backgroundImage("background-new.png");
        childLayoutVertical();
        panel(new PanelBuilder("navigation") {{
          width("100%");
          height("63px");
          backgroundColor("#5588");
          childLayoutHorizontal();
          padding("20px");
          control(MenuButtonControlDefinition.getControlBuilder("menuButtonListBox", "ListBox", "This demonstrates the ListBox control.\n\nThis example shows adding and removing items from a ListBox\nas well as the different selection modes that are available."));
          panel(builders.hspacer("10px"));
          control(MenuButtonControlDefinition.getControlBuilder("menuButtonDropDown", "DropDown", "The DropDown demonstration.\n\nThis shows how to dynamically add items to the\nDropDown control as well as the change event."));
          panel(builders.hspacer("10px"));
          control(MenuButtonControlDefinition.getControlBuilder("menuButtonTextField", "TextField", "The TextField demonstration.\n\nThis example demonstrates the Textfield example using the password\nmode and the input length restriction. It also demonstrates\nall of the new events the Textfield publishes on the Eventbus."));
          panel(builders.hspacer("10px"));
          control(MenuButtonControlDefinition.getControlBuilder("menuButtonSlider", "Slider & Scrollbars", "Sliders and Scrollbars example.\n\nThis creates sliders to change a RGBA value and it\ndisplays a scrollbar that can be customized."));
          panel(builders.hspacer("10px"));
          control(MenuButtonControlDefinition.getControlBuilder("menuButtonScrollPanel", "ScrollPanel", "ScrollPanel demonstration.\n\nThis simply shows an image and uses the ScrollPanel\nto scroll around its area. You can directly input\nthe x/y position you want the ScrollPanel to scroll to."));
          panel(builders.hspacer("10px"));
          control(MenuButtonControlDefinition.getControlBuilder("menuButtonChatControl", "ChatControl", "Nifty User ractoc contributed a chat control"));
          panel(builders.hspacer("10px"));
          control(MenuButtonControlDefinition.getControlBuilder("menuButtonDragAndDrop", "Drag and Drop", "Demonstration of the extended Drag and Drop possibilities with Nifty 1.3"));
        }});
        panel(new PanelBuilder("dialogParent") {{
          childLayoutOverlay();
          width("100%");
          alignCenter();
          valignCenter();
          control(new ControlBuilder("dialogListBox", ListBoxDialogControlDefinition.NAME));
          control(new ControlBuilder("dialogTextField", TextFieldDialogControlDefinition.NAME));
          control(new ControlBuilder("dialogSliderAndScrollbar", SliderAndScrollbarDialogControlDefinition.NAME));
          control(new ControlBuilder("dialogDropDown", DropDownDialogControlDefinition.NAME));
          control(new ControlBuilder("dialogScrollPanel", ScrollPanelDialogControlDefinition.NAME));
          control(new ControlBuilder("dialogChatControl", ChatControlDialogDefinition.NAME));
          control(new ControlBuilder("dialogDragAndDrop", DragAndDropDialogDefinition.NAME));
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

  private static void registerTextLinkStyle(final Nifty nifty) {
    // add a special style we use for the link
    new StyleBuilder() {{
      id("base-font-link");
      base("base-font");
      color("#8fff");
      interactOnRelease("$action");
      onHoverEffect(new HoverEffectBuilder("changeMouseCursor") {{
        effectParameter("id", "hand");
      }});
    }}.build(nifty);
  }

  private static void registerConsolePopup(Nifty nifty) {
    new PopupBuilder("consolePopup") {{
      childLayoutAbsolute();
      panel(new PanelBuilder() {{
        childLayoutCenter();
        width("100%");
        height("100%");
        alignCenter();
        valignCenter();
        control(new ConsoleBuilder("console") {{
          width("80%");
          lines(25);
          alignCenter();
          valignCenter();
          onStartScreenEffect(new EffectBuilder("move") {{
            length(150);
            inherit();
            neverStopRendering(true);
            effectParameter("mode", "in");
            effectParameter("direction", "top");
          }});
          onEndScreenEffect(new EffectBuilder("move") {{
            length(150);
            inherit();
            neverStopRendering(true);
            effectParameter("mode", "out");
            effectParameter("direction", "top");
          }});
        }});
      }});
    }}.registerPopup(nifty);
  }
}
