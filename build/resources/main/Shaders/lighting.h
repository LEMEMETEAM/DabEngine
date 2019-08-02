struct Light{
    vec3 position;
    vec3 color;
};

uniform Light[32] lights;
uniform float ambientStrength = 1.0;

vec4 calcAmbient(int current_light){
    return ambientStrength * lights[current_light].color;
}

vec4 calcDiffuse(int current_light, vec3 Normal, vec3 Position){
    vec3 normal = normalize(Normal);
    vec3 lightDir = normalize(lights[current_light].position - Position);
    float diff = max(dot(normal, lightDir), 0.0);
    return diff * lights[current_light].color;
}