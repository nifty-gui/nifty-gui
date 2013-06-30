#ifdef GL_ES
precision highp float;
#endif

attribute vec2 aVertex;
attribute vec4 aColor;
attribute vec2 aTexture;

varying vec4 vColor;
varying vec2 vTexture;

uniform mat4 uModelViewProjectionMatrix;

void main() {
  gl_Position = uModelViewProjectionMatrix * vec4(aVertex.x, aVertex.y, 0.0, 1.0);
  vColor = aColor;
  vTexture = aTexture;
}
