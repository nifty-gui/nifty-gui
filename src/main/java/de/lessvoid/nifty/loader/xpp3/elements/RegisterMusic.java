package de.lessvoid.nifty.loader.xpp3.elements;


import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlElementProcessor;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.sound.SoundSystem;
/**
 * RegisterMusic.
 * @author void
 */
public class RegisterMusic implements XmlElementProcessor {
  /**
   * SoundSystem.
   */
  private SoundSystem soundSystem;

  /**
   * Create.
   * @param soundSystemParam SoundSystem
   */
  public RegisterMusic(final SoundSystem soundSystemParam) {
    this.soundSystem = soundSystemParam;
  }

  /**
   * process.
   * @param xmlParser parser
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    String id = attributes.get("id");
    String filename = attributes.get("filename");
    soundSystem.addMusic(id, filename);
    xmlParser.nextTag();
  }
}