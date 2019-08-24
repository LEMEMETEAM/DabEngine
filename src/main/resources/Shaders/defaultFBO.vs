#version 330 core

layout (location =0) in vec3 pos;
layout (location =2) in vec2 uv;

out vec2 outUV;
out vec3 outPos;

layout (std140) uniform mvp {
    mat4 projectionViewMatrix;
};

void main(){
    gl_Position = projectionViewMatrix * vec4(pos, 1.0);
    outUV = uv;
    outPos = pos;
}
