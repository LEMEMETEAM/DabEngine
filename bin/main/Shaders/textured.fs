#version 330

in vec3 outPosition;
in vec2 outTexCord;
in vec4 outColor;
in vec3 outNormal;

out vec4 fragColor;

uniform sampler2D texture;

void main(){

    fragColor = outColor * texture2D(texture, outTexCord);
}