float brightness(vec3 rgb){
    return (rgb.r + rgb.g + rgb.b) / 3.0;
}

void alphaScissor(vec4 tex_color){
    if(tex_color.a < 0.1){
        discard;
    }
}

vec4 calcFog(vec4 fog_color, vec4 mix_color, float intensity){
    float z = gl_FragCoord.z / gl_FragCoord.w;
    float fog = clamp(exp(-intensity * z * z), 0.2, 1);
    return mix(fog_color, mix_color, fog);
}