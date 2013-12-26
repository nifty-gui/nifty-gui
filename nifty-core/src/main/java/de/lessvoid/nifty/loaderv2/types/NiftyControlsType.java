package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.loaderv2.types.helper.CollectionLogger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;

public class NiftyControlsType extends XmlBaseType {
  @Nonnull
  private final Collection<ControlDefinitionType> controlDefinitions = new ArrayList<ControlDefinitionType>();
  @Nonnull
  private final Collection<UseControlsType> useControls = new ArrayList<UseControlsType>();
  @Nonnull
  private final Collection<PopupType> popups = new ArrayList<PopupType>();
  @Nonnull
  private final Collection<ResourceBundleType> resourceBundles = new ArrayList<ResourceBundleType>();

  public void addControlDefinition(final ControlDefinitionType controlDefinitionType) {
    controlDefinitions.add(controlDefinitionType);
  }

  public void addUseControls(final UseControlsType useControlsType) {
    useControls.add(useControlsType);
  }

  public void addPopup(final PopupType popupType) {
    popups.add(popupType);
  }

  public void addResourceBundle(final ResourceBundleType resourceBundle) {
    resourceBundles.add(resourceBundle);
  }

  public void loadControls(
      @Nonnull final NiftyLoader niftyLoader,
      @Nonnull final NiftyType niftyType) throws Exception {
    for (UseControlsType useControl : useControls) {
      useControl.loadControl(niftyLoader, niftyType);
    }
    for (ControlDefinitionType controlDefinition : controlDefinitions) {
      niftyType.addControlDefinition(controlDefinition);
    }
    for (ResourceBundleType bundle : resourceBundles) {
      niftyType.addResourceBundle(bundle);
    }
    for (PopupType popup : popups) {
      niftyType.addPopup(popup);
    }
  }

  @Nonnull
  public String output() {
    int offset = 1;
    return
        "\nNifty Data:\n" + CollectionLogger.out(offset, controlDefinitions, "controlDefinitions")
            + "\n" + CollectionLogger.out(offset, useControls, "useControls");
  }
}
