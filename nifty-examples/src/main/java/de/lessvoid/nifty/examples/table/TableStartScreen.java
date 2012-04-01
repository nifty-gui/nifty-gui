package de.lessvoid.nifty.examples.table;

import java.util.Random;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.*;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.listbox.builder.ListBoxBuilder;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * ScreenController for TableStartScreen.
 * @author void
 */
public class TableStartScreen implements ScreenController, NiftyExample {
  private Nifty nifty;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    this.nifty = newNifty;
  }

  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return null;
  }

  @Override
  public String getTitle() {
    return "Nifty Table Example";
  }


  @Override
  public void prepareStart(Nifty nifty) {
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
}
