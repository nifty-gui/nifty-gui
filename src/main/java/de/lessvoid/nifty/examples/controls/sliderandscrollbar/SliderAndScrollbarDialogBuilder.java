package de.lessvoid.nifty.examples.controls.sliderandscrollbar;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.scrollbar.builder.ScrollbarBuilder;
import de.lessvoid.nifty.controls.slider.builder.SliderBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.examples.controls.common.CommonBuilders;

/**
 * The SliderAndScrollbarDialogBuilder registers a new control (the whole SliderAndScrollbarDialog) with
 * Nifty and gives us an appropriate ControlBuilder back we can use to Builder-construct
 * the actual control.
 * 
 * @author void
 */
public class SliderAndScrollbarDialogBuilder {
  /**
   * We'll register the whole SliderAndScrollbarDialog as a Nifty control with this name here
   * when the class gets instantiated. The getControlBuilder() method can then be
   * used to dynamically create the Dialog (the control) later.
   */
  private static final String CONTROL_NAME = "sliderAndScrollbarDialogControl";

  /**
   * Just a helper class to assist in Builder-create elements.
   */
  private CommonBuilders builders = new CommonBuilders();

  /**
   * This registers the dialog as a new ControlDefintion with Nifty so that we can
   * later create the dialog dynamically.
   * @param nifty
   */
  public SliderAndScrollbarDialogBuilder(final Nifty nifty) {
    new ControlDefinitionBuilder(CONTROL_NAME) {{
      controller(new SliderAndScrollbarDialogController());
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
            control(builders.createLabel("Alpha:"));
            control(new SliderBuilder("sliderA", false) {{
              width("*");
            }});
          }});
          panel(builders.vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            control(builders.createLabel("Color:"));
            height("35%");
            panel(new PanelBuilder() {{
              childLayoutVertical();
              width("23px");
              control(new SliderBuilder("sliderR", true));
              control(new LabelBuilder() {{
                alignCenter();
                text("R");
                width("100%");
              }});
            }});
            panel(builders.hspacer("5px"));
            panel(new PanelBuilder() {{
              childLayoutVertical();
              control(new SliderBuilder("sliderG", true));
              width("23px");
              control(new LabelBuilder() {{
                alignCenter();
                text("G");
                width("100%");
              }});
            }});
            panel(builders.hspacer("5px"));
            panel(new PanelBuilder() {{
              childLayoutVertical();
              control(new SliderBuilder("sliderB", true));
              width("23px");
              control(new LabelBuilder() {{
                alignCenter();
                text("B");
                width("100%");
              }});
            }});
            panel(builders.hspacer("35px"));
            panel(new PanelBuilder() {{
              valignCenter();
              childLayoutVertical();
              width("20%");
              panel(new PanelBuilder() {{
                childLayoutHorizontal();
                control(builders.createLabel("Red:", "50px"));
                panel(builders.hspacer("5px"));
                control(builders.createLabel("redLabel", "", "50px"));
              }});
              panel(new PanelBuilder() {{
                childLayoutHorizontal();
                control(builders.createLabel("Green:", "50px"));
                panel(builders.hspacer("5px"));
                control(builders.createLabel("greenLabel", "", "50px"));
              }});
              panel(new PanelBuilder() {{
                childLayoutHorizontal();
                control(builders.createLabel("Blue:", "50px"));
                panel(builders.hspacer("5px"));
                control(builders.createLabel("blueLabel", "", "50px"));
              }});
              panel(new PanelBuilder() {{
                childLayoutHorizontal();
                control(builders.createLabel("Alpha:", "50px"));
                panel(builders.hspacer("5px"));
                control(builders.createLabel("alphaLabel", "", "50px"));
              }});
            }});
            panel(builders.hspacer("15px"));
            panel(new PanelBuilder() {{
              valignTop();
              childLayoutCenter();
              width("20%");
              panel(new PanelBuilder("color") {{
                alignCenter();
                valignCenter();
                width("50px");
                height("50px");
                backgroundColor("#ffff");
              }});
            }});
            panel(builders.hspacer("9px"));
          }});
          panel(builders.vspacer());
          panel(builders.vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            control(builders.createLabel("Horizontal:"));
            control(new ScrollbarBuilder("scrollbarH", false) {{
              width("*");
            }});
          }});
          panel(builders.vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            control(builders.createLabel("Current Value:"));
            control(new TextFieldBuilder("scrollbarH_CurrentValue_Textfield") {{
              width("50px");
            }});
            panel(builders.hspacer("20px"));
            control(new LabelBuilder() {{
              text("World Max:");
              alignLeft();
              textVAlignCenter();
              textHAlignLeft();
              width("70px");
            }});
            panel(builders.hspacer("15px"));
            control(new TextFieldBuilder("scrollbarH_WorldMax_Textfield") {{
              width("50px");
            }});
            panel(builders.hspacer("20px"));
            control(new LabelBuilder() {{
              text("View Max:");
              alignLeft();
              textVAlignCenter();
              textHAlignLeft();
            }});
            panel(builders.hspacer("15px"));
            control(new TextFieldBuilder("scrollbarH_ViewMax_Textfield") {{
              width("50px");
            }});
          }});
          panel(builders.vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            control(builders.createLabel("Button Step:"));
            control(new TextFieldBuilder("scrollbarH_ButtonStepSize_Textfield") {{
              width("50px");
            }});
            panel(builders.hspacer("20px"));
            control(new LabelBuilder() {{
              text("Page Step:");
              alignLeft();
              textVAlignCenter();
              textHAlignLeft();
              width("70px");
            }});
            panel(builders.hspacer("15px"));
            control(new TextFieldBuilder("scrollbarH_PageStepSize_Textfield") {{
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
