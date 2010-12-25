package de.lessvoid.nifty.effects;

import java.util.ArrayList;
import java.util.List;

/**
 * This stores all active effects and is used by the EffectProcessor. It will
 * classify effects on add (overlay, pre, post).
 * @author void
 */
public class ActiveEffects {
  private List<Effect> all = new ArrayList<Effect>();
  private List<Effect> post = new ArrayList<Effect>();
  private List<Effect> pre = new ArrayList<Effect>();
  private List<Effect> overlay = new ArrayList<Effect>();

  public void clear() {
    all.clear();
    post.clear();
    pre.clear();
    overlay.clear();
  }

  public void add(final Effect e) {
    all.add(e);

    if (e.isOverlay()) {
      overlay.add(e);
    } else if (e.isPost()) {
      post.add(e);
    } else {
      pre.add(e);
    }
  }

  public void remove(final Effect e) {
    all.remove(e);
    post.remove(e);
    pre.remove(e);
    overlay.remove(e);
  }

  public boolean isEmpty() {
    return all.isEmpty();
  }

  public boolean contains(final Effect e) {
    return all.contains(e);
  }

  public int size() {
    return all.size();
  }

  public boolean containsActiveEffects() {
    for (int i=0; i<all.size(); i++) {
      Effect e = all.get(i);
      if (e.isActive()) {
        return true;
      }
    }
    return false;
  }

  public List<Effect> getActive() {
    return all;
  }

  public List<Effect> getActivePost() {
    return post;
  }

  public List<Effect> getActivePre() {
    return pre;
  }

  public List<Effect> getActiveOverlay() {
    return overlay;
  }
}
