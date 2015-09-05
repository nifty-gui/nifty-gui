package de.lessvoid.niftyinternal.tree;

import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

/**
 * Commonly used NiftyTreeNodeConverters.
 *
 * Created by void on 21.08.15.
 */
public class NiftyTreeNodeConverters {

  public static NiftyTreeNodeConverter<NiftyNodeImpl<?>> toNodeImpl() {
    return new NiftyTreeNodeConverter<NiftyNodeImpl<?>>() {
      @Override
      public NiftyNodeImpl<?> convert(final NiftyNodeImpl<?> niftyNodeImpl) {
        return niftyNodeImpl;
      }
    };
  }

  public static <T extends NiftyNodeImpl<?>> NiftyTreeNodeConverter<T> toNodeImplClass(final Class<T> clazz) {
    return new NiftyTreeNodeConverter<T>() {
      @Override
      public T convert(final NiftyNodeImpl<?> niftyNodeImpl) {
        return clazz.cast(niftyNodeImpl);
      }
    };
  }

  public static NiftyTreeNodeConverter<NiftyNode> toNiftyNode() {
    return new NiftyTreeNodeConverter<NiftyNode>() {
      @Override
      public NiftyNode convert(final NiftyNodeImpl<?> niftyNodeImpl) {
        return niftyNodeImpl.getNiftyNode();
      }
    };
  }

  public static <T extends NiftyNode> NiftyTreeNodeConverter<T> toNiftyNodeClass(final Class<T> clazz) {
    return new NiftyTreeNodeConverter<T>() {
      @Override
      public T convert(final NiftyNodeImpl<?> niftyNodeImpl) {
        return clazz.cast(niftyNodeImpl.getNiftyNode());
      }
    };
  }
}
