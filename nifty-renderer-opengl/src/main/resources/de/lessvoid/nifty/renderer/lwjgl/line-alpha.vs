#version 150 core

in vec2 aVertex;

void main() {
  gl_Position = vec4(aVertex.xy, 0.0, 1.0);
}
