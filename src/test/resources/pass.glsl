#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec4 color;
layout (location = 2) in vec2 uvs;
layout (location = 3) in vec3 normals;

out vec2 outUV;
out vec4 outColor;

layout(std140) uniform globals {
    mat4 matrix;
};

void main(){
    gl_Position = globals.matrix * vec4(position, 1.0);

    outUV = uvs;
    outColor = color;
}