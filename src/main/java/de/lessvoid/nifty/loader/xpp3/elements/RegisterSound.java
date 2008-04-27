package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.logging.Logger;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlElementProcessor;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.sound.SoundSystem;

/**
 * Register Effect.
 * @author void
 */
public class RegisterSound implements XmlElementProcessor {

  /**
   * logger.
   */
  private static Logger log = Logger.getLogger(RegisterSound.class.getName());

  /**
   * SoundSystem.
   */
  private SoundSystem soundSystem;

  /**
   * Create.
   * @param soundSystemParam SoundSystem
   * @throws Exception exception
   */
  public RegisterSound(final SoundSystem soundSystemParam) throws Exception {
    this.soundSystem = soundSystemParam;
  }

  /**
   * process.
   * @param xmlParser parser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    String id = attributes.get("id");
    String filename = attributes.get("filename");
    log.info("register sound [" + id + ":" + filename + "]");
    soundSystem.addSound(id, filename);
    xmlParser.nextTag();
  }
}
