package de.lessvoid.nifty.loaderv2.types;

import java.util.ArrayList;
import java.util.Collection;

import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.loaderv2.types.helper.CollectionLogger;

public class NiftyControlsType extends XmlBaseType {
  private Collection < ControlDefinitionType > controlDefinitions = new ArrayList < ControlDefinitionType >();
  private Collection < UseControlsType > useControls = new ArrayList < UseControlsType >();
  private Collection < PopupType > popups = new ArrayList < PopupType >();

  public void addControlDefinition(final ControlDefinitionType controlDefinitionType) {
    controlDefinitions.add(controlDefinitionType);
  }

  public void addUseControls(final UseControlsType useControlsType) {
    useControls.add(useControlsType);
  }

  public void addPopup(final PopupType popupType) {
    popups.add(popupType);
  }

  public void loadControls(final NiftyLoader niftyLoader, final NiftyType niftyType) throws Exception {
    for (UseControlsType useControl : useControls) {
      useControl.loadControl(niftyLoader, niftyType);
    }
    for (ControlDefinitionType controlDefinition : controlDefinitions) {
      niftyType.addControlDefinition(controlDefinition);
    }
    for (PopupType popup : popups) {
      niftyType.addPopup(popup);
    }
  }

  public String output() {
    int offset = 1;
    return
      "\nNifty Data:\n" + CollectionLogger.out(offset, controlDefinitions, "controlDefinitions")
      + "\n" + CollectionLogger.out(offset, useControls, "useControls");
  }
}
