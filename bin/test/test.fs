#version 330

out vec4 outColor;

void main(){
    #ifdef BLUE
        outCOlor = vec4(0.0, 0.0, 1.0, 1.0);
    #endif
}