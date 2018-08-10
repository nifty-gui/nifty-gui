package de.lessvoid.nifty.controls.imageselect.builder;

import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;

public class ImageSelectBuilder extends ControlBuilder {
  public ImageSelectBuilder(@Nonnull final String id) {
    super(id, "imageSelect");
  }

  public ImageSelectBuilder setImageWidth(@Nonnull final SizeValue imageWidth) {
    set("imageWidth", imageWidth.getValueAsString());
    return this;
  }

  public ImageSelectBuilder setImageHeight(@Nonnull final SizeValue imageHeight) {
    set("imageHeight", imageHeight.getValueAsString());
    return this;
  }

  public ImageSelectBuilder addImage(@Nonnull final String filename) {
    String imageList = get("imageList");
    if (imageList == null || imageList.length() == 0) {
      set("imageList", filename);
      return this;
    }
    set("imageList", imageList + "," + filename);
    return this;
  }
}
