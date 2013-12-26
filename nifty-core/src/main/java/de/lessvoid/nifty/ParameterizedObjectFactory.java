package de.lessvoid.nifty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.logging.Logger;

public class ParameterizedObjectFactory<T extends Parameterizable> {
  @Nonnull
	private static final Logger log = Logger.getLogger(ParameterizedObjectFactory.class.getName());

  @Nonnull
	private final Map<String, Class<? extends T>> m_objectNameToClassMapping;
  @Nonnull
	private final String m_fallbackObjectName;

	public ParameterizedObjectFactory(
      @Nonnull final Map<String, Class<? extends T>> objectNameToClassMapping,
			@Nonnull final String fallbackObjectName) {
		m_objectNameToClassMapping = objectNameToClassMapping;
		m_fallbackObjectName = fallbackObjectName;
	}

  @Nonnull
	public T create(@Nullable String objectDescription) {
		T object;
		try {
			object = createInternal(objectDescription);
		} catch (IllegalArgumentException e) {
			log.warning(e.getMessage() + " -> Falling back to default " + m_fallbackObjectName + ".");
			object = createInternal(m_fallbackObjectName);
		}

		return object;
	}

  @Nonnull
	private T createInternal(@Nullable String objectDescription) {
		T object = instanciateObject(objectDescription);
		initializeObject(object, objectDescription);

		return object;
	}

  @Nonnull
	private T instanciateObject(@Nullable String objectDescription) {
		String objectName = m_fallbackObjectName;
		if (objectDescription != null) {
			objectName = objectDescription.split(":")[0];
		}

		Class<? extends T> objectClass = m_objectNameToClassMapping.get(objectName);
		if (objectClass == null) {
			throw new IllegalArgumentException("No class found for [" + objectName + "].");
		}

		try {
			return objectClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to instantiate class [" + objectClass.getName() + "].", e);
		}
	}

	private void initializeObject(@Nonnull T object, @Nullable String objectDescription) {
		String objectParameters = null;
		if (objectDescription != null) {
			String[] tokens = objectDescription.split(":");
			if (tokens.length > 1) {
				objectParameters = tokens[1];
			}
		}

		object.setParameters(objectParameters);
	}
}
