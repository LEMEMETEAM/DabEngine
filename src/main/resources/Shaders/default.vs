#version 330

layout (location =0) in vec3 position;
layout (location =1) in vec4 color;
layout (location =2) in vec2 texCoords;
layout (location =3) in vec3 normals;

out vec3 outPosition;
out vec2 outTexCord;
out vec4 outColor;
out vec3 outNormal;

layout (std140) uniform mvp {
    mat4 mvpMatrix;
};

void main(){
    gl_Position = mvpMatrix * vec4(position, 1.0);
    outPosition = position;
    outTexCord = texCoords;
    outColor = color;
    outNormal = normals;
}