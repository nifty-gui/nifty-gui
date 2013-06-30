#ifdef GL_ES
precision highp float;
#endif

uniform sampler2D uTex;

varying vec4 vColor;
varying vec2 vTexture;

void main() {
  vec4 frag = gl_FragCoord;
  gl_FragColor = vColor * texture2D(uTex, vTexture);
}
