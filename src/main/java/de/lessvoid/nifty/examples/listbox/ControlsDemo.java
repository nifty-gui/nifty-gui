/*
 * Created on 12.02.2005
 *  
 */
package de.lessvoid.nifty.examples.listbox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.LabelBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.tools.TimeProvider;

public final class ControlsDemo {

  public static void main(final String[] args) {
    // init lwjgl
    if (!LwjglInitHelper.initSubSystems("Nifty Controls Demonstation")) {
      System.exit(0);
    }

    // create Nifty and load default styles and controls
    Nifty nifty = new Nifty(new LwjglRenderDevice(), new OpenALSoundDevice(), LwjglInitHelper.getInputSystem(), new TimeProvider());
//    nifty.loadStyleFile("grey-style/nifty-grey-styles.xml");
    nifty.loadStyleFile("nifty-default-styles.xml");
    nifty.loadControlFile("nifty-default-controls.xml");
    nifty.registerSound("popup", "sound/744__elmomo__MutantJewsArp.wav");

    // create a screen and add it to nifty and then start the screen
    Screen screen = createScreen(nifty);
    nifty.addScreen("start", screen);
    nifty.gotoScreen("start");

    // start the render loop
    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }

  private static Screen createScreen(final Nifty nifty) {
    Screen screen = new ScreenBuilder("start") {{
      controller(new ControlsDemoScreenController()); // we use this class as the screen controller

      layer(new LayerBuilder("layer") {{
        backgroundImage("background-new.png");
        childLayoutCenter();

        panel(new PanelBuilder("dialog") {{
          style("nifty-panel");
          childLayoutVertical();
          padding("18px,28px,28px,16px");
          width("55%");
          height("65%");
          alignCenter();
          valignCenter();
          onStartScreenEffect(createMoveEffect("in", "top"));
          onEndScreenEffect(createMoveEffect("out", "bottom"));
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            label(createLabel("ListBox:"));
            control(new ControlBuilder("listBox", "listBox") {{
              set("displayItems", "4");
              set("vertical", "true");
              set("horizontal", "true");
              width("*");
            }});
          }});
          panel(vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            label(createLabel("Append:"));
            control(new ControlBuilder("addTextField", "textfield"));
            panel(new PanelBuilder() {{
              width("8px");
            }});
            control(new ControlBuilder("appendButton", "button") {{
              set("label", "Append");
              width("100px");
              height("28px");
              interactOnClick("addListBoxItem()");
            }});
          }});
          panel(vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            label(createLabel("Multi Selection:"));
            control(new ControlBuilder("multiSelectionCheckBox", "checkbox") {{
              set("checked", "false"); // start with uncheck
            }});
            panel(hspacer("20px"));
            label(createShortLabel("Force Selection:"));
            panel(hspacer("6px"));
            control(new ControlBuilder("forceSelectionCheckBox", "checkbox") {{
              set("checked", "false"); // start with uncheck
            }});
            panel(hspacer("20px"));
            label(createShortLabel("Disable Selection:"));
            panel(hspacer("6px"));
            control(new ControlBuilder("disableSelectionCheckBox", "checkbox") {{
              set("checked", "false"); // start with uncheck
            }});
          }});
          panel(vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            label(createLabel("Current Sel.:"));
            control(new ControlBuilder("selectionListBox", "listBox") {{
              set("displayItems", "4");
              set("selectionMode", "Disabled");
              set("vertical", "true");
              set("horizontal", "false");
              width("*");
              childLayoutVertical();
            }});
          }});
          panel(vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            label(createLabel("Remove Sel.:"));
            control(new ControlBuilder("removeSelectionButton", "button") {{
              set("label", "Remove Selection From ListBox");
              width("280px");
              interactOnClick("removeSelectionAction()");
            }});
          }});
        }});
      }});
      layer(new LayerBuilder("navigation") {{
        childLayoutVertical();
        panel(new PanelBuilder() {{
          backgroundColor("#f002");
          height("10%");
          childLayoutHorizontal();
          control(new ControlBuilder("changeButton", "button") {{
            set("label", "Change");
            interactOnClick("changeStyle(grey-style/nifty-grey-styles.xml)");
          }});
        }});
      }});
    }}.build(nifty);

    // just add some items to the listbox
    @SuppressWarnings("unchecked")
    ListBox<JustAnExampleModelClass> listBox = (ListBox<JustAnExampleModelClass>) screen.findNiftyControl("listBox", ListBox.class);
    listBox.addItem(new JustAnExampleModelClass("You can add more lines to this ListBox."));
    listBox.addItem(new JustAnExampleModelClass("Use the append button to do this."));

    return screen;
  }

  private static EffectBuilder createMoveEffect(final String mode, final String direction) {
    return new EffectBuilder("move") {{
      parameter("mode", mode);
      parameter("direction", direction);
      parameter("timeType", "exp");
      parameter("factor", "3.5");
      length(500);
      startDelay(0);
      inherit(true);
    }};
  }

  private static PanelBuilder vspacer() {
    return new PanelBuilder() {{
      childLayoutHorizontal();
      height("12px");
    }};
  }

  private static PanelBuilder hspacer(final String width) {
    return new PanelBuilder() {{
      width(width);
    }};
  }

  private static LabelBuilder createLabel(final String name) {
    return new LabelBuilder() {{
      text(name);
      width("120px");
      alignLeft();
      textVAlignCenter();
      textHAlignLeft();
    }};
  }

  private static LabelBuilder createShortLabel(final String name) {
    return new LabelBuilder() {{
      text(name);
      alignLeft();
      textVAlignCenter();
      textHAlignLeft();
    }};
  }
}
