#version 150 core

in vec2 aVertex;

uniform mat4 uMvp;
uniform vec3 uOffset;

void main() {
  gl_Position = uMvp * vec4(aVertex.x + uOffset.x, aVertex.y + uOffset.y, uOffset.z, 1.0);
}
