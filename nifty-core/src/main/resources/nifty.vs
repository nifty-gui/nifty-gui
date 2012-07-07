#version 150 core

// uniforms
uniform mat4 mProjection;

// vertex attributes
in vec2 vVertex;
in vec2 vTexCoords;
in vec4 instanceColor;
in float instanceScale;
in float instanceAngle;
in vec2 instancePos;
in vec2 instanceDim;

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

  mat4 translate = translate(instancePos.x, instancePos.y, 0.0);
  mat4 scale = scale(instanceScale * instanceDim.x, instanceScale * instanceDim.y, 1.0);
  mat4 rotate = rotate(instanceAngle, 0.0, 1.0, 0.0);
  mat4 translate2 = translate(-0.5, -0.5, 0.0);
  mat4 transform = translate * scale * rotate * translate2;
 
//    Matrix4f.mul(local, new Matrix4f().translate(new Vector2f(box.getX(), box.getY())), local);
//    scale = (float) Math.sin(x) / 4.0f + 1.0f;
//    Matrix4f.mul(local, new Matrix4f().scale(new Vector3f(box.getWidth() * scale, box.getHeight() * scale, 1.0f)), local);
//    Matrix4f.mul(local, new Matrix4f().rotate(angle, new Vector3f(0.f, 0.f, 1.f)), local);
//    Matrix4f.mul(local, new Matrix4f().translate(new Vector2f(-0.5f, -0.5f)), local);
 
 
  gl_Position = mProjection * transform * vec4(vVertex, -1.0, 1.0);

  color = instanceColor;
}

