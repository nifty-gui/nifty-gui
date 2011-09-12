package de.lessvoid.nifty.render.image.areaprovider;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.spi.render.RenderImage;

public class CachedAreaProvider implements AreaProvider {
	private final AreaProvider m_cachedProvider;

	private RenderImage m_lastProcessedImage;
	private Box m_cachedArea;

	public CachedAreaProvider(AreaProvider cachedProvider) {
		m_cachedProvider = cachedProvider;
	}

	@Override
	public void setParameters(String parameters) {
		m_lastProcessedImage = null;
		m_cachedArea = null;
	}

	@Override
	public Box getSourceArea(RenderImage renderImage) {
		if (renderImage != m_lastProcessedImage) {
			m_lastProcessedImage = renderImage;
			m_cachedArea = m_cachedProvider.getSourceArea(renderImage);
		}

		return m_cachedArea;
	}

	@Override
	public Size getNativeSize(NiftyImage image) {
		return m_cachedProvider.getNativeSize(image);
	}
}
