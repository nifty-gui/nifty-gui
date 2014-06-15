package de.lessvoid.nifty.internal.canvas;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyFont;
import de.lessvoid.nifty.api.NiftyImage;
import de.lessvoid.nifty.api.NiftyLinearGradient;

public class InternalNiftyCanvas {
  private final List<Command> commands = new ArrayList<Command>();
  private boolean changed = true;

  public InternalNiftyCanvas() {
  }

  public void setFillColor(final NiftyColor color) {
    commands.add(new CommandFillColor(color));
    changed = true;
  }

  public void setFillLinearGradient(final NiftyLinearGradient gradient) {
    commands.add(new CommandFillLinearGradient(gradient));
    changed = true;
  }

  public void setStrokeStyle(final NiftyColor color) {
    commands.add(new CommandStrokeStyle(color));
    changed = true;
  }

  public void setTextColor(final NiftyColor color) {
    commands.add(new CommandTextColor(color));
    changed = true;
  }

  public void setTextSize(final float textSize) {
    commands.add(new CommandTextSize(textSize));
    changed = true;
  }

  public void setLineWidth(final double lineWidth) {
    commands.add(new CommandLineWidth(lineWidth));
    changed = true;
  }

  public void line(final double x0, final double y0, final double x1, final double y1) {
    commands.add(new CommandLine(x0, y0, x1, y1));
    changed = true;
  }

  public void rect(final double x0, final double y0, final double x1, final double y1) {
    commands.add(new CommandRect(x0, y0, x1, y1));
    changed = true;
  }

  public void filledRect(final double x0, final double y0, final double x1, final double y1) {
    commands.add(new CommandFilledRect(x0, y0, x1, y1));
    changed = true;
  }

  public void text(final NiftyFont niftyFont, final int x, final int y, final String text) {
    commands.add(new CommandText(niftyFont, x, y, text));
    changed = true;
  }

  public void image(final int x, final int y, final NiftyImage image) {
    commands.add(new CommandImage(x, y, image));
    changed = true;
  }

  public boolean isChanged() {
    return changed;
  }

  public List<Command> getCommands() {
    changed = false;
    return new ArrayList<Command>(commands);
  }

  public void reset() {
    commands.clear();
    changed = true;
  }

  public void clear() {
  }
}
