#version 150 core

in vec2 aVertex;
in vec4 aColor;
in vec2 aTexture;

out vec4 vColor;
out vec2 vTexture;

uniform mat4 uModelViewProjectionMatrix;

void main() {
  gl_Position = uModelViewProjectionMatrix * vec4(aVertex.x, aVertex.y, 0.0, 1.0);
  vColor = aColor;
  vTexture = aTexture;
}
