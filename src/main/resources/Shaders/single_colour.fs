#version 330 core

in vec2 outPosition;
in vec2 outTexCord;
in vec4 outColor;

out vec4 fragColor;

void main(){
	fragColor = outColor;
}