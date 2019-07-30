#version 330

in vec3 outPosition;
in vec2 outTexCord;
in vec4 outColor;
in vec3 outNormal;

out vec4 fragColor;

#include /Shaders/lighting.h

uniform sampler2D texture;

void main(){
    vec4 color;
    for(int i = 0; i < lights.length(); i++){
        vec4 ambient = calcAmbient(i);
        vec4 diffuse = calcDiffuse(i, outNormal, outPosition);
        color += (ambient + diffuse);
    }
    fragColor = outColor * vec4(color.rgb, 1) * texture(texture, outTexCord);

}