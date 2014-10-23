#version 150 core

// vertex attributes
in vec2 aVertex;
in vec2 aUV;

// output
out vec2 t;

// model view projection matrix
uniform mat4 uMvp;

void main() {
  gl_Position = uMvp * vec4(aVertex.x, aVertex.y, 0.0, 1.0);
  t = aUV;
}
