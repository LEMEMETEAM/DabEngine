#version 330

in vec2 outPosition;
in vec2 outTexCord;
in vec4 outColor;

out vec4 fragColor;

uniform sampler2D texture;

void main(){
	fragColor = outColor * texture(texture, outTexCord);
}