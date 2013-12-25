package de.lessvoid.nifty.effects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

public class ActiveEffectsTest {
  private ActiveEffects active = new ActiveEffects();
  private Effect e;

  @Before
  public void before() {
    e = createMock(Effect.class);
  }

  @After
  public void after() {
    verify(e);
  }

  @Test
  public void testDefault() {
    replay(e);
    assertEmpty();
  }

  @Test
  public void testClearOverlayPost() {
    prepareOverlayPost();
    active.add(e);
    active.clear();
    assertEmpty();
  }

  @Test
  public void testClearOverlayPre() {
    prepareOverlayPre();
    active.add(e);
    active.clear();
    assertEmpty();
  }

  @Test
  public void testClearNoOverlayPost() {
    prepareNoOverlayPost();
    active.add(e);
    active.clear();
    assertEmpty();
  }

  @Test
  public void testClearNoOverlayPre() {
    prepareNoOverlayPre();
    active.add(e);
    active.clear();
    assertEmpty();
  }

  @Test
  public void testRemoveNoOverlayPost() {
    prepareNoOverlayPost();
    active.add(e);
    active.remove(e);
    assertEmpty();
  }

  @Test
  public void testRemoveNoOverlayPre() {
    prepareNoOverlayPre();
    active.add(e);
    active.remove(e);
    assertEmpty();
  }

  @Test
  public void testRemoveOverlayPost() {
    prepareOverlayPost();
    active.add(e);
    active.remove(e);
    assertEmpty();
  }

  @Test
  public void testRemoveOverlayPre() {
    prepareOverlayPre();
    active.add(e);
    active.remove(e);
    assertEmpty();
  }

  @Test
  public void testAddNoOverlayPostEffectInactive() {
    prepareNoOverlayPostInactive();
    active.add(e);
    assertSingleEntry();
    assertFalse(active.containsActiveEffects());
    assertActive(0, 1, 0);
  }

  @Test
  public void testAddNoOverlayPostEffectActive() {
    prepareNoOverlayPostActive();
    active.add(e);
    assertSingleEntry();
    assertTrue(active.containsActiveEffects());
    assertActive(0, 1, 0);
  }

  @Test
  public void testAddNoOverlayPreEffectInactive() {
    prepareNoOverlayPreInactive();
    active.add(e);
    assertSingleEntry();
    assertFalse(active.containsActiveEffects());
    assertActive(0, 0, 1);
  }

  @Test
  public void testAddNoOverlayPreEffectActive() {
    prepareNoOverlayPreActive();
    active.add(e);
    assertSingleEntry();
    assertTrue(active.containsActiveEffects());
    assertActive(0, 0, 1);
  }

  @Test
  public void testAddOverlayPostEffectInactive() {
    prepareOverlayPostInactive();
    active.add(e);
    assertSingleEntry();
    assertFalse(active.containsActiveEffects());
    assertActive(1, 0, 0);
  }

  @Test
  public void testAddOverlayPostEffectActive() {
    prepareOverlayPostActive();
    active.add(e);
    assertSingleEntry();
    assertTrue(active.containsActiveEffects());
    assertActive(1, 0, 0);
  }

  @Test
  public void testAddOverlayPreEffectInactive() {
    prepareOverlayPreInactive();
    active.add(e);
    assertSingleEntry();
    assertFalse(active.containsActiveEffects());
    assertActive(1, 0, 0);
  }

  @Test
  public void testAddOverlayPreEffectActive() {
    prepareOverlayPreActive();
    active.add(e);
    assertSingleEntry();
    assertTrue(active.containsActiveEffects());
    assertActive(1, 0, 0);
  }

  private void assertActive(final int overlayCount, final int postCount, final int preCount) {
    assertEquals(overlayCount, active.getActiveOverlay().size());
    assertEquals(postCount, active.getActivePost().size());
    assertEquals(preCount, active.getActivePre().size());
  }

  private void assertSingleEntry() {
    assertFalse(active.isEmpty());
    assertTrue(active.contains(e));
    assertEquals(1, active.size());
    assertEquals(1, active.getActive().size());
  }

  private void assertEmpty() {
    assertTrue(active.isEmpty());
    assertFalse(active.contains(e));
    assertEquals(0, active.size());
    assertFalse(active.containsActiveEffects());
    assertEquals(0, active.getActive().size());
    assertEquals(0, active.getActivePost().size());
    assertEquals(0, active.getActivePre().size());
    assertEquals(0, active.getActiveOverlay().size());
  }

  private void prepareOverlayPost() {
    expect(e.isOverlay()).andReturn(true);
    replay(e);
  }

  private void prepareOverlayPre() {
    expect(e.isOverlay()).andReturn(true);
    replay(e);
  }

  private void prepareNoOverlayPost() {
    expect(e.isOverlay()).andReturn(false);
    expect(e.isPost()).andReturn(true);
    replay(e);
  }

  private void prepareNoOverlayPre() {
    expect(e.isOverlay()).andReturn(false);
    expect(e.isPost()).andReturn(false);
    replay(e);
  }

  private void prepareNoOverlayPostInactive() {
    expect(e.isOverlay()).andReturn(false);
    expect(e.isPost()).andReturn(true);
    expect(e.isActive()).andReturn(false);
    replay(e);
  }

  private void prepareNoOverlayPostActive() {
    expect(e.isOverlay()).andReturn(false);
    expect(e.isPost()).andReturn(true);
    expect(e.isActive()).andReturn(true);
    replay(e);
  }

  private void prepareNoOverlayPreInactive() {
    expect(e.isOverlay()).andReturn(false);
    expect(e.isPost()).andReturn(false);
    expect(e.isActive()).andReturn(false);
    replay(e);
  }

  private void prepareNoOverlayPreActive() {
    expect(e.isOverlay()).andReturn(false);
    expect(e.isPost()).andReturn(false);
    expect(e.isActive()).andReturn(true);
    replay(e);
  }

  private void prepareOverlayPostInactive() {
    expect(e.isOverlay()).andReturn(true);
    expect(e.isActive()).andReturn(false);
    replay(e);
  }

  private void prepareOverlayPostActive() {
    expect(e.isOverlay()).andReturn(true);
    expect(e.isActive()).andReturn(true);
    replay(e);
  }

  private void prepareOverlayPreActive() {
    expect(e.isOverlay()).andReturn(true);
    expect(e.isActive()).andReturn(true);
    replay(e);
  }

  private void prepareOverlayPreInactive() {
    expect(e.isOverlay()).andReturn(true);
    expect(e.isActive()).andReturn(false);
    replay(e);
  }
}
