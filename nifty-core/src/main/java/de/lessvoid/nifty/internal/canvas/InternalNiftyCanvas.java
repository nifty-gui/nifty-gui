package de.lessvoid.nifty.internal.canvas;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.api.NiftyColor;

public class InternalNiftyCanvas {
  private final List<Command> commands = new ArrayList<Command>();
  private boolean changed = true;

  public InternalNiftyCanvas() {
  }

  public void setFillColor(final NiftyColor color) {
    commands.add(new CommandFillColor(color));
    changed = true;
  }

  public void setStrokeColor(final NiftyColor color) {
    commands.add(new CommandStrokeColor(color));
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

  public boolean isChanged() {
    return changed;
  }

  public List<Command> getCommands() {
    ArrayList<Command> result = new ArrayList<Command>(commands);
    changed = false;
    return result;
  }

  public void reset() {
    commands.clear();
    changed = true;
  }

  public void clear() {
  }
}
