#version 150 core

uniform sampler2DArray uTexture;

in vec3 vUVL; // uv and texture layer to use
out vec4 color;

void main() {
  color = texture(uTexture, vUVL);
}
