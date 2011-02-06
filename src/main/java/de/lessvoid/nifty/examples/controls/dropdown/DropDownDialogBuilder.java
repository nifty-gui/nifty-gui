package de.lessvoid.nifty.examples.controls.dropdown;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.examples.controls.common.CommonBuilders;

/**
 * The DropDownDialogBuilder registers a new control with
 * Nifty and gives us an appropriate ControlBuilder back we can use to Builder-construct
 * the actual control.
 * 
 * @author void
 */
public class DropDownDialogBuilder {
  private static final String CONTROL_NAME = "dropDownDialogControl";
  private CommonBuilders builders = new CommonBuilders();

  public DropDownDialogBuilder(final Nifty nifty) {
    new ControlDefinitionBuilder(CONTROL_NAME) {{
      controller(new DropDownDialogController());
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
            control(builders.createLabel("DropDown:"));
            control(new DropDownBuilder("dropDown") {{
              width("*");
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
