#version 150 core

uniform sampler2D uTexture;

// the line color
uniform vec4 lineColor;

in vec2 vUV;

out vec4 color;

void main() {
  color.r = lineColor.r;
  color.g = lineColor.g;
  color.b = lineColor.b;
  color.a = texture(uTexture, vUV).r;
}
