package de.lessvoid.nifty.examples.usecase;

import java.io.IOException;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.HAlign;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyFont;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.VAlign;
import de.lessvoid.nifty.api.controls.Label;

/**
 * Display a simple text.
 * @author void
 */
public class UseCase_c01_LabelControlTextAlign implements UseCaseUpdateable {
  private final NiftyNode niftyNode;
  private final NiftyFont font;

  public UseCase_c01_LabelControlTextAlign(final Nifty nifty) throws IOException {
    niftyNode = nifty.createRootNodeFullscreen(ChildLayout.Vertical);
    niftyNode.setBackgroundColor(NiftyColor.fromString("#003f"));

    font = nifty.createFont("fonts/aurulent-sans-16.fnt");
    addLabel("TextHAlign: center, TextVAlign: top", HAlign.center, VAlign.top, NiftyColor.fromString("#f60f"));
    addLabel("TextHAlign: center, TextVAlign: center", HAlign.center, VAlign.center, NiftyColor.fromString("#f80f"));
    addLabel("TextHAlign: center, TextVAlign: bottom", HAlign.center, VAlign.bottom, NiftyColor.fromString("#fa0f"));
    addLabel("TextHAlign: left, TextVAlign: center", HAlign.left, VAlign.center, NiftyColor.fromString("#fc0f"));
    addLabel("TextHAlign: center, TextVAlign: center", HAlign.center, VAlign.center, NiftyColor.fromString("#fe0f"));
    addLabel("TextHAlign: right, TextVAlign: center", HAlign.right, VAlign.center, NiftyColor.fromString("#ff2f"));
  }

  private void addLabel(final String text, final HAlign halign, final VAlign valign, final NiftyColor backgroundColor) {
    Label label = niftyNode.newControl(Label.class);
    label.setFont(font);
    label.setText(text);
    label.setColor(NiftyColor.BLACK());
    label.setHAlign(halign);
    label.setVAlign(valign);
    label.getNode().setBackgroundColor(backgroundColor);
    label.getNode().setWidthConstraint(UnitValue.percent(75));
    label.getNode().setHeightConstraint(UnitValue.percent(15));
    label.getNode().setHAlign(HAlign.center);
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_c01_LabelControlTextAlign.class, args);
  }

  @Override
  public void update(Nifty nifty, float deltaTime) {
    niftyNode.requestRedraw();
  }
}
