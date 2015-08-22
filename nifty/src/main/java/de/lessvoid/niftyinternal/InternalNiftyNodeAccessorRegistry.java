package de.lessvoid.niftyinternal;

import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.spi.node.NiftyNodeAccessor;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import de.lessvoid.niftyinternal.common.NiftyServiceLoader;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class InternalNiftyNodeAccessorRegistry {
  @Nonnull
  private final Map<Class, NiftyNodeAccessor> accessorMap;

  public InternalNiftyNodeAccessorRegistry() {
    accessorMap = new HashMap<>();

    for (NiftyNodeAccessor accessor : NiftyServiceLoader.load(NiftyNodeAccessor.class)) {
      accessorMap.put(accessor.getNodeClass(), accessor);
    }
  }

  public <T extends NiftyNode> NiftyNodeImpl<T> getImpl(@Nonnull final T niftyNode) {
    //noinspection unchecked
    NiftyNodeAccessor<T> niftyNodeAccessor = accessorMap.get(niftyNode.getClass());
    if (niftyNodeAccessor == null) {
      throw new IllegalArgumentException("There is no accessor registered for a node of the type: " +
          niftyNode.getClass().getSimpleName());
    }

    // shouldn't be required, maybe this can be removed.
    if (!niftyNodeAccessor.getNodeClass().isInstance(niftyNode)) {
      throw new IllegalStateException("Internal error. The accessor is registered to a specific type, but the " +
          "returned class does not match.");
    }

    return niftyNodeAccessor.getImplementation(niftyNode);
  }
}
