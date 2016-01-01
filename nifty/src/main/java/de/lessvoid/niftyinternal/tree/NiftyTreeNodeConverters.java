/*
 * Copyright (c) 2016, Nifty GUI Community
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.lessvoid.niftyinternal.tree;

import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * Commonly used NiftyTreeNodeConverters.
 *
 * Created by void on 21.08.15.
 */
public class NiftyTreeNodeConverters {
  @Nonnull
  private static final Map<Class<?>, ConverterToNodeImplClass> NODE_IMPL_CACHE = new HashMap<>();
  @Nonnull
  private static final Map<Class<?>, ConverterToNodeClass> NODE_CACHE = new HashMap<>();

  @Nonnull
  public static NiftyTreeNodeConverter<NiftyNodeImpl<?>> toNodeImpl() {
    return ConverterToNodeImpl.Instance;
  }

  @Nonnull
  public static <T extends NiftyNodeImpl<?>> NiftyTreeNodeConverter<T> toNodeImplClass(final Class<T> clazz) {
    @SuppressWarnings("unchecked")
    ConverterToNodeImplClass<T> instance = NODE_IMPL_CACHE.get(clazz);
    if (instance == null) {
      instance = new ConverterToNodeImplClass<>(clazz);
      NODE_IMPL_CACHE.put(clazz, instance);
    }
    return instance;
  }

  @Nonnull
  public static NiftyTreeNodeConverter<NiftyNode> toNiftyNode() {
    return ConverterToNode.Instance;
  }

  @Nonnull
  public static <T extends NiftyNode> NiftyTreeNodeConverter<T> toNiftyNodeClass(final Class<T> clazz) {
    @SuppressWarnings("unchecked")
    ConverterToNodeClass<T> instance = NODE_CACHE.get(clazz);
    if (instance == null) {
      instance = new ConverterToNodeClass<>(clazz);
      NODE_CACHE.put(clazz, instance);
    }
    return instance;
  }

  private NiftyTreeNodeConverters() {}

  private enum ConverterToNodeImpl implements NiftyTreeNodeConverter<NiftyNodeImpl<?>> {
    Instance;
    @Override
    public NiftyNodeImpl<?> convert(final NiftyNodeImpl<? extends NiftyNode> niftyNodeImpl) {
      return niftyNodeImpl;
    }
  }

  private enum ConverterToNode implements NiftyTreeNodeConverter<NiftyNode> {
    Instance;
    @Override
    public NiftyNode convert(final NiftyNodeImpl<?> niftyNodeImpl) {
      return niftyNodeImpl.getNiftyNode();
    }
  }

  private static final class ConverterToNodeImplClass<T extends NiftyNodeImpl<?>> implements NiftyTreeNodeConverter<T> {
    @Nonnull
    private final Class<T> clazz;

    ConverterToNodeImplClass(@Nonnull final Class<T> clazz) {
      this.clazz = clazz;
    }

    @Override
    public T convert(final NiftyNodeImpl<? extends NiftyNode> niftyNodeImpl) {
      return clazz.cast(niftyNodeImpl);
    }
  }

  private static final class ConverterToNodeClass<T extends NiftyNode> implements NiftyTreeNodeConverter<T> {
    @Nonnull
    private final Class<T> clazz;

    ConverterToNodeClass(@Nonnull final Class<T> clazz) {
      this.clazz = clazz;
    }

    @Override
    public T convert(final NiftyNodeImpl<? extends NiftyNode> niftyNodeImpl) {
      return clazz.cast(niftyNodeImpl.getNiftyNode());
    }
  }
}
