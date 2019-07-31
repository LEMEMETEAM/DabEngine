#version 330

in vec3 outPosition;
in vec2 outTexCord;
in vec4 outColor;
in vec3 outNormal;

out vec4 fragColor;

uniform sampler2D texture;

void main(){
    vec4 tex = texture2D(texture, outTexCord)
    if(tex.a < 0.1){
        discard;
    }
    fragColor = outColor * tex;
}