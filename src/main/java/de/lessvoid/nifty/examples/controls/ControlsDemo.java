/*
 * Created on 12.02.2005
 *  
 */
package de.lessvoid.nifty.examples.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.examples.controls.common.CommonBuilders;
import de.lessvoid.nifty.examples.controls.common.LwjglInitHelper;
import de.lessvoid.nifty.examples.controls.custom.MenuButtonBuilder;
import de.lessvoid.nifty.examples.controls.dropdown.DropDownDialogBuilder;
import de.lessvoid.nifty.examples.controls.listbox.ListBoxDialogBuilder;
import de.lessvoid.nifty.examples.controls.scrollpanel.ScrollPanelDialogBuilder;
import de.lessvoid.nifty.examples.controls.sliderandscrollbar.SliderAndScrollbarDialogBuilder;
import de.lessvoid.nifty.examples.controls.textfield.TextFieldDialogBuilder;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
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
    nifty.registerMouseCursor("hand", "mouse-cursor-hand.png", 5, 4);

    // register the dialogs
    new ListBoxDialogBuilder(nifty);
    new DropDownDialogBuilder(nifty);
    new TextFieldDialogBuilder(nifty);
    new SliderAndScrollbarDialogBuilder(nifty);
    new ScrollPanelDialogBuilder(nifty);

    // create a screen and add it to nifty and then start the screen
    Screen screen = createScreen(nifty);
    nifty.addScreen("start", screen);
    nifty.gotoScreen("start");

    // start the render loop
    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }

  private static Screen createScreen(final Nifty nifty) {
    final MenuButtonBuilder menuButtonBuilder = new MenuButtonBuilder(nifty);

    Screen screen = new ScreenBuilder("start") {{
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
          control(menuButtonBuilder.getControlBuilder("menuButtonListBox", "ListBox"));
          panel(builders.hspacer("10px"));
          control(menuButtonBuilder.getControlBuilder("menuButtonDropDown", "DropDown"));
          panel(builders.hspacer("10px"));
          control(menuButtonBuilder.getControlBuilder("menuButtonTextField", "TextField"));
          panel(builders.hspacer("10px"));
          control(menuButtonBuilder.getControlBuilder("menuButtonSlider", "Slider & Scrollbars"));
          panel(builders.hspacer("10px"));
          control(menuButtonBuilder.getControlBuilder("menuButtonScrollPanel", "ScrollPanel"));
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
}
