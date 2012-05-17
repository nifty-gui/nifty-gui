package de.lessvoid.nifty.examples.defaultcontrols.tabs;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.TabGroup;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.tabs.builder.TabBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.examples.defaultcontrols.common.CommonBuilders;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The TabsControlDialogController registers a new control with Nifty that represents the whole Dialog. This gives us
 * later an appropriate ControlBuilder to actual construct the Dialog (as a control).
 *
 * @author ractoc
 */
public class TabsControlDialogController implements Controller {

  private TabGroup tabs;
  private static CommonBuilders builders = new CommonBuilders();

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    tabs = screen.findNiftyControl("tabs", TabGroup.class);
    tabs.addTab(new TabBuilder("tab_1", "Tab 1") {{
      childLayoutCenter();
      control(new LabelBuilder("tab1_label", "Tab 1") {{
        height("100%");
        width("100%");
      }});
      height("100%");
      width("100%");
    }});
    tabs.addTab(new TabBuilder("tab_2", "Tab 2") {{
      childLayoutCenter();
      control(new LabelBuilder("tab2_label", "Tab 2") {{
        height("100%");
        width("100%");
      }});
      height("100%");
      width("100%");
    }});
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }
}
