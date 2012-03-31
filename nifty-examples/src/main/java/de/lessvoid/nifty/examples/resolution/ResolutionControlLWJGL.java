package de.lessvoid.nifty.examples.resolution;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * Created by IntelliJ IDEA. User: Martin Karing Date: 31.03.12 Time: 13:29 To change this template use File | Settings
 * | File Templates.
 */
public class ResolutionControlLWJGL implements ResolutionControl<DisplayMode> {
  @Override
  public Collection<DisplayMode> getResolutions() {
    try {
      DisplayMode currentMode = Display.getDisplayMode();
      List<DisplayMode> sorted = new ArrayList<DisplayMode>();

      DisplayMode[] modes = Display.getAvailableDisplayModes();
      for (int i=0; i<modes.length; i++) {
        DisplayMode mode = modes[i];
        if (mode.getBitsPerPixel() == 32 && mode.getFrequency() == currentMode.getFrequency()) {
          sorted.add(mode);
        }
      }

      Collections.sort(sorted, new Comparator<DisplayMode>() {
        @Override
        public int compare(DisplayMode o1, DisplayMode o2) {
          int widthCompare = Integer.valueOf(o1.getWidth()).compareTo(Integer.valueOf(o2.getWidth()));
          if (widthCompare != 0) {
            return widthCompare;
          }
          int heightCompare = Integer.valueOf(o1.getHeight()).compareTo(Integer.valueOf(o2.getHeight()));
          if (heightCompare != 0) {
            return heightCompare;
          }
          return o1.toString().compareTo(o2.toString());
        }
      });

      return sorted;
    } catch (Exception e) {
    }

    return new ArrayList<DisplayMode>();
  }

  @Override
  public void setResolution(DisplayMode newResolution) {
    try {
      Display.setDisplayMode(newResolution);
      GL11.glMatrixMode(GL11.GL_PROJECTION);
      GL11.glLoadIdentity();
      GL11.glOrtho(0, newResolution.getWidth(), newResolution.getHeight(), 0, -9999, 9999);

      GL11.glMatrixMode(GL11.GL_MODELVIEW);
    } catch (final LWJGLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public DisplayMode getCurrentResolution() {
    return Display.getDisplayMode();
  }
}
