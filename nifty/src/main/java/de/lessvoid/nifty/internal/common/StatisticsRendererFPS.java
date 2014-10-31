package de.lessvoid.nifty.internal.common;

import java.io.IOException;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCallback;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.NiftyRuntimeException;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.controls.Label;

public class StatisticsRendererFPS {

  public StatisticsRendererFPS(final Nifty nifty) {
    NiftyNode fpsNode = nifty.createRootNode(ChildLayout.Vertical, UnitValue.percent(100), UnitValue.wildcard(), ChildLayout.Horizontal);
    fpsNode.setRenderOrder(1000);

    try {
      final Label label = fpsNode.newControl(Label.class);
      label.setFont(nifty.createFont("fonts/aurulent-sans-16.fnt"));
      label.getNode().setBackgroundColor(NiftyColor.BLACK());
      label.getNode().startAnimated(0, 1000, new NiftyCallback<Float>() {
        @Override
        public void execute(final Float t) {
          label.setText(nifty.getStatistics().getFpsText());
        }
      });
    } catch (IOException e) {
      throw new NiftyRuntimeException(e);
    }
  }
}
