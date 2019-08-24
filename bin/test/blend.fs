#version 330
#extension GL_ARB_shading_language_420pack: enable
#include /Shaders/pp_blur.h

in vec2 outUV;

out vec4 _Color;

layout(binding=0) uniform sampler2D texture;
layout(binding=1) uniform sampler2D unshaded;
layout(binding=2) uniform sampler2D scaled;

void main(){
    vec4 n = texture(texture, outUV);
    vec4 u = texture(unshaded, outUV);
    vec4 s = vec4(blur(scaled, outUV), 1.0);

    vec4 border = max(s - u, 0.0);
    _Color = n + border * 5;
}