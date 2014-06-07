package de.lessvoid.nifty.controls.slider;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SliderImplTest {
  private SliderImpl slider = new SliderImpl();
  private SliderView sliderView;

  @Before
  public void before() {
    sliderView = createMock(SliderView.class);
    expect(sliderView.getSize()).andReturn(100).anyTimes();
    sliderView.update(0);
    sliderView.valueChanged(0.f);
  }

  @After
  public void after() {
    verify(sliderView);
  }

  @Test
  public void testDefaultPosition() {
    replay(sliderView);
    bindToView();
    assertEquals(0.f, slider.getValue());
  }

  @Test
  public void testChangePositionMax() {
    sliderView.update(100);
    sliderView.valueChanged(1.f);
    replay(sliderView);
    bindToView();
    slider.setValue(1.0f);
    assertEquals(1.f, slider.getValue());
  }

  @Test
  public void testChangePositionHalf() {
    sliderView.update(50);
    sliderView.valueChanged(.5f);
    replay(sliderView);
    bindToView();
    slider.setValue(.5f);
    assertEquals(.5f, slider.getValue());
  }

  @Test
  public void testStepUp() {
    sliderView.update(10);
    sliderView.valueChanged(.1f);
    replay(sliderView);
    bindToView();
    slider.stepUp();
    assertEquals(.1f, slider.getValue());
  }

  @Test
  public void testStepDown() {
    sliderView.update(0);
    replay(sliderView);
    bindToView();
    slider.stepDown();
    assertEquals(0.f, slider.getValue());
  }

  @Test
  public void testStepDownWithMax() {
    sliderView.update(100);
    sliderView.valueChanged(1.f);
    sliderView.update(90);
    sliderView.valueChanged(.90000004f);
    replay(sliderView);
    bindToView();
    slider.setValue(1.f);
    slider.stepDown();
    assertEquals(.9f, slider.getValue());
  }

  @Test
  public void testFromViewValueMin() {
    expect(sliderView.filter(0, 0)).andReturn(0);
    sliderView.update(0);
    replay(sliderView);
    bindToView();
    slider.setValueFromPosition(0, 0);
    assertEquals(0.f, slider.getValue());
  }

  @Test
  public void testFromViewValueMax() {
    expect(sliderView.filter(100, 0)).andReturn(100);
    sliderView.update(100);
    sliderView.valueChanged(1.f);
    replay(sliderView);
    bindToView();
    slider.setValueFromPosition(100, 0);
    assertEquals(1.f, slider.getValue());
  }

  @Test
  public void testFromViewValueCenter() {
    expect(sliderView.filter(50, 0)).andReturn(50);
    sliderView.update(50);
    sliderView.valueChanged(.5f);
    replay(sliderView);
    bindToView();
    slider.setValueFromPosition(50, 0);
    assertEquals(0.5f, slider.getValue());
  }

  @Test
  public void testFromViewValueCenterWithStepSize() {
    expect(sliderView.filter(35, 0)).andReturn(35);
    sliderView.update(25);
    sliderView.valueChanged(.25f);
    replay(sliderView);
    slider.bindToView(sliderView, 0f, 1.f, .25f, .25f);
    slider.setValueFromPosition(35, 0);
    assertEquals(0.25f, slider.getValue());
  }

  @Test
  public void testWithDifferentStepSize() {
    expect(sliderView.filter(35, 0)).andReturn(35);
    sliderView.update(40);
    sliderView.valueChanged(0.4f);
    sliderView.update(20);
    sliderView.valueChanged(0.2f);
    replay(sliderView);
    slider.bindToView(sliderView, 0f, 1.f, .1f, .25f);

    slider.setValueFromPosition(35, 0);
    assertEquals(0.4f, slider.getValue(), .01f);

    slider.stepDown();
    assertEquals(0.2f, slider.getValue(), 0.01f);
  }

  @Test
  public void testChangeMin() {
    sliderView.update(0);
    sliderView.valueChanged(.5f);
    replay(sliderView);
    bindToView();
    slider.setMin(.5f);
    assertEquals(.5f, slider.getValue());
  }

  @Test
  public void testChangeMax() {
    sliderView.update(0);
    replay(sliderView);
    bindToView();
    slider.setMax(.5f);
    assertEquals(0.f, slider.getValue());
  }

  @Test
  public void testChangeStepSize() {
    sliderView.update(10);
    sliderView.valueChanged(.1f);
    sliderView.update(0);
    sliderView.valueChanged(0.f);
    replay(sliderView);
    bindToView();
    slider.setValue(0.1f);
    slider.setStepSize(0.25f);
    assertEquals(.25f, slider.getValue());
  }

  private void bindToView() {
    slider.bindToView(sliderView, 0f, 1.f, .1f, .1f);
  }
}
