package de.lessvoid.nifty.renderer.lwjgl;

import java.util.Hashtable;
import java.util.Map;

import de.lessvoid.coregl.CoreFactory;
import de.lessvoid.coregl.CoreShader;

/**
 * Helper class to manage CoreShaders. This will cache existing CoreShader instances for later access.
 * @author void
 */
public class ShaderFactory {
  private final Map<String, CoreShader> lookup = new Hashtable<String, CoreShader>();
  private final CoreFactory coreFactory;

  /**
   * Create a new ShaderFactory.
   * @param coreFactory CoreFactory to use
   */
  public ShaderFactory(final CoreFactory coreFactory) {
    this.coreFactory = coreFactory;
  }

  /**
   * Create a shader (or return the same shader if a shader with that name has been created before)
   * @param name the name of the resource to load (without the .vs and .fs file extension)
   * @param vertexAttributes the vertex attributes that shader requires
   * @return the CoreShader instance
   */
  public CoreShader shaderWithVertexAttributes(final String name, final String ... vertexAttributes) {
    if (lookup.containsKey(name)) {
      return lookup.get(name);
    }
    CoreShader result = coreFactory.newShaderWithVertexAttributes(vertexAttributes);
    result.vertexShader(name + ".vs");
    result.fragmentShader(name + ".fs");
    result.link();
    lookup.put(name, result);
    return result;
  }
}
