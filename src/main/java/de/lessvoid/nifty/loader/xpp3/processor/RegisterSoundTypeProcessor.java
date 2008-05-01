package de.lessvoid.nifty.loader.xpp3.processor;

import java.util.Hashtable;
import java.util.Map;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.RegisterSoundType;
import de.lessvoid.nifty.sound.SoundSystem;

/**
 * Register Effect.
 * @author void
 */
public class RegisterSoundTypeProcessor implements XmlElementProcessor {

  /**
   * registered sound.
   */
  private Map < String, RegisterSoundType > registeredSound = new Hashtable < String, RegisterSoundType >();

  /**
   * process.
   * @param xmlParser parser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    String id = attributes.get("id");
    String filename = attributes.get("filename");
    registeredSound.put(id, new RegisterSoundType(id, filename));

    xmlParser.nextTag();
  }

  /**
   * register sound.
   * @param soundSystem soundSystem
   */
  public void register(final SoundSystem soundSystem) {
    for (RegisterSoundType registerSoundType : registeredSound.values()) {
      registerSoundType.register(soundSystem);
    }
  }
}
