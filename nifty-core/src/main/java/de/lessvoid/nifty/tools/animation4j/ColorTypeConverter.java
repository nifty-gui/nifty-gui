package de.lessvoid.nifty.tools.animation4j;

import com.gemserk.animation4j.converters.TypeConverter;

import de.lessvoid.nifty.tools.Color;

public class ColorTypeConverter implements TypeConverter<Color> {

  @Override
  public int variables() {
    return 4;
  }

  @Override
  public float[] copyFromObject(final Color color, final float[] target) {
    float[] result = target;
    if (result == null) {
      result = new float[variables()];
    }
    result[0] = color.getRed();
    result[1] = color.getGreen();
    result[2] = color.getBlue();
    result[3] = color.getAlpha();
    return result;
  }

  @Override
  public Color copyToObject(final Color color, final float[] source) {
    Color result = color;
    if (result == null) {
      result = Color.NONE();
    }
    result.setRed(source[0]);
    result.setGreen(source[1]);
    result.setBlue(source[2]);
    result.setAlpha(source[3]);
    return result;
  }
}
