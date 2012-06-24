package de.lessvoid.nifty.render.image;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class ImageModeHelperTest {

	private Properties m_normalProperties;
	private Properties m_resizeProperties;
	private Properties m_subImageProperties;
	private Properties m_repeatProperties;
	private Properties m_spriteProperties;
	private Properties m_spriteResizeProperties;

	@Before
	public void setUp() {
		m_normalProperties = createMock(Properties.class);
		expect(m_normalProperties.getProperty("imageMode")).andReturn("normal").anyTimes();
		replay(m_normalProperties);

		m_resizeProperties = createMock(Properties.class);
		expect(m_resizeProperties.getProperty("imageMode")).andReturn("resize:0,1,2,3,4,5,6,7,8,9,10,11").anyTimes();
		replay(m_resizeProperties);

		m_subImageProperties = createMock(Properties.class);
		expect(m_subImageProperties.getProperty("imageMode")).andReturn("subImage:0,1,2,3").anyTimes();
		replay(m_subImageProperties);

		m_repeatProperties = createMock(Properties.class);
		expect(m_repeatProperties.getProperty("imageMode")).andReturn("repeat:0,1,2,3").anyTimes();
		replay(m_repeatProperties);

		m_spriteProperties = createMock(Properties.class);
		expect(m_spriteProperties.getProperty("imageMode")).andReturn("sprite:0,1,2").anyTimes();
		replay(m_spriteProperties);

		m_spriteResizeProperties = createMock(Properties.class);
		expect(m_spriteResizeProperties.getProperty("imageMode")).andReturn(
				"sprite-resize:0,1,2,3,4,5,6,7,8,9,10,11,12,13,14").anyTimes();
		replay(m_spriteResizeProperties);
	}

	@Test
	public void testGetAreaProviderPropertyExtractsNormalImageMode() {
		String areaProviderProperty = ImageModeHelper.getAreaProviderProperty(m_normalProperties);
		assertEquals(areaProviderProperty, "fullimage");

		verify(m_normalProperties);
	}

	@Test
	public void testGetRenderStrategyPropertyExtractsNormalImageMode() {
		String renderStrategyProperty = ImageModeHelper.getRenderStrategyProperty(m_normalProperties);
		assertEquals(renderStrategyProperty, "resize");

		verify(m_normalProperties);
	}

	@Test
	public void testGetAreaProviderPropertyExtractsResizeImageMode() {
		String renderStrategyProperty = ImageModeHelper.getAreaProviderProperty(m_resizeProperties);
		assertEquals(renderStrategyProperty, "fullimage");

		verify(m_resizeProperties);
	}

	@Test
	public void testGetRenderStrategyPropertyExtractsImageMode() {
		String renderStrategyProperty = ImageModeHelper.getRenderStrategyProperty(m_resizeProperties);
		assertEquals(renderStrategyProperty, "nine-part:0,1,2,3,4,5,6,7,8,9,10,11");

		verify(m_resizeProperties);
	}

	@Test
	public void testGetAreaProviderPropertyExtractsSubImageImageMode() {
		String renderStrategyProperty = ImageModeHelper.getAreaProviderProperty(m_subImageProperties);
		assertEquals(renderStrategyProperty, "subimage:0,1,2,3");

		verify(m_resizeProperties);
	}

	@Test
	public void testGetRenderStrategyPropertyExtractsSubImageImageMode() {
		String renderStrategyProperty = ImageModeHelper.getRenderStrategyProperty(m_subImageProperties);
		assertEquals(renderStrategyProperty, "resize");

		verify(m_subImageProperties);
	}

	@Test
	public void testGetAreaProviderPropertyExtractsRepeatImageMode() {
		String renderStrategyProperty = ImageModeHelper.getAreaProviderProperty(m_repeatProperties);
		assertEquals(renderStrategyProperty, "subimage:0,1,2,3");

		verify(m_resizeProperties);
	}

	@Test
	public void testGetRenderStrategyPropertyExtractsRepeatImageMode() {
		String renderStrategyProperty = ImageModeHelper.getRenderStrategyProperty(m_repeatProperties);
		assertEquals(renderStrategyProperty, "repeat");

		verify(m_repeatProperties);
	}

	@Test
	public void testGetAreaProviderPropertyExtractsSpriteImageMode() {
		String renderStrategyProperty = ImageModeHelper.getAreaProviderProperty(m_spriteProperties);
		assertEquals(renderStrategyProperty, "sprite:0,1,2");

		verify(m_resizeProperties);
	}

	@Test
	public void testGetRenderStrategyPropertyExtractsSpriteImageMode() {
		String renderStrategyProperty = ImageModeHelper.getRenderStrategyProperty(m_spriteProperties);
		assertEquals(renderStrategyProperty, "resize");

		verify(m_spriteProperties);
	}

	@Test
	public void testGetAreaProviderPropertyExtractsSpriteResizeImageMode() {
		String renderStrategyProperty = ImageModeHelper.getAreaProviderProperty(m_spriteResizeProperties);
		assertEquals(renderStrategyProperty, "sprite:0,1,2");

		verify(m_resizeProperties);
	}

	@Test
	public void testGetRenderStrategyPropertyExtractsSpriteResizeImageMode() {
		String renderStrategyProperty = ImageModeHelper.getRenderStrategyProperty(m_spriteResizeProperties);
		assertEquals(renderStrategyProperty, "nine-part:3,4,5,6,7,8,9,10,11,12,13,14");

		verify(m_spriteResizeProperties);
	}

	@Test
	public void testGetAreaProviderPropertyReturnsImageAreaPropertyWhenFound() {
		Properties properties = createMock(Properties.class);
		expect(properties.getProperty("imageMode")).andReturn(null);
		expect(properties.getProperty("imageArea")).andReturn("dummy:foo");
		replay(properties);

		assertEquals(ImageModeHelper.getAreaProviderProperty(properties), "dummy:foo");

		verify(properties);
	}

	@Test
	public void testGetAreaProviderPropertyReturnsNullWithUnknownProperty() {
		Properties properties = createMock(Properties.class);
		expect(properties.getProperty("imageMode")).andReturn(null);
		expect(properties.getProperty("imageArea")).andReturn(null);
		replay(properties);

		assertNull(ImageModeHelper.getAreaProviderProperty(properties));

		verify(properties);
	}

	@Test
	public void testGetAreaProviderPropertyReturnsNullWithUnknownImageMode() {
		Properties properties = createMock(Properties.class);
		expect(properties.getProperty("imageMode")).andReturn("blah:1,2,3");
		expect(properties.getProperty("imageArea")).andReturn(null);
		replay(properties);

		assertNull(ImageModeHelper.getAreaProviderProperty(properties));

		verify(properties);
	}

	@Test
	public void testGetRenderStrategyPropertyReturnsRenderStrategyPropertyWhenFound() {
		Properties properties = createMock(Properties.class);
		expect(properties.getProperty("imageMode")).andReturn(null);
		expect(properties.getProperty("renderStrategy")).andReturn("dummy:foo");
		replay(properties);

		assertEquals(ImageModeHelper.getRenderStrategyProperty(properties), "dummy:foo");

		verify(properties);
	}

	@Test
	public void testGetRenderStrategyPropertyReturnsNullWithUnknownProperty() {
		Properties properties = createMock(Properties.class);
		expect(properties.getProperty("imageMode")).andReturn("blah:1,2,3");
		expect(properties.getProperty("renderStrategy")).andReturn(null);
		replay(properties);

		assertNull(ImageModeHelper.getRenderStrategyProperty(properties));

		verify(properties);
	}

	@Test
	public void testGetRenderStrategyPropertyReturnsNullWithUnknownImageMode() {
		Properties properties = createMock(Properties.class);
		expect(properties.getProperty("imageMode")).andReturn("blah:1,2,3");
		expect(properties.getProperty("renderStrategy")).andReturn(null);
		replay(properties);

		assertNull(ImageModeHelper.getRenderStrategyProperty(properties));

		verify(properties);
	}
}
