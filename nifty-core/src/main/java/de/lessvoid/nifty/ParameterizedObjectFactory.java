package de.lessvoid.nifty;

import java.util.Map;
import java.util.logging.Logger;

public class ParameterizedObjectFactory<T extends Parameterizable> {
	private static Logger log = Logger.getLogger(ParameterizedObjectFactory.class.getName());

	private final Map<String, Class<? extends T>> m_objectNameToClassMapping;
	private final String m_fallbackObjectName;

	public ParameterizedObjectFactory(Map<String, Class<? extends T>> objectNameToClassMapping,
			String fallbackObjectName) {
		m_objectNameToClassMapping = objectNameToClassMapping;
		m_fallbackObjectName = fallbackObjectName;
	}

	public T create(String objectDescription) {
		T object;
		try {
			object = createInternal(objectDescription);
		} catch (IllegalArgumentException e) {
			log.warning(e.getMessage() + " -> Falling back to default " + m_fallbackObjectName + ".");
			object = createInternal(m_fallbackObjectName);
		}

		return object;
	}

	private T createInternal(String objectDescription) {
		T object = instanciateObject(objectDescription);
		initializeObject(object, objectDescription);

		return object;
	}

	private T instanciateObject(String objectDescription) {
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

	private void initializeObject(T object, String objectDescription) {
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
