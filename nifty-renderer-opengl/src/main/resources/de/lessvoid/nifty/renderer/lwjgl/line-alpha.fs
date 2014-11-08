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

// line join styles
//#define JOIN_NONE
//#define JOIN_MITER

// x = (2*r + w)
// y = (2*r + w) / 2.f
// z = (2*r + w) / 2.f - 2*r
// W = (2*r)
uniform vec4 lineParameters;

// the lineColorAlpha
uniform float lineColorAlpha;

in vec2 uv;

out vec4 color;

void main() {
  // get params into individual variables
  float halfWidth = lineParameters.y;
  float halfWidthMinus2R = lineParameters.z;
  float r2 = lineParameters.w;

#ifdef CAP_ROUND

  float distance = sqrt(uv.x*uv.x + uv.y*uv.y);
  float intensity = 1.0 - smoothstep (halfWidthMinus2R, halfWidth, distance);
  float alpha = intensity*lineColorAlpha;

#else

  float distanceY = sqrt(uv.y*uv.y);
  float intensityY = 1.0 - smoothstep (halfWidthMinus2R, halfWidth, distanceY);

  float distanceX = sqrt(uv.x*uv.x);

  #ifdef CAP_BUTT
  float intensityX = 1.0 - smoothstep (0, r2, distanceX);
  #elif defined(CAP_SQUARE)
  float intensityX = 1.0 - smoothstep(halfWidthMinus2R, halfWidth, distanceX);
  #endif

  float alpha = intensityX*intensityY*lineColorAlpha;

#endif
  color = vec4(alpha, 0.0, 0.0, 0.0);
}
