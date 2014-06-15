#version 150 core

// texture sampler
uniform sampler2D uTexture;

// input attributes
in vec2 vUV;
in vec4 vColor;

// output attributes
out vec4 color;

void main() {
  color = texture(uTexture, vUV) * vColor;
}
