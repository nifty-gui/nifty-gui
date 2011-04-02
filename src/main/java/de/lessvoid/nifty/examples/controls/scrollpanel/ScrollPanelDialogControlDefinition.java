package de.lessvoid.nifty.examples.controls.scrollpanel;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.scrollpanel.builder.ScrollPanelBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.examples.controls.common.CommonBuilders;
import de.lessvoid.nifty.examples.controls.common.DialogPanelControlDefinition;

/**
 * The ScrollPanelDialogControlDefinition registers a new control with Nifty
 * that represents the whole Dialog. This gives us later an appropriate
 * ControlBuilder to actual construct the Dialog (as a control).
 * @author void
 */
public class ScrollPanelDialogControlDefinition {
  public static final String NAME = "dialogScrollPanel";
  private static CommonBuilders builders = new CommonBuilders();

  public static void register(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new ScrollPanelDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("ScrollPanel:"));
          control(new ScrollPanelBuilder("scrollPanel") {{
            width("*");
            height("*");
            image(new ImageBuilder() {{
              filename("background-new.png");
            }});
          }});
        }});
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Position X:"));
          control(new TextFieldBuilder("scrollpanelXPos") {{
            width("50px");
          }});
        }});
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Position Y:"));
          control(new TextFieldBuilder("scrollpanelYPos") {{
            width("50px");
          }});
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
