// Copyright (c) 2015, Nifty GUI Community 
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

// line join styles
//#define JOIN_NONE
//#define JOIN_MITER

#define PI 3.1415926535897932384626433832795

layout (lines_adjacency) in;
layout (triangle_strip, max_vertices = 8) out;

// model view projection matrix
uniform mat4 uMvp;

// x = (2*r + w)
// y = (2*r + w) / 2.f
// z = (2*r + w) / 2.f - 2*r
// W = (2*r)
uniform vec4 lineParameters;

out vec2 uv;

void calcMiterAngleOffset(in vec2 a, in vec2 b, in vec2 lineDir, in float halfWidth, out vec2 offset) {
  vec2 l1 = normalize(a);
  vec2 l2 = normalize(b);
  float angle = acos(dot(l1, l2)) / 2;
  offset = lineDir * (halfWidth / tan(angle)) * -sign(l1.x * l2.y - l2.x * l1.y);
}

void main(void) {
  // get params into individual variables
  float totalWidth = lineParameters.x;
  float halfWidth = lineParameters.y;
  float r2 = lineParameters.w;

  // (x0, y0) -> second point of previous line segment 
  // (x1, y1) -> first point of the current line segment 
  // (x2, y2) -> second point of the current line segment 
  // (x3, y3) -> first point of the next line segment 
  float x0 = gl_in[0].gl_Position.x;
  float y0 = gl_in[0].gl_Position.y;
  float x1 = gl_in[1].gl_Position.x;
  float y1 = gl_in[1].gl_Position.y;
  float x2 = gl_in[2].gl_Position.x;
  float y2 = gl_in[2].gl_Position.y;
  float x3 = gl_in[3].gl_Position.x;
  float y3 = gl_in[3].gl_Position.y;

  // calculate the vector for the line segment and normalize it
  vec2 start = vec2(x1, y1);
  vec2 end = vec2(x2, y2);
  vec2 lineDir = normalize(end - start);

  // calculate a vector in the direction of the line that is halfwidth and r2 length 
  vec2 lineDirHalfWidth = lineDir * halfWidth;

  // now get a vector orthogonal to the line dir halfwidth long vector
  vec2 flip = vec2(-lineDirHalfWidth.y, lineDirHalfWidth.x);

#ifdef CAP_BUTT

  float capU = r2;
  vec2 capExtrude = lineDir * r2;

#elif defined(CAP_SQUARE) || defined(CAP_ROUND)

  float capU = halfWidth;
  vec2 capExtrude = lineDirHalfWidth;

#endif

  // handle the beginning of the line segment
  if (x0 != x1 || y0 != y1) {
    // this is a line join at the beginning of the current line segment

#ifdef JOIN_NONE

    vec2 p2 = start + flip;
    vec2 p0 = p2 - capExtrude;
    gl_Position = uMvp * vec4(p0.x, p0.y, 0., 1.);
    uv = vec2(-capU, halfWidth);
    EmitVertex();

    vec2 p3 = start - flip;
    vec2 p1 = p3 - capExtrude;
    gl_Position = uMvp * vec4(p1.x, p1.y, 0., 1.);
    uv = vec2(-capU, -halfWidth);
    EmitVertex();

    gl_Position = uMvp * vec4(p2.x, p2.y, 0., 1.);
    uv = vec2(0.0, halfWidth);
    EmitVertex();

    gl_Position = uMvp * vec4(p3.x, p3.y, 0., 1.);
    uv = vec2(0.0, -halfWidth);
    EmitVertex();

#elif defined(JOIN_MITER)

    vec2 offset;
    calcMiterAngleOffset(vec2(x0, y0)-vec2(x1, y1), vec2(x2, y2)-vec2(x1, y1), lineDir, halfWidth, offset);

    vec2 p2 = start + flip + offset;
    gl_Position = uMvp * vec4(p2.x, p2.y, 0., 1.);
    uv = vec2(0.0, halfWidth);
    EmitVertex();

    vec2 p3 = start - flip - offset;
    gl_Position = uMvp * vec4(p3.x, p3.y, 0., 1.);
    uv = vec2(0.0, -halfWidth);
    EmitVertex();

#endif

  } else {
    // this is the start point of the line
    vec2 p2 = start + flip;
    vec2 p0 = p2 - capExtrude;
    gl_Position = uMvp * vec4(p0.x, p0.y, 0., 1.);
    uv = vec2(-capU, halfWidth);
    EmitVertex();

    vec2 p3 = start - flip;
    vec2 p1 = p3 - capExtrude;
    gl_Position = uMvp * vec4(p1.x, p1.y, 0., 1.);
    uv = vec2(-capU, -halfWidth);
    EmitVertex();

    gl_Position = uMvp * vec4(p2.x, p2.y, 0., 1.);
    uv = vec2(0.0, halfWidth);
    EmitVertex();

    gl_Position = uMvp * vec4(p3.x, p3.y, 0., 1.);
    uv = vec2(0.0, -halfWidth);
    EmitVertex();
  }

  // handle the end point of the line segment
  if (x3 != x2 || y3 != y2) {
    // this is a line join at the end of the current line segment

#ifdef JOIN_NONE

    vec2 pp0 = end + flip;
    gl_Position = uMvp * vec4( pp0.x, pp0.y, 0., 1.);
    uv = vec2(0.0, halfWidth);
    EmitVertex();

    vec2 pp1 = end - flip;
    gl_Position = uMvp * vec4( pp1.x, pp1.y, 0., 1.);
    uv = vec2(0.0, -halfWidth);
    EmitVertex();

    vec2 p6 = end + flip + capExtrude;
    gl_Position = uMvp * vec4( p6.x, p6.y, 0., 1.);
    uv = vec2(capU, halfWidth);
    EmitVertex();

    vec2 p7 = end - flip + capExtrude;
    gl_Position = uMvp * vec4( p7.x, p7.y, 0., 1.);
    uv = vec2(capU, -halfWidth);
    EmitVertex();

#elif defined(JOIN_MITER)

    vec2 offset;
    calcMiterAngleOffset(vec2(x1, y1)-vec2(x2, y2), vec2(x3, y3)-vec2(x2, y2), lineDir, halfWidth, offset);

    vec2 pp0 = end + flip - offset;
    gl_Position = uMvp * vec4( pp0.x, pp0.y, 0., 1.);
    uv = vec2(0.0, halfWidth);
    EmitVertex();

    vec2 pp1 = end - flip + offset;
    gl_Position = uMvp * vec4( pp1.x, pp1.y, 0., 1.);
    uv = vec2(0.0, -halfWidth);
    EmitVertex();

#endif

  } else {
    // the end point of the line
    vec2 p4 = end + flip;
    gl_Position = uMvp * vec4(p4.x, p4.y, 0., 1.);
    uv = vec2(0.0, halfWidth);
    EmitVertex();

    vec2 p5 = end - flip;
    gl_Position = uMvp * vec4(p5.x, p5.y, 0., 1.);
    uv = vec2(0.0, -halfWidth);
    EmitVertex();

    vec2 p6 = end + flip + capExtrude;
    gl_Position = uMvp * vec4(p6.x, p6.y, 0., 1.);
    uv = vec2(capU, halfWidth);
    EmitVertex();

    vec2 p7 = end - flip + capExtrude;
    gl_Position = uMvp * vec4(p7.x, p7.y, 0., 1.);
    uv = vec2(capU, -halfWidth);
    EmitVertex();
  }

  EndPrimitive();
}
