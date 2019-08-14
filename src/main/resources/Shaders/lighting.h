in vec3 outNormal;

struct Light{
    vec3 position;
    vec3 color;
};

layout (std140) uniform lighting{
    Light[32] lights;
    float ambientStrength;
};
layout(binding=1) uniform sampler2D specular;


vec4 calcAmbient(int current_light){
    return ambientStrength * vec4(lights[current_light].color, 1.0);
}

vec4 calcDiffuse(int current_light, vec3 Normal, vec3 Position){
    vec3 normal = normalize(Normal);
    vec3 lightDir = normalize(lights[current_light].position - Position);
    float diff = max(dot(normal, lightDir), 0.0);
    return diff * vec4(lights[current_light].color, 1.0);
}

vec4 calcSpecular(int current_light, vec3 viewPos, vec3 Normal, vec3 Position, float strength, float shininess){
    vec3 viewDir = normalize(viewPos - Position);
    vec3 lightDir = normalize(lights[current_light].position - Position);
    vec3 reflectDir = reflect(-lightDir, Normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), shininess);
    return spec * vec4(lights[current_light].color, 1.0) * strength;
}
