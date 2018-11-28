#version 330

layout (location =0) in vec3 position;
layout (location =1) in vec2 texCords;

out vec2 outTexCord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform mat4 texModifier;

void main(){
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 1.0);
    outTexCord = (texModifier * vec4(texCords, 0, 1)).xy;
}