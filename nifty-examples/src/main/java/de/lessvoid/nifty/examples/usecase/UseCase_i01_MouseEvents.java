package de.lessvoid.nifty.examples.usecase;

import net.engio.mbassy.listener.Handler;
import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCallback;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.NiftyStatisticsMode;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.event.NiftyMouseEnterNodeEvent;
import de.lessvoid.nifty.api.event.NiftyMouseHoverEvent;
import de.lessvoid.nifty.api.event.NiftyMouseLeaveNodeEvent;

/**
 * Mouse hover over an element.
 * @author void
 */
public class UseCase_i01_MouseEvents {
  private NiftyNode childNode;

  public UseCase_i01_MouseEvents(final Nifty nifty) {
    nifty.showStatistics(NiftyStatisticsMode.ShowFPS);

    NiftyNode niftyNode = nifty.createRootNode(UnitValue.px(256), UnitValue.px(256), ChildLayout.Center);
    niftyNode.setBackgroundColor(NiftyColor.BLUE());

    childNode = niftyNode.newChildNode(UnitValue.px(100), UnitValue.px(100));
    childNode.setBackgroundColor(NiftyColor.RED());
    childNode.startAnimated(0, 16, new NiftyCallback<Float>() {
      @Override
      public void execute(final Float t) {
        childNode.setRotationZ(t * 100.f);
      }
    });
    childNode.subscribe(this);
  }

  @Handler
  private void onMouseEnter(final NiftyMouseEnterNodeEvent event) {
    childNode.setBackgroundColor(NiftyColor.GREEN());
  }

  @Handler
  private void onMouseHover(final NiftyMouseHoverEvent event) {
  }

  @Handler
  private void onMouseLeave(final NiftyMouseLeaveNodeEvent event) {
    childNode.setBackgroundColor(NiftyColor.RED());
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_i01_MouseEvents.class, args);
  }
}
