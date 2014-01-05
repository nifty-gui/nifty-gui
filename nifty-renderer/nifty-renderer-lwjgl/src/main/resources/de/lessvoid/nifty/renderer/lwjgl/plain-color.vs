#version 150 core

uniform mat4 uMvp;
uniform vec3 uOffset;

in vec2 aVertex;
in vec4 aColor;

out vec4 vColor;

void main() {
  gl_Position = uMvp * vec4(aVertex.x + uOffset.x, aVertex.y + uOffset.y, uOffset.z, 1.0);
  vColor = aColor;
}
