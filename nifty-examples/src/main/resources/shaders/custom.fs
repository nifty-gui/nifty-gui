#version 150 core

out vec4 color;

uniform float time;
uniform vec2 resolution;

float calc(float f, vec2 uPos) {
  float t = time / 5.0;
  uPos.y += sin( t + uPos.x * 5.0) * 0.1;
  uPos.x += sin( t + uPos.y * 6.0) + 0.2;
  float value = sin((uPos.x) * 5.0) + sin(uPos.y * 4.0);
  return 1.0/sqrt(abs(value))/1.0 * pow(f, 10.);
}

void main( void ) {
  vec2 p = ( gl_FragCoord.xy / resolution.xy );
  float f;
  if (p.y > 0.5) {
    f = (0.5 - (p.y - 0.5)) * 2.;
  } else {
    f = p.y * 2.;
  }
    
  float vertColor = calc(f, p) + 0.12345;
  color = vec4(vertColor, vertColor*sin(time / 4.0), vertColor*cos(time / 4.2), 1.0);
}