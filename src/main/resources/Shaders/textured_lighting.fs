#version 330

in vec3 outPosition;
in vec2 outTexCord;
in vec4 outColor;
in vec3 outNormal;

out vec4 fragColor;

uniform sampler2D texture;

struct Light{
    vec3 position;
    vec3 color;
};

uniform Light[32] lights;
uniform float ambientStrength;

void main(){

    vec3 color;
    for(int i = 0; i < 32; i++){
        vec3 ambient = ambientStrength * lights[i].color;

        vec3 normal = normalize(outNormal);
        vec3 lightDir = normalize(lights[i].position - outPosition);
        float diff = max(dot(normal, lightDir), 0.0);
        vec3 diffuse = diff * lights[i].color ;

        color += (ambient + diffuse);
    }
    fragColor = vec4(color, 1.0) * outColor * texture2D(texture, outTexCord);
}