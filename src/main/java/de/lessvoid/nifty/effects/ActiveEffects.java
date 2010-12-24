package de.lessvoid.nifty.effects;

import java.util.ArrayList;
import java.util.List;

public class ActiveEffects {
  private List<Effect> allEffects = new ArrayList<Effect>();
  private List<Effect> postEffects = new ArrayList<Effect>();
  private List<Effect> preEffects = new ArrayList<Effect>();
  private List<Effect> overlayEffects = new ArrayList<Effect>();

  public void clear() {
    allEffects.clear();
    postEffects.clear();
    preEffects.clear();
    overlayEffects.clear();
  }

  public void add(final Effect e) {
    allEffects.add(e);

    if (e.isOverlay()) {
      overlayEffects.add(e);
    } else if (e.isPost()) {
      postEffects.add(e);
    } else {
      preEffects.add(e);
    }
  }

  public void remove(final Effect e) {
    allEffects.remove(e);
    postEffects.remove(e);
    preEffects.remove(e);
    overlayEffects.remove(e);
  }

  public boolean isEmpty() {
    return allEffects.isEmpty();
  }

  public boolean contains(final Effect e) {
    return allEffects.contains(e);
  }

  public int size() {
    return allEffects.size();
  }

  public boolean containsActiveEffects() {
    for (int i=0; i<allEffects.size(); i++) {
      Effect e = allEffects.get(i);
      if (e.isActive()) {
        return true;
      }
    }
    return false;
  }

  public List<Effect> getActive() {
    return allEffects;
  }

  public List<Effect> getActivePost() {
    return postEffects;
  }

  public List<Effect> getActivePre() {
    return preEffects;
  }

  public List<Effect> getActiveOverlay() {
    return overlayEffects;
  }
}
