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
 * This contains a couple of helper classes and caches for {@link NiftyTreeNodePredicate} instances.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class NiftyTreeNodePredicates {
  @Nonnull
  private static final Map<Class<?>, PredicateToNodeImplClass> NODE_IMPL_CACHE = new HashMap<>();
  @Nonnull
  private static final Map<Class<?>, PredicateToNodeClass> NODE_CACHE = new HashMap<>();

  @Nonnull
  public static NiftyTreeNodePredicate nodeImplAny() {
    return PredicateAny.Instance;
  }

  @Nonnull
  public static <T extends NiftyNodeImpl> NiftyTreeNodePredicate nodeImplClass(@Nonnull final Class<T> clazz) {
    PredicateToNodeImplClass instance = NODE_IMPL_CACHE.get(clazz);
    if (instance == null) {
      instance = new PredicateToNodeImplClass(clazz);
      NODE_IMPL_CACHE.put(clazz, instance);
    }
    return instance;
  }

  @Nonnull
  public static <T extends NiftyNode> NiftyTreeNodePredicate nodeClass(final Class<T> clazz) {
    PredicateToNodeClass instance = NODE_CACHE.get(clazz);
    if (instance == null) {
      instance = new PredicateToNodeClass(clazz);
      NODE_CACHE.put(clazz, instance);
    }
    return instance;
  }

  private NiftyTreeNodePredicates() {}

  private enum PredicateAny implements NiftyTreeNodePredicate {
    Instance;
    @Override
    public boolean accept(@Nonnull final NiftyNodeImpl<? extends NiftyNode> niftyNodeImpl) {
      return true;
    }
  }

  private static final class PredicateToNodeImplClass implements NiftyTreeNodePredicate {
    @Nonnull
    private final Class<?> clazz;

    PredicateToNodeImplClass(@Nonnull final Class<?> clazz) {
      this.clazz = clazz;
    }

    @Override
    public boolean accept(@Nonnull final NiftyNodeImpl<? extends NiftyNode> niftyNodeImpl) {
      return clazz.isAssignableFrom(niftyNodeImpl.getClass());
    }
  }

  private static final class PredicateToNodeClass implements NiftyTreeNodePredicate {
    @Nonnull
    private final Class<?> clazz;

    PredicateToNodeClass(@Nonnull final Class<?> clazz) {
      this.clazz = clazz;
    }

    @Override
    public boolean accept(@Nonnull final NiftyNodeImpl<? extends NiftyNode> niftyNodeImpl) {
      return clazz.isAssignableFrom(niftyNodeImpl.getNiftyNode().getClass());
    }
  }
}
