struct Light{
    vec3 position;
    vec3 color;
};

struct Surface{
    vec3 albedo;
    vec3 normal;
    vec3 gloss;
};

vec3 lambertLighting(Surface s, Light light, vec3 lightdir, float distance){
    vec3 c;

    float diff = max(dot(s.normal, lightdir), 0.0);
    float att = 1.0 / (distance * distance);

    c.rgb = diff * light.color * s.albedo.rgb * att;


    return c;
}

vec3 blinnPhongLighting(Surface s, Light light, vec3 lightdir, vec3 viewDir, float distance){
    vec3 c;

    vec3 halfDir = normalize(lightdir + viewDir);

    float diff = max(dot(s.normal, lightdir), 0.0);
    float spec = pow(max(dot(s.normal, halfDir), 0.0), 32);
    float att = 1.0 / (pow(distance, 2));

    c.rgb = ((diff * light.color * s.albedo.rgb) + (spec * light.color * s.gloss)) * att;
    
    return c;
}
