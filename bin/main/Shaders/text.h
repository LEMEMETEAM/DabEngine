vec4 sampleText(sampler2D text, vec2 uv){

        vec4 sampled = vec4(1.0, 1.0, 1.0, texture(text, uv).r);
        return sampled; 
}
