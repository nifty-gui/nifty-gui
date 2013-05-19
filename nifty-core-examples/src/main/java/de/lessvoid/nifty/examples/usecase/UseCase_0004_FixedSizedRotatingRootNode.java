package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.NiftyNode.ChildLayout;
import de.lessvoid.nifty.api.UnitValue;

/**
 * A single root node of a fixed size with a background color that is constantly rotating.
 * @author void
 */
public class UseCase_0004_FixedSizedRotatingRootNode implements UseCase {
  private final NiftyNode niftyNode;
  private final NiftyNode childNode;
  private final NiftyNode grandChildNode;
  private float rot = 0;
  private float totalTime = 0;

  public UseCase_0004_FixedSizedRotatingRootNode(final Nifty nifty) {
    niftyNode = nifty.createRootNode(UnitValue.px(300), UnitValue.px(300), ChildLayout.Absolute);
    niftyNode.setBackgroundColor(NiftyColor.GREEN());

    childNode = niftyNode.newChildNode(UnitValue.px(100), UnitValue.px(100), ChildLayout.Center);
    childNode.setBackgroundColor(NiftyColor.BLACK());
    childNode.setXConstraint(UnitValue.px(50));
    childNode.setYConstraint(UnitValue.px(50));

    grandChildNode = childNode.newChildNode(UnitValue.px(25), UnitValue.px(25));
    grandChildNode.setBackgroundColor(NiftyColor.RED());
}

  @Override
  public void update(final Nifty nifty, final float deltaTime) {
    totalTime += deltaTime;
//    rot += deltaTime / 50.f;
    //niftyNode.setRotation(rot/10);
//    niftyNode.setBackgroundColor(NiftyColor.randomColor());
    /*
    if (totalTime > 2000) {
      childNode.setBackgroundColor(NiftyColor.BLUE());
      childNode.setXConstraint(UnitValue.px(50));
      childNode.setYConstraint(UnitValue.px(50));
      totalTime = 0;
    }
    */

    if (totalTime > 100) {
      rot += 1.f;
      childNode.setRotation(rot);
      grandChildNode.setRotation(rot*10);
      totalTime = 0;
    }
    
    /*
    grandChildNode.setRotation(rot);
    if (totalTime > 20) {
      childNode.setBackgroundColor(NiftyColor.randomColor());
      childNode.setRotation(Math.random()*360);
      childNode.setXConstraint(UnitValue.px((int)(Math.random() * 200)));
      childNode.setYConstraint(UnitValue.px((int)(Math.random() * 200)));
      totalTime = 0;
    }
    */
  }
}
