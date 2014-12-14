// Copyright (c) 2014, Jens Hohmuth 
// All rights reserved. 
// 
// Redistribution and use in source and binary forms, with or without 
// modification, are permitted provided that the following conditions are 
// met: 
// 
// * Redistributions of source code must retain the above copyright 
//   notice, this list of conditions and the following disclaimer. 
// * Redistributions in binary form must reproduce the above copyright 
//   notice, this list of conditions and the following disclaimer in the 
//   documentation and/or other materials provided with the distribution. 
// 
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND 
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
// PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE 
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
// THE POSSIBILITY OF SUCH DAMAGE. 

//#version 150 core

// line cap styles
//#define CAP_BUTT
//#define CAP_SQUARE
//#define CAP_ROUND

#define PI 3.1415926535897932384626433832795
#define TWO_PI (PI * 2)

in vec2 t;
out vec4 color;

// param.x = startAngle
// param.y = endAngle
// param.z = line width / 2
// param.w = line color alpha
uniform vec4 param;

float normalizeAngle(float angle) {
  return angle - floor(angle / TWO_PI) * TWO_PI;
}

void main() {
    float radius = 1.0;
    float startAngle = param.x;
    float endAngle = param.y;
    float lineWidth = param.z;
    float lineColorAlpha = param.w;

    float dist = distance(t, vec2(0.0, 0.0));
    float delta = fwidth(dist)*1.10;
    float alpha = 1 - (1 - smoothstep(radius + lineWidth, radius + lineWidth + delta, dist)) *
                      (1 - smoothstep(radius - lineWidth, radius - lineWidth - delta, dist));
    float delta2 = delta * 2;

    float at = atan(t.y, t.x);
    if (at < 0) {
      at += 2 * PI;
    }
    at = 2 * PI - at;

    float angleMix;

#ifdef CAP_BUTT

    if ((endAngle + delta2) > (startAngle - delta2)) {
        angleMix = 1 - smoothstep(startAngle - delta2, startAngle, at) * (1 - smoothstep(endAngle, endAngle + delta2, at));
    } else {
        angleMix = (1 - (smoothstep(startAngle - delta2, startAngle, at)) * (1 - step(2*PI, at))) *
                   (1 - (step(0, at) * (1 - smoothstep(endAngle, endAngle + delta2, at))));
    }

#elif defined(CAP_ROUND)

    if (endAngle > startAngle) {
        angleMix = 1 - (step(startAngle, at) * (1 - step(endAngle, at)));
    } else {
        angleMix = (1 - (step(startAngle, at) * (1 - step(    2*PI, at)))) *
                   (1 - (step(        0., at) * (1 - step(endAngle, at))));
    }

#elif defined(CAP_SQUARE)

    float start = normalizeAngle(startAngle - lineWidth - delta2);
    float end = normalizeAngle(endAngle + lineWidth + delta2);
    if (end > start) {
        angleMix = 1 - (smoothstep(start, normalizeAngle(startAngle - lineWidth), at)) * (1 - smoothstep(normalizeAngle(endAngle + lineWidth), end, at));
    } else {
        float end2 = normalizeAngle(endAngle + lineWidth);
        float angleMixStart;
        float angleMixEnd;

        if (normalizeAngle(startAngle - lineWidth) > start) {
            angleMixStart = (1 - (smoothstep(start, normalizeAngle(startAngle - lineWidth), at) * (1 - step(2*PI, at))));
        } else {
            angleMixStart = (1 - (smoothstep(start, 2*PI, at) * (1 - step(2*PI, at))));
        }

        if (end < normalizeAngle(endAngle + lineWidth)) {
            angleMixEnd = (1 - (step(0, at) * (1 - smoothstep(   0, end, at))));
	    } else {
            angleMixEnd = (1 - (step(0, at) * (1 - smoothstep(end2, end, at))));
	    }

        angleMix = angleMixStart * angleMixEnd;
    }

#endif

    float finalAlpha = mix(lineColorAlpha, 0.0, alpha + angleMix);

#ifdef CAP_ROUND

    float startAngleAlpha = startAngle;
    if (startAngleAlpha < 0) {
      startAngleAlpha += 2 * PI;
    }
    startAngleAlpha = 2 * PI - startAngleAlpha;
    vec2 capCenterPoint = vec2(cos(startAngleAlpha) * 0.5, sin(startAngleAlpha) * 0.5);
    vec2 capPoint = vec2(t.x, t.y) - capCenterPoint;
    vec2 capCenterNormal = normalize(vec2(cos(startAngleAlpha), sin(startAngleAlpha)));
    vec2 capCenterNormalFlip = vec2(-capCenterNormal.y, capCenterNormal.x);
    float distance = smoothstep(lineWidth, lineWidth + delta, distance(capPoint, capCenterPoint));
    finalAlpha = max(finalAlpha, mix(lineColorAlpha, 0.0, distance) * step(0.0, dot(capCenterNormalFlip, capPoint)));

    startAngleAlpha = endAngle;
    if (startAngleAlpha < 0) {
      startAngleAlpha += 2 * PI;
    }
    startAngleAlpha = 2 * PI - startAngleAlpha;
    capCenterPoint = vec2(cos(startAngleAlpha) * 0.5, sin(startAngleAlpha) * 0.5);
    capPoint = vec2(t.x, t.y) - capCenterPoint;
    capCenterNormal = normalize(vec2(cos(startAngleAlpha), sin(startAngleAlpha)));
    capCenterNormalFlip = vec2(-capCenterNormal.y, capCenterNormal.x);
    distance = smoothstep(lineWidth, lineWidth + delta, distance(capPoint, capCenterPoint));
    finalAlpha = max(finalAlpha, mix(lineColorAlpha, 0.0, distance) * (1.0 - step(0.0, dot(capCenterNormalFlip, capPoint))));

#endif

    color.rgba = vec4(0.0, 0.0, 0.0, finalAlpha);
}
