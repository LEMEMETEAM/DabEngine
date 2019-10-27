#version 330 core

in vec2 outUV;
in vec4 outColor;
uniform sampler2D texture0;

out vec4 finalColor;

uniform vec2 resolution;


void main() {
    float data = texture(texture0, outUV).a;

    vec2 position = outUV.st *2.0 -1.0;

    float len = length(position);

    finalColor = vec4(vec3(1.0), data * smoothstep(1.0, 0.0, len));
}