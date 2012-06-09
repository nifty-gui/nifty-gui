#version 150 core

uniform sampler2D tex;
uniform vec4 backgroundColor;

in vec2 vVaryingTexCoords;
in vec4 color;

out vec4 outputColor;

void main() {
  outputColor = color; //texture(tex, vVaryingTexCoords.st) * backgroundColor;
}
