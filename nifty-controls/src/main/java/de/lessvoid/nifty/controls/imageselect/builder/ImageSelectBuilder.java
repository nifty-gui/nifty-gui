package de.lessvoid.nifty.controls.imageselect.builder;

import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;

public class ImageSelectBuilder extends ControlBuilder {
  public ImageSelectBuilder(@Nonnull final String id) {
    super(id, "imageSelect");
  }

  public void setImageWidth(@Nonnull final SizeValue imageWidth) {
    set("imageWidth", imageWidth.toString());
  }

  public void setImageHeight(@Nonnull final SizeValue imageHeight) {
    set("imageHeight", imageHeight.toString());
  }

  public void addImage(@Nonnull final String filename) {
    String imageList = get("imageList");
    if (imageList == null || imageList.length() == 0) {
      set("imageList", filename);
      return;
    }
    set("imageList", imageList + "," + filename);
  }
}
