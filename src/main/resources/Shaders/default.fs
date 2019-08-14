#version 330 core

in vec3 outPosition;
in vec4 outColor;

#include /Shaders/lighting.h
#include /Shaders/textured.h
#include /Shaders/text.h

sampler2D normalmap;

out vec4 fragColor;

void main(){
    vec4 finalColor = outColor;

    #if defined(TEXTURED) && !defined(LIT)
        finalColor *= getTexel();
    #endif

    #ifdef LIT
        vec4 color;
        for(int i = 0; i < lights.length(); i++){
            vec4 ambient = calcAmbient(i) * getTexel();
            #ifdef NORMAL_MAP
                vec4 tex = texture(normalmap, outTexCord);
                tex = (tex * 2) - 1;
                vec4 diffuse = calcDiffuse(i, tex, outPosition) * getTexel();
            #else
                vec4 diffuse = calcDiffuse(i, outNormal, outPosition) * getTexel();
            #endif
            color += (diffuse + ambient);
        }
        finalColor *= vec4(color.rgb, 1);
    #endif

    #ifdef TEXT
        vec4 sampled = sampleText();
        finalColor *= sampled;
    #endif

    fragColor = finalColor;
}
