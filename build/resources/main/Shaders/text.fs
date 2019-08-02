#version 330

in vec2 outPosition;
in vec2 outTexCord;
in vec4 outColor;

out vec4 fragColor;

uniform sampler2D texture;

void main(){
    vec4 sampled = vec4(1.0, 1.0, 1.0, texture(texture, outTexCord).r);
	fragColor = vec4(outColor.rgb, 1.0) * sampled;
}