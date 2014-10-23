#version 150 core

uniform mat4 uMvp;
uniform vec2 resolution;

in vec2 aVertex;

void main() {
  gl_Position = uMvp * vec4(aVertex.x * resolution.x, aVertex.y * resolution.y, 0.0, 1.0);
}
