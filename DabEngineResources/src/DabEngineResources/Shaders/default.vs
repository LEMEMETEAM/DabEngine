#version 330

layout (location =0) in vec2 position;
layout (location =1) in vec4 color;
layout (location =2) in vec2 texCoords;

out vec2 outPosition;
out vec2 outTexCord;
out vec4 outColor;

uniform mat4 projectionMatrix;

void main(){
    gl_Position = projectionMatrix * vec4(position, 0.0, 1.0);
    outPosition = position;
    outTexCord = texCoords;
    outColor = color;
}