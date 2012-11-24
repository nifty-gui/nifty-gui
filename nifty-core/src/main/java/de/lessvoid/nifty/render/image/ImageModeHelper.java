package de.lessvoid.nifty.render.image;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import de.lessvoid.nifty.ParameterizedObjectFactory;

public class ImageModeHelper {
	private static Logger log = Logger.getLogger(ParameterizedObjectFactory.class.getName());

	private ImageModeHelper() {
	}

	public static String getAreaProviderProperty(final Properties properties) {
		return getAreaProviderProperty(new StringPropertyAdapter(properties));
	}

	public static String getAreaProviderProperty(final Map<String, String> properties) {
		final String imageModeProperty = properties.get("imageMode");
		String property = getAreaProviderProperty(imageModeProperty);
		if (property != null) {
			log.fine("imageMode property converted to imageArea property : " + imageModeProperty + " -> " + property);
			return property;
		}

		return properties.get("imageArea");
	}

	public static String getAreaProviderProperty(final String imageModeProperty) {
		if (imageModeProperty != null) {
			final String[] imageMode = imageModeProperty.split(":");
			final String imageModeName = imageMode[0];
			if (imageModeName.equals("normal") || imageModeName.equals("resize")) {
				return "fullimage";
			} else if (imageModeName.equals("subImage") || imageModeName.equals("subImageDirect") || imageModeName.equals("repeat")) {
				return "subimage:" + getImageModeParameters(imageMode);
			} else if (imageModeName.equals("sprite")) {
				return "sprite:" + getImageModeParameters(imageMode);
			} else if (imageModeName.equals("sprite-resize")) {
				String imageModeParameters = imageMode[1];
				return "sprite:" + imageModeParameters.replace("," + getNinePartParameters(imageModeParameters), "");
			} else {
				log.warning("imageMode property could not be converted to imageArea property : " + imageModeProperty);
			}
		}

		return null;
	}

	public static String getRenderStrategyProperty(final Properties properties) {
		return getRenderStrategyProperty(new StringPropertyAdapter(properties));
	}

	public static String getRenderStrategyProperty(final Map<String, String> properties) {
		final String imageModeProperty = properties.get("imageMode");
		String property = getRenderStrategyProperty(imageModeProperty);
		if (property != null) {
			log.fine("imageMode property converted to renderStrategy property : " + imageModeProperty + " -> "
					+ property);
			return property;
		}

		return properties.get("renderStrategy");
	}

	public static String getRenderStrategyProperty(final String imageModeProperty) {
		if (imageModeProperty != null) {
			final String[] imageMode = imageModeProperty.split(":");
			final String imageModeName = imageMode[0];
			if (imageModeName.equals("normal") || imageModeName.equals("subImage") || imageModeName.equals("sprite")) {
				return "resize";
			} else if (imageModeName.equals("subImageDirect")) {
	      return "direct";
			} else if (imageModeName.equals("resize")) {
				return "nine-part:" + getImageModeParameters(imageMode);
			} else if (imageModeName.equals("sprite-resize")) {
				return "nine-part:" + getNinePartParameters(getImageModeParameters(imageMode));
			} else if (imageModeName.equals("repeat")) {
				return "repeat";
			} else {
				log.warning("imageMode property could not be converted to renderStrategy property : "
						+ imageModeProperty);
			}
		}

		return null;
	}

	private static String getImageModeParameters(final String[] imageMode) {
		if (imageMode.length > 1) {
			return imageMode[1];
		}

		return "";
	}

	private static String getNinePartParameters(final String imageMode) {
		String[] split = imageMode.split("(\\d+,){3}", 2);
		if (split.length > 1) {
			return split[1];
		}

		return "";
	}

	/**
	 * Hack to circumvent a JRE oddity : Properties class is a Map<Object, Object> that can (actually should) only
	 * contain <String, String> pairs ! Thus we cannot share a single get*Property taking a Map<String, String>
	 * for code that calls it with Attributes and code that calls it with Properties.
	 */
	private static class StringPropertyAdapter implements Map<String, String> {
		private final Properties m_properties;

		public StringPropertyAdapter(Properties properties) {
			m_properties = properties;
		}

		@Override
		public void clear() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean containsKey(Object key) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean containsValue(Object value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Set<java.util.Map.Entry<String, String>> entrySet() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String get(Object key) {
			return m_properties.getProperty((String) key);
		}

		@Override
		public boolean isEmpty() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Set<String> keySet() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String put(String key, String value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void putAll(Map<? extends String, ? extends String> m) {
			throw new UnsupportedOperationException();
		}

		@Override
		public String remove(Object key) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int size() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Collection<String> values() {
			throw new UnsupportedOperationException();
		}
	}
}
