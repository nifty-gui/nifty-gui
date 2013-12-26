package de.lessvoid.nifty.examples.defaultcontrols.dragndrop;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.*;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.dragndrop.builder.DraggableBuilder;
import de.lessvoid.nifty.controls.dragndrop.builder.DroppableBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.examples.defaultcontrols.common.CommonBuilders;
import de.lessvoid.nifty.examples.defaultcontrols.common.DialogPanelControlDefinition;

import javax.annotation.Nonnull;

/**
 * The DragAndDropDialogRegister registers a new control (the whole DragAndDropDialog) with
 * Nifty. We can later simply generate the whole dialog using a control with the given NAME.
 * 
 * @author void
 */
public class DragAndDropDialogDefinition {
  @Nonnull
  public static final String NAME = "dragAndDropDialog";
  @Nonnull
  private static final CommonBuilders builders = new CommonBuilders();

  public static void register(@Nonnull final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new DragAndDropDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        control(new LabelBuilder("dragAndDropDescription", "Drop the Key on the Chest to open it.") {{
          width("100%");
          textHAlignCenter();
        }});
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(new DroppableBuilder("chest") {{
            width("101px");
            height("171px");
            panel(new PanelBuilder() {{
              childLayoutOverlay();
              image(new ImageBuilder("chest-image") {{
                filename("defaultcontrols/dragndrop/Chest Open.png");
              }});
              image(new ImageBuilder("chest-open") {{
                filename("defaultcontrols/dragndrop/Chest Lid.png");
                onCustomEffect(new EffectBuilder("move") {{
                  effectParameter("mode", "toOffset");
                  effectParameter("offsetY", "-100");
                  length(250);
                  customKey("switchOpen");
                  neverStopRendering(true);
                }});
                onCustomEffect(new EffectBuilder("fade") {{
                  effectParameter("start", "#f");
                  effectParameter("end", "#0");
                  length(250);
                  customKey("switchOpen");
                  neverStopRendering(true);
                }});
              }});
            }});
          }});
          panel(new PanelBuilder() {{
            width("*");
          }});
          control(new DroppableBuilder("key-initial") {{
            width("101px");
            height("171px");
            onActiveEffect(new EffectBuilder("border") {{
              effectParameter("color", "#0003");
            }});
            control(new DraggableBuilder("key") {{
              childLayoutCenter();
              image(new ImageBuilder() {{
                filename("defaultcontrols/dragndrop/Key.png");
              }});
            }});
          }});
        }});
        panel(new PanelBuilder() {{
          width("100%");
          childLayoutVertical();
          text(new TextBuilder() {{
            style("base-font-link");
            text("\"Danc's Miraculously Flexible Game Prototyping Tiles\"");
            set("action", "openLink(http://www.lostgarden.com/2007/05/dancs-miraculously-flexible-game.html)");
            textHAlignRight();
            alignRight();
          }});
        }});
        panel(new PanelBuilder() {{
          width("100%");
          childLayoutVertical();
          text(new TextBuilder() {{
            style("base-font");
            text("art by Daniel Cook (Lostgarden.com)");
            textHAlignRight();
            alignRight();
          }});
        }});
        panel(builders.vspacer());
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          width("100%");
          childLayoutVertical();
          control(new ButtonBuilder("resetButton", "Reset") {{
            alignCenter();
          }});
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
