#include /Shaders/kernel.h

uniform float offset = 1.0 / 200.0;

vec3 edge(sampler2D texture, vec2 uv){
    vec2 offsets[9];
    genOffset(offset, offsets);
    float kernel[9] = float[](
        0,1,0,
        -1,0,1,
        0,-1,0
    );
    return calcKernel(kernel, offsets, texture, uv);
}