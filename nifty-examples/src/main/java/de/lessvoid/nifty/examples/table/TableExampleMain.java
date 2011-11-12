package de.lessvoid.nifty.examples.table;

import java.io.IOException;
import java.util.Random;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.controls.listbox.builder.ListBoxBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.TimeProvider;

public class TableExampleMain {

  /**
   * The "model" class. This should really be your own class.
   */
  public static class TableRow {
    public int index;
    public String[] data = new String[5];

    public TableRow(final int index, final String ... param) {
      this.index = index;
      for (int i=0; i<param.length; i++) {
        data[i] = param[i];
      }
    }
  }

  /**
   * The ViewConverter class that connects the model class (TableRow) to the nifty world.
   */
  public static class TableRowViewConverter implements ListBoxViewConverter<TableRow> {
    @Override
    public void display(final Element listBoxItem, final TableRow item) {
      for (int i=0; i<5; i++) {
        Color color = new Color("#ff05");
        if (item.index % 2 == 0) {
          color = new Color("#00f5");
        }
        // get the text element for the row
        Element textElement = listBoxItem.findElementByName("#col-" + String.valueOf(i));
        textElement.getRenderer(TextRenderer.class).setText(item.data[i]);
        listBoxItem.getRenderer(PanelRenderer.class).setBackgroundColor(color);
      }
    }

    @Override
    public int getWidth(final Element listBoxItem, final TableRow item) {
      int width = 0;
      for (int i=0; i<5; i++) {
        TextRenderer renderer = listBoxItem.findElementByName("#col-" + String.valueOf(i)).getRenderer(TextRenderer.class);
        width += renderer.getFont().getWidth(item.data[i]);
      }
      return width;
    }
  }

  public static void main(final String[] args) throws IOException {
    if (!LwjglInitHelper.initSubSystems("Nifty Table Example")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new LwjglRenderDevice(),
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new TimeProvider());

    nifty.loadControlFile("nifty-default-controls.xml");
    nifty.loadStyleFile("nifty-default-styles.xml");

    // first step is to register the control that we'll use for the rows
    ControlDefinitionBuilder rowControlBuilder = new ControlDefinitionBuilder("row") {{
      panel(new PanelBuilder() {{
        height("30px");
        childLayoutHorizontal();
        width("100%");
        alignCenter();
        text(new TextBuilder("#col-0") {{
          width("20%");
          height("100%");
          textVAlignCenter();
          style("base-font");
        }});
        text(new TextBuilder("#col-1") {{
          width("20%");
          height("100%");
          textVAlignCenter();
          style("base-font");
        }});
        text(new TextBuilder("#col-2") {{
          width("20%");
          height("100%");
          textVAlignCenter();
          style("base-font");
        }});
        text(new TextBuilder("#col-3") {{
          width("20%");
          height("100%");
          textVAlignCenter();
          style("base-font");
        }});
        text(new TextBuilder("#col-4") {{
          width("20%");
          height("100%");
          textVAlignCenter();
          style("base-font");
        }});
        visibleToMouse();
        controller("de.lessvoid.nifty.controls.listbox.ListBoxItemController");
        inputMapping("de.lessvoid.nifty.input.mapping.MenuInputMapping");
        onHoverEffect(new HoverEffectBuilder("colorBar") {{
          effectParameter("color", "#880f");
          post(true);
          inset("1px");
          neverStopRendering(true);
          effectParameter("timeType", "infinite");
        }});
        onCustomEffect(new EffectBuilder("colorBar") {{
          customKey("focus");
          post(false);
          effectParameter("color", "#f00f");
          neverStopRendering(true);
          effectParameter("timeType", "infinite");
        }});
        onCustomEffect(new EffectBuilder("colorBar") {{
          customKey("select");
          post(false);
          effectParameter("color", "#f00f");
          neverStopRendering(true);
          effectParameter("timeType", "infinite");
        }});
        onCustomEffect(new EffectBuilder("textColor") {{
          customKey("select");
          post(false);
          effectParameter("color", "#000f");
          neverStopRendering(true);
          effectParameter("timeType", "infinite");
        }});
        onClickEffect(new EffectBuilder("focus") {{
          effectParameter("targetElement", "#parent#parent");
        }});
        interactOnClick("listBoxItemClicked()");
      }});
    }};
    rowControlBuilder.registerControlDefintion(nifty);

    // now build the screen
    ScreenBuilder builder = new ScreenBuilder("start") {{
      layer(new LayerBuilder("layer") {{
        childLayoutCenter();
        backgroundColor("#400f");
        control(new ListBoxBuilder("serverBox") {{
          viewConverterClass(TableRowViewConverter.class);
          displayItems(20);
          hideHorizontalScrollbar();
          width("765px");
          height("500px");
          childLayoutVertical();
          optionalVerticalScrollbar();
          alignCenter();
          valignCenter();
          control(new ControlBuilder("row"));
        }});      
      }});
    }};
    Screen startScreen = builder.build(nifty);

    ListBox<TableRow> listBox = startScreen.findNiftyControl("serverBox", ListBox.class);
    for (int i=0; i<1000; i++) {
      listBox.addItem(new TableRow(i, randomString(), randomString(), randomString(), randomString(), randomString()));
    }

    // finally start the screen
    nifty.gotoScreen("start");

    // and the render loop
    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }

  static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  static Random rnd = new Random();

  private static String randomString() {
     StringBuilder sb = new StringBuilder(5);
     for(int i=0; i<5; i++) { 
        sb.append(AB.charAt(rnd.nextInt(AB.length())));
     }
     return sb.toString();
  }


  private int $() {
    return 9;
  }
}
