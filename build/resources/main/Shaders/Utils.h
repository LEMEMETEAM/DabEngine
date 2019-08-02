float brightness(vec3 rgb){
    return (rgb.r + rgb.g + rgb.b) / 3.0;
}

void alphaScissor(vec4 tex_color){
    if(tex_color.a < 0.1){
        discard;
    }
}