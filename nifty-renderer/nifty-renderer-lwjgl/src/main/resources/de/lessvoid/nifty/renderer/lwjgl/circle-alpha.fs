#version 150 core

#define PI 3.1415926535897932384626433832795

in vec2 t;
out vec4 color;

// param.x = startAngle
// param.y = endAngle
// param.z = line width / 2
// param.w = line color alpha
uniform vec4 param;

void main() {
    float radius = 1.0;
    float startAngle = param.x;
    float endAngle = param.y;
    float lineWidth = param.z;
    float lineColorAlpha = param.w;

    float dist = distance(t, vec2(0.0, 0.0));
    float delta = fwidth(dist);
    float alpha = 1 - (smoothstep(radius - delta*2 - lineWidth, radius - lineWidth, dist) *
                  (1 - smoothstep(radius - delta*2 + lineWidth, radius + lineWidth, dist)));

    float at = atan(t.y, t.x);
    if (at < 0) {
      at += 2 * PI;
    }
    at = 2 * PI - at;
    float angleMix = 1 - (smoothstep(startAngle - delta*2, startAngle, at) *
                     (1 - smoothstep(endAngle, endAngle + delta*2, at)));
    float c = mix(lineColorAlpha, 0.0, alpha);
    color.r = mix(c, 0.0, angleMix);
    color.gba = vec3(0.0, 0.0, 0.0);
}
