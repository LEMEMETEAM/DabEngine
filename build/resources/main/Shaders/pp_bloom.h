#include /Shaders/Utils.h
#include /Shaders/pp_blur.h

vec3 bloom(sampler2D tex, vec2 uv, float blur_weight, float bloom_weight){
    vec4 rgb = texture(tex, uv);
    float bright = brightness(rgb.rgb);
    vec3 blur_pass = mix(rgb.rgb*bright, blur(tex, uv), blur_weight);
    vec3 bloom_pass = mix(blur_pass, rgb.rgb, bloom_weight);
    return bloom_pass;
}

