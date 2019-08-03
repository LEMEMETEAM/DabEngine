#version 330

#include /Shaders/lighting.h

in vec3 outPosition;
in vec2 outTexCord;
in vec4 outColor;
in vec3 outNormal;

out vec4 fragColor;

uniform sampler2D texture;
uniform int lit = 0;
#define LIT lit

void main(){
        #if LIT == 1
                vec4 color;
                for(int i = 0; i < lights.length(); i++){
                        vec4 ambient = calcAmbient(i);
                        vec4 diffuse = calcDiffuse(i, outNormal, outPosition);
                        color += (ambient + diffuse);
                }
                vec4 sampled = vec4(1.0, 1.0, 1.0, texture(texture, outTexCord).r);
	        fragColor = vec4(outColor.rgb, 1.0) * sampled * vec4(color.rgb, 1.0);
        #else
                vec4 sampled = vec4(1.0, 1.0, 1.0, texture(texture, outTexCord).r);
	        fragColor = vec4(outColor.rgb, 1.0) * sampled; 
        #endif
}