#version 150 core

// uniforms
uniform mat4 mProjection;

// vertex attributes
in vec2 vVertex;
in vec2 vTexCoords;
in vec4 instanceColor;
in vec4 instanceTransform1;
in vec4 instanceTransform2;
in vec4 instanceTransform3;
in vec4 instanceTransform4;

// output
out vec2 vVaryingTexCoords;
out vec4 color;

mat4 identity() {
  return mat4(
    1.f, 0.f, 0.f, 0.f,
    0.f, 1.f, 0.f, 0.f,
    0.f, 0.f, 1.f, 0.f,
    0.f, 0.f, 0.f, 1.f);
}

mat4 translate(float x, float y, float z) {
  return mat4(
    1.f, 0.f, 0.f, 0.f,
    0.f, 1.f, 0.f, 0.f,
    0.f, 0.f, 1.f, 0.f,
    x,   y,   z,   1.f);
}

mat4 rotate(float angle, float x, float y, float z) {
  float c = cos(angle);
  float s = sin(angle);

  return mat4(
    x*x*(1-c)+c,   y*x*(1-c)+z*s, x*z*(1-c)-y*s, 0.0f,
    x*y*(1-c)-z*s, y*y*(1-c)+c,   y*z*(1-c)+x*s, 0.0f,
    x*z*(1-c)+y*s, y*z*(1-c)-x*s, z*z*(1-c)+c,   0.0f,
    0.0f,          0.0f,          0.0f,          1.0f);
}

mat4 scale(float x, float y, float z) {
  return mat4(
    x,   0.f, 0.f, 0.f,
    0.f,   y, 0.f, 0.f,
    0.f, 0.f,   z, 0.f,
    0.f, 0.f, 0.f, 1.f);
}

void main() {
  vVaryingTexCoords = vTexCoords;

  mat4 transform = mat4(instanceTransform1, instanceTransform2, instanceTransform3, instanceTransform4);
 
  gl_Position = mProjection * transform * vec4(vVertex, 0.0, 1.0);

  color = instanceColor;
}

