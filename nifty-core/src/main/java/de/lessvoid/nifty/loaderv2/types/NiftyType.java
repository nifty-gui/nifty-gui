package de.lessvoid.nifty.loaderv2.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.loaderv2.types.helper.CollectionLogger;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.tools.StopWatch;

public class NiftyType extends XmlBaseType {
  private static Logger log = Logger.getLogger(NiftyType.class.getName());

  private Collection < StyleType > styles = new ArrayList < StyleType >();
  private Collection < UseStylesType > useStyles = new ArrayList < UseStylesType >();
  private Collection < UseControlsType > useControls = new ArrayList < UseControlsType >();
  private Collection < RegisterSoundType > registeredSounds = new ArrayList < RegisterSoundType >();
  private Collection < RegisterMusicType > registeredMusic = new ArrayList < RegisterMusicType >();
  private Collection < RegisterMouseCursorType > registeredMouseCursor = new ArrayList < RegisterMouseCursorType >();
  private Collection < RegisterEffectType > registeredEffect = new ArrayList < RegisterEffectType >();
  private Collection < ResourceBundleType > resourceBundles = new ArrayList < ResourceBundleType >();
  private Collection < PopupType > popups = new ArrayList < PopupType >();
  private Collection < ControlDefinitionType > controlDefinitions = new ArrayList < ControlDefinitionType >();
  private Collection < ScreenType > screens = new ArrayList < ScreenType >();

  public void addStyle(final StyleType newStyle) {
    styles.add(newStyle);
  }

  public void addUseStyles(final UseStylesType newStyle) {
    useStyles.add(newStyle);
  }

  public void addUseControls(final UseControlsType useControl) {
    useControls.add(useControl);
  }

  public void addRegisterSound(final RegisterSoundType registerSound) {
    registeredSounds.add(registerSound);
  }

  public void addRegisterMusic(final RegisterMusicType registerMusic) {
    registeredMusic.add(registerMusic);
  }

  public void addRegisterMouseCursor(final RegisterMouseCursorType registerMouseCursor) {
    registeredMouseCursor.add(registerMouseCursor);
  }

  public void addResourceBundle(final ResourceBundleType resourceBundle) {
    resourceBundles.add(resourceBundle);
  }

  public void addRegisterEffect(final RegisterEffectType registerEffect) {
    registeredEffect.add(registerEffect);
  }

  public void addPopup(final PopupType popupType) {
    popups.add(popupType);
  }

  public void addControlDefinition(final ControlDefinitionType controlDefinition) {
    controlDefinitions.add(controlDefinition);
  }

  public void addScreen(final ScreenType screenType) {
    screens.add(screenType);
  }

  public void create(final Nifty nifty, final TimeProvider timeProvider) {
    StopWatch stopWatch = new StopWatch(timeProvider);
    stopWatch.start();
    log.fine("debug out [" + stopWatch.stop() + "]");

    stopWatch.start();
    for (ResourceBundleType resourceBundle : resourceBundles) {
      resourceBundle.translateSpecialValues(nifty, null);
      resourceBundle.materialize(nifty);
    }
    log.fine("resourceBundles [" + stopWatch.stop() + "]");

    stopWatch.start();
    for (StyleType style : styles) {
      style.translateSpecialValues(nifty, null);
      nifty.registerStyle(style);
    }
    log.fine("registerStyle [" + stopWatch.stop() + "]");

    stopWatch.start();
    for (ControlDefinitionType controlDefintion : controlDefinitions) {
      controlDefintion.translateSpecialValues(nifty, null);
      controlDefintion.makeFlat();
      nifty.registerControlDefintion(controlDefintion);
    }
    log.fine("registerControlDefinition [" + stopWatch.stop() + "]");

    stopWatch.start();
    for (RegisterEffectType registerEffectType : registeredEffect) {
      registerEffectType.translateSpecialValues(nifty, null);
      nifty.registerEffect(registerEffectType);
    }
    log.fine("registerEffect [" + stopWatch.stop() + "]");

    stopWatch.start();
    for (RegisterSoundType registerSoundType : registeredSounds) {
      registerSoundType.translateSpecialValues(nifty, null);
      registerSoundType.materialize(nifty.getSoundSystem());
    }
    log.fine("registerSound [" + stopWatch.stop() + "]");

    stopWatch.start();
    for (RegisterMusicType registerMusicType : registeredMusic) {
      registerMusicType.translateSpecialValues(nifty, null);
      registerMusicType.materialize(nifty.getSoundSystem());
    }
    log.fine("registerMusic [" + stopWatch.stop() + "]");

    stopWatch.start();
    for (RegisterMouseCursorType registerMouseCursorType : registeredMouseCursor) {
      registerMouseCursorType.translateSpecialValues(nifty, null);
      registerMouseCursorType.materialize(nifty, log);
    }
    log.fine("registerMouseCursor [" + stopWatch.stop() + "]");

    stopWatch.start();
    for (PopupType popup : popups) {
      popup.translateSpecialValues(nifty, null);
      nifty.registerPopup(popup);
    }
    log.fine("registerPopup [" + stopWatch.stop() + "]");

    stopWatch.start();
    for (ScreenType screen : screens) {
      screen.translateSpecialValues(nifty, null);
      screen.create(nifty, this, timeProvider);
    }
    log.fine("create Screens [" + stopWatch.stop() + "]");
  }

  public void loadStyles(final NiftyLoader niftyLoader, final Nifty nifty) throws Exception {
    for (UseStylesType useStyle : useStyles) {
      useStyle.loadStyle(niftyLoader, this, nifty);
    }
  }

  public void loadControls(final NiftyLoader niftyLoader) throws Exception {
    for (UseControlsType useControl : useControls) {
      useControl.loadControl(niftyLoader, this);
    }
  }

  public String output() {
    int offset = 1;
    return
      "\nNifty Data:\n" + CollectionLogger.out(offset, styles, "styles")
      + "\n" + CollectionLogger.out(offset, useStyles, "useStyles")
      + "\n" + CollectionLogger.out(offset, useControls, "useControls")
      + "\n" + CollectionLogger.out(offset, registeredSounds, "registerSounds")
      + "\n" + CollectionLogger.out(offset, registeredMusic, "registeredMusic")
      + "\n" + CollectionLogger.out(offset, registeredEffect, "registeredEffect")
      + "\n" + CollectionLogger.out(offset, popups, "popups")
      + "\n" + CollectionLogger.out(offset, controlDefinitions, "controlDefinitions")
      + "\n" + CollectionLogger.out(offset, screens, "screens");
  }
}
