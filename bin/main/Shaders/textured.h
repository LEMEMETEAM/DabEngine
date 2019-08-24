#include /Shaders/Utils.h

vec4 getTexel(sampler2D texture, vec2 uv){
    vec4 tex = texture(texture, uv);
    alphaScissor(tex);
    return tex;
}
