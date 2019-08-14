#include /Shaders/Utils.h

in vec2 outTexCord;

uniform sampler2D texture;

vec4 getTexel(){
    vec4 tex = texture(texture, outTexCord);
    alphaScissor(tex);
    return tex;
}
