package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.loaderv2.types.helper.CollectionLogger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

public class NiftyStylesType extends XmlBaseType {
  @Nonnull
  private final Collection<RegisterMouseCursorType> registeredMouseCursor = new ArrayList<RegisterMouseCursorType>();
  @Nonnull
  private final Collection<StyleType> styles = new ArrayList<StyleType>();
  @Nonnull
  private final Collection<UseStylesType> useStyles = new ArrayList<UseStylesType>();
  @Nonnull
  private final Collection<RegisterSoundType> registeredSounds = new ArrayList<RegisterSoundType>();
  @Nonnull
  private final Collection<RegisterEffectType> registeredEffect = new ArrayList<RegisterEffectType>();
  
  public void addRegisterMouseCursor(final RegisterMouseCursorType registerMouseCursor) {
    registeredMouseCursor.add(registerMouseCursor);
  }

  public void addStyle(final StyleType newStyle) {
    styles.add(newStyle);
  }

  public void addUseStyles(final UseStylesType newStyle) {
    useStyles.add(newStyle);
  }
  
  public void addregisterSound(RegisterSoundType newSound){
      registeredSounds.add(newSound);
  }
  public void addregisterEffect(RegisterEffectType newEffect){
      registeredEffect.add(newEffect);
  }
  
  public void loadStyles(
      @Nonnull final NiftyLoader niftyLoader,
      @Nonnull final NiftyType niftyType,
      @Nonnull final Nifty nifty,
      @Nonnull final Logger log) throws Exception {
    for (RegisterMouseCursorType registerMouseCursorType : registeredMouseCursor) {
      registerMouseCursorType.translateSpecialValues(nifty, null);
      registerMouseCursorType.materialize(nifty, log);
    }
    for (UseStylesType useStyle : useStyles) {
      useStyle.loadStyle(niftyLoader, niftyType, nifty);
    }
    for (StyleType style : styles) {
      niftyType.addStyle(style);
    }
    for(RegisterSoundType sound : registeredSounds){
        niftyType.addRegisterSound(sound);
    }
    for(RegisterEffectType effect : registeredEffect){
        niftyType.addRegisterEffect(effect);
    }
  }

  @Nonnull
  public String output() {
    int offset = 1;
    return
        "\nNifty Data:\n" + CollectionLogger.out(offset, styles, "styles")
            + "\n" + CollectionLogger.out(offset, useStyles, "useStyles");
  }
}
