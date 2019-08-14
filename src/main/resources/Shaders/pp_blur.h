#include /Shaders/kernel.h

uniform float offset = 1.0 / 750.0;

vec3 blur(sampler2D texture, vec2 uv){
    vec2 offsets[9];
    genOffset(offset, offsets);
    float kernel[9] = float[](
        0.077847,	0.123317,	0.077847,
        0.123317,	0.195346,	0.123317,
        0.077847,	0.123317,	0.077847
    );
    return calcKernel(kernel, offsets, texture, uv);
}