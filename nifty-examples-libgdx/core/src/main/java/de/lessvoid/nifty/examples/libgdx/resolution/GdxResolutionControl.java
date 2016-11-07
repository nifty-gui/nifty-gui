package de.lessvoid.nifty.examples.libgdx.resolution;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.utils.Array;
import de.lessvoid.nifty.examples.libgdx.resolution.GdxResolutionControl.Resolution;
import de.lessvoid.nifty.examples.resolution.ResolutionControl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 *
 * Based on ResolutionControlJOGL & ResolutionControlSlick
 */
public class GdxResolutionControl implements ResolutionControl<Resolution> {
  private Array<Resolution> resolutions;
  private Resolution current;
  private boolean initialized;

  public GdxResolutionControl() {
    initialized = false;
  }

  @Nonnull
  @Override
  public Collection<Resolution> getResolutions() {
    // Use lazy initialization, since LibGDX is not yet initialized during the construction of this class
    if (! initialized) {
      initialize();
    }
    return Arrays.asList(resolutions.toArray());
  }

  @Override
  public void setResolution(@Nonnull final Resolution newResolution) {
    // Use lazy initialization, since LibGDX is not yet initialized during the construction of this class
    if (! initialized) {
      initialize();
    }
    if (newResolution.apply()) current = newResolution;
  }

  @Override
  public Resolution getCurrentResolution() {
    // Use lazy initialization, since LibGDX is not yet initialized during the construction of this class
    if (! initialized) {
      initialize();
    }
    return current;
  }

  private void initialize() {
    populateResolutions();
    sortResolutions();
    initialized = true;
  }

  private void populateResolutions() {
    resolutions = new Array();
    current = new Resolution(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    resolutions.add(current);
    for (DisplayMode displayMode : Gdx.graphics.getDisplayModes()) {
      Resolution temp = new Resolution(displayMode.width, displayMode.height);
      if (! resolutions.contains(temp, false)) resolutions.add(temp);
    }
  }

  private void sortResolutions() {
    resolutions.sort(new Comparator<Resolution>() {
      @Override
      public int compare(@Nonnull Resolution o1, @Nonnull Resolution o2) {
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
  }

  public static class Resolution {
    private final int width;
    private final int height;

    public Resolution(int width, int height) {
      this.width = width;
      this.height = height;
    }

    // Returns true if the resolution was successfully applied, false otherwise.
    public boolean apply() {
      boolean wasApplied = Gdx.graphics.setWindowedMode(width, height);
      if (wasApplied) {
        Gdx.gl.glViewport(0, 0, width, height);
/*        Gdx.gl.glMatr(GL11.GL_PROJECTION);
        Gdx.gl.glLoadIdentity();
        Gdx.gl.glOrthof(0.0f, (float) width, height, 0.0f, -9999.0f, 9999.0f);
        Gdx.gl.glMatrixMode(GL11.GL_MODELVIEW);
        Gdx.gl.glLoadIdentity();*/
      }
      return wasApplied;
    }

    public int getWidth() {
      return width;
    }

    public int getHeight() {
      return height;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + getHeight();
      result = prime * result + getWidth();
      return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Resolution other = (Resolution) obj;
      if (getHeight() != other.getHeight())
        return false;
      if (getWidth() != other.getWidth())
        return false;
      return true;
    }

    @Nonnull
    @Override
    public String toString() {
      return String.valueOf(width) + " x " + height;
    }
  }
}
