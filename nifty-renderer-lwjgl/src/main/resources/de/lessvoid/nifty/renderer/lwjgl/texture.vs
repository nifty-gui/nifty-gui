#version 150 core

in vec2 aVertex;
in vec3 aUVL;

out vec3 vUVL;

uniform mat4 uMvp;
uniform vec3 uOffset;

void main() {
  gl_Position = uMvp * vec4(aVertex.x + uOffset.x, aVertex.y + uOffset.y, uOffset.z, 1.0);
  vUVL = aUVL;
}
