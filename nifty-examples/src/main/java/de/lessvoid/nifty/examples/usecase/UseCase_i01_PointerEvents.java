package de.lessvoid.nifty.examples.usecase;

import net.engio.mbassy.listener.Handler;
import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCallback;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.NiftyStatisticsMode;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.event.NiftyPointerClickedEvent;
import de.lessvoid.nifty.api.event.NiftyPointerDraggedEvent;
import de.lessvoid.nifty.api.event.NiftyPointerEnterNodeEvent;
import de.lessvoid.nifty.api.event.NiftyPointerExitNodeEvent;
import de.lessvoid.nifty.api.event.NiftyPointerHoverEvent;
import de.lessvoid.nifty.api.event.NiftyPointerPressedEvent;

/**
 * Mouse hover over an element.
 * @author void
 */
public class UseCase_i01_PointerEvents {
  private NiftyNode niftyNode;
  private NiftyNode childNode;
  private int mouseStartX;
  private int mouseStartY;

  public UseCase_i01_PointerEvents(final Nifty nifty) {
    nifty.showStatistics(NiftyStatisticsMode.ShowFPS);

    niftyNode = nifty.createRootNode(UnitValue.px(256), UnitValue.px(256), ChildLayout.Absolute);
    niftyNode.setBackgroundColor(NiftyColor.BLUE());

    childNode = niftyNode.newChildNode(UnitValue.px(100), UnitValue.px(100));
    childNode.setXConstraint(UnitValue.px(100));
    childNode.setYConstraint(UnitValue.px(100));
    childNode.setBackgroundColor(NiftyColor.RED());
    childNode.startAnimated(0, 16, new NiftyCallback<Float>() {
      @Override
      public void execute(final Float t) {
        //childNode.setRotationZ(t * 100.f);
      }
    });
    childNode.subscribe(this);
  }

  @Handler
  private void onPointerEnter(final NiftyPointerEnterNodeEvent event) {
    childNode.setBackgroundColor(NiftyColor.GREEN());
  }

  @Handler
  private void onPointerHover(final NiftyPointerHoverEvent event) {
  }

  @Handler
  private void onPointerLeave(final NiftyPointerExitNodeEvent event) {
    childNode.setBackgroundColor(NiftyColor.RED());
  }

  @Handler
  private void onPointerPressed(final NiftyPointerPressedEvent event) {
    childNode.setBackgroundColor(NiftyColor.YELLOW());
    mouseStartX = event.getX();
    mouseStartY = event.getY();
  }

  @Handler
  private void onPointerLeave(final NiftyPointerClickedEvent event) {
    childNode.setBackgroundColor(NiftyColor.WHITE());
  }

  @Handler
  private void onPointerDragged(final NiftyPointerDraggedEvent event) {
    childNode.setBackgroundColor(NiftyColor.BLACK());
    
    int dx = event.getX() - mouseStartX;
    int dy = event.getY() - mouseStartY;


    childNode.setXConstraint(UnitValue.px(childNode.getX() + dx));
    childNode.setYConstraint(UnitValue.px(childNode.getY() + dy));

    mouseStartX = event.getX();
    mouseStartY = event.getY();
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_i01_PointerEvents.class, args);
  }
}
