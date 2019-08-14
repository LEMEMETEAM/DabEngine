#include /Shaders/textured.h

vec4 sampleText(){

        vec4 sampled = vec4(1.0, 1.0, 1.0, getTexel().r);
        return sampled; 
}
