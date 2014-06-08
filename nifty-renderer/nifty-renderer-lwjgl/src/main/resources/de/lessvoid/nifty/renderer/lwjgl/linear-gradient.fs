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
