#version 330

in vec2 outPos;
in vec2 outUV;

out vec4 _Color;

uniform sampler2D texture;

void main(){
    _Color = texture(texture, outUV);
}