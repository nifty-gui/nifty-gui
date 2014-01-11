#version 150 core

uniform mat4 uMvp;

in vec2 aVertex;
in vec4 aColor;

out vec4 vColor;

void main() {
  gl_Position = uMvp * vec4(aVertex, 0.0, 1.0);
  vColor = aColor;
}
