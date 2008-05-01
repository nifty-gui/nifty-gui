package de.lessvoid.nifty.loader.xpp3.processor;

import java.util.Hashtable;
import java.util.Map;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.RegisterMusicType;
import de.lessvoid.nifty.sound.SoundSystem;

/**
 * RegisterMusic.
 * @author void
 */
public class RegisterMusicTypeProcessor implements XmlElementProcessor {

  /**
   * registered sound.
   */
  private Map < String, RegisterMusicType > registeredMusic = new Hashtable < String, RegisterMusicType >();

  /**
   * process.
   * @param xmlParser parser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    String id = attributes.get("id");
    String filename = attributes.get("filename");
    registeredMusic.put(id, new RegisterMusicType(id, filename));

    xmlParser.nextTag();
  }

  /**
   * register music in soundSystem.
   * @param soundSystem soundSystem
   */
  public void register(final SoundSystem soundSystem) {
    for (RegisterMusicType registerMusicType : registeredMusic.values()) {
      registerMusicType.register(soundSystem);
    }
  }
}
