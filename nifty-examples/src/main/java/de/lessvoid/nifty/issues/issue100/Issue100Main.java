package de.lessvoid.nifty.issues.issue100;

import java.util.Locale;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public class Issue100Main {
  private Issue100Main() {
  }

  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Issue 100")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new LwjglRenderDevice(),
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new AccurateTimeProvider());

    nifty.fromXml("issues/issue100/issue100.xml", "start", new StartScreen());

    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }

  public static class StartScreen implements ScreenController {
    private Nifty nifty;

    @Override
    public void bind(final Nifty nifty, final Screen screen) {
      this.nifty = nifty;

      DropDown<Locale> dropdown = screen.findNiftyControl("LocaleDropDown", DropDown.class);
      dropdown.addItem(Locale.GERMAN);
      dropdown.addItem(Locale.ENGLISH);
      dropdown.addItem(Locale.ITALIAN);
      dropdown.addItem(new Locale("NL"));
      dropdown.addItem(Locale.getDefault());
      dropdown.selectItemByIndex(0);
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    @NiftyEventSubscriber(id="LocaleDropDown")
    public void changeLocale(String id, DropDownSelectionChangedEvent<Locale> e){
      nifty.setLocale(e.getSelection());
    }
  }
}
