package de.lessvoid.nifty.sound.paulssoundsystem;

public class SupportedCodec {
  private String extension;
  private Class codecClass;

  public SupportedCodec(final String extension, final Class codecClass) {
    this.extension = extension;
    this.codecClass = codecClass;
  }

  public String getExtension() {
    return extension;
  }

  public Class getCodecClass() {
    return codecClass;
  }
}
