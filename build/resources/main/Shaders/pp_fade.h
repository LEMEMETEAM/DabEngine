uniform sampler2D fade;

uniform vec4 fade_color;
uniform float cutoff;

vec4 fade(sample2D base_tex, vec2 uv){
    vec4 tex = texture(fade, uv);
    if(tex.b < cutoff){
        return fade_color;
    }
    return texture(base_tex, uv);
}