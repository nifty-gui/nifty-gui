#version 150 core

// uniforms
uniform mat4 mProjection;
uniform float t;

// vertex attributes
in vec2 vVertex;
in vec2 vTexCoords;
in vec4 instanceColor;
in vec4 instanceTransform1;
in vec4 instanceTransform2;
in vec4 instanceTransform3;

// output
out vec2 vVaryingTexCoords;
out vec4 color;

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
  float a = sin((t + 1000 * gl_InstanceID) / 1000.0 / 3.1415);
  float b = sin((t + 1000 * gl_InstanceID) / 2000.0 / 3.1415);
  vec2 sinPos = vec2(a * 512 + 512, b * 384 + 384);

  mat4 translate1 = translate( 25.f,  25.f, 0.f);
  mat4 translate2 = translate(-25.f, -25.f, 0.f);
  mat4 rotateX = rotate(0.f, 1.f, 0.f, 0.f);
  mat4 scale = scale(1.f, 1.f, 1.f);
  mat4 transform = translate1 * rotateX * scale * translate2;

//  mat4 transform = mat4(instanceTransform1, instanceTransform2, instanceTransform3, vec4(0.0f, 0.0f, 0.0f, 1.0f));
 
  gl_Position = mProjection * ((transform * vec4(vVertex, 0.0, 1.0)) + vec4(sinPos, 0.0, 1.0));

  color = instanceColor;
}

