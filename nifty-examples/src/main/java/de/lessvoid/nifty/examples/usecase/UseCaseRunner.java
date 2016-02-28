/*
 * Copyright (c) 2015, Nifty GUI Community 
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
package de.lessvoid.nifty.examples.usecase;

import com.lessvoid.coregl.jogl.CoreSetupJogl;
import com.lessvoid.coregl.jogl.JoglCoreGL;
import com.lessvoid.coregl.lwjgl.CoreSetupLwjgl;
import com.lessvoid.coregl.lwjgl.LwjglCoreGL;
import de.lessvoid.nifty.NiftyConfiguration;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * A helper class that initializes the rendering subsystem and the main Nifty instance. It will then instantiate
 * the given class using the Nifty instance as it's only parameter.
 *
 * @author void
 */
public class UseCaseRunner {

  // We register all the UseCaseRunnerAdapters that will initialize Nifty
  // with the corresponding NiftyRenderDevices and then run the UseCase class.
  static Map<String, UseCaseRunnerAdapter> initAdapters = new HashMap<>();
  static {
    initAdapters.put("lwjgl", new UseCaseRunnerAdapterOpenGL(new CoreSetupLwjgl(new LwjglCoreGL()), new LwjglCoreGL()));
    initAdapters.put("jogl", new UseCaseRunnerAdapterOpenGL(new CoreSetupJogl(new JoglCoreGL()), new JoglCoreGL()));
    initAdapters.put("java2d", new UseCaseRunnerAdapterJava2D());
    initAdapters.put("dummy", new UseCaseRunnerAdapterDummy());
  }

  static void run(final Class<?> useCaseClass, final String[] args) throws Exception {
    run(useCaseClass, args, new NiftyConfiguration());
  }

  static void run(final Class<?> useCaseClass, final String[] args, final NiftyConfiguration niftyConfiguration) throws Exception {
    // This is only needed since we use the CoreGL lib that logs with java.util.logging
    LogManager.getLogManager().reset();
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
    Logger.getLogger("global").setLevel(Level.FINEST);

    UseCaseRunnerAdapter adapter = initAdapters.get(provideAdapterName(args));
    adapter.run(useCaseClass, args, niftyConfiguration);
  }

  private static String provideAdapterName(final String[] args) {
    String adapter = "lwjgl";
    if (args.length > 0) {
      adapter = args[0];
    }
    System.out.println("Using '" + adapter + "' to initialize Nifty instance");
    return adapter;
  }
}
