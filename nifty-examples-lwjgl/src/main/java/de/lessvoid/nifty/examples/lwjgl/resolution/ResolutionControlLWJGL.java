package de.lessvoid.nifty.examples.lwjgl.resolution;

import de.lessvoid.nifty.examples.resolution.ResolutionControl;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ResolutionControlLWJGL implements ResolutionControl<DisplayMode> {
  private final boolean useCoreProfile;

  public ResolutionControlLWJGL(final boolean useCoreProfile) {
    this.useCoreProfile = useCoreProfile;
  }

  @Nonnull
  @Override
  public Collection<DisplayMode> getResolutions() {
    try {
      DisplayMode currentMode = Display.getDisplayMode();
      List<DisplayMode> sorted = new ArrayList<DisplayMode>();

      DisplayMode[] modes = Display.getAvailableDisplayModes();
      for (int i = 0; i < modes.length; i++) {
        DisplayMode mode = modes[i];
        if (mode.getBitsPerPixel() == 32 && mode.getFrequency() == currentMode.getFrequency()) {
          sorted.add(mode);
        }
      }

      Collections.sort(sorted, new Comparator<DisplayMode>() {
        @Override
        public int compare(@Nonnull DisplayMode o1, @Nonnull DisplayMode o2) {
          int widthCompare = Integer.valueOf(o1.getWidth()).compareTo(o2.getWidth());
          if (widthCompare != 0) {
            return widthCompare;
          }
          int heightCompare = Integer.valueOf(o1.getHeight()).compareTo(o2.getHeight());
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
  public void setResolution(@Nonnull DisplayMode newResolution) {
    try {
      Display.setDisplayMode(newResolution);

      if (!useCoreProfile) {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, newResolution.getWidth(), newResolution.getHeight(), 0, -9999, 9999);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
      }
    } catch (@Nonnull final LWJGLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public DisplayMode getCurrentResolution() {
    return Display.getDisplayMode();
  }
}
