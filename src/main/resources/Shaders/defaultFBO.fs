#version 330 core

in vec3 outPos;
in vec2 outUV;

out vec4 _Color;

uniform sampler2D texture;
uniform vec3 viewPos = vec3(0.0);

void main(){
    vec4 texel = texture(texture, outUV);
    _Color = texel / (texel + vec4(1.0));
}
