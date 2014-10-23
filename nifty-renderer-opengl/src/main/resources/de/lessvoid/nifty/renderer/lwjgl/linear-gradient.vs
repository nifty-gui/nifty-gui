#version 150 core

// model view projection matrix
uniform mat4 uMvp;

// vertex attributes (x, y)
in vec2 aVertex;

// output
out vec2 vUV;

void main() {
  gl_Position = uMvp * vec4(aVertex.xy, 0.0, 1.0);
  vUV = aVertex.xy;
}
