#version 150 core

uniform sampler2D uTex;

layout(origin_upper_left) in vec4 gl_FragCoord;

in vec4 vColor;
in vec2 vTexture;

out vec4 fColor;

void main() {
  vec4 frag = gl_FragCoord;
  fColor = vColor * texture(uTex, vTexture, 0);
}