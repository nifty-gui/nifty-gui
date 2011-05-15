package de.lessvoid.nifty.examples.resolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * ScreenController for Hello World Example.
 * @author void
 */
public class ResolutionScreen implements ScreenController {
  private Nifty nifty;
  private Screen screen;
  private DropDown<DisplayMode> dropDown;
  private ListBox<String> listBox;

  @SuppressWarnings("unchecked")
  @Override
  public void bind(final Nifty newNifty, final Screen newScreen) {
    this.nifty = newNifty;
    this.screen = newScreen;
    this.dropDown = screen.findNiftyControl("resolutions", DropDown.class);
    this.listBox = screen.findNiftyControl("listBox", ListBox.class);

    // get all DisplayModes from LWJGL and add their descriptions to the DropDown
    fillResolutionDropDown(screen);

    // and make sure the current is selected too
    dropDown.selectItem(Display.getDisplayMode());

    listBox.addItem("Test");
    listBox.addItem("TestTestTestTestTestTestTestTestTestTestTestTest");
    listBox.selectItem("Test");
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  @NiftyEventSubscriber(id="resolutions")
  public void onResolution(final String id, final DropDownSelectionChangedEvent<DisplayMode> event) {
    DisplayMode displayMode = event.getSelection();
    try {
      Display.setDisplayMode(displayMode);

      GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, displayMode.getWidth(), displayMode.getHeight(), 0, -9999, 9999);

      GL11.glMatrixMode(GL11.GL_MODELVIEW);

      nifty.resolutionChanged();
    } catch (LWJGLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get all LWJGL DisplayModes into the DropDown
   * @param screen
   */
  private void fillResolutionDropDown(final Screen screen) {
    try {
      DisplayMode currentMode = Display.getDisplayMode();
      List <DisplayMode> sorted = new ArrayList<DisplayMode>();

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

      for (DisplayMode mode : sorted) {
        dropDown.addItem(mode);
      }
    } catch (Exception e) {
    }
  }
}
