void genOffset(float offset, inout vec2 array[9]){
    array = vec2[](
        vec2(-offset,  offset), // top-left
        vec2( 0.0f,    offset), // top-center
        vec2( offset,  offset), // top-right
        vec2(-offset,  0.0f),   // center-left
        vec2( 0.0f,    0.0f),   // center-center
        vec2( offset,  0.0f),   // center-right
        vec2(-offset, -offset), // bottom-left
        vec2( 0.0f,   -offset), // bottom-center
        vec2( offset, -offset)  // bottom-right    
    );
}

vec3 calcKernel(float kernel[9], vec2 offsets[9], sampler2D texture, vec2 uv){
    vec3 sampleTex[9];
    for(int i = 0; i < kernel.length(); i++)
    {
        sampleTex[i] = vec3(texture(texture, uv.st + offsets[i]));
    }
    vec3 col = vec3(0.0);
    for(int i = 0; i < kernel.length(); i++){
        col += sampleTex[i] * kernel[i];
    }
    return col;
}