#include /Shaders/kernel.h

uniform float offset = 1.0 / 250.0;

vec3 blur(sampler2D texture, vec2 uv){
    vec2 offsets[9];
    genOffset(offset, offsets);
    float kernel[9] = float[](
        1.0 / 16, 2.0 / 16, 1.0 / 16,
        2.0 / 16, 4.0 / 16, 2.0 / 16,
        1.0 / 16, 2.0 / 16, 1.0 / 16 
    );
    return calcKernel(kernel, offsets, texture, uv);
}