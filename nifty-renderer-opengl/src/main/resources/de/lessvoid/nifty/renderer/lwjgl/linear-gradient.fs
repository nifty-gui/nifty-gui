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
#version 150 core

// define the maximum number of stops this shader supports
#define MAX_STOPS  10

// gradient stops, interval [0.0, 1.0] sorted ascending 
uniform float gradientStop[MAX_STOPS];

// gradient colors
uniform vec4 gradientColor[MAX_STOPS];

// actual number of stops
uniform int numStops;

// gradient start and end (startX, startY, endX, endY)
uniform vec4 gradient;

// input
in vec2 vUV;

// output
out vec4 color;

void main(void) {
    // direction vector of the linear gradient 
    vec2 gradientDir = gradient.zw - gradient.xy;

    // normalized direction vector 
    vec2 gradientDirUnit = normalize(gradientDir);

    // where are we on the linear gradient
    float t = dot(gradientDirUnit, vUV - gradient.xy) / length(gradientDir);

    // use first color when before the first gradient point
    if (t <= 0.0) {
        color = gradientColor[0];
        return;
    }

    // now check the individual stops
    for (int i=0; i<numStops-1; i++) {
        if (t > gradientStop[i] && t <= gradientStop[i+1]) {
            float inStopT = (t - gradientStop[i]) / (gradientStop[i+1] - gradientStop[i]);
            color = mix(gradientColor[i], gradientColor[i+1], inStopT);
            return;
        }
    }

    // if we didn't find one of the stops we'll use the last one
    color = gradientColor[numStops-1];
}
