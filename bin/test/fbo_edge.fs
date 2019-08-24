#version 330

#include /Shaders/pp_edge.h

in vec2 outUV;

out vec4 _Color;

uniform sampler2D texture;

void main(){
    _Color = vec4(edge(texture, outUV), 1.0);
}