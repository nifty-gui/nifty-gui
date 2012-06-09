#version 150 core

uniform sampler2D tex;

in vec2 vVaryingTexCoords;
in vec4 color;

out vec4 outputColor;

void main() {
  outputColor = texture(tex, vVaryingTexCoords.st);
}
