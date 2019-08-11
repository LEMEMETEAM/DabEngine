#version 330 core

#include /Shaders/Utils.h

in vec3 outPosition;
in vec2 outTexCord;
in vec4 outColor;
in vec3 outNormal;

out vec4 fragColor;

uniform sampler2D texture;

void main(){
    vec4 tex = texture(texture, outTexCord);
    alphaScissor(tex);
    fragColor = outColor * tex;
}
