#version 330 core

in vec3 outPosition;
in vec2 outTexCord;
in vec4 outColor;
in vec3 outNormal;

out vec4 fragColor;

#include /Shaders/lighting.h
#include /Shaders/Utils.h

uniform sampler2D texture;
uniform bool textured;

void main(){
    vec4 color;
    for(int i = 0; i < lights.length(); i++){
        vec4 ambient = calcAmbient(i);
        vec4 diffuse = calcDiffuse(i, outNormal, outPosition);
        color += (ambient + diffuse);
    }
    vec4 tex;
    if(textured){
        tex = texture(texture, outTexCord);
    }
    else{
        tex = vec4(1.0);
    }
    alphaScissor(tex);
    fragColor = outColor * vec4(color.rgb, 1) * tex;

}
