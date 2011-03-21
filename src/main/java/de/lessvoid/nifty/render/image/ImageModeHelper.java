package de.lessvoid.nifty.render.image;

import java.util.Properties;
import java.util.logging.Logger;

import de.lessvoid.nifty.ParameterizedObjectFactory;

public class ImageModeHelper {
	private static Logger log = Logger.getLogger(ParameterizedObjectFactory.class.getName());

	public String getAreaProviderProperty(final Properties properties) {
		final String imageModeProperty = properties.getProperty("imageMode");
		String property = getAreaProviderProperty(imageModeProperty);
		if (property != null) {
			log.info("imageMode property converted to imageArea property : " + imageModeProperty + " -> " + property);
			return property;
		}

		return properties.getProperty("imageArea");
	}

	public String getAreaProviderProperty(final String imageModeProperty) {
		if (imageModeProperty != null) {
			final String[] imageMode = imageModeProperty.split(":");
			final String imageModeName = imageMode[0];
			if (imageModeName.equals("normal") || imageModeName.equals("resize")) {
				return "fullimage";
			} else if (imageModeName.equals("subImage") || imageModeName.equals("repeat")) {
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

	public String getRenderStrategyProperty(final Properties properties) {
		final String imageModeProperty = properties.getProperty("imageMode");
		String property = getRenderStrategyProperty(imageModeProperty);
		if (property != null) {
			log.info("imageMode property converted to renderStrategy property : " + imageModeProperty + " -> "
					+ property);
			return property;
		}

		return properties.getProperty("renderStrategy");
	}

	public String getRenderStrategyProperty(final String imageModeProperty) {
		if (imageModeProperty != null) {
			final String[] imageMode = imageModeProperty.split(":");
			final String imageModeName = imageMode[0];
			if (imageModeName.equals("normal") || imageModeName.equals("subImage") || imageModeName.equals("sprite")) {
				return "resize";
			} else if (imageModeName.equals("resize")) {
				return "nine-part:" + getImageModeParameters(imageMode);
			} else if (imageModeName.equals("sprite-resize")) {
				return "nine-part:" + getNinePartParameters(getImageModeParameters(imageMode));
			} else if (imageModeName.equals("repeat")) {
				return "repeat";
			} else {
				log.warning("imageMode property could not be converted to renderStrategy property : " + imageModeProperty);
			}
		}

		return null;
	}

	private String getImageModeParameters(final String[] imageMode) {
		if (imageMode.length > 1) {
			return imageMode[1];
		}

		return "";
	}

	private String getNinePartParameters(final String imageMode) {
		String[] split = imageMode.split("(\\d+,){3}", 2);
		if (split.length > 1) {
			return split[1];
		}

		return "";
	}
}
