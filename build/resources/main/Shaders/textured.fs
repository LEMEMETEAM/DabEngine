#version 330
#include /Shaders/Utils.h

in vec3 outPosition;
in vec2 outTexCord;
in vec4 outColor;
in vec3 outNormal;

out vec4 fragColor;

uniform sampler2D texture;

void main(){
    vec4 tex = texture2D(texture, outTexCord);
    alphaScissor(tex);
    fragColor = outColor * tex;
}