package de.lessvoid.nifty.examples.controls.scrollpanel;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.scrollpanel.builder.ScrollPanelBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.examples.controls.common.CommonBuilders;

/**
 * Demo the ScrollPanel. 
 * @author void
 */
public class ScrollPanelDialogBuilder {
  private static final String CONTROL_NAME = "dialogScrollPanel";
  private CommonBuilders builders = new CommonBuilders();

  public ScrollPanelDialogBuilder(final Nifty nifty) {
    new ControlDefinitionBuilder(CONTROL_NAME) {{
      controller(new ScrollPanelDialogController());
      panel(new PanelBuilder() {{
        visible(false);
        childLayoutCenter();
        panel(new PanelBuilder("#effectPanel") {{
          style("nifty-panel");
          childLayoutVertical();
          alignCenter();
          valignCenter();
          width("50%");
          height("60%");
          padding("18px,28px,28px,16px");
          onShowEffect(builders.createMoveEffect("in", "left", 500));
          onHideEffect(builders.createMoveEffect("out", "right", 600));
          onHideEffect(builders.createFadeEffect());
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
      }});
    }}.registerControlDefintion(nifty);
  }

  public static ControlBuilder getControlBuilder(final String id) {
    return new ControlBuilder(id, CONTROL_NAME);
  }
}
