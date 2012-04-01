package de.lessvoid.nifty.examples.slick2d.resolution;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.*;

import de.lessvoid.nifty.examples.resolution.ResolutionControl;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

/**
 * This is the resolution controller that is used for the Slick2D demo. For the lack of the required functions in
 * Slick2D this class uses LWJGL to fetch a list of all valid resolutions and applies them to the game container upon
 * request.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ResolutionControlSlick implements ResolutionControl<DisplayMode> {
  /**
   * The game container. The new resolution will be applied to this one.
   */
  private AppGameContainer slickGameContainer;

  @Override
  public DisplayMode getCurrentResolution() {
    return Display.getDisplayMode();
  }

  @Override
  public Collection<DisplayMode> getResolutions() {
    try {
      DisplayMode currentMode = Display.getDisplayMode();
      List<DisplayMode> sorted = new ArrayList<DisplayMode>();

      DisplayMode[] modes = Display.getAvailableDisplayModes();
      for (int i = 0; i < modes.length; i++) {
        DisplayMode mode = modes[i];
        if ((mode.getBitsPerPixel() == 32) && ((currentMode.getFrequency() == 0) || (mode.getFrequency() == 
            currentMode.getFrequency()))) {
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

  /**
   * Set the container the new resolution is supposed to be applied to.
   *
   * @param container the new container
   */
  public void setContainer(final AppGameContainer container) {
    slickGameContainer = container;
  }

  @Override
  public void setResolution(DisplayMode newResolution) {
    try {
      slickGameContainer.setDisplayMode(newResolution.getWidth(), newResolution.getHeight(), false);
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }
}
