package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.api.Nifty;

public interface Updateable {
  void update(Nifty nifty, float deltaTime);
}
