/*
 * Created on 12.02.2005
 *  
 */
package de.lessvoid.nifty.examples.defaultcontrols;

import org.lwjgl.opengl.DisplayMode;

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
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.console.builder.ConsoleBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.slider.builder.SliderBuilder;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.examples.defaultcontrols.chatcontrol.ChatControlDialogDefinition;
import de.lessvoid.nifty.examples.defaultcontrols.common.CommonBuilders;
import de.lessvoid.nifty.examples.defaultcontrols.common.DialogPanelControlDefinition;
import de.lessvoid.nifty.examples.defaultcontrols.common.MenuButtonControlDefinition;
import de.lessvoid.nifty.examples.defaultcontrols.dragndrop.DragAndDropDialogDefinition;
import de.lessvoid.nifty.examples.defaultcontrols.dropdown.DropDownDialogControlDefinition;
import de.lessvoid.nifty.examples.defaultcontrols.eventconsume.EventConsumeDialogDefinition;
import de.lessvoid.nifty.examples.defaultcontrols.listbox.ListBoxDialogControlDefinition;
import de.lessvoid.nifty.examples.defaultcontrols.messagebox.MessageBoxDialogDefinition;
import de.lessvoid.nifty.examples.defaultcontrols.scrollpanel.ScrollPanelDialogControlDefinition;
import de.lessvoid.nifty.examples.defaultcontrols.sliderandscrollbar.SliderAndScrollbarDialogControlDefinition;
import de.lessvoid.nifty.examples.defaultcontrols.tabs.TabsControlDialogDefinition;
import de.lessvoid.nifty.examples.defaultcontrols.textfield.TextFieldDialogControlDefinition;
import de.lessvoid.nifty.examples.defaultcontrols.treebox.TreeBoxControlDialogDefinition;
import de.lessvoid.nifty.examples.resolution.ResolutionControl;
import de.lessvoid.nifty.examples.resolution.ResolutionControlLWJGL;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;
import de.lessvoid.nifty.tools.Color;

public class ControlsDemo<T> implements NiftyExample {
  private static CommonBuilders builders = new CommonBuilders();

  private final ResolutionControl<T> resolutionControl;

  public ControlsDemo(final ResolutionControl<T> resControl) {
    resolutionControl = resControl;
  }

  public static void main(final String[] args) {
    // init lwjgl
    if (!LwjglInitHelper.initSubSystems("Nifty Controls Demonstation")) {
      System.exit(0);
    }

    // create Nifty and load default styles and controls
    Nifty nifty = new Nifty(new LwjglRenderDevice(true), new OpenALSoundDevice(), LwjglInitHelper.getInputSystem(),
        new AccurateTimeProvider());
    System.out.println(nifty.getVersion());

    ControlsDemo<DisplayMode> demo = new ControlsDemo<DisplayMode>(new ResolutionControlLWJGL());
    demo.prepareStart(nifty);

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
                length(1000);
                effectValue("time", "1700", "value", "0.0");
                effectValue("time", "2000", "value", "1.0");
                effectValue("time", "2600", "value", "1.0");
                effectValue("time", "3200", "value", "0.0");
                post(false);
                neverStopRendering(true);
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
              filename("defaultcontrols/yin.png");
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
              filename("defaultcontrols/yang.png");
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
                length(1000);
                effectValue("time", "1700", "value", "0.0");
                effectValue("time", "2000", "value", "1.0");
                effectValue("time", "2600", "value", "1.0");
                effectValue("time", "3200", "value", "0.0");
                post(false);
                neverStopRendering(true);
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

  private static <T> Screen createDemoScreen(final Nifty nifty, final ResolutionControl<T> resControl) {
    final CommonBuilders common = new CommonBuilders();
    Screen screen = new ScreenBuilder("demo") {{
      controller(
          new ControlsDemoScreenController<T>(
              resControl,
              "menuButtonListBox", "dialogListBox",
              "menuButtonDropDown", "dialogDropDown",
              "menuButtonTextField", "dialogTextField",
              "menuButtonSlider", "dialogSliderAndScrollbar",
              "menuButtonScrollPanel", "dialogScrollPanel",
              "menuButtonDragAndDrop", "dialogDragAndDrop",
              "menuButtonChatControl", "dialogChatControl",
              "menuButtonMessageBox", "dialogMessageBox",
              "menuButtonTabsControl", "dialogTabsControl",
              "menuButtonTreeBoxControl", "dialogTreeBoxControl",
              "menuButtonEventConsumeControl", "dialogEventConsumeControl"));
      inputMapping(
          "de.lessvoid.nifty.input.mapping.DefaultInputMapping"); // this will enable Keyboard events for the screen
          // controller
      layer(new LayerBuilder("layer") {{
        backgroundImage("defaultcontrols/background-new.png");
        childLayoutVertical();
        panel(new PanelBuilder("navigation-1") {{
          width("100%");
          height("43px");
          backgroundColor("#5588");
          childLayoutHorizontal();
          paddingLeft("20px");
          paddingRight("20px");
          paddingTop("10px");
          paddingBottom("10px");
          control(MenuButtonControlDefinition.getControlBuilder("menuButtonListBox", "ListBox",
              "ListBox demonstration\n\nThis example shows adding and removing items from a ListBox\nas well as the " +
                  "different selection modes that are available."));
          panel(builders.hspacer("10px"));
          control(MenuButtonControlDefinition.getControlBuilder("menuButtonDropDown", "DropDown",
              "DropDown and RadioButton demonstration\n\nThis shows how to dynamically add items to the\nDropDown " +
                  "control as well as the change event."));
          panel(builders.hspacer("10px"));
          control(MenuButtonControlDefinition.getControlBuilder("menuButtonTextField", "TextField",
              "TextField demonstration\n\nThis example demonstrates the Textfield example using the password\nmode " +
                  "and the input length restriction. It also demonstrates\nall of the new events the Textfield " +
                  "publishes on the Eventbus."));
          panel(builders.hspacer("10px"));
          control(MenuButtonControlDefinition.getControlBuilder("menuButtonSlider", "Slider & Scrollbars",
              "Sliders and Scrollbars demonstration\n\nThis creates sliders to change a RGBA value and it\ndisplays a" +
                  " scrollbar that can be customized."));
          panel(builders.hspacer("10px"));
          control(MenuButtonControlDefinition.getControlBuilder("menuButtonScrollPanel", "ScrollPanel",
              "ScrollPanel demonstration\n\nThis simply shows an image and uses the ScrollPanel\nto scroll around its" +
                  " area. You can directly input\nthe x/y position you want the ScrollPanel to scroll to."));
          panel(builders.hspacer("10px"));
          control(MenuButtonControlDefinition.getControlBuilder("menuButtonDragAndDrop", "Drag and Drop",
              "Drag and Drop demonstration\n\nDrag and Drop has been extended with Nifty 1.3"));
          panel(builders.hspacer("10px"));
          control(
              MenuButtonControlDefinition.getControlBuilder("menuButtonCredits", "?", "Credits\n\nCredits and Thanks!",
                  "25px"));
        }});
        panel(new PanelBuilder("navigation-2") {{
          width("100%");
          height("33px");
          backgroundColor("#5588");
          childLayoutHorizontal();
          paddingLeft("20px");
          paddingRight("20px");
          paddingTop("0px");
          paddingBottom("10px");
          control(MenuButtonControlDefinition.getControlBuilderUser("menuButtonChatControl", "ChatControl",
              "Chat Control demonstration\n\nThis control was contributed by Nifty User ractoc. It demonstrates\nhow " +
                  "you can combine Nifty standard controls to build more\ncomplex stuff. In this case we've just " +
                  "included his work as\nanother standard control to Nifty! :)"));
          panel(builders.hspacer("10px"));
          control(MenuButtonControlDefinition.getControlBuilderUser("menuButtonMessageBox", "MessageBox",
              "MessageBox demonstration\n\nThis control was contributed by Nifty User ractoc. It demonstrates\nhow " +
                  "you can combine Nifty standard controls to build more\ncomplex stuff. In this case we've just " +
                  "included his work as\nanother standard control to Nifty! :)"));
          panel(builders.hspacer("10px"));
          control(MenuButtonControlDefinition.getControlBuilderUser("menuButtonTabsControl", "TabGroupControl",
              "TabGroup Control demonstration\n\nThis control was contributed by Nifty User ractoc. It " +
                  "demonstrates\nhow you can combine Nifty standard controls to build more\ncomplex stuff. In this " +
                  "case we've just included his work as\nanother standard control to Nifty! :)"));
          panel(builders.hspacer("10px"));
          control(MenuButtonControlDefinition.getControlBuilderUser("menuButtonTreeBoxControl", "TreeBoxControl",
              "TreeBox Control demonstration\n\nThis control was contributed by Nifty User ractoc. It " +
                  "demonstrates\nhow you can combine Nifty standard controls to build more\ncomplex stuff. In this " +
                  "case we've just included his work as\nanother standard control to Nifty! :)"));
          panel(builders.hspacer("10px"));
          control(MenuButtonControlDefinition.getControlBuilderSpecial("menuButtonEventConsumeControl", "Event Consuming",
              "Nifty Event Consuming demonstration\n\nThis demonstrates how Nifty consumes events and how you can" +
                  "influence\nthe event processing to block certain elements from\nreceiving events."));
        }});
        panel(new PanelBuilder("dialogParent") {{
          childLayoutOverlay();
          width("100%");
          alignCenter();
          valignCenter();
          control(new ControlBuilder("dialogListBox", ListBoxDialogControlDefinition.NAME));
          control(new ControlBuilder("dialogDropDown", DropDownDialogControlDefinition.NAME));
          control(new ControlBuilder("dialogTextField", TextFieldDialogControlDefinition.NAME));
          control(new ControlBuilder("dialogSliderAndScrollbar", SliderAndScrollbarDialogControlDefinition.NAME));
          control(new ControlBuilder("dialogScrollPanel", ScrollPanelDialogControlDefinition.NAME));
          control(new ControlBuilder("dialogDragAndDrop", DragAndDropDialogDefinition.NAME));
          control(new ControlBuilder("dialogChatControl", ChatControlDialogDefinition.NAME));
          control(new ControlBuilder("dialogMessageBox", MessageBoxDialogDefinition.NAME));
          control(new ControlBuilder("dialogTabsControl", TabsControlDialogDefinition.NAME));
          control(new ControlBuilder("dialogTreeBoxControl", TreeBoxControlDialogDefinition.NAME));
          control(new ControlBuilder("dialogEventConsumeControl", EventConsumeDialogDefinition.NAME));
        }});
      }});
      layer(new LayerBuilder() {{
        childLayoutVertical();
        panel(new PanelBuilder() {{
          height("*");
        }});
        panel(new PanelBuilder() {{
          childLayoutCenter();
          height("50px");
          width("100%");
          backgroundColor("#5588");
          panel(new PanelBuilder() {{
            paddingLeft("25px");
            paddingRight("25px");
            height("50%");
            width("100%");
            alignCenter();
            valignCenter();
            childLayoutHorizontal();
            control(new LabelBuilder() {{
              label("Screen Resolution:");
            }});
            panel(common.hspacer("7px"));
            control(new DropDownBuilder("resolutions") {{
              width("200px");
            }});
            panel(common.hspacer("20px"));
            control(new LabelBuilder() {{
              label("Scale Resolution:");
            }});
            panel(common.hspacer("7px"));
            control(new SliderBuilder(false) {{
              id("scale-resolution");
              min(0.5f);
              max(1.5f);
              initial(1.0f);
              stepSize(0.01f);
              buttonStepSize(0.01f);
            }});
            panel(common.hspacer("*"));
            control(new ButtonBuilder("resetScreenButton", "Restart Screen") {{
            }});
          }});
        }});
      }});
      layer(new LayerBuilder("whiteOverlay") {{
        onCustomEffect(new EffectBuilder("renderQuad") {{
          customKey("onResolutionStart");
          length(350);
          neverStopRendering(false);
        }});
        onStartScreenEffect(new EffectBuilder("renderQuad") {{
          length(300);
          effectParameter("startColor", "#ddff");
          effectParameter("endColor", "#0000");
        }});
        onEndScreenEffect(new EffectBuilder("renderQuad") {{
          length(300);
          effectParameter("startColor", "#0000");
          effectParameter("endColor", "#ddff");
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

  private static void registerStyles(final Nifty nifty) {
    new StyleBuilder() {{
      id("base-font-link");
      base("base-font");
      color("#8fff");
      interactOnRelease("$action");
      onHoverEffect(new HoverEffectBuilder("changeMouseCursor") {{
        effectParameter("id", "hand");
      }});
    }}.build(nifty);

    new StyleBuilder() {{
      id("creditsImage");
      alignCenter();
    }}.build(nifty);

    new StyleBuilder() {{
      id("creditsCaption");
      font("defaultcontrols/verdana-48-regular.fnt");
      width("100%");
      textHAlignCenter();
    }}.build(nifty);

    new StyleBuilder() {{
      id("creditsCenter");
      base("base-font");
      width("100%");
      textHAlignCenter();
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

  private static void registerCreditsPopup(final Nifty nifty) {
    final CommonBuilders common = new CommonBuilders();
    new PopupBuilder("creditsPopup") {{
      childLayoutCenter();
      panel(new PanelBuilder() {{
        width("80%");
        height("80%");
        alignCenter();
        valignCenter();
        onStartScreenEffect(new EffectBuilder("move") {{
          length(400);
          inherit();
          effectParameter("mode", "in");
          effectParameter("direction", "top");
        }});
        onEndScreenEffect(new EffectBuilder("move") {{
          length(400);
          inherit();
          neverStopRendering(true);
          effectParameter("mode", "out");
          effectParameter("direction", "top");
        }});
        onEndScreenEffect(new EffectBuilder("fadeSound") {{
          effectParameter("sound", "credits");
        }});
        onActiveEffect(new EffectBuilder("gradient") {{
          effectValue("offset", "0%", "color", "#00bffecc");
          effectValue("offset", "75%", "color", "#00213cff");
          effectValue("offset", "100%", "color", "#880000cc");
        }});
        onActiveEffect(new EffectBuilder("playSound") {{
          effectParameter("sound", "credits");
        }});
        padding("10px");
        childLayoutVertical();
        panel(new PanelBuilder() {{
          width("100%");
          height("*");
          childLayoutOverlay();
          childClip(true);
          panel(new PanelBuilder() {{
            width("100%");
            childLayoutVertical();
            onActiveEffect(new EffectBuilder("autoScroll") {{
              length(100000);
              effectParameter("start", "0");
              effectParameter("end", "-3200");
              inherit(true);
            }});
            panel(common.vspacer("800px"));
            text(new TextBuilder() {{
              text("Nifty 1.3");
              style("creditsCaption");
            }});
            text(new TextBuilder() {{
              text("Standard Controls Demonstration using JavaBuilder pattern");
              style("creditsCenter");
            }});
            panel(common.vspacer("30px"));
            text(new TextBuilder() {{
              text("\"Look ma, No XML!\" :)");
              style("creditsCenter");
            }});
            panel(common.vspacer("70px"));
            panel(new PanelBuilder() {{
              width("100%");
              height("256px");
              childLayoutCenter();
              panel(new PanelBuilder() {{
                alignCenter();
                valignCenter();
                childLayoutHorizontal();
                width("656px");
                panel(new PanelBuilder() {{
                  width("200px");
                  height("256px");
                  childLayoutCenter();
                  text(new TextBuilder() {{
                    text("Nifty 1.3 Core");
                    style("base-font");
                    alignCenter();
                    valignCenter();
                  }});
                }});
                panel(new PanelBuilder() {{
                  width("256px");
                  height("256px");
                  alignCenter();
                  valignCenter();
                  childLayoutOverlay();
                  image(new ImageBuilder() {{
                    filename("defaultcontrols/yin.png");
                  }});
                  image(new ImageBuilder() {{
                    filename("defaultcontrols/yang.png");
                  }});
                }});
                panel(new PanelBuilder() {{
                  width("200px");
                  height("256px");
                  childLayoutCenter();
                  text(new TextBuilder() {{
                    text("Nifty 1.3 Standard Controls");
                    style("base-font");
                    alignCenter();
                    valignCenter();
                  }});
                }});
              }});
            }});
            panel(common.vspacer("70px"));
            text(new TextBuilder() {{
              text("written and performed\nby void");
              style("creditsCenter");
            }});
            panel(common.vspacer("100px"));
            text(new TextBuilder() {{
              text("Sound Credits");
              style("creditsCaption");
            }});
            text(new TextBuilder() {{
              text(
                  "This demonstration uses creative commons licenced sound samples\nand music from the following " +
                      "sources");
              style("creditsCenter");
            }});
            panel(common.vspacer("30px"));
            image(new ImageBuilder() {{
              style("creditsImage");
              filename("defaultcontrols/freesound.png");
            }});
            panel(common.vspacer("25px"));
            text(new TextBuilder() {{
              text("19546__tobi123__Gong_mf2.wav");
              style("creditsCenter");
            }});
            panel(common.vspacer("50px"));
            image(new ImageBuilder() {{
              style("creditsImage");
              filename("defaultcontrols/cc-mixter-logo.png");
              set("action", "openLink(http://ccmixter.org/)");
            }});
            panel(common.vspacer("25px"));
            text(new TextBuilder() {{
              text("\"Almost Given Up\" by Loveshadow");
              style("creditsCenter");
            }});
            panel(common.vspacer("100px"));
            text(new TextBuilder() {{
              text("Additional Credits");
              style("creditsCaption");
            }});
            text(new TextBuilder() {{
              text("ueber awesome Yin/Yang graphic by Dori\n(http://www.nadori.de)\n\nThanks! :)");
              style("creditsCenter");
            }});
            panel(common.vspacer("100px"));
            text(new TextBuilder() {{
              text("Special thanks go to");
              style("creditsCaption");
            }});
            text(new TextBuilder() {{
              text(
                  "The following people helped creating Nifty with valuable feedback," +
                      "\nfixing bugs or sending patches.\n(in no particular order)\n\n" +
                      "chaz0x0\n" +
                      "Tumaini\n" +
                      "arielsan\n" +
                      "gaba1978\n" +
                      "ractoc\n" +
                      "bonechilla\n" +
                      "mdeletrain\n" +
                      "mulov\n" +
                      "gouessej\n");
              style("creditsCenter");
            }});
            panel(common.vspacer("75px"));
            text(new TextBuilder() {{
              text("Greetings and kudos go out to");
              style("creditsCaption");
            }});
            text(new TextBuilder() {{
              text("Ariel Coppes and Ruben Garat of Gemserk\n(http://blog.gemserk.com/)\n\n\n" +
                  "Erlend, Kirill, Normen, Skye and Ruth of jMonkeyEngine\n(http://www.jmonkeyengine.com/home/)\n\n\n" +
                  "Brian Matzon, Elias Naur, Caspian Rychlik-Prince for lwjgl\n(http://www.lwjgl.org/\n\n\n" +
                  "KappaOne, MatthiasM, aho, Dragonene, darkprophet, appel, woogley, Riven, " +
                  "NoobFukaire\nfor valuable input and discussions at #lwjgl IRC on the freenode network\n\n\n" +
                  "... and Kevin Glass\n(http://slick.cokeandcode.com/)\n\n\n\n\n\n\n\n" +
                  "As well as everybody that has not yet given up on Nifty =)\n\n" +
                  "And again sorry to all of you that I've forgotten. You rock too!\n\n\n");
              style("creditsCenter");
            }});
            panel(common.vspacer("350px"));
            image(new ImageBuilder() {{
              style("creditsImage");
              filename("defaultcontrols/nifty-logo.png");
            }});
          }});
        }});
        panel(new PanelBuilder() {{
          width("100%");
          paddingTop("10px");
          childLayoutCenter();
          control(new ButtonBuilder("creditsBack") {{
            label("Back");
            alignRight();
            valignCenter();
          }});
        }});
      }});
    }}.registerPopup(nifty);
  }

  @Override
  public String getStartScreen() {
    return "demo";
  }

  @Override
  public String getMainXML() {
    return null;
  }

  @Override
  public String getTitle() {
    return "Nifty Controls Demonstation";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    nifty.loadStyleFile("nifty-default-styles.xml");
    nifty.loadControlFile("nifty-default-controls.xml");
    nifty.registerSound("intro", "defaultcontrols/sound/19546__tobi123__Gong_mf2.wav");
    nifty.registerMusic("credits", "defaultcontrols/sound/Loveshadow_-_Almost_Given_Up.ogg");
    nifty.registerMouseCursor("hand", "defaultcontrols/mouse-cursor-hand.png", 5, 4);
    nifty.enableAutoScaling(1024, 768);

    registerMenuButtonHintStyle(nifty);
    registerStyles(nifty);
    registerConsolePopup(nifty);
    registerCreditsPopup(nifty);

    // register some helper controls
    MenuButtonControlDefinition.register(nifty);
    DialogPanelControlDefinition.register(nifty);

    // register the dialog controls
    ListBoxDialogControlDefinition.register(nifty);
    DropDownDialogControlDefinition.register(nifty);
    ScrollPanelDialogControlDefinition.register(nifty);
    MessageBoxDialogDefinition.register(nifty);
    ChatControlDialogDefinition.register(nifty);
    TabsControlDialogDefinition.register(nifty);
    TreeBoxControlDialogDefinition.register(nifty);
    TextFieldDialogControlDefinition.register(nifty);
    SliderAndScrollbarDialogControlDefinition.register(nifty);
    DragAndDropDialogDefinition.register(nifty);
    EventConsumeDialogDefinition.register(nifty);

    createIntroScreen(nifty);
    createDemoScreen(nifty, resolutionControl);
  }
}
