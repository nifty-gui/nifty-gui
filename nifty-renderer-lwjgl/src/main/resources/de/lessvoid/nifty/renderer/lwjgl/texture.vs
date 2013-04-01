#version 150 core

in vec2 aVertex;
in vec3 aUVL;

out vec3 vUVL;

uniform mat4 uMvp;

void main() {
  gl_Position = uMvp * vec4(aVertex.x, aVertex.y, 0.0, 1.0);
  vUVL = aUVL;
}
